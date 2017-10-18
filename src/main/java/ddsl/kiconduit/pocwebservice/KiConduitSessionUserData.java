package ddsl.kiconduit.pocwebservice;

public class KiConduitSessionUserData {

	private String name;
	private int nbConducteur;
	private int nbLignes;
	private int nbConduit;

	public KiConduitSessionUserData(String name, int nbLignes, int nbConducteur, int nbConduit) {
		this.name = name;
		this.nbLignes = nbLignes;
		this.nbConducteur = nbConducteur;
		this.nbConduit = nbConduit;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNbConducteur() {
		return nbConducteur;
	}

	public void setNbConducteur(int nbConducteur) {
		this.nbConducteur = nbConducteur;
	}

	public int getNbLignes() {
		return nbLignes;
	}

	public void setNbLignes(int nbLignes) {
		this.nbLignes = nbLignes;
	}

	public int getNbConduit() {
		return nbConduit;
	}

	public void setNbConduit(int nbConduit) {
		this.nbConduit = nbConduit;
	}

}
