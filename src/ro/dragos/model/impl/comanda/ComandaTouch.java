package ro.dragos.model.impl.comanda;

import ro.dragos.controller.Sistem;
import ro.dragos.model.spec.Comanda;

public class ComandaTouch extends Comanda{

	public ComandaTouch(String parametru, Sistem sistem, String numeComanda) {
		super(parametru, sistem, numeComanda);
	}

	@Override
	public String executa() {
		String rezultat = sistem.touch(parametru);
		return rezultat;
	}

}
