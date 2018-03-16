package ro.dragos.model.impl.comanda;

import ro.dragos.controller.Sistem;
import ro.dragos.model.spec.Comanda;

public class ComandaChuser extends Comanda {
	
	public ComandaChuser(String parametru, Sistem sistem, String numeComanda) {
		super(parametru, sistem, numeComanda);
	}
	
	@Override
	public String executa() {
		return sistem.chuser(parametru);
	}
	


}
 