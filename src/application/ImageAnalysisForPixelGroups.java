package application;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;

import java.awt.image.BufferedImage;

public class ImageAnalysisForPixelGroups {
	private int noiseReduction;
	private static List<PixelGroups> pixelGroups;

	public ImageAnalysisForPixelGroups(final int noiseReduction) {
		this.setNoiseReduction((noiseReduction > 1 ? noiseReduction : 1));
	}

	public static List<PixelGroups> findPixelGroups(BufferedImage bi) {

		BufferedImage biImage = MainMenuController.bufferedImage;

		int imageHeightInt = (int) MainMenuController.bufferedImage.getHeight();
		int imageWidthInt = (int) MainMenuController.bufferedImage.getWidth();
		QuickUnionFind uFind = new QuickUnionFind(imageWidthInt * imageHeightInt);

		for (int x = 0; x < imageWidthInt; x++) {
			for (int y = 0; y < imageHeightInt; y++) {

				int pixel = ToBlackAndWhiteConverter.bufferedBwImage.getRGB(x, y);
				final int pixelId = getId(x, y, imageWidthInt);

				if ((pixel & 1) == 1) {
					continue;
				}
				// Checks left
				if (x > 0 && pixel == biImage.getRGB(x - 1, y)) {
					uFind.union(pixelId, getId(x, y - 1, imageWidthInt));
				}
				// Checks right
				if (x < imageWidthInt - 1 && pixel == biImage.getRGB(x + 1, y)) {
					uFind.union(pixelId, getId(x + 1, y, imageWidthInt));
				}
				// Checks up
				if (y > 0 && pixel == biImage.getRGB(x, y - 1)) {
					uFind.union(pixelId, getId(x, y - 1, imageWidthInt));
				}
				// Checks down
				if (y < imageHeightInt - 1 && pixel == biImage.getRGB(x, y + 1)) {
					uFind.union(pixelId, getId(x, y + 1, imageWidthInt));
				}
				// Checks down right
				if (y < imageHeightInt - 1 && x < imageWidthInt - 1 && pixel == biImage.getRGB(x + 1, y + 1)) {
					uFind.union(pixelId, getId(x + 1, y + 1, imageWidthInt));
				}
				// Checks up left
				if (y > 0 && x > 0 && pixel == biImage.getRGB(x - 1, y - 1)) {
					uFind.union(pixelId, getId(x - 1, y - 1, imageWidthInt));
				}
				// Checks down left
				if (y > 0 && x < imageWidthInt - 1 && pixel == biImage.getRGB(x + 1, y - 1)) {
					uFind.union(pixelId, getId(x + 1, y - 1, imageWidthInt));
				}
				// Checks up right
				if (y < imageHeightInt - 1 && x > 0 && pixel == biImage.getRGB(x - 1, y + 1)) {
					uFind.union(pixelId, getId(x - 1, y + 1, imageWidthInt));
				}

			}
		}
		// returns coordinates for each node in tree to enable to draw a boxes

		Image image = ToBlackAndWhiteConverter.bwImage;
		List<PixelGroups> pixelGroups = new ArrayList<PixelGroups>();
		for (int root : uFind.getRoots(1)) {
			List<Integer> treeElements = uFind.getElementsOfTree(root);
			int firstNode = treeElements.get(0);
			int lastNode = treeElements.get(treeElements.size() - 1);

			final int x1 = (int) (firstNode % image.getWidth());
			final int y1 = (int) (firstNode / image.getWidth());
			final int x2 = (int) (lastNode % image.getWidth());
			final int y2 = (int) (lastNode / image.getWidth());

			pixelGroups.add(new PixelGroups(x1, y1, x2, y2));

		}
		return pixelGroups;
	}

//returns correct array position in quickUnionFind
	private static int getId(int x, int y, int imageWidthInt) {
		return x + y * imageWidthInt;
	}

	public int getNoiseReduction() {
		return noiseReduction;
	}

	public void setNoiseReduction(int noiseReduction) {
		this.noiseReduction = noiseReduction;
	}

	public List<PixelGroups> getPixelGroups() {
		return pixelGroups;
	}

	public static int getNumberOfPixelGroups() {
		return pixelGroups.size();
	}
}
