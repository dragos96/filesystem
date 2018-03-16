package ro.dragos.model.impl.comanda;

import ro.dragos.controller.Sistem;
import ro.dragos.model.spec.Comanda;

public class ComandaRmdir extends Comanda {
	
	public ComandaRmdir(String parametru, Sistem sistem, String numeComanda) {
		super(parametru, sistem, numeComanda);
	}
	
	@Override
	public String executa() {
		return sistem.rmdir(parametru);
	}
	
	

}
