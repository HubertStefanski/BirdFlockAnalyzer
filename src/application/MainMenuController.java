package application;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.event.ChangeEvent;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

public class MainMenuController {
	/*
	 * FXML element Initialization
	 */
	@FXML
	MenuBar mainMenuBar;
	@FXML
	Menu systemMenu, fileMenu, helpMenu, viewOptionsMenu;
	@FXML
	MenuItem openFileMenuItem, exitMenuItem, aboutMenuItem, viewBlackAndWhiteMenuItem;
	@FXML
	ImageView mainImageView, blackAndWhiteImageView;
	@FXML
	Slider setGammaSlider, setContrastSlider, setBrightnessSlider, setNoiseReductionSlider;
	@FXML
	static Slider setThresholdSlider;
	@FXML
	RadioMenuItem sequentiallyLabelRadioMenuItem;
	@FXML
	Button setToBlackAndWhiteButton, analyseFlockDataButton;
	@FXML
	Text fileNameText, fileDimensionText, filePathText, numberOfBirdsText, FlockPatternText, brightnessText;
	/*
	 * Variable initialization
	 */

	public static Image image;
	public static BufferedImage bufferedImage;

	Slider setBrighntessSlider = new Slider(-1, 1, 0.1);

	File selectedFile;
	public static int imageWidth;
	public static int imageHeight;
	String imageWidthString, imageHeightString;

	/*
	 * >Opens File chooser >user picks file >method converts the file into
	 * appropriate format >method sets the file with appropriate format into
	 * imageview
	 */

