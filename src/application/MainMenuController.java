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
	MenuItem openFileMenuItem, exitMenuItem, aboutMenuItem;
	@FXML
	ImageView mainImageView, blackAndWhiteImageView;
	@FXML
	Slider setGammaSlider, setContrastSlider, setBrightnessSlider, setNoiseReductionSlider, setThresholdSlider;
	@FXML
	RadioMenuItem sequentiallyLabelRadioMenuItem;
	@FXML
	Button analyseFlockDataButton;
	@FXML
	Text fileNameText, fileDimensionText, filePathText, numberOfBirdsText, FlockPatternText, brightnessText,
			noiseReductionValueText, thresholdLevelSliderValueText;
	/*
	 * Variable initialization
	 */

	public static Image image;
	public static BufferedImage bufferedImage;

	File selectedFile;
	public static int imageWidth;
	public static int imageHeight;
	String imageWidthString, imageHeightString;
	int numberOfBirdsInt;
	private int currentNoiseReduction = 1;

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
				String filePath = selectedFile.getAbsolutePath();
				double imageWidthDouble = image.getWidth();
				double imageHeigthDouble = image.getHeight();
				imageWidthString = Double.toString(imageHeigthDouble);
				imageHeightString = Double.toString(imageWidthDouble);

				mainImageView.setImage(image);
				processSetToBlackAndWhite();
				analyseFlockData(e);
				filePathText.setText(filePath);
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

	public void processSetToBlackAndWhite() {
		System.out.println(">Image sent to conversion");
		Image processedBWImage = ToBlackAndWhiteConverter.processToBlackAndWhite(image,
				ToBlackAndWhiteConverter.threshold);
		System.out.println(">Image converted");
		System.out.println(">Setting converted image to image view");
		blackAndWhiteImageView.setFitHeight(163);
		blackAndWhiteImageView.setFitWidth(391);
		blackAndWhiteImageView.setImage(processedBWImage);
		System.out.println(">converted Image set to image view");

	}

	@FXML
	public void analyseFlockData(ActionEvent e) {
		List<PixelGroups> pgs = ImageAnalysisForPixelGroups.findPixelGroups(ToBlackAndWhiteConverter.bufferedBwImage,
				currentNoiseReduction);
		drawBoxforEachPixelGroup(pgs);
	}

	/*
	 * Shuts down the application
	 */

	public void drawBoxforEachPixelGroup(List<PixelGroups> pgs) {
		List<PixelGroups> pgs1 = ImageAnalysisForPixelGroups.findPixelGroups(bufferedImage, currentNoiseReduction);

		for (PixelGroups pg : pgs1) {
			Graphics2D box = bufferedImage.createGraphics();
			box.setColor(Color.BLUE);
			box.drawRect(pg.getX1(), pg.getY1(), pg.getX2() - pg.getX1(), pg.getY2() - pg.getY1());
			box.dispose();
		}
		image = SwingFXUtils.toFXImage(bufferedImage, null);
		mainImageView.setImage(image);
		numberOfBirdsInt = pgs.size();

		numberOfBirdsText.setText(Integer.toString(numberOfBirdsInt));
	}

	@FXML
	public void initialize() {
		System.out.println("init");
		setNoiseReductionSlider.setValue(currentNoiseReduction);
		setNoiseReductionSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				currentNoiseReduction = newValue.intValue();
				noiseReductionValueText.setText(Integer.toString(currentNoiseReduction));
				numberOfBirdsText.setText(Integer.toString(numberOfBirdsInt));
				drawBoxforEachPixelGroup(null);
			}

		});
		setThresholdSlider.setValue(ToBlackAndWhiteConverter.threshold);
		setThresholdSlider.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				ToBlackAndWhiteConverter.threshold = newValue.intValue();
				processSetToBlackAndWhite();
				thresholdLevelSliderValueText.setText(Integer.toString(ToBlackAndWhiteConverter.threshold));
			}
		});
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
