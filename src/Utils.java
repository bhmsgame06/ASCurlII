package ru.bhms.ascurlii;

import java.awt.*;
import java.awt.image.*;

public final class Utils {
	public static final int generalHWColor(BufferedImage img) {
		int width = img.getWidth();
		int height = img.getHeight();
		
		double result = 0.0d;
		
		double inc = (double)(width * height);
		inc = 255.0d / inc;
		
		for(int y = 0;y < height;y++) {
			for(int x = 0;x < width;x++) {
				int color = img.getRGB(x,y);
				
				double r = (double)((color >> 16) & 0xff);
				double g = (double)((color >> 8) & 0xff);
				double b = (double)(color & 0xff);
				
				result += (((r + g + b) / 3.0d) * inc) / 255;
			}
		}
		
		return (int)result;
	}

	public static BufferedImage normalize(BufferedImage src,int blockSize) {
		int newWidth = (src.getWidth() / blockSize + 1) * blockSize;
		int newHeight = (src.getHeight() / blockSize + 1) * blockSize;

		BufferedImage result = new BufferedImage(newWidth,newHeight,BufferedImage.TYPE_INT_RGB);
		result.getGraphics().drawImage(src,0,0,null);

		return result;
	}
}
