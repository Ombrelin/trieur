package fr.thetrieur.main;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Appli extends Application {
	/**
	 * Lance l'application 
	 */
	@Override
	public void start(Stage stage) {
		try {
			FXMLLoader loader = new FXMLLoader();
		    Parent root = (Parent) loader.load(getClass().getResource("trieurMain.fxml").openStream());
		    MainController main = loader.getController();
		    main.setStage(stage);
		    stage.setTitle("Trieur");
		    stage.setScene(new Scene(root));
		    stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
