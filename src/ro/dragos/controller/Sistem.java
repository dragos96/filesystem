package ro.dragos.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ro.dragos.common.PermissionsEnum;
import ro.dragos.common.PermissionsUtil;
import ro.dragos.common.Type;
import ro.dragos.composite.Director;
import ro.dragos.exceptions.PermisiuniInsuficienteException;
import ro.dragos.model.spec.Comanda;
import ro.dragos.security.Entitate;
import ro.dragos.security.User;
import ro.dragos.security.UserHelper;

public class Sistem {

	private List<User> users = new ArrayList<>();
	private Director rootDirectory = null;
	private User currentUser = null;
	private Director currentDirectory = null;
	private Comanda ultimaComandaExecutata = null;

	public void setUltimaComandaExecutata(Comanda ultimaComandaExecutata) {
		this.ultimaComandaExecutata = ultimaComandaExecutata;
	}

	public Director getRootDirectory() {
		return rootDirectory;
	}

	public Director getCurrentDirectory() {
		return currentDirectory;
	}

	public Sistem() {
		rootDirectory = new Director("/", "/");
		rootDirectory.setPermissions("rwxr-x");
		currentUser = new User("root");
		users.add(currentUser);
		rootDirectory.setOwner(currentUser);
		currentDirectory = rootDirectory;
	}

	private String addUser(User user) {

		if (UserHelper.userExists(user.getUsername(), this.users)) {
			// System.err.println("-9: "+ultimaComandaExecutata.getNumeComanda()+":
			// User already exists");
			return "-9: " + ultimaComandaExecutata.getNumeComanda() + ": User already exists\n";
		}

		users.add(user);
		Director directorUser = new Director(user.getUsername(), "/" + user.getUsername());
		directorUser.setPermissions("rwx---");
		directorUser.setOwner(user);
		rootDirectory.addEntitate(directorUser);
		return "";

	}

	public String adduser(String newUsername) {
		if (!currentUser.getUsername().equals("root")) {
			// -10: "+ultimaComandaExecutata.getNumeComanda()+": No rights to change
			// user status
			// System.err.println("-10: "+ultimaComandaExecutata.getNumeComanda()+":
			// No rights to change user status");
			return "-10: " + ultimaComandaExecutata.getNumeComanda() + ": No rights to change user status\n";
		}

		User usr = new User(newUsername);
		addUser(usr);
		return "";
	}

	private User findUser(String username) {
		for (User u : users) {
			if (u.getUsername().equals(username.trim())) {
				return u;
			}
		}
		return null;
	}

	public String chuser(String username) {
		User user = findUser(username);
		if (user == null) {
			return "-8: " + ultimaComandaExecutata.getNumeComanda() + ": User does not exist\n";
		}
		currentUser = user;
		if (user.getUsername().equals("root"))
			currentDirectory = rootDirectory;
		else
			currentDirectory = (Director) rootDirectory.getEntitate(user.getUsername());
		return "";
	}

	private Director findDirector(String dirpath, PermissionsEnum permisiune) throws PermisiuniInsuficienteException {
		if (dirpath.equals("/")) {
			return rootDirectory;
		}
		if (dirpath.charAt(0) == '/') {
			// absolute path
			dirpath = dirpath.substring(1);
			String elemente[] = dirpath.split("/");
			Director directorNavigare = rootDirectory;
			for (int i = 0; i < elemente.length; i++) {
				if (elemente[i].equals("..")) {
					if (!directorNavigare.getNume().equals("/"))
						directorNavigare = getDirectorParinte(directorNavigare.getPath(), permisiune);
				} else if (elemente[i].equals(".")) {
					// directorNavigare = directorNavigare;
				} else {
					directorNavigare = (Director) directorNavigare.getEntitate(elemente[i]);
				}
				if (permisiune == null) {

				} else if (directorNavigare == null) {
					return null;
				} else if (!directorNavigare.areVoie(currentUser, permisiune)) {
					// -6: "+ultimaComandaExecutata.getNumeComanda()+": No rights to
					// execute
					throw new PermisiuniInsuficienteException("NU AI VOIE");
				}
			}

			return directorNavigare;
		} else {
			// relative path
			String elemente[] = dirpath.split("/");
			Director directorNavigare = currentDirectory;
			for (int i = 0; i < elemente.length; i++) {
				if (elemente[i].equals("..")) {
					if (!directorNavigare.getNume().equals("/"))
						directorNavigare = getDirectorParinte(directorNavigare.getPath(), permisiune);
				}

				else if (elemente[i].equals(".")) {

				} else {
					directorNavigare = (Director) directorNavigare.getEntitate(elemente[i]);
				}
			}
			return directorNavigare;
		}
	}

