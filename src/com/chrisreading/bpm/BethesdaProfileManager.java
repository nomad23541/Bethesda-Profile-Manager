package com.chrisreading.bpm;

import java.io.IOException;

import com.chrisreading.bpm.gui.controller.RootController;
import com.chrisreading.bpm.utility.Vars;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Main executing class
 */
public class BethesdaProfileManager extends Application {
	
	private Stage primaryStage;
	private BorderPane rootLayout;
	private RootController controller;

	public static void main(String[] args) {
		launch(args);
	}
	
	/** JavaFX Application Stuff */
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle(Vars.APP_TITLE);
		this.primaryStage.setResizable(false);
		
		initRootLayout();
		
		/* Load css */
		this.primaryStage.getScene().getStylesheets().add(BethesdaProfileManager.class.getResource("gui/view/style.css").toExternalForm());		
	}
	
	public void initRootLayout() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(BethesdaProfileManager.class.getResource("gui/view/RootLayout.fxml"));
		rootLayout = (BorderPane) loader.load();
		
		Scene scene = new Scene(rootLayout);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		controller = new RootController();
		controller.setApplication(this);
	}
	
	public RootController getRootController() {
		return controller;
	}
	
	public Stage getPrimaryStage() {
		return primaryStage;
	}
	
}
