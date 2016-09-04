package Common;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import model.Lha;

public class Info {
	static public ArrayList<Furniture> F_Array = new ArrayList<Furniture>();
	static public ArrayList<Rule> R_Array = new ArrayList<Rule>();
	static public ArrayList<LhaSpec> LhaArray = new ArrayList<LhaSpec>();
	static public ArrayList<FsmSpec> FsmArray = new ArrayList<FsmSpec>();
	static public ArrayList<FurListObj> FurList = new ArrayList<FurListObj>();
	
	static private CheckType Mode = CheckType.Fsm;
	static public CheckType tempMode = CheckType.Fsm;
	
	public static void setMode(CheckType m) {
		Mode = m;
	}
	
	public static CheckType getMode() {
		return Mode;
	}
	
	public static String getFurName(int SN) {
		Furniture fur = findFur(SN);
		String name = Lha.furName(fur);
		return name;
	}
	
	public static void Check() throws FileNotFoundException {
		if(Mode == CheckType.Fsm) {
			Check a = new model.Fsm();
			a.generate();
			a.check();
		}
		else {
			Check a = new model.Lha();
			a.generate();
			a.check();
		}
	}
	
	public static Furniture findFur(int SN) {
		for(Furniture fur : Info.F_Array) {
			if(fur.SN == SN)
				return fur;
		}
		return null;
	}
	
	public static Action findAction(int SN, String name) {
		Furniture fur = findFur(SN);
		for(Action act : fur.actionArr)
			if(act.name.equals(name))
				return act;
		return null;
	}
	
	public static Action findAction(Furniture fur, String name) {
		for(Action act : fur.actionArr)
			if(act.name.equals(name))
				return act;
		return null;
	}
	
	// find an variable of a furniture
	public static Variable findVar(Furniture fur, String name) {
		for(Variable var : fur.variArr) {
			if(var.name.equals(name))
				return var;
		}
		return null;
	}
	// important for getting Lha style specification
	static public String getLhaSpec()
	{
		String str = "forbidden={";
		for(int i = 0; i < Common.Info.LhaArray.size(); ++i)
		{
			LhaSpec spec = Common.Info.LhaArray.get(i);
			str += spec.getName() + "." + spec.getState();
			if(spec.getVarCondition() != null)
				str += "{" + spec.getVarCondition() + "}";
			
			if(i < Common.Info.LhaArray.size() - 1)
				str += "&";
		}
		
		str += "};";
		return str;
	}
}