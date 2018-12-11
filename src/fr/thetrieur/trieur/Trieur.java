package fr.thetrieur.trieur;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import fr.thetrieur.fichiers.Dossier;
import fr.thetrieur.fichiers.Fichier;
import javafx.collections.ObservableList;
import javafx.scene.control.ProgressBar;

public class Trieur implements Runnable {

	private ObservableList<Dossier> dossiers;
	private ProgressBar progress;
	private String dossier;
	private ObservableList<String> exclus;

	public Trieur(ObservableList<Dossier> dossiers, ProgressBar progress, String dossier,
			ObservableList<String> exclus) {
		super();
		this.dossier = dossier;
		this.dossiers = dossiers;
		this.progress = progress;
		this.exclus = exclus;
	}

	@Override
	public void run() {
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
			System.out.println(p);
			for (Dossier d : dossiers) {
				for (Fichier f : d.getFichiers()) {
					for (String exclu : this.exclus) {
						if (p.getFileName().toString().contains(f.getExtension())
								&& !p.getFileName().toString().equals(exclu)) {
							File file = new File(d.getNom());
							if (!file.canExecute()) {
								file.mkdirs();
							}

							try {
								System.out.println("moved : " + Files.move(p,
										Paths.get(dossier + "\\" + d.getNom()).resolve(p.getFileName())));
							} catch (IOException e) {
								e.printStackTrace();
							}

						}
					}

				}

			}
			progress.setProgress(progress.getProgress() + progressValue);
			System.out.println(progress.getProgress());
		}
	}
}
