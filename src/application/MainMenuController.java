package application;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

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
	Text fileNameText, fileDimensionsText, filePathText, numberOfBirdsText, FlockPatternText, brightnessText,
			noiseReductionValueText, thresholdLevelSliderValueText;
	/*
	 * Variable initialization
	 */

	public static Image image;
	public static BufferedImage bufferedImage;

	File selectedFile;
	String imageWidthString, imageHeightString;
	int numberOfBirdsInt;
	private int currentNoiseReduction = 1;
	private int currentBlackWhiteThreshold = 127;

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

				mainImageView.setImage(image);
				processSetToBlackAndWhite(currentBlackWhiteThreshold);
				// Uncomment this if you want to outline when an image is first opened.
				// analyseFlockData(e);
				filePathText.setText(filePath);
				fileNameText.setText(fileName);
				fileDimensionsText.setText(bufferedImage.getWidth() + " x " + bufferedImage.getWidth());

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

	public void processSetToBlackAndWhite(int threshold) {
		System.out.println(">Image sent to conversion");
		Image processedBWImage = ToBlackAndWhiteConverter.processToBlackAndWhite(image, threshold);
		System.out.println(">Image converted");
		System.out.println(">Setting converted image to image view");
		blackAndWhiteImageView.setFitHeight(163);
		blackAndWhiteImageView.setFitWidth(391);
		blackAndWhiteImageView.setImage(processedBWImage);
		System.out.println(">converted Image set to image view");
	}

	@FXML
	public void analyseFlockData(ActionEvent e) {
		updateOutlinedImage();
	}
	
	// Wrapper function for easy refreshing of the outlined image.
	public void updateOutlinedImage() {
		List<PixelGroups> pgs = ImageAnalysisForPixelGroups.findPixelGroups(ToBlackAndWhiteConverter.bufferedBwImage,
				currentNoiseReduction);
		drawPixelGroupsToImage(bufferedImage, pgs);
	}
	
	public void drawPixelGroupsToImage(BufferedImage bi, List<PixelGroups> pgs) {
		// Copy image so when we're changing values we don't keep drawing boxes to the same image
		BufferedImage imageCopy = copyImage(bi);
		for (PixelGroups pg : pgs) {
			Graphics2D box = imageCopy.createGraphics();
			box.setColor(Color.BLUE);
			box.drawRect(pg.getX1(), pg.getY1(), pg.getX2() - pg.getX1(), pg.getY2() - pg.getY1());
			box.dispose();
		}
		image = SwingFXUtils.toFXImage(imageCopy, null);
		mainImageView.setImage(image);

		numberOfBirdsText.setText(Integer.toString(pgs.size()));
	}
	
	private BufferedImage copyImage(final BufferedImage image) {
		final BufferedImage copy = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		Graphics copyGraphics = copy.getGraphics();
		copyGraphics.drawImage(image, 0, 0, null);
		copyGraphics.dispose();
		return copy;
	}
	
	@FXML
	public void initialize() {
		setNoiseReductionSlider.setValue(currentNoiseReduction);
		// Noise reduction should never be below 1, that would be all pixels.
		setNoiseReductionSlider.setMin(1);
		setNoiseReductionSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (image != null) {
					currentNoiseReduction = newValue.intValue();
					noiseReductionValueText.setText(Integer.toString(currentNoiseReduction));
					numberOfBirdsText.setText(Integer.toString(numberOfBirdsInt));
					updateOutlinedImage();
				}
			}

		});
		setThresholdSlider.setValue(currentBlackWhiteThreshold);
		setThresholdSlider.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (image != null) {
					currentBlackWhiteThreshold = newValue.intValue();
					processSetToBlackAndWhite(currentBlackWhiteThreshold);
					thresholdLevelSliderValueText.setText(Integer.toString(currentBlackWhiteThreshold));
				}
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
