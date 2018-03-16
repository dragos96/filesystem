package ro.dragos.model.impl.comanda;

import ro.dragos.controller.Sistem;
import ro.dragos.model.spec.Comanda;

public class ComandaCD extends Comanda{

	public ComandaCD(String parametru, Sistem sistem, String numeComanda) {
		super(parametru, sistem, numeComanda);
	}

	@Override
	public String executa() {
		return sistem.cd(parametru);
	}

}
