package ro.dragos.model.spec;

import ro.dragos.controller.Sistem;

public abstract class Comanda implements IComanda{

	protected String parametru;
	protected Sistem sistem;
	protected String numeComanda;
	
	public Comanda(String parametru, Sistem sistem, String numeComanda){
		this.parametru = parametru;
		this.sistem = sistem;
		this.numeComanda = numeComanda;
		
	}
	
	
	
	public String getParametru() {
		return parametru;
	}
	public void setParametru(String parametru) {
		this.parametru = parametru;
	}
	public Sistem getSistem() {
		return sistem;
	}
	public void setSistem(Sistem sistem) {
		this.sistem = sistem;
	}



	public String getNumeComanda() {
		return numeComanda;
	}



	public void setNumeComanda(String numeComanda) {
		this.numeComanda = numeComanda;
	}
	
	
	
	
}
