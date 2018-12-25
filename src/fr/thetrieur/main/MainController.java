package fr.thetrieur.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Optional;

import fr.thetrieur.fichiers.Dossier;
import fr.thetrieur.fichiers.Fichier;
import fr.thetrieur.trieur.Trieur;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController {
	Stage stage;
	//Menus
	@FXML MenuBar menu;
	@FXML Menu menuFile;
	@FXML MenuItem menuImportConfig;
	@FXML MenuItem menuQuit;
	@FXML Menu menuHelp;
	@FXML MenuItem menuAbout;
	
	//Buttons
	@FXML Button btnSelectFolder;
	@FXML Button btnPersonnaliser;
	@FXML Button btnExclure;
	@FXML Button btnTrier;
	@FXML Button btnAddFolder;
	@FXML Button btnRemoveFolder;
	@FXML Button btnAddFile;
	@FXML Button btnRemoveFile;
	
	//Paneau
	@FXML VBox mainPane;
	@FXML ImageView welcome;
	@FXML GridPane paneExclure;
	@FXML GridPane panePersonnaliser;
	
	//fenetre Exclure
	@FXML Label labelTitreExclus;
	@FXML ListView<String> listExlus;
	@FXML Button btnValiderEclus;
	
	//Fenetre Personnaliser
	@FXML Button btnValiderExclus;
	@FXML ListView<Dossier> listDossiers;
	@FXML ListView<Fichier> listFichiers;
	@FXML Label labelTitrePerso;
	@FXML Button btnValiderPerso;
	
	@FXML ProgressBar progressBar;
	
	//Variables de l'application
	private String dossierChoisi;
	private ObservableList<Dossier> dossiers;
	private ObservableList<Fichier> currentFichiers;
	private ObservableList<String> exclus;

	@FXML
	public void initialize() {
		//Cacher les panneaux
		paneExclure.setVisible(false);
		panePersonnaliser.setVisible(false);
		
		//Initialiser les listes
		exclus = FXCollections.observableArrayList();
		dossiers = FXCollections.observableArrayList();
		currentFichiers = FXCollections.observableArrayList();
		
		//Mettre les listes dans les listview
		listDossiers.itemsProperty().bind(new SimpleListProperty<Dossier>(dossiers));
		listFichiers.itemsProperty().bind(new SimpleListProperty<Fichier>(currentFichiers));
		
		listDossiers.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Dossier>() {

			@Override
			public void changed(ObservableValue<? extends Dossier> changed, Dossier ancien, Dossier nouveau) {
				currentFichiers.clear();
				for (Fichier f : nouveau.getFichiers()) {
					currentFichiers.add(f);
				}
			}

		});
		
		//Chargement de la configuration
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
	}
	
	public void handleBtnSelectFolder() {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("Choisir le dossier à trier");
		File selectedDirectory = directoryChooser.showDialog(stage);

		if (selectedDirectory != null) {
			dossierChoisi = selectedDirectory.getAbsolutePath();
			System.out.println(dossierChoisi);
		}
	}
	
	public void handleBtnPersonnaliser() {
		System.out.println("Personnaliser...");
		paneExclure.setVisible(false);
		panePersonnaliser.setVisible(true);
		welcome.setVisible(false);
	}
	
	public void handleBtnExclure() {
		System.out.println("Exclure...");
		paneExclure.setVisible(true);
		panePersonnaliser.setVisible(false);
		welcome.setVisible(false);
	}

	public void addFolder() {
		TextInputDialog dialog = new TextInputDialog("");
		dialog.setTitle("Ajouter un dossier");
		dialog.setHeaderText("Création d'un nouveau dossier");
		dialog.setContentText("Entrez le nom du nouveau dossier");
		dialog.initModality(Modality.APPLICATION_MODAL);
		// Traditional way to get the response value.
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()){
		    this.dossiers.add(new Dossier(result.get()));
		}

	}
	
	public void removeFolder() {
		dossiers.remove(listDossiers.getSelectionModel().getSelectedItem());
	}

	public void addFile() {
		TextInputDialog dialog = new TextInputDialog("");
		dialog.setTitle("Ajouter un fichier");
		dialog.setHeaderText("Création d'un nouveau fichier");
		dialog.setContentText("Entrez l'extension du nouveau type de fichier");
		dialog.initModality(Modality.APPLICATION_MODAL);
		// Traditional way to get the response value.
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()){
			Fichier f = new Fichier(result.get(), result.get());
			listDossiers.getSelectionModel().getSelectedItem().getFichiers().add(f);
			this.reloadFichiers(listDossiers.getSelectionModel().getSelectedItem());
		}
	}

	public void removeFile() {
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

	public void handleBtnTrier() {
		Thread t = new Thread(new Trieur(dossiers, progressBar, dossierChoisi, exclus));
		t.start();
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
	
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
}
