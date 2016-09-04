package UI;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class DeviceList extends JPanel{
	public static JPanel displaydevice;
	public JList devicelist;
	public DefaultListModel listmodel;
	
	public JPanel createPanel() {
		displaydevice = new JPanel();
		listmodel = new DataModel(1);
		devicelist = new JList(listmodel);
		devicelist.setBorder(BorderFactory.createTitledBorder("Devices"));
		devicelist.addMouseListener(new MouseAdapter());
		
		JScrollPane scroller = new JScrollPane(devicelist);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		displaydevice.setLayout(null);
		scroller.setBounds(10, 10, 470, 100);
		displaydevice.add(scroller);
		return displaydevice;
	}
	
	class DataModel extends DefaultListModel {
		DataModel(int flag) {
			if (flag == 1) {
				System.out.println("F_Array size : " + Common.Info.F_Array.size());
				for (Common.Furniture sta : Common.Info.F_Array) {
					String name = sta.furname + "_" + Integer.toString(sta.SN);
					addElement(name);
				}
			}
		}
	}
	
	class MouseAdapter implements MouseListener {
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			if (e.getClickCount() == 2) {
				String tip = "Do you want to delete this device?";
				String options[] = {"No", "Yes"};
				int choose = JOptionPane.showOptionDialog(null, tip, "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
				System.out.println("choose : " + choose);
				System.out.println("Click");
				int index = devicelist.locationToIndex(e.getPoint());
				System.out.println("index : " + index);
				String temp = listmodel.get(index).toString();
				System.out.println("string : " + temp);
				if (choose == 1) {
					int no;
					for (no = 0; no < Common.Info.F_Array.size(); no++) {
						String name = Common.Info.F_Array.get(no).furname + "_" + Integer.toString(Common.Info.F_Array.get(no).SN);
						if (name.equals(temp)) {
							System.out.println("no : " + no);
							model.ChangeInfo.delFur(no);
							Common.Data.display.refresh_devicelist();
							Common.Data.display.refresh_rulelist();
							Common.Data.display.refresh_specilist();
							System.out.println("After delete F_Array size : " + Common.Info.F_Array.size());
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