	@FXML
	public void openFileChooser(ActionEvent e) {

		FileChooser fileChooser = new FileChooser();
		// FileChooser.ExtensionFilter extFilter = new
		// FileChooser.ExtensionFilter("image Files",
		// "Image files (.jpg, .jpeg, .jpe, .jfif, .png) | .jpg; .jpeg; .jpe; .jfif;
		// .png");
		// fileChooser.getExtensionFilters().add(extFilter);

		selectedFile = fileChooser.showOpenDialog(null);

		if (selectedFile != null) {
			try {
				bufferedImage = ImageIO.read(selectedFile);
				image = SwingFXUtils.toFXImage(bufferedImage, null);
				String fileName = selectedFile.getName();
//				String filePath = selectedFile.getAbsolutePath();
				double imageWidthDouble = image.getWidth();
				double imageHeigthDouble = image.getHeight();
				imageWidthString = Double.toString(imageHeigthDouble);
				imageHeightString = Double.toString(imageWidthDouble);

				mainImageView.setImage(image);
				// filePathText.setText(filePath);
				fileNameText.setText(fileName);
				// fileDimensionText.setText("imageHeightString" + "imageWidthString");

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		} else {
			System.out.println("---------------------------------------------------------------------------");
			System.out.println("-----------------------------INVALID FILE INPUT----------------------------");
			System.out.println("---------------------------------------------------------------------------");

		}
	}

	@FXML
	public void processSetToBlackAndWhite(ActionEvent e) {
		System.out.println(">Image sent to conversion");
		Image processedBWImage = ToBlackAndWhiteConverter.processToBlackAndWhite(image);
		System.out.println(">Image converted");
		System.out.println(">Setting converted image to image view");
		blackAndWhiteImageView.setImage(processedBWImage);
		System.out.println(">converted Image set to image view");

	}

	@FXML
	public void analyseFlockData(ActionEvent e) {
		ImageAnalysisForPixelGroups.findPixelGroups(ToBlackAndWhiteConverter.bufferedBwImage);
		drawBoxforEachPixelGroup(null);

	}

	@FXML
	public void Initialize() {
//		setBrightnessSlider.valueProperty().addListener(new ChangeListener<Number>() {
//
//			@Override
//			public void changed(ObservableValue<? extends Number> observable, Number oldVal, Number newVal) {
//				// TODO Auto-generated method stub
//				int imageHeightInt = imageHeight;
//				int imageWidthInt = imageWidth;
//
//				imageHeightInt = (int) image.getHeight();
//				imageWidthInt = (int) image.getWidth();
//
//				double originalVal = 127;
////				double newValSlider = setBrightnessSlider.getValue();
////				double oldValSlider = setBrightnessSlider.getValue() - originalVal;
////				originalVal = setBrightnessSlider.getValue();
//
////				double newVal = newValSlider;
////				double oldVal = oldValSlider;
//
//				int brightness;
//				if (newVal > 0) {
//					brightness = 2;
//				} else {
//					brightness = -2;
//				}
//
//				BufferedImage bi = new BufferedImage(imageHeightInt, imageWidthInt, 0);
//
//				for (int x = 0; x < imageWidthInt; x++) {
//					for (int y = 0; y < imageHeightInt; x++) {
//						int rgb = bufferedImage.getRGB(x, y);
//
//						int r = (rgb >> 16) & 0xff;
//						int g = (rgb >> 8) & 0xff;
//						int b = (rgb >> 0) & 0xff;
//
//						r += ((brightness * r) / 100);
//						g += ((brightness * g) / 100);
//						b += ((brightness * b) / 100);
//
//						r = Math.min(Math.max(0, r), 255);
//						g = Math.min(Math.max(0, g), 255);
//						b = Math.min(Math.max(0, b), 255);
//
//						rgb = (rgb & 0xff000000) | (r << 16) | (g << 8) | (b << 0);
//
//						bi.setRGB(x, y, rgb);
//
//						image = SwingFXUtils.toFXImage(bi, null);
//						mainImageView.setImage(image);
//
//					}
//					return;
//				}
//
//			}
//
//		});
//
	}
	/*
	 * Shuts down the application
	 */

	@SuppressWarnings("unused")
	public void stateChanged(ChangeEvent e) {
		int imageHeightInt = imageHeight;
		int imageWidthInt = imageWidth;

		imageHeightInt = (int) image.getHeight();
		imageWidthInt = (int) image.getWidth();

		double originalVal = 127;
		double newVal = setBrightnessSlider.getValue();
		double val = setBrightnessSlider.getValue() - originalVal;
		originalVal = setBrightnessSlider.getValue();

		int brightness;
		if (newVal > 0) {
			brightness = 2;
		} else {
			brightness = -2;
		}

		BufferedImage bi = new BufferedImage(imageHeightInt, imageWidthInt, 0);

		for (int x = 0; x < imageWidthInt; x++) {
			for (int y = 0; y < imageHeightInt; x++) {
				int rgb = bufferedImage.getRGB(x, y);

				int r = (rgb >> 16) & 0xff;
				int g = (rgb >> 8) & 0xff;
				int b = (rgb >> 0) & 0xff;

				r += ((brightness * r) / 100);
				g += ((brightness * g) / 100);
				b += ((brightness * b) / 100);

				r = Math.min(Math.max(0, r), 255);
				g = Math.min(Math.max(0, g), 255);
				b = Math.min(Math.max(0, b), 255);

				rgb = (rgb & 0xff000000) | (r << 16) | (g << 8) | (b << 0);

				bi.setRGB(x, y, rgb);

				image = SwingFXUtils.toFXImage(bi, null);
				mainImageView.setImage(image);

			}
			return;
		}
	}

	public void drawBoxforEachPixelGroup(PixelGroups pixelGroup) {
		List<PixelGroups> pgs = ImageAnalysisForPixelGroups.findPixelGroups(bufferedImage);

		for (PixelGroups pg : pgs) {
			Graphics2D box = bufferedImage.createGraphics();
			box.setColor(Color.BLUE);
			box.drawRect(pg.getX1(), pg.getY1(), pg.getX2() - pg.getX1(), pg.getY2() - pg.getY1());
			box.dispose();
		}
		image = SwingFXUtils.toFXImage(bufferedImage, null);
		mainImageView.setImage(image);

		numberOfBirdsText.setText((Integer.toString(ImageAnalysisForPixelGroups.getNumberOfPixelGroups())));
	}

	@FXML
	public void exit(ActionEvent e) {
		System.out.println("---------------------------------------------------------------------------");
		System.out.println("----------------------APPLICATION HAS BEEN TERMINATED----------------------");
		System.out.println("---------------------------------------------------------------------------");

		Platform.exit();

	}

	public static void main(String[] args) {
		Application.launch(args);
	}

}
