package fr.thetrieur.main;

import java.io.IOException;

import fr.thetrieur.gui.Loader;
import javafx.application.Application;
import javafx.stage.Stage;

public class Appli extends Application {
	/**
	 * Lance l'application 
	 */
	@Override
	public void start(Stage stage) {
		try {
			Loader.getInstance().load(stage);
		    stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
