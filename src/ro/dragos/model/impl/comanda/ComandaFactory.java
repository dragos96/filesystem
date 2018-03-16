package ro.dragos.model.impl.comanda;

import ro.dragos.controller.Sistem;
import ro.dragos.model.spec.Comanda;

public class ComandaFactory {

	public static Comanda createCommand(String nume, String param, Object param2, Sistem sistem, String numeComanda) {
		switch (nume) {
		case "ls":
			return new ComandaLs(param, sistem, numeComanda);
		case "mkdir":
			return new ComandaMkdir(param, sistem, numeComanda);
		case "cd":
			return new ComandaCD(param, sistem, numeComanda);
		case "touch":
			return new ComandaTouch(param, sistem, numeComanda);
		case "deluser":
			return new ComandaDelUser(param, sistem, numeComanda);
		case "adduser":
			return new ComandaAddUser(param, sistem, numeComanda);
		case "chuser":
			return new ComandaChuser(param, sistem, numeComanda);
		case "rmdir":
			return new ComandaRmdir(param, sistem, numeComanda);
		case "rm":
			if (param2 != null)
				return new ComandaRm(param, sistem, true, numeComanda);
			else
				return new ComandaRm(param, sistem, false, numeComanda);

		case "chmod":
			return new ComdandaChMod(param, sistem, (String) param2, numeComanda);
		case "cat":
			return new ComandaCat(param, sistem, numeComanda);
		case "writetofile":
			return new ComandaWriteToFile(param, sistem, (String) param2, numeComanda);
		}
		return null;
	}

}