	private Entitate findEntitate(String path, PermissionsEnum permission) throws PermisiuniInsuficienteException {
		if (path.equals("/")) {
			return rootDirectory;
		} else if (path.charAt(0) == '/') {
			String elemente[] = path.substring(1).split("/");
			if (elemente.length == 1) {
				Entitate e = rootDirectory.getEntitate(elemente[0]);
				return e;
			} else if (elemente.length > 1) {

				String ultimulElement = elemente[elemente.length - 1];

				String path2 = path.substring(0, path.length() - ultimulElement.length());
				if (path2.charAt(path2.length() - 1) == '/')
					path2 = path2.substring(0, path2.length() - 1);

				Director directorParinte = findDirector(path2, permission);

				Entitate e = directorParinte.getEntitate(elemente[elemente.length - 1]);

				return e;
			}
		} else {
			String elemente[] = path.substring(0).split("/");
			if (elemente.length == 1) {
				if (elemente[0].equals(".")) {
					return currentDirectory;
				}
				Entitate e = currentDirectory.getEntitate(elemente[0]);
				return e;
			} else if (elemente.length > 1) {

				String ultimulElement = elemente[elemente.length - 1];
				System.out.println("-------");
				String path2 = path.substring(0, path.length() - ultimulElement.length());
				if(path2.length() > 1){
					if(path2.charAt(path2.length()-1) == '/'){
						path2 = path2.substring(0, path2.length()-1);
					}
				}

				Director directorParinte = findDirector(path2, permission);
				String numeEntitateCautata = elemente[elemente.length - 1];
				if(numeEntitateCautata.equals(".")){
					return directorParinte;
				}
				Entitate e = directorParinte.getEntitate(elemente[elemente.length - 1]);
				return e;
			}
		}

		// TEST
		return null;
	}

	public String writeToFile(String path, String content) {
		Entitate ent = null;
		try {
			ent = findEntitate(path, PermissionsEnum.WRITE);
		} catch (PermisiuniInsuficienteException e) {
			return "-5: " + ultimaComandaExecutata.getNumeComanda() + ": No rights to write\n";
		}

		if (ent == null) {
			return "-11: " + ultimaComandaExecutata.getNumeComanda() + ": No such file\n";
		}

		if (ent.getType().equals(Type.DIRECTOR)) {
			return "-1: " + ultimaComandaExecutata.getNumeComanda() + ": Is a directory\n";
		}
		ent.setText(content);
		return "";

	}

	public String cat(String path) {
		Entitate ent = null;
		try {
			ent = findEntitate(path, PermissionsEnum.READ);
		} catch (PermisiuniInsuficienteException e) {
			return "-4: " + ultimaComandaExecutata.getNumeComanda() + ": No rights to read\n";
		}

		if (ent == null) {
			return "-11: " + ultimaComandaExecutata.getNumeComanda() + ": No such file\n";
		}

		if (ent.getType().equals(Type.DIRECTOR)) {
			return "-1: " + ultimaComandaExecutata.getNumeComanda() + ": Is a directory\n";

		}

		return ent.getText();
	}

