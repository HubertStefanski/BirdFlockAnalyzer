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
	static int threshold = 127;

	static Image processToBlackAndWhite(Image image) {
		Image localImage;
		localImage = SwingFXUtils.toFXImage(MainMenuController.bufferedImage, null);
		System.out.println(">image reading to pixel reader");
		PixelReader pixelReader = localImage.getPixelReader();

		System.out.println(">image reading COMPLETED");

		int imageHeightInt = MainMenuController.imageHeight;
		int imageWidthInt = MainMenuController.imageWidth;

		imageHeightInt = (int) localImage.getHeight();
		imageWidthInt = (int) localImage.getWidth();

		bwImage = new WritableImage(imageWidthInt, imageHeightInt);

		@SuppressWarnings("unused")
		PixelWriter pixelWriter = bwImage.getPixelWriter();
		for (int x = 0; x < imageWidthInt; x++) {
			for (int y = 0; y < imageHeightInt; y++) {
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
