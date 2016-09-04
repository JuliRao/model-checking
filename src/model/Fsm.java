package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class Fsm implements Common.Check {	
	private Boolean Debug = false;
	private PrintWriter writer;
	
	private void initProc() {
		for(Common.Furniture fur : Common.Info.F_Array) {
			// modify signal variable Arraylist	
			for(Common.Action act : fur.actionArr) {
				if(act.signal == true) {
					Common.sigVar sig = new Common.sigVar();
					sig.name = act.name.toLowerCase() + "_a";
					sig.start = act.begState;
					sig.end = act.endState;
					fur.sigArr.add(sig);
				}
			}
			
			for(Common.Transition trans : fur.transArr) {
				if(trans.signal == true) {
					Common.sigVar sig = new Common.sigVar();
					sig.name = trans.name.toLowerCase() + "_t";
					sig.start = trans.begState;
					sig.end = trans.endState;
					fur.sigArr.add(sig);
				}
			}
			
			// get next state array by transitions
			for(Common.Transition trans : fur.transArr) {
				Common.transNext next = new Common.transNext();
				next.state = trans.begState;
				next.nextSta = trans.endState;
				if(trans.trigger != null)
					next.condition = getName(fur, trans.trigger.var) + trans.trigger.rel.getStr_f() + trans.trigger.value;
				
				fur.nextSta.add(next);
				if(Debug)
					System.out.println(fur.furname + " " + next.state + " " + next.condition + " " + next.nextSta);
			}
			
			// get next variable array by transitions
			for(Common.Transition trans : fur.transArr) {
				for(Common.Assignment asg : trans.asgArr) {
					Common.transNext next = new Common.transNext();
					next.state = trans.begState;
					next.nextVal = asg.valRst;
					if(trans.trigger != null)
						next.condition = getName(fur, trans.trigger.var) + trans.trigger.rel.getStr_f() + trans.trigger.value;
					Common.Variable var = Common.Info.findVar(fur, asg.var);
					
					var.nextArr.add(next);
					if(Debug)
						System.out.println(fur.furname + " " + next.state + " " + next.condition + " " + next.nextVal);
				}
			}
		}
		
		// get next state array by rules
		for(Common.Rule rule : Common.Info.R_Array) {
			Common.Furniture src = Common.Info.findFur(rule.srcSN);
			Common.Furniture fur = Common.Info.findFur(rule.dstSN);
			String con = new String();
			
			// TODO: rule set by user
			if(rule.sigTrig != null)
				con = getSig(rule.srcSN, rule.sigTrig) + "=TRUE";
			else
				con = getName(src, rule.trigger.var) + rule.trigger.rel.getStr_f() + rule.trigger.value;
			
			Common.Action act = Common.Info.findAction(fur, rule.dstAct);
			Common.transNext next = new Common.transNext();
			next.state = act.begState;
			next.nextSta = act.endState;
			next.condition = con;

			if(act.trigger != null)
				next.condition += " & " + getName(fur, act.trigger.var) + act.trigger.rel.getStr_f() + act.trigger.value;
			fur.nextSta.add(next);
			
			if(Debug)
				System.out.println(fur.furname + " " + next.state + " " + next.condition + " " + next.nextSta);
		}
		
		// get next variable array by rules
		for(Common.Rule rule : Common.Info.R_Array) {
			Common.Furniture src = Common.Info.findFur(rule.srcSN);
			Common.Furniture fur = Common.Info.findFur(rule.dstSN);
			String con = new String();
			
			// TODO: rule set by user
			if(rule.sigTrig != null)
				con = getSig(rule.srcSN, rule.sigTrig) + "=TRUE";
			else
				con = getName(src, rule.trigger.var) + rule.trigger.rel.getStr_f() + rule.trigger.value;
			
			if(Debug)
				System.out.println("con: " + con);
			
			Common.Action act = Common.Info.findAction(fur, rule.dstAct);
			for(Common.Assignment asg : act.asgArr) {
				Common.transNext next = new Common.transNext();
				next.state = act.begState;
				next.nextVal = asg.valRst;
				next.condition = con;
				if(act.trigger !=  null)
					next.condition += " & " + getName(fur, act.trigger.var) + act.trigger.rel.getStr_f() + act.trigger.value;
				
				Common.Variable var = Common.Info.findVar(fur, asg.var);
				var.nextArr.add(next);
				if(Debug)
					System.out.println(fur.furname + " " + next.state + " " + next.condition + " " + next.nextVal);
			}
		}
	}
	
	// get the name of a furniture module
	private String getName(Common.Furniture fur) {
		if(Debug)
			return fur.furname.toLowerCase() + '_' + String.valueOf(fur.SN);
		else
			return '_' + String.valueOf(fur.SN);
	}
	
	// get the name of an internal variable of furniture fur
	private String getName(Common.Furniture fur, Common.Variable var) {
		return getName(fur) + '.' + var.name;
	}
	
	// get the name of a signal variable of furniture fur
	private String getName(Common.Furniture fur, String name) {
		return getName(fur) + '.' + name;
	}
	
	// get the name of state of furniture fur
	private String getState(Common.Furniture fur) {
		return getName(fur) + ".state";
	}
	
	// get the name of a signal variable of furniture
	private String getSig(int SN, String sig) {
		Common.Furniture fur = Common.Info.findFur(SN);
		for(Common.Action act : fur.actionArr) {
			if(act.name.equals(sig))
				return getName(fur) + "." + sig.toLowerCase() + "_a";
		}
		
		for(Common.Transition trans : fur.transArr) {
			if(trans.name.equals(sig))
				return getName(fur) + "." + sig.toLowerCase() + "_t";
		}
		
		return null;
	}
		
	private void outModule() {
		for(Common.Furniture fur : Common.Info.F_Array) {
			writer.println("MODULE " + fur.furname + '_' + fur.SN);
			writer.println("VAR");
			writer.print("\tstate: {");
			
			// declare states
			for(int i = 0; i < fur.stateArr.length; ++i) {
				writer.print(fur.stateArr[i].name);
				if(i < fur.stateArr.length - 1)
					writer.print(", ");
				else
					writer.println("};");
			}
			
			// declare internal variables
			for(Common.Variable var : fur.variArr) {
				writer.print('\t' + var.name + ": ");
				writer.println(var.lowBond + ".." + var.highBond + ';');
			}
			
			// declare signal variables
			for(Common.sigVar sig : fur.sigArr) {
				writer.println('\t' + sig.name + ": boolean;");
			}
			
			// initial states and variables
			writer.println("ASSIGN");
			if(fur.initState.length() > 0 && fur.stateArr.length > 1) {
				writer.println("\tinit(state) := " + fur.initState + ';');
			}
			
			for(Common.Variable var : fur.variArr) {
				if(var.init != Integer.MAX_VALUE)
					writer.println("\tinit(" + var.name + ") := " + var.init + ';');
			}
			
			// initial signal variables
			for(Common.sigVar sig : fur.sigArr) {
				writer.println("\tinit(" + sig.name + ") := FALSE;");
			}
			
			writer.println();
		}
	}
	
	private void outMain() {
		writer.println("MODULE main");
		writer.println("VAR");
		for(Common.Furniture fur : Common.Info.F_Array) {
			writer.println('\t' + getName(fur) + ": " + fur.furname + "_" + fur.SN + ';');
		}
		
		writer.println();
		writer.println("ASSIGN");
		
		// output state transitions
		for(Common.Furniture fur : Common.Info.F_Array) {
			if(fur.nextSta.size() > 0) {	
				writer.println("\tnext(" + getState(fur) + "):=");
				writer.println("\tcase");
				for(Common.transNext next : fur.nextSta) {
					writer.print("\t\t" + getState(fur) + "=" + next.state);
					if(next.condition != null)
						writer.print(" & " + next.condition);
					writer.println(": " + next.nextSta + ";");
				}
				writer.println("\t\tTRUE: " + getState(fur) + ";");
				writer.println("\tesac;");
				writer.println();
			}
		}
		
		// output internal variable transitions
		for(Common.Furniture fur : Common.Info.F_Array) {
			for(Common.Variable var : fur.variArr) {
				writer.println("\tnext(" + getName(fur, var) + "):=");
				writer.println("\tcase");
				for(Common.transNext next : var.nextArr) {
					writer.print("\t\t" + getState(fur) + "=" + next.state);
					if(next.condition != null)
						writer.print(" & " + next.condition);
					writer.println(": " + next.nextVal + ";");
				}
				dirty(fur, var);
				writer.println("\t\tTRUE: " + getName(fur, var) + ";");
				writer.println("\tesac;");
				writer.println();
			}
		}
		
		// output signal variable transitions
		for(Common.Furniture fur : Common.Info.F_Array) {
			for(Common.sigVar sig : fur.sigArr) {
				writer.println("\tnext(" + getName(fur, sig.name) + "):=");
				writer.println("\tcase");
				writer.println("\t\t" + getState(fur) + "=" + sig.start + " " +
						"& next(" + getState(fur) + ")=" + sig.end + ": TRUE;");
				writer.println("\t\tTRUE: FALSE;");
				writer.println("\tesac;");
				writer.println();
			}
		}
	}
	
	// output specifications
	private void outSpec() {	
		for(Common.FsmSpec spec : Common.Info.FsmArray) {
			writer.println(spec.str);
		}
	}
	
	// dirty work...eh
	private void dirty(Common.Furniture fur, Common.Variable var) {
		// in case variable out of range, TODO: range!!!
		writer.print("\t\t" + getName(fur, var) + "=" + var.highBond);
		writer.println(": {toint(" + getName(fur, var) + "), toint(" + getName(fur, var) + ")-1};");
		
		writer.print("\t\t" + getName(fur, var) + "=" + var.lowBond);
		writer.println(": {toint(" + getName(fur, var) + "), toint(" + getName(fur, var) + ")+1};");
		
		// dynamic in each state
		for(Common.State s : fur.stateArr) {
			for(Common.Dynamic d : s.dynamic) {
				if(d.name.equals(var.name)) {
					String []tmp = d.rate.split("\\[|\\]|\\,");
					if(tmp.length == 1) {
						int diff = Integer.parseInt(tmp[0]);
						writer.print("\t\t" + getState(fur) + " = " + s.name + ": ");
						if(diff > 0)
							writer.println("toint(" + getName(fur, var) + ")+" + diff + ";");
						else if(diff < 0)
							writer.println("toint(" + getName(fur, var) + ")" + diff + ";");
						else
							writer.println(getName(fur, var) + ";");
					}
					else {
						int diff1 = Integer.parseInt(tmp[1]);
						int diff2 = Integer.parseInt(tmp[2]);
						writer.print("\t\t" + getState(fur) + " = " + s.name + ": ");
						if(diff1 >= 0)
							writer.print("{toint(" + getName(fur, var) + ")+" + diff1 + ", ");
						else
							writer.print("{toint(" + getName(fur, var) + ")" + diff1 + ", ");
						
						writer.print("toint(" + getName(fur) + "." + var.name + ")" + ", ");
						
						if(diff2 >= 0)
							writer.println("toint(" + getName(fur, var) + ")+" + diff2 + "};");
						else
							writer.println("toint(" + getName(fur, var) + ")" + diff2 + "};");
					}
				}
			}
		}
	}
	
	public void generate() throws FileNotFoundException {
		initProc();
		File outFile = new File("data/tst/FUR.txt");
		writer = new PrintWriter(outFile);
		outModule();
		outMain();
		outSpec();
		writer.close();
	}
	
	public void check() {
		String cmd="/home/user/NuSMV-2.5.4/NuSMV-2.5.4/nusmv/NuSMV ./data/tst/FUR.txt";
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
	}
}