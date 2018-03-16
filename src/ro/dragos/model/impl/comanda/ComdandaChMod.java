package ro.dragos.model.impl.comanda;

import ro.dragos.controller.Sistem;
import ro.dragos.model.spec.Comanda;

public class ComdandaChMod extends Comanda{

	private String parametruSecundar = null;
	
	
	public ComdandaChMod(String parametru, Sistem sistem, String parametruSecundar, String numeComanda) {
		super(parametru, sistem, numeComanda);
		this.parametruSecundar = parametruSecundar;
		// TODO Auto-generated constructor stub
	}


	@Override
	public String executa() {
		return sistem.chmod(parametru, parametruSecundar);
	}
	
	

}
