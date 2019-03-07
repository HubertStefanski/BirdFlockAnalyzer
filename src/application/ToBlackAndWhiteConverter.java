package application;

import java.awt.image.BufferedImage;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

/*
 * this method "changes" the image to black and white, the image that the method returns
 * is in fact a completely new image as opposed to a processed one
 *
 */
class ToBlackAndWhiteConverter {
	static int bwPixel = 0;
	static WritableImage bwImage;
	static BufferedImage bufferedBwImage;

	static Image processToBlackAndWhite(Image image,int threshold) {
		System.out.println(">image reading to pixel reader");
		PixelReader pixelReader = image.getPixelReader();

		System.out.println(">image reading COMPLETED");

		int imageWidth = (int) image.getWidth();
		int imageHeight = (int) image.getHeight();
		
		bwImage = new WritableImage(imageWidth, imageHeight);

		@SuppressWarnings("unused")
		PixelWriter pixelWriter = bwImage.getPixelWriter();
		for (int x = 0; x < imageWidth; x++) {
			for (int y = 0; y < imageHeight; y++) {
				int pixel = pixelReader.getArgb(x, y);

				int red = ((pixel >> 16) & 0xff);
				int green = ((pixel >> 8) & 0xff);
				int blue = (pixel & 0xff);

				int newPixel = (red + green + blue) / 3;

				int black = 0xff000000;
				int white = 0xffffffff;

				if (newPixel < threshold) {
					bwPixel = black;
				} else {
					bwPixel = white;
				}
				bwImage.getPixelWriter().setArgb(x, y, bwPixel);

			}
			bufferedBwImage = SwingFXUtils.fromFXImage(bwImage, null);
		}
		return bwImage;

	}

}
