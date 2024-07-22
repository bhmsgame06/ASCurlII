package ru.bhms.ascurlii;

import java.io.*;
import javax.imageio.*;
import java.awt.image.*;

import static ru.bhms.ascurlii.Utils.*;

public class ASCurlII {
	public static final int[] BRIGHTNESS_MAP = loadBrightnessMap("/brightness_map.bin");
	public static final char[] CHAR_MAP = loadCharMap("/char_map.bin");

	public static final int[] loadBrightnessMap(String path) {
		final Class LOADER = new ASCurlII().getClass();
		int[] data;
		try {
			InputStream in = LOADER.getResourceAsStream(path);
			data = new int[in.available()];
			for(int i = 0;i < data.length;i++) {
				data[i] = in.read();
			}
			in.close();
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(-1);
			return null;
		}
		return data;
	}

	public static final char[] loadCharMap(String path) {
		final Class LOADER = new ASCurlII().getClass();
		char[] data;
		try {
			InputStream in = LOADER.getResourceAsStream(path);
			data = new char[in.available()];
			for(int i = 0;i < data.length;i++) {
				data[i] = (char)in.read();
			}
			in.close();
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(-1);
			return null;
		}
		return data;
	}

	public static void main(String[] args) throws Exception {
		if(args.length < 2) {
			System.out.println("Too few arguments!\n\nUsage: java -jar ascurlii.jar <block size in image for single symbol> <timeout> [path to image 1...] [path to image 2...] ...\n");
			System.exit(-1);
		}

		int blockSize = Integer.parseInt(args[0]);

		BufferedImage image = null;

		String timeout = args[1];

		System.out.println("while true; do");

		for(int arg = 2;arg < args.length;arg++) {
			image = ImageIO.read(new File(args[arg]));
			BufferedImage resized = new BufferedImage(image.getWidth(),image.getHeight() / 2,BufferedImage.TYPE_INT_RGB);
			resized.getGraphics().drawImage(image,0,0,resized.getWidth(),resized.getHeight(),null);
			image = resized;
			image = normalize(image,blockSize);

			int width = image.getWidth();
			int height = image.getHeight();

			for(int y = 0;y < height;y += blockSize) {
				System.out.print("echo -e \"");
				for(int x = 0;x < width;x += blockSize) {
					BufferedImage sub = image.getSubimage(x,y,blockSize,blockSize);

					int approxGeneral = Utils.generalHWColor(sub);

					int i = 0;
					while(i < BRIGHTNESS_MAP.length) {
						if(i == BRIGHTNESS_MAP.length - 1) break;
						if(approxGeneral <= BRIGHTNESS_MAP[i]) break;
						i++;
					}

					System.out.print(CHAR_MAP[i]);
				}
				System.out.println("\"");
			}

			System.out.printf("sleep %s\n",timeout);
			System.out.println("clear");
			System.out.println();
		}

		System.out.println("done");
	}
}
