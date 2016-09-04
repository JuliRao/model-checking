package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;


public class Lha implements Common.Check {
	
	private PrintWriter writer;
	private boolean Debug = false;
	private ArrayList<Common.Automate> autoArr = new ArrayList<Common.Automate>();
	
	// the name of a furniture
	public static String furName(Common.Furniture fur) {
//		if (fur == null)
//			return null;
		return fur.furname + "_" + fur.SN;
	}
	
	// the name of a rule
	static public String ruleName(int i) {
		return "Trigger" + i;
	}
	
	// the name of the variable of a rule
	private String ruleVar(int i) {
		return "Trigger" + i + "_timer";
	}
	
	// the label of a transition
	private String transLabel(String fur, String trans) {
		return fur + "_" + trans;
	}
	
	// the label of a signaled action
	private String signalLabel(String fur, String action) {
		// TODO: signal???  "Signal_" + 
		return fur + "_" + action;
	}

	// the label of the convertion of a rule
	private String ruleLabel(int src, String var, Common.Relation rel, String value, int dst, String action) {	
		Common.Furniture sfur = Common.Info.findFur(src);
		String sname = furName(sfur);
		
		Common.Furniture dfur = Common.Info.findFur(dst);
		String dname = furName(dfur);
		
		String s;
		if(rel.equals(Common.Relation.Equal))
			s = "EQ";
		else if(rel.equals(Common.Relation.Larger))
			s = "LT";
		else
			s = "ST";
		
		return sname + "_" + var + s + value + "_" + dname + "_" + action;
	}
	
	private String ruleLabel(int src, String signal, int dst, String action) {
		Common.Furniture sfur = Common.Info.findFur(src);
		String sname = furName(sfur);
		
		Common.Furniture dfur = Common.Info.findFur(dst);
		String dname = furName(dfur);

		return sname + "_" + signal + "_" + dname + "_" + action;
	}
	
	// the label when a condition satisfied
	private String conLabel(int src, String var, Common.Relation rel, String value, int idx) {
		Common.Furniture sfur = Common.Info.findFur(src);
		String name = furName(sfur);
		
		String s;
		if(rel.equals(Common.Relation.Equal))
			s = "EQ";
		else if(rel.equals(Common.Relation.Larger))
			s = "LT";
		else
			s = "ST";
		
		return "sensor_" + name + "_" + var + s + value + "_" + idx;
	}
	
	private String conLabel(int src, String signal, int idx) {
		Common.Furniture sfur = Common.Info.findFur(src);
		String name = furName(sfur);
		return name + "_" + signal;
	}
	
	// find if a signal variable signal_t is needed
	private boolean findSig(Common.Furniture fur) {
		for(Common.Transition trans : fur.transArr) {
			if(trans.signal == true)
				return true;
		}
		for(Common.Action act : fur.actionArr) {
			if(act.signal == true)
				return true;
		}
		return false;
	}
	
	// find the automation of a furniture in the array
	private Common.Automate findAuto(int SN) {
		for(Common.Automate auto : autoArr) {
			if(auto.name.contains(String.valueOf(SN)))
				return auto;
		}
		return null;
	}
	
	private void convertToAuto() {
		addFurAuto();
		addRuleAuto();
	}
	
