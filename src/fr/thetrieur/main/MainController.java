package fr.thetrieur.main;

import fr.thetrieur.fichiers.Dossier;
import fr.thetrieur.fichiers.Fichier;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class MainController {
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
	
	//Paneau
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
	
	@FXML
	public void initialize() {
		paneExclure.setVisible(false);
		panePersonnaliser.setVisible(false);
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
	
}
