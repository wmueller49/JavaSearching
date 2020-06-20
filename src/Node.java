import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

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
	private boolean isWall;
	
	private int xCoord;
	private int yCoord;
	private int state;
	
	private Node previousNode;

	/**
	 * 
	 */
	public Node(boolean s, boolean e, int x, int y) {
		
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				changeState();
			}
		});
		
		isStart = s;
		isEnd = e;
		previousNode = null;
		setX(x);
		setY(y);
		state = 0;
	}
	
	public void reset() {
		isStart = false;
		isEnd = false;
		isPath = false;
		isPossiblePath = false;
		isWall = false;
		state = 0;
		repaint();
	}
	
	public boolean isStart() {
		return isStart;
	}
	
	public boolean isEnd() {
		return isEnd;
	}
	
	public boolean isWall() {
		return isWall;
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
	
	public void changeState() {
		state++;
		
		if(state == 0) {
			isStart = false;
			isEnd = false;
		}
		else if(state == 1) {
			isStart = true;
			repaint();
		}
		else if(state == 2) {
			isStart = false;
			isEnd = true;
			repaint();
		}
		else if(state == 3) {
			isEnd = false;
			isWall = true;
		}
		else if (state == 4){
			isStart = false;
			isEnd = false;
			isWall = false;
			repaint();
			state = 0;
		}
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
		else if(isWall) {
			this.setBackground(Color.darkGray);
		}
		else {
			this.setBackground(Color.white);
		}
		Border blackline = BorderFactory.createLineBorder(Color.black);
		this.setBorder(blackline);
	}
}