	public boolean isChildOf(Director parinte, Director copil) {

		for (Entitate ent : parinte.getEntitati()) {
			if (ent.getType().equals(Type.DIRECTOR)) {
				Director dir = (Director) ent;
				// System.out.println("COMPARAM: " + dir.getNume() + " CU " +
				// copil.getNume());
				if (dir.getNume().equals(copil.getNume())) {
					return true;
				}
				boolean childOf = isChildOf(dir, copil);
				if (childOf)
					return isChildOf(dir, copil);
			}
		}
		return false;
	}

	public String rm(String path, boolean r) {
		Entitate ent = null;
		try {
			System.out.println("FIND ENTITATE: " + path);
			ent = findEntitate(path, null);
		} catch (PermisiuniInsuficienteException e1) {

			e1.printStackTrace();
		}
		System.out.println("ENTITATE GASITA: " + ent);
		if (!r) {
			try {

				if (ent != null) {
					if (ent.getType().equals(Type.DIRECTOR)) {
						return "-1: " + ultimaComandaExecutata.getNumeComanda() + ": Is a directory\n";
					}
					Director dir = getDirectorParinte(path, PermissionsEnum.WRITE);

					// return true;
				} else {
					return "-11: " + ultimaComandaExecutata.getNumeComanda() + ": No such file\n";
				}
			} catch (PermisiuniInsuficienteException e) {
				return "-5: " + ultimaComandaExecutata.getNumeComanda() + ": No rights to write\n";
			}
		} else {
			//
			try {
				if (ent != null) {
					Director dir = getDirectorParinte(path, PermissionsEnum.WRITE);
				} else {
					return "-12: " + ultimaComandaExecutata.getNumeComanda() + ": No such file or directory\n";
				}
			} catch (PermisiuniInsuficienteException e) {
				return "-5: " + ultimaComandaExecutata.getNumeComanda() + ": No rights to write\n";
			}
		}
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		// stergere director / fisier
		try {
			Director dirParinte = getDirectorParinte(path, PermissionsEnum.WRITE);
			if (!dirParinte.areVoie(currentUser, PermissionsEnum.WRITE)) {
				return "-5: " + ultimaComandaExecutata.getNumeComanda() + ": No rights to write\n";

			}
			Iterator<Entitate> entitati = dirParinte.getEntitati().iterator();
			while (entitati.hasNext()) {
				Entitate entitateDinParinte = entitati.next();
				if (entitateDinParinte.getNume().equals(ent.getNume())) {
					entitati.remove();
					return "";
				}
			}
		} catch (PermisiuniInsuficienteException e) {
			e.printStackTrace();
		}
		// end stergere director / fisier
		return "";
	}

	public String rmdir(String path) {
		Entitate directorul = null;
		try {
			directorul = findEntitate(path, PermissionsEnum.WRITE);
		} catch (PermisiuniInsuficienteException e) {
			return "-5: " + ultimaComandaExecutata.getNumeComanda() + ": No rights to write\n";
		}

		System.out.println("DIRECTOR GASIT: " + directorul);
		System.out.println("DIRECTOR CURENT: " + currentDirectory);
		Director dir = null;
		try {
			dir = (Director) directorul;
		} catch (ClassCastException e) {
			return "-3: " + ultimaComandaExecutata.getNumeComanda() + ": Not a directory\n";
		}

		if (dir == null) {
			return "-2: " + ultimaComandaExecutata.getNumeComanda() + ": No such directory\n";
		}

		if (dir.getNume().equals(currentDirectory.getNume())) {
			return "-13: " + ultimaComandaExecutata.getNumeComanda() + ": Cannot delete parent or current directory\n";
		}

		if (isChildOf(dir, currentDirectory)) {
			return "-13: " + ultimaComandaExecutata.getNumeComanda() + ": Cannot delete parent or current directory\n";
		}

		if (dir.getEntitati().size() != 0) {
			return "-14: " + ultimaComandaExecutata.getNumeComanda() + ": Non empty directory\n";
		}

		try {
			Director dirParinte = getDirectorParinte(path, null);
			Iterator<Entitate> entitati = dirParinte.getEntitati().iterator();
			while (entitati.hasNext()) {
				Entitate ent = entitati.next();
				if (ent.getType().equals(Type.DIRECTOR) && ent.getNume().equals(dir.getNume())) {
					entitati.remove();
					return "";
				}
			}
		} catch (PermisiuniInsuficienteException e) {
			e.printStackTrace();
		}
		return "";
	}

