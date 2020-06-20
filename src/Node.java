import java.awt.Color;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 * 
 */

/**
 * @author Owner
 *
 */
public class Node extends JPanel{
	
	private boolean isStart;
	private boolean isEnd;
	private boolean isPath;
	private boolean isPossiblePath;
	
	private int xCoord;
	private int yCoord;
	
	private Node previousNode;

	/**
	 * 
	 */
	public Node(boolean s, boolean e, int x, int y) {
		isStart = s;
		isEnd = e;
		previousNode = null;
		setX(x);
		setY(y);
	}
	
	public void setStart() {
		isStart = true;
		repaint();
	}
	
	public void setEnd() {
		isEnd = true;
		repaint();
	}
	
	public void setPath(boolean b) {
		isPath = b;
		repaint();
	}
	
	public void setPossiblePath(boolean b) {
		isPossiblePath = b;
		repaint();
	}
	
	public void setX(int x) {
		xCoord = x;
	}
	
	public void setY(int y) {
		yCoord = y;
	}
	
	public int getX() {
		return xCoord;
	}
	
	public int getY() {
		return yCoord;
	}
	
	public int distanceFromEnd(Node end) {
		
		int x1 = getX();
		int y1 = getY();
		
		int x2 = end.getX();
		int y2 = end.getY();
		
		return (int) (Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1)));
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if(isStart) {
			this.setBackground(Color.green);
		}
		else if(isEnd) {
			this.setBackground(Color.red);
		}
		else if(isPossiblePath) {
			this.setBackground(Color.lightGray);
		}
		else if(isPath) {
			this.setBackground(Color.blue);
		}
		else {
			this.setBackground(Color.white);
		}
		Border blackline = BorderFactory.createLineBorder(Color.black);
		this.setBorder(blackline);
	}
}
