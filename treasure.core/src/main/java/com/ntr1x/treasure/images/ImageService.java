package com.ntr1x.treasure.images;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.enterprise.context.ApplicationScoped;
import javax.imageio.ImageIO;

@ApplicationScoped
public class ImageService {

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
}