	private void addFurAuto() {
		for(Common.Furniture fur : Common.Info.F_Array) {
			Common.Automate auto = new Common.Automate();
			auto.name = furName(fur);
			auto.signal = findSig(fur);
			
			// whether the furniture has an internal variable
			if(fur.variArr.length > 0) {
				auto.hasSensor = true;
				auto.variable = fur.variArr[0].name;
			}
			else {
				auto.hasSensor = false;
				auto.signal = true;
			}
			autoArr.add(auto);
			if(Debug)
				System.out.println(auto.name + " " + auto.variable);
					
			// add location S0
			Common.Location init = new Common.Location();
			init.name = "S0";
			init.invariant = "true";
			if(auto.signal)
				init.dynamic = "signal_t'==0";
			if(auto.hasSensor) {
				if(auto.signal)
					init.dynamic += "&";
				init.dynamic += auto.variable + "'==0";
			}
			auto.locArr.add(init);
			if(Debug)
				System.out.println("S0" + " " + init.dynamic + " true");
			
			// add convertion from S0 to initial state
			Common.Convert beg = new Common.Convert();
			beg.from = "S0";
			beg.to = fur.initState;
			beg.condition = "true";
			beg.label = auto.name + "_init";
			if(auto.hasSensor && fur.variArr[0].init != Integer.MAX_VALUE)
				beg.asg = auto.variable + "':=" + fur.variArr[0].init;
			auto.conArr.add(beg);
			
			// add locations based on states
			for(Common.State state : fur.stateArr) {
				Common.Location loc = new Common.Location();
				loc.name = state.name;
				loc.invariant = state.invariant;
				loc.getDyna(state, auto.signal);
				
				auto.locArr.add(loc);
				//if(Debug)
					//System.out.println("Location: " + loc.name + " " + loc.dynamic + " " + loc.invariant);
			}
			
			// add convertions based on transitions
			for(Common.Transition trans : fur.transArr) {
				Common.Convert con = new Common.Convert();
				con.from = trans.begState;
				con.to = trans.endState;
				con.getCond(trans);
				con.getAsg(trans);
				con.label = transLabel(auto.name, trans.name);
				auto.conArr.add(con);
				
				if(Debug)
					System.out.println("Convert: " + con.from + " " + con.to + " " + con.label + " " + con.asg + " " + con.condition);
			}
			
			// add locations and convertions based on signaled actions
			for(Common.Action act : fur.actionArr) {
				if(act.signal) {
					Common.Location loc = new Common.Location();
					loc.name = "Signal_" + act.name;
					loc.invariant = "signal_t==0";
					loc.sigGetDyna(auto.variable);
					auto.locArr.add(loc);
					if(Debug)
						System.out.println("Location: " + loc.name + " " + loc.dynamic + " " + loc.invariant);
					
					Common.Convert con = new Common.Convert();
					con.from = loc.name;
					con.to = act.endState;
					con.condition = "true";
					con.asg = "signal_t':=0";
					con.label = signalLabel(auto.name, act.name);
					
					auto.conArr.add(con);
					if(Debug)
						System.out.println("Convert: " + con.from + " " + con.to + " " + con.label + " " + con.asg + " " + con.condition);
				}
			}
		
			if(Debug)
				System.out.println();
		}
	}
	
