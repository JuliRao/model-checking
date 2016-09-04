package model;

import java.io.FileNotFoundException;
import java.util.Iterator;

import Common.Info;
import Common.LhaSpec;
//这不是洗澡改的
public class ChangeInfo {	
	private static int SN_num = 0;
	private static Boolean Debug = false;
	
	// add a new device
	public static void addDevice(String pathname, int loc_index, int loc_x, int loc_y) throws FileNotFoundException {
		Common.Furniture fur = jsonParser.JsonParserObject(pathname, loc_x, loc_y);
		fur.furnitureInfo.loc_index = loc_index;
		fur.SN = setSN(getSN() + 1);
		ChangeInfo.addFurSpec(fur.SN, fur.initState, null);
		
		if(Debug)
			System.out.println("add info from " + pathname + " successfully.");		
	}
	
	// add a new rule
	public static void addRule(int src_SN, String var, Common.Relation rel, int value, int dst_SN, String action) {
		Common.Rule rule = new Common.Rule();
		
		rule.srcSN = src_SN;
		rule.trigger = new Common.Trigger();
		rule.trigger.var = var;
		rule.trigger.rel = rel;
		rule.trigger.value = String.valueOf(value);
		
		rule.dstSN = dst_SN;
		rule.dstAct = action;
		
		Common.Info.R_Array.add(rule);
	}
	
	// add a new rule
	public static void addRule(int src_SN, String api, int dst_SN, String action) {
		Common.Rule rule = new Common.Rule();
		
		rule.srcSN = src_SN;
		rule.sigTrig = api;
		
		rule.dstSN = dst_SN;
		rule.dstAct = action;
		
		Common.Info.R_Array.add(rule);
	}

	// add a new specification
	public static void addSpecLha(int furnitureSN, String state, String cond) {
		changeFurSpec(furnitureSN, state, cond);
	}
	
	// add a new specification
	public static void addSpecFsm(int no, String first, String second, String spe) {
		Common.FsmSpec spec = new Common.FsmSpec();
		spec.spe = spe;
		spec.str = "CTLSPEC ";
		
		switch(no)
		{
		case 1:
			spec.str += "AG( " + first + " );"; 
			break;
		case 2:
			spec.str += "AF( " + first + " );"; 
			break;
		case 3:
			spec.str += "AG !( " + first + " );"; 
			break;
		case 4:
			spec.str += "AG( (" + first + ") -> next( " + second + " );";
			break;
		case 5:
			spec.str += "AG( (" + first + ") -> AF (" + second + " );";
			break;
		case 6:
			spec.str = "LTLSPEC G( (" + first + ") -> FG (" + second + " );";
			break;
		}
		
		Common.Info.FsmArray.add(spec);
	}
	
	// add a new specification
	public static void addSpecLha(String name, String state, String cond) {
		Common.LhaSpec spec = new Common.LhaSpec();
		spec.setName(name);
		spec.setState(state);
		spec.setVarCondition(cond);
		Common.Info.LhaArray.add(spec);
	}
	
	public static void addSpecFsm(String str, String spe) {
		Common.FsmSpec spec = new Common.FsmSpec();
		spec.str = str;
		spec.spe = spe;
		Common.Info.FsmArray.add(spec);
	}
	
	// delete a rule
	public static boolean delRule(int no) {
		if(Common.Info.R_Array.size() <= no)
			return false;
		Common.Info.R_Array.remove(no);
		
		// remove Lha specification of this rule
		Info.LhaArray.remove(Info.LhaArray.size() - 1);
		return true;
	}
	
	// delete a specification: start from 0
	public static boolean delSpecLha(int no) {
		if(Common.Info.LhaArray.get(no).getState().contains("Ready"))
			return false;
		
		Common.LhaSpec spec = Info.LhaArray.get(no);
		String name = spec.getName();
		for(Common.Furniture fur : Info.F_Array)
		{
			if(name.contains(fur.furname))
			{
				spec.setState(fur.initState);
				spec.setVarCondition(null);
				return true;
			}
		}
			
		//System.out.println("Error: no such furniture");
		return false;
	}
		
	public static boolean delSpecFsm(int no) {
		Info.FsmArray.remove(no);
		return true;
	}
		
	// delete a furniture
	public static boolean delFur(int no) {
		int SN = Info.F_Array.get(no).SN;
		String SN_str = "_" + String.valueOf(SN);
		
		// remove rules containing fur
		Iterator<Common.Rule> rListIterator = Info.R_Array.iterator();  
		while(rListIterator.hasNext()){  
			Common.Rule rule = rListIterator.next();  
		    if(rule.srcSN == SN || rule.dstSN == SN)
			{
		    	rListIterator.remove(); 
				
				// remove Lha specification of this rule
				for(int j = Info.LhaArray.size() - 1; j >= 0; --j)
				{
					Common.LhaSpec spec = Info.LhaArray.get(j);
					if(spec.getState().contains("Ready"))
					{
						Info.LhaArray.remove(j);
						break;
					}
				}
		    }  
		}  
			
		// remove fsm specification containing fur
		Iterator<Common.FsmSpec> fListIterator = Info.FsmArray.iterator();  
		while(fListIterator.hasNext()){  
			Common.FsmSpec spec = fListIterator.next();  
			if(spec.str.contains(SN_str))
			{
		    	fListIterator.remove();  
		    }  
		}
		
		// remove lha specification containing fur
		Iterator<Common.LhaSpec> lListIterator = Info.LhaArray.iterator();  
		while(lListIterator.hasNext()){  
			LhaSpec spec = lListIterator.next();  
			if(spec.getName().contains(SN_str))
			{
				lListIterator.remove();  
		    }  
		}
		
		// remove furniture
		Common.Info.F_Array.remove(no);
		return true;
	}

	// get current max-SN
	public static int getSN() {
		return SN_num;
	}

	public static int setSN(int x) {
		SN_num = x;
		return x;
	}
	
	public static void changeFurSpec(int furnitureSN, String state, String cond)
	{
		Common.Furniture fur = Common.Info.findFur(furnitureSN);
		String furName = model.Lha.furName(fur);
		for(Common.LhaSpec spec : Common.Info.LhaArray)
		{
			if(spec.getName().equals(furName))
			{
				spec.setState(state);
				spec.setVarCondition(cond);
				System.out.println(Info.getLhaSpec());
				return;
			}
		}
		
		System.out.println("Error: no such specification!");
	}
	
	public static void addFurSpec(int furnitureSN, String state, String cond)
	{
		Common.LhaSpec spec = new Common.LhaSpec();
		
		Common.Furniture fur = Common.Info.findFur(furnitureSN);
		spec.setName(model.Lha.furName(fur));
		spec.setState(state);
		spec.setVarCondition(cond);
		
		Common.Info.LhaArray.add(spec);
	}
	
	public static void addRuleSpec(int ruleNo)
	{
		Common.LhaSpec spec = new Common.LhaSpec();
		
		spec.setName(Lha.ruleName(ruleNo));
		spec.setState("Ready");
		spec.setVarCondition(null);
		
		Common.Info.LhaArray.add(spec);
	}
}
