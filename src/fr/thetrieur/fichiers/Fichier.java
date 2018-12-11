package fr.thetrieur.fichiers;

import java.io.Serializable;

public class Fichier implements Serializable {
	private static final long serialVersionUID = -2285548067366548486L;
	private String nom;
	private String extension;

	public Fichier(String nom, String extension) {
		super();
		this.nom = nom;
		this.extension = extension;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	@Override
	public String toString() {
		return this.getNom();
	}
}
