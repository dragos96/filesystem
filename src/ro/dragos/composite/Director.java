package ro.dragos.composite;


import java.util.ArrayList;
import java.util.List;

import ro.dragos.common.Type;
import ro.dragos.security.Entitate;

public class Director extends Entitate {

	private List<Entitate> entitati = new ArrayList<>();
	private String path;

	public Director(String nume, String path) {
		super(nume);
		this.path = path;
	}

	
	
	public String getPath() {
		return path;
	}



	public Type getType() {
		return Type.DIRECTOR;
	}

	public void addEntitate(Entitate ent) {
		entitati.add(ent);
	}

	public String afisareLsHelper() {
		String result =  "" + nume + " d" + getPermissions() + " " + owner.getUsername();
		return result;
	}

	public String afisareLs() {
		String linii = "";
		// linii +=  nume + " d" + getPermissions() + " " + owner.getUsername() + "\n";
		for (Entitate e : entitati) {
			if (e.getType().equals(Type.DIRECTOR)) {
				linii += ((Director) e).afisareLsHelper() + "\n";
			} else {
				linii += e.afisareLs() + "\n";
			}
		}
		return linii;
	}

	public String toString() {
		String afisare = "/" + nume + "\n";
		for (Entitate entitate : entitati) {
			afisare += "\t" + entitate.toString() + "\n";
		}
		return afisare;
	}

	public Entitate getEntitate(String numeEntitate) {
		for (Entitate e : entitati) {
			if (e.getNume().equals(numeEntitate))
				return e;
		}
		return null;
	}

	public List<Entitate> getEntitati() {
		return entitati;
	}
	
	

}
