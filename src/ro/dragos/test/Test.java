package ro.dragos.test;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ro.dragos.controller.Sistem;
import ro.dragos.model.impl.comanda.ComandaFactory;
import ro.dragos.model.spec.Comanda;

public class Test {

	public static void main(String[] args) throws IOException, InterruptedException {
	
		Sistem sist = new Sistem();

		BufferedReader br = new BufferedReader(new FileReader(args[0]));
		String linie = null;
		List<Comanda> comenzi = new ArrayList<>();
		while ((linie = br.readLine()) != null) {
			String elementeComanda[] = linie.split(" ");
			Comanda com = null;
			if (elementeComanda.length == 2)
				com = ComandaFactory.createCommand(elementeComanda[0], elementeComanda[1], null, sist, linie);
			else
				com = ComandaFactory.createCommand(elementeComanda[0], elementeComanda[1], elementeComanda[2], sist, linie);

			comenzi.add(com);
			

		}
	
		
		List<String> rezultate = new ArrayList<>();
		for (Comanda c : comenzi) {
			sist.setUltimaComandaExecutata(c);
			rezultate.add(c.executa());
		}
		System.out.println("-------------------------------");
		
		StringBuilder sb = new StringBuilder("");

		for (String rezultat : rezultate) {
			sb.append(rezultat);
		}
		sb.append(sist.toArbore());
		
		String rezultatFinal = sb.toString();
		System.out.println(rezultatFinal);

		br.close();

	}

}
