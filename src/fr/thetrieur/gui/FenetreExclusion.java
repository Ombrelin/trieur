package fr.thetrieur.gui;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class FenetreExclusion {
	private ListView<String> listExclusion;
	private Button ajouter;
	private Button supprimer;
	private Button valider;
	private ObservableList<String> exclus;

	public FenetreExclusion(ObservableList<String> exclus) {
		this.exclus = exclus;
	}

	public void play() {
		Stage stage = new Stage();
		stage.setTitle("Exclure des fichiers");
		GridPane root = new GridPane();
		root.setPadding(new Insets(30, 30, 30, 30));
		Scene scene = new Scene(root, 300, 600);
		stage.setResizable(false);

		// Controls
		listExclusion = new ListView<String>(this.exclus);
		ajouter = new Button("+");
		ajouter.setMinWidth((scene.getWidth() - 60) / 2);
		supprimer = new Button(" - ");
		supprimer.setMinWidth((scene.getWidth() - 60) / 2);

		valider = new Button("Valider");
		valider.setMinWidth(scene.getWidth() - 60);
		valider.setMinHeight(50);
		FlowPane validerPane = new FlowPane();
		validerPane.setPadding(new Insets(50,0,0,0));
		validerPane.getChildren().add(valider);
		root.add(listExclusion, 0, 0, 2, 1);
		root.add(ajouter, 0, 1);
		root.add(supprimer, 1, 1);
		root.add(validerPane, 0, 2, 1, 2);

		// Event Handler
		ajouter.setOnAction(e -> new FenetreAjouterFichierExclu(this.exclus).play());

		supprimer.setOnAction(e -> exclus.remove(listExclusion.getSelectionModel().getSelectedItem()));

		valider.setOnAction(e -> stage.close());

		scene.getStylesheets().add("style.css");
		stage.setScene(scene);
		stage.show();
	}

}
