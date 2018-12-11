package fr.thetrieur.fichiers;

import java.io.Serializable;
import java.util.ArrayList;

public class Dossier implements Serializable {
	private static final long serialVersionUID = -128716587031594624L;
	private ArrayList<Fichier> fichiers;
	private String nom;

	public Dossier(String nom) {
		super();
		this.nom = nom;
		this.fichiers = new ArrayList<Fichier>();
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public ArrayList<Fichier> getFichiers() {
		return fichiers;
	}

	@Override
	public String toString() {
		return this.getNom();
	}

}
