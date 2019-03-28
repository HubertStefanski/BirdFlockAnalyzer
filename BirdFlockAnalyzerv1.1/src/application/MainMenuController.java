/*
 * @author Hubert Stefanski
 * @version 1.1 (Added functionality above bare-bones)
 */

package application;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
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
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

public class MainMenuController {
	/*
	 * FXML element Initialization
	 */
	@FXML
	MenuBar mainMenuBar;
	@FXML
	Menu systemMenu, fileMenu, helpMenu;
	@FXML
	MenuItem openFileMenuItem, exitMenuItem, aboutMenuItem, sequentiallyLabelMenuItem;
	@FXML
	ImageView mainImageView, blackAndWhiteImageView;
	@FXML
	Slider setGammaSlider, setContrastSlider, setBrightnessSlider, setNoiseReductionSlider, setThresholdSlider;
	@FXML
	Button analyseFlockDataButton;
	@FXML
	Text fileNameText, fileDimensionText, filePathText, numberOfBirdsText, FlockPatternText, brightnessText,
			noiseReductionValueText, thresholdLevelSliderValueText, textTextBox;
	/*
	 * Variable initialization
	 */

	public static Image image;
	public static BufferedImage bufferedImage;

	File selectedFile;
	public static int imageWidth;
	public static int imageHeight;
	String imageWidthString, imageHeightString;
	static List<PixelGroups> pgs;
	private static int birdSequentialNumber = 0;

	// default value for the black and white conversion, can be set
	// trough setThresholdSlider
	int numberOfBirdsInt, threshold = 127;
	// default value for the noise reduction slider and thus the required size of
	// the union to be qualified as a pixelGroup(Bird)
	private int currentNoiseReduction = 1;

	/*
	 * Opens file chooser window, the user chooses the file to be loaded in, file is
	 * returned into an imageview, automatically sent to black and white conversion
	 * converted black and white image is then set to its respective image view
	 * AnalyseFlockData method is called to analyse the image for pixel groups
	 */

	@FXML
	public void openFileChooser(ActionEvent e) {

		FileChooser fileChooser = new FileChooser();
		// TODO: extension filter to be properly implemented
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

	/*
	 * runs the black and white conversion, takes in the image chosen from the file
	 * by the user sends it to the ToBlackAndWhiteConverter class, where the
	 * threshold value is taken from the slider and is then processed accordingly to
	 * the threshold, ie below = black, above = white
	 */
	public void processSetToBlackAndWhite() {
		System.out.println(">Image sent to conversion");
		Image processedBWImage = ToBlackAndWhiteConverter.processToBlackAndWhite(image, threshold);
		System.out.println(">Image converted");
		System.out.println(">Setting converted image to image view");
		blackAndWhiteImageView.setFitHeight(300);
		blackAndWhiteImageView.setFitWidth(300);
		blackAndWhiteImageView.setImage(processedBWImage);
		System.out.println(">converted Image set to image view");

	}
	/*
	 * runs unionfind on the image to locate "Birds" or groupings of pixels in the
	 * image, returns the data found into a draw method, which draws a box around
	 * each pixel group found
	 */

	@FXML
	public void analyseFlockData(ActionEvent e) {
		List<PixelGroups> pgs = ImageAnalysisForPixelGroups.findPixelGroups(ToBlackAndWhiteConverter.bufferedBwImage,
				currentNoiseReduction);
		drawBoxforEachPixelGroup(pgs);
	}

	/*
	 * takes in all found pixel groups in the image, according to the noise
	 * reduction threshold set by the slider in the main menu control calculates the
	 * approximate box required for each bird and then superimposes a rectangle over
	 * the bird to mark its location Also responsible for returning the estimated
	 * amount of birds in the image according to the noise Reduction threshold
	 * 
	 */

	public void drawBoxforEachPixelGroup(List<PixelGroups> pgs) {
		List<PixelGroups> pgs1 = ImageAnalysisForPixelGroups.findPixelGroups(bufferedImage, currentNoiseReduction);

		for (PixelGroups pg : pgs) {
			Graphics2D box = bufferedImage.createGraphics();
			Graphics2D numbering = bufferedImage.createGraphics();
			box.setColor(Color.YELLOW);
			box.drawRect(pg.getX1(), pg.getY1(), pg.getX2() - pg.getX1(), pg.getY2() - pg.getY1());
			box.dispose();
			numbering.setColor(Color.BLUE);
			numbering.drawString(birdSequentialNumber + 1 + "", pg.getX1(), pg.getY1());
			//numbering.setFont(new Font("Arial",Font.PLAIN,imageWidth/50));
			birdSequentialNumber++;
			box.dispose();
			numbering.dispose();
			image = SwingFXUtils.toFXImage(bufferedImage, null);
			mainImageView.setImage(image);
			numberOfBirdsInt = pgs.size();

			numberOfBirdsText.setText(Integer.toString(numberOfBirdsInt));
		}
	}

	/*
	 * Initialization method for setThresholdSlider and setNoiseReductionSlider,
	 * returns values for the respective slider and re-runs the method so that the
	 * new variable is taken into account, Black and white conversion is seen in
	 * real time NoiseReduction requires the "Analyse flock data" button to be
	 * pressed
	 */

	@FXML
	public void initialize() {
		System.out.println("init");
		setNoiseReductionSlider.setValue(currentNoiseReduction);
		setNoiseReductionSlider.setMin(1); // prevents the code from breaking, i.e. looking for unions equal to zero
		setNoiseReductionSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (image != null) {

					currentNoiseReduction = newValue.intValue();
					noiseReductionValueText.setText(Integer.toString(currentNoiseReduction));
					numberOfBirdsText.setText(Integer.toString(numberOfBirdsInt));
					drawBoxforEachPixelGroup(pgs);

				}
			}

		});
		setThresholdSlider.setValue(threshold);
		setThresholdSlider.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (image != null) {
					threshold = newValue.intValue();
					processSetToBlackAndWhite();
					thresholdLevelSliderValueText.setText(Integer.toString(threshold));
					drawBoxforEachPixelGroup(pgs);
				}
			}
		});
	}

	/*
	 * Exit method for the system
	 */

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
