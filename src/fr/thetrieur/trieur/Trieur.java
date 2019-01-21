package fr.thetrieur.trieur;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.concurrent.*;

import fr.thetrieur.fichiers.Dossier;
import fr.thetrieur.fichiers.Fichier;
import fr.thetrieur.util.Logger;
import javafx.collections.ObservableList;
import javafx.scene.control.ProgressBar;

public class Trieur extends Task<Void> {

	private ObservableList<Dossier> dossiers;
	private ProgressBar progress;
	private String dossier;
	private String destination;
	private ObservableList<String> exclus;

	public Trieur(ObservableList<Dossier> dossiers, ProgressBar progress, String dossier,
			ObservableList<String> exclus, String destination) {
		super();
		this.dossier = dossier;
		this.dossiers = dossiers;
		this.progress = progress;
		this.exclus = exclus;
		this.destination = destination;
	}

	@Override
	public Void call() {
		progress.setProgress(0);
		double fileCount = new File(this.dossier).listFiles().length;
		double progressValue = 1 / fileCount;

		DirectoryStream<Path> dossierCourant = null;
		try {
			dossierCourant = Files.newDirectoryStream(Paths.get(dossier));
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (Path p : dossierCourant) {
			Logger.getInstance().log("Traitement du fichier : " + p);
			for (Dossier d : dossiers) {
				for (Fichier f : d.getFichiers()) {
					if (!exclus.contains(p.getFileName().toString())) {
						if (p.getFileName().toString().contains(f.getExtension())) {
							File file = new File(destination + "\\" + d.getNom());
							if (!file.canExecute()) {
								file.mkdirs();
							}

							try {
								Logger.getInstance().log("Déplacement : " + Files.move(p,
										Paths.get(destination + "\\" + d.getNom()).resolve(p.getFileName())));
							} catch (IOException e) {
								e.printStackTrace();
							}

						}
					}
				}

			}
			progress.setProgress(progress.getProgress() + progressValue);
		}
		return null;
	}
}
