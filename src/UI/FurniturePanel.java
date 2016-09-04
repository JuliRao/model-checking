package UI;
import javax.swing.*;
import java.awt.*;

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

@SuppressWarnings("serial")
public class FurniturePanel extends JPanel {
	static int flag = 0;
 	public int annimationCnt = -1; 

	public FurniturePanel() {
		for (int i = 0; i < Common.Data.x_start.length; i++) {
			Common.Data.x[i] = Common.Data.x_start[i];
			Common.Data.y[i] = Common.Data.y_start[i];
		}
	}
	
	public void paintComponent(Graphics g) {
		Image house = new ImageIcon("data/images/background/House2.png").getImage();
		g.drawImage(house, 0, 0, this);
		
		for(int i = 0; i < Common.Data.imageList.size(); i++) {
			Common.imageInfo tempII = Common.Data.imageList.get(i);
			String path = "data/images/furnitures/" + tempII.orignalName + "/" + tempII.state + ".png";
			Image image = new ImageIcon(path).getImage();
			g.drawImage(image, tempII.location_x, tempII.location_y, 40, 40, this);
			g.setFont(new Font("Consolas", Font.BOLD, 8));
			g.setColor(Color.RED);
			g.drawString(tempII.furName, tempII.location_x + 5, tempII.location_y + 45);
		}
	}
	
	public void animationRefresh() {
		//init应该在check()之后被调用
		Common.animationStateSetList.initList();
		
		if(annimationCnt >= Common.animationStateSetList.ListSize - 1) return;
		annimationCnt++;

		for(int i = 0; i < Common.Data.imageList.size(); i++) {
			Common.imageInfo tempII = Common.Data.imageList.get(i);
			for(int j = 0; j < Common.animationStateSetList.ElementSize; j++) {
				if(Common.animationStateSetList.info[annimationCnt].element[j].SN == tempII.SN) {
					tempII.state = Common.animationStateSetList.info[annimationCnt].element[j].state;
					break;
				}
			}
		}
		repaint();
	}
		
	public void addDeviceRefresh() {
		Common.Data.imageList.clear();
		
		for (int i = 0; i < Common.Data.x_start.length; i++) {
			Common.Data.x[i] = Common.Data.x_start[i];
			Common.Data.y[i] = Common.Data.y_start[i];
		}

		for(int i = 0; i < Common.Info.F_Array.size(); i++) {
			Common.imageInfo imageinfo = new Common.imageInfo();
			imageinfo.orignalName = Common.Info.F_Array.get(i).furname;
			imageinfo.furName = imageinfo.orignalName + "_" + Common.Info.F_Array.get(i).SN;
			imageinfo.SN = Common.Info.F_Array.get(i).SN;
			
			int location_index = Common.Info.F_Array.get(i).furnitureInfo.loc_index;
			imageinfo.location_index = location_index;
			imageinfo.location_x = Common.Data.x[location_index];
			imageinfo.location_y = Common.Data.y[location_index];
			imageinfo.state = "off";

			Common.Data.imageList.add(imageinfo);

			// 更新坐标
			Common.Data.x[location_index] += Common.Data.interval;
			if (Common.Data.x[location_index] >= Common.Data.x_end[location_index]) {
				Common.Data.x[location_index] = Common.Data.x_start[location_index];
				Common.Data.y[location_index] += Common.Data.interval;
			}
		}
		repaint();
	}

	public void reset() {
		annimationCnt = -1;
		for(int i = 0; i < Common.Data.imageList.size(); i++) {
			Common.imageInfo tempII = Common.Data.imageList.get(i);
			tempII.state = "off";
		}
		repaint();
	}
}
