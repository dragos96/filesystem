package ro.dragos.model.impl.comanda;

import ro.dragos.controller.Sistem;
import ro.dragos.model.spec.Comanda;

public class ComandaLs extends Comanda{

	
	public ComandaLs(String param, Sistem sistem, String numeComanda) {
		super(param, sistem, numeComanda);
	}

	@Override
	public String executa() {
		
		
		return sistem.ls(parametru);
	}

}
