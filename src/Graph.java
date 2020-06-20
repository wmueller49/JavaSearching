/**
 * 
 */

/**
 * @author Owner
 *
 */

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Graph {

	private static final int ROWS = 20;
	private static final int COLS = 20;   
	
	private static final int HEIGHT = 50*ROWS;
	private static final int WIDTH = 50*COLS;
	
	private static JFrame window;
	private static JPanel p;
	
	private static Node[][] squares = new Node[ROWS][COLS];
	private Node start;
	private Node end;
	
	/**
	 * 
	 */
	public Graph(Node n1, Node n2) {
		start = n1;
		end = n2;
		
		window = new JFrame("A* Search");
		window.setSize(new Dimension(WIDTH, HEIGHT));
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		p = new JPanel();
		p.setLayout(new GridLayout(ROWS,COLS));
		
		for(int r = 0; r < 20; r++) {
			for(int c = 0; c < 20; c++) {
				Node n = new Node(false, false, r, c);
				squares[r][c] = n;
				p.add(n);
			}
		}
		
		squares[n1.getX()][n1.getY()].setStart();
		squares[n2.getX()][n2.getY()].setEnd();
		
		window.add(p);
		window.setVisible(true);
	}
	
	private ArrayList<Node> reconstructPath(Map<Node, Node> from, Node c){
		ArrayList<Node> path = new ArrayList<Node>();
		path.add(c);
		
		while(from.get(c) != null) {
			c = from.get(c);
			path.add(0, c);
		}
		return path;
	}
	
	private ArrayList<Node> AStar(){
		SortByF comp = new SortByF();
		comp.end = end;
		
		PriorityQueue<Node> openSet = new PriorityQueue<Node>(5, comp);
		openSet.add(start);
		
		Map<Node, Node> cameFrom = new HashMap<Node, Node>();
		
		Map<Node, Integer> gScore = new HashMap<Node, Integer>();
		gScore.put(start, new Integer(0));
		
		Map<Node, Integer> fScore = new HashMap<Node, Integer>(); 
		fScore.put(start, fScore(start));
		
		while(!openSet.isEmpty()) {
			Node current = openSet.peek();
			if(current.getX() == end.getX() && current.getY() == end.getY()) {
				return reconstructPath(cameFrom, current);
			}
			
			openSet.remove(current);
			
			int currentX = current.getX();
			int currentY = current.getY();
			
			int x = currentX - 1;
			int y = currentY -1;
			
			for(int i = x; i <= currentX + 1; i++) {
				for(int j = y; j <= currentY + 1; j++) {
					if(i == currentX && j == currentY) {
						
					}
					else {
						if(!(squares[i][j] == null)) {
							Node neighbor = squares[i][j];
							
							int tentativeGScore = gScore(current) + distance(current, neighbor);
							
							if(tentativeGScore <= gScore(neighbor)) {
								cameFrom.put(neighbor, current);
								gScore.put(neighbor, tentativeGScore);
								fScore.put(neighbor, fScore(neighbor));
								if(!(openSet.contains(neighbor))) {
									openSet.add(neighbor);
									neighbor.setPossiblePath(true);
									try {
										Thread.sleep(50);
									}
									catch(InterruptedException ex) {
										Thread.currentThread().interrupt();
									}
									
								}
							}
						}
					}
				}
			}
		}
		
		return null;
	}
	
	private int gScore(Node n) {
		if(n.equals(start)) {
			return 0;
		}
		else {
			return distance(n, start);
		}
	}
	
	private int fScore(Node n) {
		return gScore(n) + distance(n, end);
	}
	
	private int distance(Node n1, Node n2) {
		int x1 = n1.getX();
		int y1 = n1.getY();
		
		int x2 = n2.getX();
		int y2 = n2.getY();
		
		return (int) (Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1)));
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Node s = new Node(true, false, 1, 1);
		Node e = new Node(false, true, 18, 15);
		Graph g = new Graph(s, e);
		
		ArrayList<Node> path = g.AStar();
		
		for(Node n : path) {
			n.setPossiblePath(false);
			n.setPath(true);
			try {
				Thread.sleep(50);
			}
			catch(InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
		}
		
		
	}

}