	public String ls(String path) {
		Entitate e = null;
		String pathClone = new String(path);
		try {
			e = findEntitate(path, PermissionsEnum.EXECUTE);
		} catch (PermisiuniInsuficienteException e1) {
			return "-6: " + ultimaComandaExecutata.getNumeComanda() + ": No rights to execute\n";
		}

		try {
			e = findEntitate(pathClone, PermissionsEnum.READ);
		} catch (PermisiuniInsuficienteException e1) {
			return "-4: " + ultimaComandaExecutata.getNumeComanda() + ": No rights to read\n";
		}

		if (e == null) {
			return "-12: " + ultimaComandaExecutata.getNumeComanda() + ": No such file or directory\n";
		}

		if (e != null) {
			String afisare = e.afisareLs();
			// if(e.getType().equals(Type.DIRECTOR))
			System.out.println(afisare);
			return afisare;
		}
		return "";
	}

	public String cd(String dirpath) {
		Director directorInitial = currentDirectory;
		try {
			try {
				currentDirectory = findDirector(dirpath, PermissionsEnum.EXECUTE);
			} catch (PermisiuniInsuficienteException e) {
				return "-6: " + ultimaComandaExecutata.getNumeComanda() + ": No rights to execute\n";
			}
		} catch (ClassCastException e) {
			// e.printStackTrace();
			// -3: "+ultimaComandaExecutata.getNumeComanda()+": Not a directory
			// System.err.println(" -3: "+ultimaComandaExecutata.getNumeComanda()+":
			// Not a directory");
			return "-3: " + ultimaComandaExecutata.getNumeComanda() + ": Not a directory\n";
		} catch (NullPointerException e) {
			// -2: "+ultimaComandaExecutata.getNumeComanda()+": No such directory
			// System.err.println(" -2: "+ultimaComandaExecutata.getNumeComanda()+":
			// No such directory");
			return "-2: " + ultimaComandaExecutata.getNumeComanda() + ": No such directory\n";
		}
		if (currentDirectory != null) {
			if (currentDirectory.areVoie(currentUser, PermissionsEnum.EXECUTE))
				return "";
			else {
				currentDirectory = directorInitial;
				return "-6: " + ultimaComandaExecutata.getNumeComanda() + ": No rights to execute\n";
			}
		} else {
			// System.err.println(" -2: "+ultimaComandaExecutata.getNumeComanda()+":
			// No such directory");
			currentDirectory = directorInitial;
			return "-2: " + ultimaComandaExecutata.getNumeComanda() + ": No such directory\n";
		}

	}

	/**
	 * 
	 * @param path
	 *            path-ul catre un fisier / director child
	 * @return
	 * @throws PermisiuniInsuficienteException
	 */
	private Director getDirectorParinte(String path, PermissionsEnum permission)
			throws PermisiuniInsuficienteException {

		if (path.equals("/")) {
			return rootDirectory;
		}

		String elemente[] = path.split("/");
		Director directorParinte = null;
		if (path.charAt(0) == '/') {
			String subPath = "";

			for (int i = 0; i < elemente.length - 1; i++) {
				subPath += elemente[i] + "/";

			}
			directorParinte = findDirector(subPath, permission);
			return directorParinte;

		} else {
			String subPath = "";
			if (elemente.length > 1) {
				for (int i = 0; i < elemente.length - 1; i++) {
					subPath += elemente[i] + "/";

				}
			} else {
				return currentDirectory;
			}
			directorParinte = findDirector(subPath, permission);
			return directorParinte;
		}

	}

