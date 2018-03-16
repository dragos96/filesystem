package ro.dragos.common;


import java.util.ArrayList;
import java.util.List;

public class PermissionsUtil {

	public static List<PermissionsEnum> getPermissionsFromNumber(int nr){
		List<PermissionsEnum> permisiuni = new ArrayList<>();
		switch(nr){
		case 0:
			break;
		case 1:
			permisiuni.add(PermissionsEnum.EXECUTE);
			break;
		case 2:
			permisiuni.add(PermissionsEnum.WRITE);
			break;
		case 3:
			permisiuni.add(PermissionsEnum.WRITE);
			permisiuni.add(PermissionsEnum.EXECUTE);
			break;
		case 4:
			permisiuni.add(PermissionsEnum.READ);
			break;
		case 5:
			permisiuni.add(PermissionsEnum.READ);
			permisiuni.add(PermissionsEnum.EXECUTE);
			break;
		case 6:
			permisiuni.add(PermissionsEnum.READ);
			permisiuni.add(PermissionsEnum.WRITE);
			break;
		case 7:
			permisiuni.add(PermissionsEnum.READ);
			permisiuni.add(PermissionsEnum.WRITE);
			permisiuni.add(PermissionsEnum.EXECUTE);
			break;
		}
		return permisiuni;
	}
	
	
	public static String getStringFromList(List<PermissionsEnum> permisiuni){
		String permisiuniString = "---";
		if(permisiuni.contains(PermissionsEnum.READ)){
			permisiuniString = "r"+permisiuniString.substring(1);
		}
		if(permisiuni.contains(PermissionsEnum.WRITE)){
			permisiuniString = ""+ permisiuniString.charAt(0) + "w" + permisiuniString.charAt(2);
		}
		if(permisiuni.contains(PermissionsEnum.EXECUTE)){
			permisiuniString = ""+ permisiuniString.charAt(0) + permisiuniString.charAt(1) + "x";
		}
		return permisiuniString;
	}
	
	public static void main(String[] args) {
		
		System.out.println(getStringFromList(getPermissionsFromNumber(7)));
		System.out.println(getStringFromList(getPermissionsFromNumber(4)));
		
	}
	
}
