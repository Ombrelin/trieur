package fr.thetrieur.gui;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Loader {
	private static Loader INSTANCE;
	
	
	public void load(Stage stage) throws IOException{

		FXMLLoader loader = new FXMLLoader();
	    Parent root = (Parent) loader.load(getClass().getResource("trieur.fxml").openStream());
	    MainController main = loader.getController();
	    main.setStage(stage);
	    stage.setTitle("Trieur");
	    stage.getIcons().add(new Image(getClass().getResource("resources/icon.png").openStream()));
	    Scene scene = new Scene(root);
	    scene.getStylesheets().add("modena_dark.css");
	    stage.setScene(scene);
	    
	    File f = new File(this.getMyDocuments());
	    if(!f.canExecute()) {
	    	f.mkdirs();
	    }
	    
	}

	public String getMyDocuments() {
		//Récupération du répertoire "Mes documents"
	    String myDocuments = null;

	    try {
	        Process p =  Runtime.getRuntime().exec("reg query \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\Shell Folders\" /v personal");
	        p.waitFor();

	        InputStream in = p.getInputStream();
	        byte[] b = new byte[in.available()];
	        in.read(b);
	        in.close();

	        myDocuments = new String(b);
	        myDocuments = myDocuments.split("\\s\\s+")[4];

	    } catch(Throwable t) {
	        t.printStackTrace();
	    }
	    return myDocuments+"\\Trieur";
	    
	}
	
	public static Loader getInstance() {
		return INSTANCE == null?new Loader():INSTANCE;
	}
	
}
