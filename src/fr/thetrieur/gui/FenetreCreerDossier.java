package fr.thetrieur.gui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import fr.thetrieur.fichiers.Dossier;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class FenetreCreerDossier {
	private TextField nom;
	private Label labelNom;
	private Button valider;
	private ObservableList<Dossier> dossiers;

	public FenetreCreerDossier(ObservableList<Dossier> dossiers) {
		this.dossiers = dossiers;
	}

	public void play() {
		Stage stage = new Stage();
		stage.setTitle("Créer Dossier");
		FlowPane root = new FlowPane(Orientation.VERTICAL, 10, 10);
		root.setPadding(new Insets(30, 30, 30, 30));
		Scene scene = new Scene(root, 450, 150);
		GridPane grid = new GridPane();
		Image image = null;
		try {
			image = new Image(new FileInputStream("resources/logoAddFolder.png"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		ImageView imageView = new ImageView(image);
		imageView.setFitWidth(128);
		imageView.setFitHeight(128);
		nom = new TextField();
		nom.setPrefWidth(240);
		labelNom = new Label("Nom : ");
		valider = new Button("Valider");
		valider.setPrefWidth(240);
		valider.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Dossier d = new Dossier(nom.getText());
				dossiers.add(d);
				stage.close();
			}

		});
		grid.add(imageView, 0, 0,1,2);
		grid.add(labelNom, 1, 0);
		grid.add(nom, 1, 1);
		root.getChildren().addAll(grid, valider);
		scene.getStylesheets().add("style.css");
		stage.setScene(scene);
		stage.show();
	}
}
