package ro.dragos.model.impl.comanda;

import ro.dragos.controller.Sistem;
import ro.dragos.model.spec.Comanda;

public class ComandaRm extends Comanda {
	
	private boolean switchR = false;
	
	public ComandaRm(String parametru, Sistem sistem, boolean sr, String numeComanda) {
		super(parametru, sistem, numeComanda);
		this.switchR = sr;
	}
	
	@Override
	public String executa() {
		
		System.out.println("EXECUTAM CU: " + parametru + "    " + switchR + "   " + numeComanda);
		String elems[] = numeComanda.split(" ");
		System.out.println(elems.length);
		if(elems.length == 3){
			return sistem.rm(elems[2], switchR);
		}else{
			return sistem.rm(elems[1], switchR);
		}
		
	}

}
