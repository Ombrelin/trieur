package fr.thetrieur.gui;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Loader {

	public void load(Stage stage) throws IOException{
		FXMLLoader loader = new FXMLLoader();
	    Parent root = (Parent) loader.load(getClass().getResource("trieur.fxml").openStream());
	    MainController main = loader.getController();
	    main.setStage(stage);
	    stage.setTitle("Trieur");
	    stage.getIcons().add(new Image(getClass().getResource("resources/icon.png").openStream()));
	    Scene scene = new Scene(root);
	    scene.getStylesheets().add("modena_dark.css");
	    stage.setScene(scene);
	}
	
}
