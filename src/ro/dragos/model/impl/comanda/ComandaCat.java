package ro.dragos.model.impl.comanda;

import ro.dragos.controller.Sistem;
import ro.dragos.model.spec.Comanda;

public class ComandaCat extends Comanda{

	public ComandaCat(String parametru, Sistem sistem, String numeComanda) {
		super(parametru, sistem, numeComanda);
	}

	@Override
	public String executa() {
		return sistem.cat(parametru);
	}
	
	

}