	private void addRuleAuto() {
		// add automations based on rules
		int idx = 0;
		for(Common.Rule rule : Common.Info.R_Array) {
			Common.Automate auto = new Common.Automate();
			auto.name = ruleName(idx);
			auto.signal = false;
			auto.hasSensor = true;
			auto.variable = ruleVar(idx);
			autoArr.add(auto);
			if(Debug)
				System.out.println(auto.name + " " + auto.variable);
			
			// add location S0
			Common.Location loc = new Common.Location();
			loc.name = "S0";
			loc.invariant = "true";
			loc.dynamic = auto.variable + "'==0";
			auto.locArr.add(loc);
			if(Debug)
				System.out.println("Location: " + loc.name + " " + loc.dynamic + " " + loc.invariant);
			
			Common.Convert con = new Common.Convert();
			con.from = "S0";
			con.to = "Ready";
			con.condition = "true";
			con.label = auto.name + "_init";
			con.asg = auto.variable + "':=0";
			auto.conArr.add(con);
			if(Debug)
				System.out.println("Convert: " + con.from + " " + con.to + " " + con.label + " " + con.asg + " " + con.condition);
			
			// add location ready
			Common.Location ready = new Common.Location();
			ready.name = "Ready";
			ready.invariant = "true";
			ready.dynamic = auto.variable + "'==0";
			auto.locArr.add(ready);
			if(Debug)
				System.out.println("Location: " + ready.name + " " + ready.dynamic + " " + ready.invariant);
			
			Common.Convert con1 = new Common.Convert();
			con1.from = "Ready";
			con1.to = "Waiting";
			con1.condition = "true";
			if(rule.sigTrig != null)
				con1.label = conLabel(rule.srcSN, rule.sigTrig, idx);
			else
				con1.label = conLabel(rule.srcSN, rule.trigger.var, rule.trigger.rel, rule.trigger.value, idx);
			con1.asg = auto.variable + "':=0";
			auto.conArr.add(con1);
			if(Debug)
				System.out.println("Convert: " + con1.from + " " + con1.to + " " + con1.label + " " + con1.asg + " " + con1.condition);
			
			// add location waiting
			Common.Location waiting = new Common.Location();
			waiting.name = "Waiting";
			waiting.invariant = auto.variable + "<=5";
			waiting.dynamic = auto.variable + "'==1";
			auto.locArr.add(waiting);
			if(Debug)
				System.out.println("Location: " + waiting.name + " " + waiting.dynamic + " " + waiting.invariant);
			
			Common.Convert con2 = new Common.Convert();
			con2.from = "Waiting";
			con2.to = "Ready";
			con2.condition = auto.variable + ">=3";
			con2.asg = auto.variable + "':=0";
			if(rule.sigTrig != null)
				con2.label = ruleLabel(rule.srcSN, rule.sigTrig, rule.dstSN, rule.dstAct);
			else
				con2.label = ruleLabel(rule.srcSN, rule.trigger.var, rule.trigger.rel, rule.trigger.value, rule.dstSN, rule.dstAct);
			auto.conArr.add(con2);
			if(Debug)
				System.out.println("Convert: " + con2.from + " " + con2.to + " " + con2.label + " " + con2.asg + " " + con2.condition);
		
			// add convertions to src furniture
			Common.Automate src = findAuto(rule.srcSN);
			if(rule.sigTrig == null) {
				for(Common.Location l : src.locArr) {
					if(l.name.contains("S0") == false && l.name.contains("Signal") == false) {
						Common.Convert tmp = new Common.Convert();
						tmp.from = l.name;
						tmp.to = l.name;
						tmp.condition = rule.trigger.var + rule.trigger.rel.getStr_l() + rule.trigger.value;
						tmp.label = conLabel(rule.srcSN, rule.trigger.var, rule.trigger.rel, rule.trigger.value, idx);
						src.conArr.add(tmp);
					}
				}
			}
			
			// add convertions to dst furniture
			Common.Automate dst = findAuto(rule.dstSN);
			Common.Action act = Common.Info.findAction(rule.dstSN, rule.dstAct);
			Common.Convert trans = new Common.Convert();
			trans.from = act.begState;
			trans.to = "Signal_" + act.name;
			trans.condition = "true";
			
			trans.getAsg(act);
			if(rule.sigTrig != null)
				trans.label = ruleLabel(rule.srcSN, rule.sigTrig, rule.dstSN, rule.dstAct);
			else
				trans.label = ruleLabel(rule.srcSN, rule.trigger.var, rule.trigger.rel, rule.trigger.value, rule.dstSN, rule.dstAct);
			dst.conArr.add(trans);
			
			++idx;
			if(Debug)
				System.out.println();
		}
	}
	
	private void outInit(int depth) {
		writer.println("composedautomaton test");
		writer.println("variable");
		writer.println("relation:true;");
		writer.println("automaton");
		for(Common.Automate auto : autoArr)
			writer.println(auto.name + ":" + auto.name + "(" + depth + ");");
	}
	
	void outSpec() {
		writer.println(Common.Info.getLhaSpec());
	}
	
	public void generate() throws FileNotFoundException {
		convertToAuto();
		File outFile = new File("data/tst/FUR.txt");
		writer = new PrintWriter(outFile);
		outInit(10);
		writer.println();
		for(Common.Automate auto : autoArr)
			auto.print(writer);
		outSpec();
		writer.close();
	}
	
	public void check() {
		String cmd="/home/user/Lha/src/bach ./data/tst/FUR.txt";
		System.out.println(cmd);
		try {
			Process p = Runtime.getRuntime().exec(cmd); 
			BufferedReader output = new BufferedReader(new InputStreamReader(p.getInputStream()));		
			String line;
			while((line = output.readLine()) != null){
				System.out.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		//BufferedReader output = new BufferedReader(new InputStreamReader(p.getInputStream()));
	}
}
