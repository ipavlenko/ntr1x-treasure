package com.ntr1x.treasure.web.services;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;

@Service
public class ImageService implements IImageService {

	public void fit(InputStream input, OutputStream output, Integer width, Integer height, String format) throws IOException {

		BufferedImage source = ImageIO.read(input);
		
		Image scaled = source.getScaledInstance(
				width != null ? width : -1,
				height != null ? height : -1,
				Image.SCALE_SMOOTH
		);
		
		BufferedImage target = new BufferedImage(
				scaled.getWidth(null),
				scaled.getHeight(null),
				BufferedImage.TYPE_INT_ARGB
		);
		
		target.getGraphics().drawImage(scaled, 0, 0, null);
		
		ImageIO.write(target, "png", output);
	}

	public void fitToWidth(InputStream input, OutputStream output, Integer width, String format) throws IOException {
		fit(input, output, width, null, format);
	}

	public void fitSquare(InputStream input, OutputStream output, Integer side, String format) throws IOException {
        fitWithCut(
                scale(input, output, side),
                output, side, format
        );
	}

    private BufferedImage scale(InputStream input, OutputStream output, Integer side) throws IOException {
        BufferedImage src = ImageIO.read(input);
        int w = src.getWidth();
        int h = src.getHeight();

        double k = (w > h) ? (side * 1.0)/(h * 1.0) : (side * 1.0)/(w * 1.0);

        Image scaled = src.getScaledInstance(
                (int)(w * k),
                (int)(h * k),
                Image.SCALE_SMOOTH
        );

        BufferedImage target = new BufferedImage(
                scaled.getWidth(null),
                scaled.getHeight(null),
                BufferedImage.TYPE_INT_ARGB
        );

        target.getGraphics().drawImage(scaled, 0, 0, null);

        return target;
    }

    private void fitWithCut(BufferedImage src, OutputStream output, Integer side, String format) throws IOException {

        BufferedImage dst = new BufferedImage(side, side, BufferedImage.TYPE_INT_ARGB);

        Integer x2 = src.getWidth();
        Integer y2 = src.getHeight();

        Graphics2D g = dst.createGraphics();
        if (x2 > y2) {
            g.drawImage(src, -(x2-side)/2, 0, x2, y2, null);
        } else {
            g.drawImage(src, 0, -(y2-side)/2, x2, y2, null);
        }
        g.dispose();

        ImageIO.write(dst, "png", output);
    }

    public InputStream getImageInputStream(BufferedImage src) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(src, "png", os);
        return new ByteArrayInputStream(os.toByteArray());
    }
}