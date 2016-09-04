package Common;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JPanel;

/*
 * Main Bedroom (10, 175) ---- (300, 30)	40
 * Sub Bedroom (110, 380) ---- (300, 220)	40
 * Sitting Room (350, 30) ---- (580, 260)	40
 * Study (630, 30) ---- (760, 170)	40
 * Balcony (810, 30) ---- (850, 250)	40
 * Bathroom (350, 380) ---- (470, 300)	40
 * Dining room (610, 220) ---- (770, 300)	40
 * Kitchen (730, 380) ---- (850, 300)	40
 */

@SuppressWarnings("rawtypes")
public class Data {
	public static String[] devices;
	public static String[] jsonpaths;

	public static String[] locations = {"Default", "Main Bedroom", "Sub Bedroom", "Sitting room", "Study", "Balcony", "Bathroom", "Dinning room", "Kitchen"};
	public static String[] numbers = { "1", "2", "3" };

//	public static int[] x_start = {-1, 10, 110, 350, 630, 810, 350, 610, 730};
//	public static int[] y_start = {-1, 175, 380, 30, 30, 30, 380, 220, 380};
//	public static int[] x = {-1, 10, 110, 350, 630, 810, 350, 610, 730};
//	public static int[] y = {-1, 175, 380, 30, 30, 30, 380, 220, 380};

	public static int[] x_start = {-1, 20, 110, 350, 630, 810, 350, 610, 730};
	public static int[] y_start = {-1, 30, 220, 30, 30, 30, 300, 220, 300};
	public static int[] x = {-1, 10, 110, 350, 630, 810, 350, 610, 730};
	public static int[] y = {-1, 175, 380, 30, 30, 30, 380, 220, 380};

/*	public static int[] x_start = {-1, 20, 110, 350, 630, 810, 350, 610, 730};
	public static int[] y_start = {-1, 70, 260, 70, 70, 70, 340, 260, 340};
	public static int[] x = {-1, 10, 110, 350, 630, 810, 350, 610, 730};
	public static int[] y = {-1, 175, 380, 30, 30, 30, 380, 220, 380};
*/
	public static int[] x_end = {-1, 290, 270, 550, 750, 850, 470, 770, 850};
	public static int interval = 40;
		
	public static ArrayList<JComboBox> DevicesList = new ArrayList<JComboBox>();
	public static ArrayList<JComboBox> LocationsList = new ArrayList<JComboBox>();
	public static ArrayList<JComboBox> NumbersList = new ArrayList<JComboBox>();
	
	public static ArrayList<String> RuleList = new ArrayList<String>();
	public static ArrayList<String> SpeciList = new ArrayList<String>();
	public static ArrayList<imageInfo> imageList = new ArrayList<imageInfo>();
	
	public static UI.FurniturePanel furniturepanel;
	public static JPanel checkresultpanel;
	public static JPanel fixresultpanel;
	public static UI.Display display;
	
	public static String[] module_Fsm = {"Default",
		"A holds forever",
		"A will happen later",
		"A never happens",
		"IF A happens, B should happen at the same time",
		"IF A happens, B should happen later",
		"IF A happens, B should happen later and last forever"
	};
	
	public static String[] Symbol = {"Default", ">", "=", "<"};
	public static String[] Equal = {"Default", "!=", "="};
	public static String[] Attribute = {"Default", "value", "signal", "state"};
	public static String[] Ranges = {"Default", "1", "2", "3", "4", "5"};
	
	final public static int MAXN = 10;
	public static int iterator = 0;
	
	public static int getLocationIndex(String str) {
		int result = 0;
		for(int i = 0; i < locations.length; i++) {
			if(str.equals(locations[i])) {
				result = i;break;
			}
		}
		return result;
	}

}