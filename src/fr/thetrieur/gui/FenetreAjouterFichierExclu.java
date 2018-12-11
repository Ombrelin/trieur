package fr.thetrieur.gui;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class FenetreAjouterFichierExclu {
	private TextField nomFichier;
	private Button valider;
	private ObservableList<String> exclus;

	public FenetreAjouterFichierExclu(ObservableList<String> exclus) {
		this.exclus = exclus;
	}

	public void play() {
		Stage stage = new Stage();
		stage.setTitle("Exclure des fichiers");
		FlowPane root = new FlowPane(Orientation.VERTICAL, 10, 10);
		root.setPadding(new Insets(30, 30, 30, 30));
		Scene scene = new Scene(root, 500, 300);

		// Controls
		valider = new Button("Valider");
		nomFichier = new TextField();
		// Event Hanldler
		valider.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				exclus.add(nomFichier.getText());
				stage.close();
			}

		});
		scene.getStylesheets().add("style.css");
		root.getChildren().addAll(nomFichier, valider);
		stage.setScene(scene);
		stage.show();
	}

}