	public String mkdir(String path) {

		String elemente[] = path.substring(0).split("/");
		if (elemente.length == 0)
			return "";
		if (path.equals("/"))
			return "";
		String ndn = elemente[elemente.length - 1];

		Director directorParinte = null;
		try {
			directorParinte = getDirectorParinte(path, PermissionsEnum.WRITE);
			if (directorParinte == null) {
				return "-2: " + ultimaComandaExecutata.getNumeComanda() + ": No such directory\n";
			}
		} catch (PermisiuniInsuficienteException e) {
			return "-5: " + ultimaComandaExecutata.getNumeComanda() + ": No rights to write\n";
		}

		try {
			Entitate ent = findEntitate(path, null);
			if (ent != null) {

				switch (ent.getType()) {
				case FISIER:
					// System.err.println("-3:
					// "+ultimaComandaExecutata.getNumeComanda()+": Not a
					// directory");
					return "-3: " + ultimaComandaExecutata.getNumeComanda() + ": Not a directory\n";
				case DIRECTOR:
					// System.err.println("-1:
					// "+ultimaComandaExecutata.getNumeComanda()+": Is a directory");
					return "-1: " + ultimaComandaExecutata.getNumeComanda() + ": Is a directory\n";
				}

				return null;
			}

		} catch (PermisiuniInsuficienteException e) {
			e.printStackTrace();
		}

		String numeDirectorNou = elemente[elemente.length - 1];
		Director dirNou = new Director(ndn, directorParinte.getNume().equals("/")
				? (directorParinte.getPath() + numeDirectorNou) : (directorParinte.getPath() + "/" + numeDirectorNou));
		dirNou.setPermissions("rwx---");
		dirNou.setOwner(currentUser);
		directorParinte.addEntitate(dirNou);

		return "";
	}

	public String touch(String path) {

		String elemente[] = path.substring(0).split("/");
		String ndn = elemente[elemente.length - 1];

		Director directorParinte = null;
		try {
			directorParinte = getDirectorParinte(path, PermissionsEnum.WRITE);
		} catch (PermisiuniInsuficienteException e) {
			// System.err.println("-5: "+ultimaComandaExecutata.getNumeComanda()+":
			// No rights to write");
			return "-5: " + ultimaComandaExecutata.getNumeComanda() + ": No rights to write\n";
		}

		try {
			Entitate ent = findEntitate(path, null);
			if (ent != null) {

				switch (ent.getType()) {
				case FISIER:
					// System.err.println("-7:
					// "+ultimaComandaExecutata.getNumeComanda()+": File already
					// exists");
					return "-7: " + ultimaComandaExecutata.getNumeComanda() + ": File already exists\n";
				case DIRECTOR:
					// System.err.println("-1:
					// "+ultimaComandaExecutata.getNumeComanda()+": Is a directory");
					return "-1: " + ultimaComandaExecutata.getNumeComanda() + ": Is a directory\n";
				}

				return "";
			}

		} catch (PermisiuniInsuficienteException e) {
			e.printStackTrace();
		}

		if (directorParinte.areVoie(currentUser, PermissionsEnum.WRITE)) {

			Entitate ent = new Entitate(elemente[elemente.length - 1]);
			ent.setPermissions("rwx---");
			ent.setOwner(currentUser);
			directorParinte.addEntitate(ent);

			return "";
		} else {
			return "-5: " + ultimaComandaExecutata.getNumeComanda() + ": No rights to write\n";
		}
	}

