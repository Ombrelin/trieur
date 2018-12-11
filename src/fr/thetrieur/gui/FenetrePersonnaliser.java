package fr.thetrieur.gui;

import fr.thetrieur.fichiers.Dossier;
import fr.thetrieur.fichiers.Fichier;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class FenetrePersonnaliser {

	private ListView<Dossier> listDossiers;
	private ListView<Fichier> listFichiers;
	private ObservableList<Dossier> dossiers;
	private ObservableList<Fichier> currentFichiers;

	public FenetrePersonnaliser(ObservableList<Dossier> dossiers, ObservableList<Fichier> currentFichiers) {
		super();
		this.dossiers = dossiers;
		this.currentFichiers = currentFichiers;
	}

	public void play() {
		currentFichiers.clear();

		Stage secondStage = new Stage();
		secondStage.setTitle("Personnalisation");
		FlowPane secondRoot = new FlowPane(Orientation.VERTICAL, 10, 10);
		secondRoot.setPadding(new Insets(30, 30, 30, 30));
		Scene secondScene = new Scene(secondRoot, 700, 550);
		GridPane grid = new GridPane();
		secondStage.setResizable(false);

		listDossiers = new ListView<Dossier>(dossiers);
		listFichiers = new ListView<Fichier>(currentFichiers);

		listDossiers.setMaxWidth((secondScene.getWidth() - 225) / 2);
		listFichiers.setMaxWidth((secondScene.getWidth() - 225) / 2);

		Button validerDossier = new Button("Valider");
		FlowPane validerPane = new FlowPane();
		validerPane.setPadding(new Insets(50, 0, 0, 0));
		validerPane.getChildren().add(validerDossier);
		validerDossier.setMinWidth(secondScene.getWidth() - 60);
		Button addDossier = new Button("+");
		Button removeDossier = new Button(" - ");
		Button addFichier = new Button("+");
		Button removeFichier = new Button(" - ");
		addDossier.setMinWidth((secondScene.getWidth() - 225) / 4);
		removeDossier.setMinWidth((secondScene.getWidth() - 225) / 4);
		addFichier.setMinWidth((secondScene.getWidth() - 225) / 4);
		removeFichier.setMinWidth((secondScene.getWidth() - 225) / 4);

		FlowPane toolFichiers = new FlowPane();
		FlowPane toolDossiers = new FlowPane();
		toolFichiers.getChildren().addAll(addFichier, removeFichier);
		toolDossiers.getChildren().addAll(addDossier, removeDossier);
		grid.add(listDossiers, 0, 0);
		grid.add(listFichiers, 1, 0);

		grid.add(toolDossiers, 0, 1);
		grid.add(toolFichiers, 1, 1);
		grid.add(validerPane, 0, 2, 2, 1);
		try {
			listDossiers.getSelectionModel().select(dossiers.get(0));

			for (Fichier f : dossiers.get(0).getFichiers()) {
				currentFichiers.add(f);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Event handling
		addDossier.setOnAction(e -> new FenetreCreerDossier(dossiers).play());

		addFichier.setOnAction(
				e -> new FenetreCreerFichier(listDossiers.getSelectionModel().getSelectedItem(), currentFichiers)
						.play());

		removeDossier.setOnAction(e -> dossiers.remove(listDossiers.getSelectionModel().getSelectedItem()));

		removeFichier.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				listDossiers.getSelectionModel().getSelectedItem().getFichiers()
						.remove(listFichiers.getSelectionModel().getSelectedItem());
				reloadFichiers(listDossiers.getSelectionModel().getSelectedItem());
			}
		});

		listDossiers.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Dossier>() {

			@Override
			public void changed(ObservableValue<? extends Dossier> changed, Dossier ancien, Dossier nouveau) {
				currentFichiers.clear();
				for (Fichier f : nouveau.getFichiers()) {
					currentFichiers.add(f);
				}
			}

		});

		validerDossier.setOnAction(e -> secondStage.close());

		secondScene.getStylesheets().add("style.css");
		secondRoot.getChildren().addAll(grid);
		secondStage.setScene(secondScene);
		secondStage.show();
	}

	private void reloadFichiers(Dossier d) {
		currentFichiers.clear();
		for (Fichier f : d.getFichiers()) {
			currentFichiers.add(f);
		}
	}

}
