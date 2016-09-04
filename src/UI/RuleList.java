package UI;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class RuleList extends JPanel{
	public DefaultListModel listmodel;
	public JList rulelist;
	
	public JPanel createPanel() {
		JPanel displayrule = new JPanel();
		
		listmodel = new DataModel(1);
		rulelist = new JList(listmodel);
		rulelist.setBorder(BorderFactory.createTitledBorder("Rules"));
		rulelist.addMouseListener(new MouseAdapter());
		
		JScrollPane scroller = new JScrollPane(rulelist);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		displayrule.setLayout(null);
		scroller.setBounds(10, 10, 470, 100);
		displayrule.add(scroller);
		return displayrule;
	}
	
	class DataModel extends DefaultListModel {
		DataModel(int flag) {
			if (flag == 1) {
				System.out.println("R_Array size : " + Common.Info.R_Array.size());
				for (Common.Rule sta : Common.Info.R_Array) {
					String rule = "";
					try {
						rule = sta.createRule();
					} catch(Exception ex) {
						ex.printStackTrace();
					}
					System.out.println("Rule : " + rule);
					addElement(rule);
				}
			}
		}
	}
	
	class MouseAdapter implements MouseListener {
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			if (e.getClickCount() == 2) {
				String tip = "Do you want to delete this rule?";
				String options[] = {"No", "Yes"};
				int choose = JOptionPane.showOptionDialog(null, tip, "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
				System.out.println("choose : " + choose);
				System.out.println("Click");
				int index = rulelist.locationToIndex(e.getPoint());
				System.out.println("index : " + index);
				String temp = listmodel.get(index).toString();
				System.out.println("string : " + temp);
				if (choose == 1) {
					int no;
					for (no = 0; no < Common.Info.R_Array.size(); no++) {
						String rule = Common.Info.R_Array.get(no).createRule();
						if (rule.equals(temp)) {
							System.out.println("no : " + no);
							model.ChangeInfo.delRule(no);
							Common.Data.display.refresh_rulelist();
							Common.Data.display.refresh_specilist();
							break;
						}
					}
				}
			}
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
	}
}
