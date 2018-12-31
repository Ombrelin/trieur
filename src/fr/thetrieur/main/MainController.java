package fr.thetrieur.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Optional;

import fr.thetrieur.fichiers.Dossier;
import fr.thetrieur.fichiers.Fichier;
import fr.thetrieur.trieur.Trieur;
import javafx.application.Platform;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController {
	Stage stage;
	// Menus
	@FXML
	MenuBar menu;
	@FXML
	Menu menuFile;
	@FXML
	MenuItem menuImportConfig;
	@FXML
	MenuItem menuExportConfig;
	@FXML
	MenuItem menuImportExclus;
	@FXML
	MenuItem menuExportExclus;
	@FXML
	MenuItem menuQuit;
	@FXML
	Menu menuHelp;
	@FXML
	MenuItem menuAbout;

	// Buttons
	@FXML
	Button btnSelectFolder;
	@FXML
	Button btnPersonnaliser;
	@FXML
	Button btnExclure;
	@FXML
	Button btnTrier;
	@FXML
	Button btnAddFolder;
	@FXML
	Button btnRemoveFolder;
	@FXML
	Button btnAddFile;
	@FXML
	Button btnRemoveFile;
	@FXML
	Button btnSelectDest;

	// Paneau
	@FXML
	VBox mainPane;
	@FXML
	ImageView welcome;
	@FXML
	GridPane paneExclure;
	@FXML
	GridPane panePersonnaliser;

	// fenetre Exclure
	@FXML
	Label labelTitreExclus;
	@FXML
	ListView<String> listExlus;
	@FXML
	Button btnValiderEclus;
	@FXML
	Button btnAddExclus;
	@FXML
	Button btnRemoveExclus;
	@FXML
	Button btnBrowseEclus;

	// Fenetre Personnaliser
	@FXML
	Button btnValiderExclus;
	@FXML
	ListView<Dossier> listDossiers;
	@FXML
	ListView<Fichier> listFichiers;
	@FXML
	Label labelTitrePerso;
	@FXML
	Button btnValiderPerso;
	@FXML
	Button btnModifier;

	@FXML
	ProgressBar progressBar;

	// Variables de l'application
	private String destination;
	private String dossierChoisi;
	private ObservableList<Dossier> dossiers;
	private ObservableList<Fichier> currentFichiers;
	private ObservableList<String> exclus;

	@FXML
	public void initialize() {
		// Cacher les panneaux
		paneExclure.setVisible(false);
		panePersonnaliser.setVisible(false);

		// Initialiser les listes
		exclus = FXCollections.observableArrayList();
		dossiers = FXCollections.observableArrayList();
		currentFichiers = FXCollections.observableArrayList();

		// Mettre les listes dans les listview
		listDossiers.itemsProperty().bind(new SimpleListProperty<Dossier>(dossiers));
		listFichiers.itemsProperty().bind(new SimpleListProperty<Fichier>(currentFichiers));
		listExlus.itemsProperty().bind(new SimpleListProperty<String>(exclus));

		listDossiers.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Dossier>() {

			@Override
			public void changed(ObservableValue<? extends Dossier> changed, Dossier ancien, Dossier nouveau) {
				currentFichiers.clear();
				for (Fichier f : nouveau.getFichiers()) {
					currentFichiers.add(f);
				}
			}

		});

		try {
			welcome.setImage(new Image(new FileInputStream("resources/icon.png")));
		} catch (FileNotFoundException e) {
			Logger.getInstance().log("Impossible de charger l'image de bienvenue");
		}
		
		dossierChoisi = System.getProperty("user.home") + "/Desktop";
		destination = System.getProperty("user.home") + "/Desktop";
		// Chargement de la configuration
		loadConfig(true);
	}

	public void handleImportConfig() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Choisir de la configuration à importer");
		File selectedFile = fileChooser.showOpenDialog(stage);

		try {
			Files.copy(selectedFile.toPath(), Paths.get("config.dat"), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Logger.getInstance().log("Importation de la configuration : " + selectedFile.getName());
		loadConfig(askOverwrite());

	}

	public void handleExportConfig() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Choisir le de configuration à exporter");
		File selectedFile = fileChooser.showOpenDialog(stage);

		try {
			Files.copy(Paths.get("config.dat"), selectedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Logger.getInstance().log("Exportation du fichier : " + selectedFile.getName());
		loadConfig(askOverwrite());

	}

	public void handleImportExclus() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Choisir le fichier d'exclusion à importer");
		File selectedFile = fileChooser.showOpenDialog(stage);

		try {
			Files.copy(selectedFile.toPath(), Paths.get("exclus.dat"), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Logger.getInstance().log("Importation de la configuration : " + selectedFile.getName());
		loadConfig(askOverwrite());

	}

	public void handleExportExclus() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Choisir le fichier d'exclusion à exporter");
		File selectedFile = fileChooser.showOpenDialog(stage);

		try {
			Files.copy(Paths.get("exclus.dat"), selectedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Logger.getInstance().log("Importation de la configuration : " + selectedFile.getName());
		loadConfig(askOverwrite());

	}

	public void handleBtnSelectFolder() {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("Choisir le dossier à trier");
		File selectedDirectory = directoryChooser.showDialog(stage);

		if (selectedDirectory != null) {
			dossierChoisi = selectedDirectory.getAbsolutePath();
		}
	}

	public void handleBtnSelectDest() {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("Choisir le dossier où trier");
		File selectedDirectory = directoryChooser.showDialog(stage);

		if (selectedDirectory != null) {
			destination = selectedDirectory.getAbsolutePath();
		}
	}

	public void handleBtnPersonnaliser() {
		paneExclure.setVisible(false);
		panePersonnaliser.setVisible(true);
		welcome.setVisible(false);
	}

	public void handleBtnExclure() {
		paneExclure.setVisible(true);
		panePersonnaliser.setVisible(false);
		welcome.setVisible(false);
	}

	public void addFolder() {
		Dossier d = null;
		TextInputDialog dialog = new TextInputDialog("");
		dialog.getDialogPane().getStylesheets().add("dialogStyle.css");
		dialog.setTitle("Ajouter un dossier");
		dialog.setHeaderText("Création d'un nouveau dossier");
		dialog.setContentText("Entrez le nom du nouveau dossier");
		
		Stage fenetreModale = (Stage) dialog.getDialogPane().getScene().getWindow();
		try {
			fenetreModale.getIcons().add(new Image(new FileInputStream("resources/icon.png")));
			dialog.setGraphic(new ImageView(new Image(new FileInputStream("resources/logoAddFolder.png"))));
		} catch (FileNotFoundException e) {
			Logger.getInstance().log("Image non trouvée");
		}
		dialog.initModality(Modality.APPLICATION_MODAL);
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			d = new Dossier(result.get());
			this.dossiers.add(d);
		}
		Logger.getInstance().log("Création du dossier : " + d);
	}

	public void editFile() {
		Fichier f = listFichiers.getSelectionModel().getSelectedItem();
		TextInputDialog dialog = new TextInputDialog("");
		dialog.getDialogPane().getStylesheets().add("dialogStyle.css");
		dialog.setTitle("Modification du fichier");
		dialog.setHeaderText("Modification du fichier");
		dialog.setContentText("Entrez le nouveau nom du fichier");
		Stage fenetreModale = (Stage) dialog.getDialogPane().getScene().getWindow();
		try {
			fenetreModale.getIcons().add(new Image(new FileInputStream("resources/icon.png")));
		} catch (FileNotFoundException e) {
			Logger.getInstance().log("Image non trouvée");
		}
		dialog.initModality(Modality.APPLICATION_MODAL);
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			f.setExtension(result.get());
			f.setNom(result.get());
			this.reloadFichiers(listDossiers.getSelectionModel().getSelectedItem());
			Logger.getInstance().log("Modification du fichier : " + f);
		}
		
	}

	public void removeFolder() {
		Logger.getInstance().log("Suppression du dossier : " + listDossiers.getSelectionModel().getSelectedItem());
		dossiers.remove(listDossiers.getSelectionModel().getSelectedItem());
	}

	public void addFile() {
		Fichier f = null;
		TextInputDialog dialog = new TextInputDialog("");
		dialog.getDialogPane().getStylesheets().add("dialogStyle.css");
		dialog.setTitle("Ajouter un fichier");
		dialog.setHeaderText("Création d'un nouveau fichier");
		dialog.setContentText("Entrez l'extension du nouveau type de fichier");
		Image icon = null;
		try {
			icon = new Image(new FileInputStream("resources/logoAddFolder.png"));			
		} catch (FileNotFoundException e) {
			Logger.getInstance().log("Image non trouvée");
			e.printStackTrace();
		}
		Stage fenetreModale = (Stage) dialog.getDialogPane().getScene().getWindow();
		try {
			fenetreModale.getIcons().add(new Image(new FileInputStream("resources/icon.png")));
		} catch (FileNotFoundException e) {
			Logger.getInstance().log("Image non trouvée");
		}
		dialog.initModality(Modality.APPLICATION_MODAL);
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			f = new Fichier(result.get(), result.get());
			listDossiers.getSelectionModel().getSelectedItem().getFichiers().add(f);
			this.reloadFichiers(listDossiers.getSelectionModel().getSelectedItem());
			Logger.getInstance().log("Création du fichier : " + f);
		}
		
	}

	public void removeFile() {
		Logger.getInstance().log("Suppression du fichier : " + listFichiers.getSelectionModel().getSelectedItem());
		listDossiers.getSelectionModel().getSelectedItem().getFichiers()
				.remove(listFichiers.getSelectionModel().getSelectedItem());
		reloadFichiers(listDossiers.getSelectionModel().getSelectedItem());
	}

	private void reloadFichiers(Dossier d) {
		currentFichiers.clear();
		for (Fichier f : d.getFichiers()) {
			currentFichiers.add(f);
		}
	}

	public void handleBrowseEclus() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Choisir le fichier à exclure");
		File selectedFile = fileChooser.showOpenDialog(stage);

		exclus.add(selectedFile.getName());
		Logger.getInstance().log("Exclusion du fichier : " + selectedFile.getName());
	}

	public void handleBtnTrier() {
		Service<Void> serviceTri = new Service<Void>() {
			@Override
			protected Task<Void> createTask() {
				return new Trieur(dossiers, progressBar, dossierChoisi, exclus, destination);
			}
		};
		serviceTri.start();

		serviceTri.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent arg0) {
				Alert alert = new Alert(AlertType.INFORMATION);
				Stage fenetreModale = (Stage) alert.getDialogPane().getScene().getWindow();
				try {
					fenetreModale.getIcons().add(new Image(new FileInputStream("resources/icon.png")));
				} catch (FileNotFoundException e) {
					Logger.getInstance().log("Image non trouvée");
				}
				alert.getDialogPane().getStylesheets().add("dialogStyle.css");
				alert.setTitle("Tri terminé");
				alert.setHeaderText("Tri terminé !");
				alert.setContentText("Le tri de vos fichiers a été réalisé avec succès");
				alert.initModality(Modality.APPLICATION_MODAL);
				alert.showAndWait();
			}

		});

	}

	public void handleBtnAddExclus() {
		TextInputDialog dialog = new TextInputDialog("");
		dialog.getDialogPane().getStylesheets().add("dialogStyle.css");
		dialog.setTitle("Exclure un fichier");
		dialog.setHeaderText("Exclusion d'un fichier");
		dialog.setContentText("Entrez le nom du fichier à exclure");
		Stage fenetreModale = (Stage) dialog.getDialogPane().getScene().getWindow();
		try {
			fenetreModale.getIcons().add(new Image(new FileInputStream("resources/icon.png")));
		} catch (FileNotFoundException e) {
			Logger.getInstance().log("Image non trouvée");
		}
		dialog.initModality(Modality.APPLICATION_MODAL);
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			exclus.add(result.get());
		}
	}

	public void handleBtnRemoveExclus() {
		exclus.remove(listExlus.getSelectionModel().getSelectedItem());
	}

	public void handleBtnValiderConfig() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					FileOutputStream fluxFichiers = new FileOutputStream("config.dat");
					ObjectOutputStream fluxObjet = new ObjectOutputStream(fluxFichiers);
					fluxObjet.writeObject(new ArrayList<Dossier>(dossiers));
					fluxObjet.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}).start();
	}

	public void listKeypress(KeyEvent event) {
		if (event.getCode() == KeyCode.DELETE) {
			ListView currentList = (ListView) event.getSource();
			currentList.getItems().remove(currentList.getSelectionModel().getSelectedItem());
			// this.reloadFichiers(listDossiers.getSelectionModel().getSelectedItem());
		}
	}

	public void listFichierKeypress(KeyEvent event) {
		if (event.getCode() == KeyCode.DELETE) {
			ListView currentList = (ListView) event.getSource();
			listDossiers.getSelectionModel().getSelectedItem().getFichiers()
					.remove(currentList.getSelectionModel().getSelectedItem());
			this.reloadFichiers(listDossiers.getSelectionModel().getSelectedItem());
		}
	}

	public void handleBtnValiderExclure() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					FileOutputStream fluxFichiers = new FileOutputStream("exclus.dat");
					ObjectOutputStream fluxObjet = new ObjectOutputStream(fluxFichiers);
					fluxObjet.writeObject(new ArrayList<String>(exclus));
					fluxObjet.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}).start();
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	private void loadConfig(boolean overwrite) {
		File f = new File("config.dat");
		if (f.exists() && !f.isDirectory()) {
			Logger.getInstance().log("Lecture des données sauvegardées (configutation)");
			try {
				FileInputStream fluxFichiers = new FileInputStream("config.dat");
				ObjectInputStream fluxObjet = new ObjectInputStream(fluxFichiers);
				ArrayList<Dossier> temp = (ArrayList<Dossier>) fluxObjet.readObject();
				if (overwrite) {
					dossiers.clear();
				}
				dossiers.addAll(temp);
				fluxObjet.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		f = new File("exclus.dat");
		if (f.exists() && !f.isDirectory()) {
			Logger.getInstance().log("Lecture des données sauvegardées (exclusions)");
			try {
				FileInputStream fluxFichiers = new FileInputStream("exclus.dat");
				ObjectInputStream fluxObjet = new ObjectInputStream(fluxFichiers);
				ArrayList<String> temp = (ArrayList<String>) fluxObjet.readObject();
				if (overwrite) {
					exclus.clear();
				}
				exclus.addAll(temp);
				fluxObjet.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private boolean askOverwrite() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.getDialogPane().getStylesheets().add("dialogStyle.css");
		alert.setTitle("Choix");
		alert.setHeaderText("Voulez vous écraser la configuration actuelle avec la configuration importée \n "
				+ "ou bien ajouter la configuration importée à la configuration actuelle ?");
		alert.setContentText("Ecraser ou ajouter");
		Stage fenetreModale = (Stage) alert.getDialogPane().getScene().getWindow();
		try {
			fenetreModale.getIcons().add(new Image(new FileInputStream("resources/icon.png")));
		} catch (FileNotFoundException e) {
			Logger.getInstance().log("Image non trouvée");
		}
		alert.initModality(Modality.APPLICATION_MODAL);
		ButtonType buttonTypeOne = new ButtonType("Ecraser");
		ButtonType buttonTypeTwo = new ButtonType("Ajouter");

		alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == buttonTypeOne) {
			return true;
		} else if (result.get() == buttonTypeTwo) {
			return false;
		}
		return false;
	}

	public void handleQuit() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.getDialogPane().getStylesheets().add("dialogStyle.css");
		alert.setTitle("Quitter");
		alert.setHeaderText("Voulez vous vraiment quitter ?");
		alert.setContentText("Quitter");
		Stage fenetreModale = (Stage) alert.getDialogPane().getScene().getWindow();
		try {
			fenetreModale.getIcons().add(new Image(new FileInputStream("resources/icon.png")));
		} catch (FileNotFoundException e) {
			Logger.getInstance().log("Image non trouvée");
		}
		alert.initModality(Modality.APPLICATION_MODAL);
		ButtonType buttonTypeOne = new ButtonType("Oui");
		ButtonType buttonTypeTwo = new ButtonType("Non");

		alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == buttonTypeOne) {
			Platform.exit();
		}

	}

	public void handleAPropos() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.getDialogPane().getStylesheets().add("dialogStyle.css");
		alert.setTitle("A Propos");
		alert.setHeaderText("A propos de Trieur");
		Stage fenetreModale = (Stage) alert.getDialogPane().getScene().getWindow();
		try {
			fenetreModale.getIcons().add(new Image(new FileInputStream("resources/icon.png")));
		} catch (FileNotFoundException e) {
			Logger.getInstance().log("Image non trouvée");
		}
		alert.setContentText(
				"Trieur est une application Open Source distribuée sous la licence CC-BY-NC.\n Elle a été conçue et développée par Arsène Lapostolet \n Contact : arsene@lapostolet.fr");
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.showAndWait();
	}
}
