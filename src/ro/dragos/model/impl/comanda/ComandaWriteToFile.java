package ro.dragos.model.impl.comanda;

import ro.dragos.controller.Sistem;
import ro.dragos.model.spec.Comanda;

public class ComandaWriteToFile extends Comanda{

	private String text;
	
	public ComandaWriteToFile(String parametru, Sistem sistem, String text, String numeComanda) {
		super(parametru, sistem, numeComanda);
		this.text = text;
	}

	@Override
	public String executa() {
		return sistem.writeToFile(parametru, text);
	}

}