	public String deluser(String username) {

		if (!currentUser.getUsername().equals("root")) {
			// -10: "+ultimaComandaExecutata.getNumeComanda()+": No rights to change
			// user status
			// System.err.println(" -10: "+ultimaComandaExecutata.getNumeComanda()+":
			// No rights to change user status");
			return "-10: " + ultimaComandaExecutata.getNumeComanda() + ": No rights to change user status\n";
		}

		if (findUser(username) == null) {
			// System.err.println(" -8: "+ultimaComandaExecutata.getNumeComanda()+":
			// User does not exist");
			return "-8: " + ultimaComandaExecutata.getNumeComanda() + ": User does not exist\n";
		}

		Director directorulUseruluiCeVaFiSters = null;
		try {
			directorulUseruluiCeVaFiSters = findDirector("/" + username, null);
		} catch (PermisiuniInsuficienteException e) {
			// System.err.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		}
		Iterator<User> it = users.iterator();

		while (it.hasNext()) {
			User userCurent = it.next();
			if (userCurent.getUsername().equals(username)) {
				it.remove();
				break;
			}
		}

		directorulUseruluiCeVaFiSters.setOwner(users.get(1));
		int index = 0;
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).getUsername().equals(username)) {
				index = i;
				break;
			}
		}
		if (index != 0) {
			users.remove(index);
			return "";
		}
		return "";

	}

	public String chmod(String number, String path) {
		Entitate ent = null;
		try {
			if (currentUser.getUsername().equals("root")) {
				ent = findEntitate(path, null);
			} else
				ent = findEntitate(path, PermissionsEnum.WRITE);
		} catch (PermisiuniInsuficienteException e) {

			return "-5: " + ultimaComandaExecutata.getNumeComanda() + ": No rights to write\n";
		}

		if (ent == null) {
			return "-12: " + ultimaComandaExecutata.getNumeComanda() + ": No such file or directory\n";
		}

		// verificare aditionala
		if (currentUser.getUsername().equals("root")) {
			int number1 = Integer.valueOf("" + number.charAt(0));
			int number2 = Integer.valueOf("" + number.charAt(1));
			String permisiuni = null;
			permisiuni = PermissionsUtil.getStringFromList(PermissionsUtil.getPermissionsFromNumber(number1));
			permisiuni += PermissionsUtil.getStringFromList(PermissionsUtil.getPermissionsFromNumber(number2));
			ent.setPermissions(permisiuni);
			return "";
		} else if (ent.areVoie(currentUser, PermissionsEnum.WRITE)) {
			int number1 = Integer.valueOf("" + number.charAt(0));
			int number2 = Integer.valueOf("" + number.charAt(1));
			String permisiuni = null;
			permisiuni = PermissionsUtil.getStringFromList(PermissionsUtil.getPermissionsFromNumber(number1));
			permisiuni += PermissionsUtil.getStringFromList(PermissionsUtil.getPermissionsFromNumber(number2));
			ent.setPermissions(permisiuni);
			return "";
		} else {
			return "-5: " + ultimaComandaExecutata.getNumeComanda() + ": No rights to write\n";
		}
		// end verificare aditionala

	}

	private String directorToArbore(Director dir, int nivel) {
		String afisare = "";
		afisare += dir.getNume() + " d" + dir.getPermissions() + " " + dir.getOwner().getUsername() + "\n";
		for (Entitate ent : dir.getEntitati()) {
			for (int i = 0; i < nivel + 1; i++) {
				afisare += "\t";
			}
			if (ent instanceof Director) {

				afisare += directorToArbore((Director) ent, nivel + 1);
			} else {
				afisare += fisierAfisare(ent) + "\n";
			}
			// afisare += "\n";
		}
		return afisare;
	}

	private String fisierAfisare(Entitate ent) {
		String afisare = "";
		afisare += ent.getNume() + " f" + ent.getPermissions() + " " + ent.getOwner().getUsername();
		return afisare;
	}

	public String toArbore() {
		return directorToArbore(rootDirectory, 0);
	}

	public String toString() {
		String info = "Current user: " + currentUser;
		info += "\n";
		info += "Current directory name: " + currentDirectory.getNume() + " PATH curent: " + currentDirectory.getPath();
		return info;
	}

}
