package fr.thetrieur.gui;

import fr.thetrieur.fichiers.Dossier;
import fr.thetrieur.fichiers.Fichier;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class FenetreCreerFichier {
	private Dossier dossier;
	private TextField nom;
	private TextField extension;
	private Label labelNom;
	private Label labelExtension;
	private Button valider;
	private ObservableList<Fichier> currentFichiers;

	public FenetreCreerFichier(Dossier d, ObservableList<Fichier> currentFichiers) {
		this.dossier = d;
		this.currentFichiers = currentFichiers;
	}

	public void play() {
		Stage stage = new Stage();
		stage.setTitle("Créer Fichier");
		FlowPane root = new FlowPane(Orientation.VERTICAL, 10, 10);
		root.setPadding(new Insets(30, 30, 30, 30));
		Scene scene = new Scene(root, 300, 300);

		nom = new TextField();
		extension = new TextField();
		labelNom = new Label("Nom : ");
		labelExtension = new Label("Extension : ");
		valider = new Button("valider");

		valider.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Fichier f = new Fichier(nom.getText(), extension.getText());
				dossier.getFichiers().add(f);
				currentFichiers.clear();
				for (Fichier file : dossier.getFichiers()) {
					currentFichiers.add(file);
				}
				stage.close();
			}

		});
		scene.getStylesheets().add("style.css");
		root.getChildren().addAll(labelNom, nom, labelExtension, extension, valider);
		stage.setScene(scene);
		stage.show();
	}
}
