package com.chrisreading.bpm.gui.controller;

import java.io.IOException;
import java.util.Optional;

import com.chrisreading.bpm.BethesdaProfileManager;
import com.chrisreading.bpm.FileManager;
import com.chrisreading.bpm.GameManager;
import com.chrisreading.bpm.gui.model.Game;
import com.chrisreading.bpm.gui.model.Profile;
import com.chrisreading.bpm.utility.DialogHandler;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;

/** 
 * Controller for the RootLayout
 */
public class RootController {

	@SuppressWarnings("unused")
	private BethesdaProfileManager main;
	
	private FileManager fm;
	private GameManager gm;
	
	@FXML
	private ComboBox<Game> comboBoxGame;
	@FXML
	private ComboBox<Profile> comboBoxProfile;
	@FXML
	private Button btnPlay;
	@FXML
	private Button btnNewProfile;
	@FXML
	private GridPane gridPane;
	@FXML
	private CheckBox checkMark;

	public RootController() {}
	
	@FXML
	private void initialize() {
		/** Start Managers */
		gm = new GameManager();
		fm = new FileManager(gm.getGames());
		
		/** Setup combo boxes */
		comboBoxGame.setItems(FXCollections.observableArrayList(gm.getGames()));
		comboBoxGame.setConverter(new StringConverter<Game>() {
			@Override
			public String toString(Game obj) {
				return obj.getTitle();
			}
			
			@Override
			public Game fromString(String string) {
				return null;
			}
		});
		
		comboBoxProfile.setConverter(new StringConverter<Profile>() {
			@Override
			public String toString(Profile obj) {
				return obj.getName();
			}
			
			@Override
			public Profile fromString(String string) {
				return null;
			}
		});
		
		/* When selection is changed, change the profiles */
		comboBoxGame.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
			disableProfileNodes(false);
			comboBoxProfile.setItems(FXCollections.observableArrayList(comboBoxGame.getSelectionModel().getSelectedItem().getProfiles()));
			comboBoxProfile.getSelectionModel().selectFirst();
		});
		
		
		/* Set disabled by default */
		disableProfileNodes(true);
	}
	
	@FXML
	private void onNewProfile() {
		Game selectedGame = comboBoxGame.getSelectionModel().getSelectedItem();
		
		/** There's gotta be a better way to do this, but I'm lazy and too tired to
		 * figure out.
		 */
		TextInputDialog dialog = new TextInputDialog();
		dialog.setGraphic(null);
		DialogPane pane = dialog.getDialogPane();
		pane.getScene().getStylesheets().add(BethesdaProfileManager.class.getResource("gui/view/style.css").toExternalForm());
		dialog.setTitle("Create Profile");
		dialog.setHeaderText("New " + selectedGame.getTitle() + " Profile");
		dialog.setContentText("Name:");
		
		Button btn = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
		btn.setText("Create");
		
		Optional<String> result = dialog.showAndWait();
		if(result.isPresent()) {
			if(result.get().isEmpty()) {
				DialogHandler.createErrorDialog("Error", null, "Field can't be empty!");
			} else if(selectedGame.containsProfile(result.get())) {
				DialogHandler.createErrorDialog("Error", null, "That name is already taken!");
			} else {
				try {
					fm.createProfile(selectedGame, result.get());
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				comboBoxProfile.setItems(FXCollections.observableArrayList(comboBoxGame.getSelectionModel().getSelectedItem().getProfiles())); // basically refresh the combobox
				comboBoxProfile.getSelectionModel().select(comboBoxProfile.getItems().size() - 1); // select the newest profile
			}
		}
	}
	
	@FXML
	private void onPlay() throws IOException, InterruptedException {
		Game game = comboBoxGame.getSelectionModel().getSelectedItem();
		Profile profile = comboBoxProfile.getSelectionModel().getSelectedItem();
		
		/* Make sure there is a profile to load */
		if(game.getProfiles().isEmpty()) {
			DialogHandler.createErrorDialog("Error", null, "A profile needs to be selected!");
		} else {
			/* Switch saves and then start the game */
			fm.switchSaves(game, fm.getLastProfileUsed(game), profile);
			fm.setLastProfileUsed(game, profile);
			new ProcessBuilder(game.getGameExecutable()).start();
				
		}
		
		/* Exit if check mark is selected */
		if(checkMark.isSelected()) {
			System.exit(0);
		}
	}
	
	/**
	 * Disable profile nodes
	 * @param disable
	 */
	private void disableProfileNodes(boolean disable) {
		comboBoxProfile.setDisable(disable);
		btnPlay.setDisable(disable);
		btnNewProfile.setDisable(disable);
		checkMark.setDisable(disable);
	}
	
	public void setApplication(BethesdaProfileManager main) {
		this.main = main;
	}
	
}
