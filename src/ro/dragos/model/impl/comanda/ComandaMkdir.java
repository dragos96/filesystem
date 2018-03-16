package ro.dragos.model.impl.comanda;

import ro.dragos.controller.Sistem;
import ro.dragos.model.spec.Comanda;

public class ComandaMkdir extends Comanda{

	public ComandaMkdir(String parametru, Sistem sistem, String numeComanda) {
		super(parametru, sistem, numeComanda);
	}

	@Override
	public String executa() {
		String rezultat = sistem.mkdir(parametru);
		return rezultat;
	}

}
