package UI;
import javax.swing.*;

@SuppressWarnings("serial")
public class Display extends JFrame{
	public static JPanel paneldl;
	public static JPanel panelrl;
	public static JPanel panelsl;
	
	public static int inc = 1;
	public static int X = 500;
	public static int Y = 450;
	
	public Display() {
		this.setTitle("Display");
		this.setSize(X, Y);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setLayout(null);
		
		DeviceList devicelist = new DeviceList();
		paneldl = devicelist.createPanel();
		paneldl.setBounds(0, 10, 480, 120);
		this.getContentPane().add(paneldl);
		
		RuleList rl = new RuleList();
		panelrl = rl.createPanel();
		panelrl.setBounds(0, 140, 480, 120);
		this.getContentPane().add(panelrl);
		
		SpeciList sl = new SpeciList();
		panelsl = sl.createPanel();
		panelsl.setBounds(0, 290, 480, 120);
		this.getContentPane().add(panelsl);
	}
	
	public void refresh_devicelist() {
		this.remove(paneldl);
		DeviceList devicelist = new DeviceList();
		paneldl = devicelist.createPanel();
		paneldl.setBounds(0, 10, 480, 120);
		this.add(paneldl);
		this.setSize(X, (Y += inc));
		this.repaint();
	}
	
	public void refresh_rulelist() {
		this.remove(panelrl);
		RuleList rl = new RuleList();
		panelrl = rl.createPanel();
		panelrl.setBounds(0, 140, 480, 120);
		this.add(panelrl);
		this.setSize(X, (Y += inc));
		this.repaint();
	}
	
	public void refresh_specilist() {
		this.remove(panelsl);
		SpeciList sl = new SpeciList();
		panelsl = sl.createPanel();
		panelsl.setBounds(0, 290, 480, 120);
		this.add(panelsl);
		this.setSize(X, (Y -= inc));
		this.repaint();
	}
}
