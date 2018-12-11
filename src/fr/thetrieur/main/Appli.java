package fr.thetrieur.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import fr.thetrieur.fichiers.Dossier;
import fr.thetrieur.fichiers.Fichier;
import fr.thetrieur.gui.FenetreExclusion;
import fr.thetrieur.gui.FenetrePersonnaliser;
import fr.thetrieur.trieur.Trieur;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.FlowPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class Appli extends Application {

	private ObservableList<Dossier> dossiers;
	private ObservableList<Fichier> currentFichiers;
	private ObservableList<String> exclus;
	private Button setup;
	private Button personnaliser;
	private Button exclusion;
	private Button valider;
	private ProgressBar progress;
	private String dossierChoisi;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		// Setup
		stage.setTitle("Trieur");
		stage.setResizable(false);
		// stage.getIcons().add(new Image(""));
		FlowPane root = new FlowPane(Orientation.VERTICAL, 10, 10);
		root.setPadding(new Insets(30, 30, 30, 30));
		Scene scene = new Scene(root, 420, 550);

		// Data init
		exclus = FXCollections.observableArrayList();
		dossiers = FXCollections.observableArrayList();
		currentFichiers = FXCollections.observableArrayList();
		File f = new File("config.dat");
		if (f.exists() && !f.isDirectory()) {
			System.out.println("Reading saved data");
			try {
				FileInputStream fluxFichiers = new FileInputStream("config.dat");
				ObjectInputStream fluxObjet = new ObjectInputStream(fluxFichiers);
				ArrayList<Dossier> temp = (ArrayList<Dossier>) fluxObjet.readObject();
				dossiers.addAll(temp);
				fluxObjet.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			dossiers = FXCollections.observableArrayList();
		}
		System.out.println(dossiers);
		f = new File("exclus.dat");
		if (f.exists() && !f.isDirectory()) {
			System.out.println("Reading saved data");
			try {
				FileInputStream fluxFichiers = new FileInputStream("exclus.dat");
				ObjectInputStream fluxObjet = new ObjectInputStream(fluxFichiers);
				ArrayList<String> temp = (ArrayList<String>) fluxObjet.readObject();
				System.out.println(temp);
				exclus.addAll(temp);
				fluxObjet.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			dossiers = FXCollections.observableArrayList();
		}
		// Test on data
		/*
		 * dossiers.add(new Dossier("Documents")); dossiers.add(new Dossier("Images"));
		 * dossiers.get(0).getFichiers().add(new Fichier("Excel Document", ".xlsx"));
		 * dossiers.get(0).getFichiers().add(new Fichier("Word Document", ".docx"));
		 * dossiers.get(0).getFichiers().add(new Fichier("PDF", ".pdf"));
		 * dossiers.get(1).getFichiers().add(new Fichier("PNG", ".png"));
		 * dossiers.get(1).getFichiers().add(new Fichier("JPG", ".jpg"));
		 * dossiers.get(1).getFichiers().add(new Fichier("JPEG", ".jpeg"));
		 */
		// Controls
		setup = new Button("Séléctionner un dossier");
		setup.setMinWidth(scene.getWidth() - 50);
		setup.setMaxWidth(scene.getWidth() - 50);
		personnaliser = new Button("Personnaliser");
		personnaliser.setMinWidth(scene.getWidth() - 50);
		personnaliser.setMaxWidth(scene.getWidth() - 50);
		exclusion = new Button("Exclure des fichiers");
		exclusion.setMinWidth(scene.getWidth() - 50);
		exclusion.setMaxWidth(scene.getWidth() - 50);
		valider = new Button("Trier");
		valider.setMinWidth(scene.getWidth() - 50);
		valider.setMaxWidth(scene.getWidth() - 50);
		progress = new ProgressBar(0.0);
		progress.setMinWidth(scene.getWidth() - 50);
		progress.setMaxWidth(scene.getWidth() - 50);
		setup.setMinHeight(50);
		exclusion.setMinHeight(50);
		personnaliser.setMinHeight(50);
		valider.setMinHeight(50);
		progress.setMinHeight(50);
		dossierChoisi = System.getProperty("user.home") + "/Desktop";
		// Event handling
		setup.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				DirectoryChooser directoryChooser = new DirectoryChooser();
				directoryChooser.setTitle("Choisir le dossier à trier");
				File selectedDirectory = directoryChooser.showDialog(stage);

				if (selectedDirectory != null) {
					dossierChoisi = selectedDirectory.getAbsolutePath();
					System.out.println(dossierChoisi);
				}
			}

		});

		personnaliser.setOnAction(e -> new FenetrePersonnaliser(dossiers, currentFichiers).play());

		exclusion.setOnAction(e -> new FenetreExclusion(exclus).play());

		valider.setOnAction(e -> new Thread(new Trieur(dossiers, progress, dossierChoisi, exclus)).start());

		// Final Setup
		scene.getStylesheets().add("style.css");
		root.getChildren().addAll(setup, personnaliser, exclusion, valider, progress);
		stage.setScene(scene);
		stage.show();
	}

	@Override
	public void stop() {
		try {
			FileOutputStream fluxFichiers = new FileOutputStream("config.dat");
			ObjectOutputStream fluxObjet = new ObjectOutputStream(fluxFichiers);
			fluxObjet.writeObject(new ArrayList<Dossier>(dossiers));
			fluxObjet.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			FileOutputStream fluxFichiers = new FileOutputStream("exclus.dat");
			ObjectOutputStream fluxObjet = new ObjectOutputStream(fluxFichiers);
			fluxObjet.writeObject(new ArrayList<String>(exclus));
			fluxObjet.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
