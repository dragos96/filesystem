package ro.dragos.security;

import ro.dragos.common.PermissionsEnum;
import ro.dragos.common.Type;

public class Entitate {

	protected String nume;
	protected User owner;
	private String permissions; // rwxrwx
	private String text;
	
	

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean areVoie(User user, PermissionsEnum permission) {
		if (user.equals(this.owner)) {
			return true;
		}

		boolean hasPermission = false;
		switch (permission) {
		case READ:
			return permissions.charAt(3) != '-';
		case WRITE:
			return permissions.charAt(4) != '-';
		case EXECUTE:
			return permissions.charAt(5) != '-';
		}
		return hasPermission;
	}

	public Type getType() {
		return Type.FISIER;
	}

	public Entitate(String nume) {
		this.nume = nume;
	}

	public Entitate(String nume, User owner, String permissions) {
		this.nume = nume;
		this.owner = owner;
		this.permissions = permissions;
	}

	public String afisareLs() {
		String result = nume + " f" + permissions + " " + owner.getUsername();

		return result;
	}

	public String getNume() {
		return nume;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public String getPermissions() {
		return permissions;
	}

	public void setPermissions(String permissions) {
		this.permissions = permissions;
	}

	public String toString() {
		return nume;
	}

}
