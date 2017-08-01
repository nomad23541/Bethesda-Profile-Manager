package com.chrisreading.bpm.utility;

import com.chrisreading.bpm.BethesdaProfileManager;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;

/** 
 * Create dialogs on the fly! 
 */
public class DialogHandler {
	
	/**
	 * Sets dialog settings
	 * @param dialog
	 */
	private static void setDialogPaneStyle(Dialog<?> dialog) {
		DialogPane pane = dialog.getDialogPane();
		dialog.setGraphic(null);
		pane.getScene().getStylesheets().add(BethesdaProfileManager.class.getResource("gui/view/style.css").toExternalForm());
	}
	
	/**
	 * Create an error dialog
	 * @param title
	 * @param header
	 * @param content
	 */
	public static void createErrorDialog(String title, String header, String content) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		
		setDialogPaneStyle(alert);
		
		alert.showAndWait();
	}

}
