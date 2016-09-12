package com.ntr1x.treasure.web.services;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public interface IImageService {
    
    public static enum Type {
        COVER,
        CONTAIN,
        SCALE,
        
        ;
    }

    BufferedImage scale(BufferedImage source, Type type, int width, int height) throws IOException;
    InputStream stream(BufferedImage src) throws IOException;
}
