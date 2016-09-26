package com.ntr1x.treasure.web.services;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;

@Service
public class ScaleImageService implements IScaleImageService {

    @Override
    public InputStream stream(BufferedImage src) throws IOException {
        
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(src, "png", os);
        return new ByteArrayInputStream(os.toByteArray());
    }
    
    @Override
    public BufferedImage scale(BufferedImage source, Type type, int width, int height) throws IOException {
        
        Rectangle bounds = bounds(
            type,
            new Dimension(source.getWidth(), source.getHeight()),
            new Dimension(width, height)
        );
        
        Image scaled = source.getScaledInstance(
            bounds.width,
            bounds.height,
            Image.SCALE_SMOOTH
        );

        BufferedImage target = new BufferedImage(
            width < 0 ? scaled.getWidth(null) : width,
            height < 0 ? scaled.getHeight(null) : height,
            BufferedImage.SCALE_DEFAULT
        );

        target.getGraphics().drawImage(
            scaled,
            bounds.x,
            bounds.y,
            bounds.width < 0 ? target.getWidth() : bounds.width,
            bounds.height < 0 ? target.getHeight() : bounds.height,
            null
        );
        
        return target;
    }
    
    private Rectangle bounds(Type type, Dimension source, Dimension target) {
        
        if (target.width < 0 && target.height < 0)
            return new Rectangle(0, 0, source.width, source.height); 
        if (target.width < 0)
            return new Rectangle(0, 0, -1, target.height);
        if (target.height < 0)
            return new Rectangle(0, 0, target.width, -1);
        
        switch (type) {
        
            case CONTAIN: {
                
                float kw = target.width / (float) source.width;
                float kh = target.height / (float) source.height;
                
                return kw < kh
                    ? new Rectangle(0, (int) ((target.height - kw * source.height) / 2.f), target.width, (int) (kw * source.height))
                    : new Rectangle((int) ((target.width - kh * source.width) / 2.f), 0, (int) (kh * source.width), target.height)
                ;
            }
            case COVER: {
                
                float kw = target.width / (float) source.width;
                float kh = target.height / (float) source.height;
                
                return kw > kh
                    ? new Rectangle(0, (int) ((target.height - kw * source.height) / 2.f), target.width, (int) (kw * source.height))
                    : new Rectangle((int) ((target.width - kh * source.width) / 2.f), 0, (int) (kh * source.width), target.height)
                ;
            }
            case SCALE:
            default: {
                return new Rectangle(0, 0, target.width, target.height);
            }
        }
    }
}