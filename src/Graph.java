/**
 * 
 */

/**
 * @author Owner
 *
 */

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

public class Graph {

	private static final int ROWS = 20;
	private static final int COLS = 20;   
	
	private static final int HEIGHT = 50*ROWS;
	private static final int WIDTH = 50*COLS;
	
	private JFrame window;
	private JPanel p;
	private JPanel buttonPanel;
	private JPanel container;
	
	private JButton b1;
	private JButton b2;
	
	
	private Node[][] squares = new Node[ROWS][COLS];
	private Node start;
	private Node end;
	
	/**
	 * 
	 */
	public Graph() {
		start = null;
		end = null;
		
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
		
		buttonPanel = new JPanel();
		buttonPanel.setPreferredSize(new Dimension(100, 100));
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
		
		b1 = new JButton("Start");
		buttonPanel.add(b1);
		b1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent s) {
				SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){
					public Void doInBackground() {
						ArrayList<Node> path = AStar();
						
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
						return null;
					}
					protected void done() {
						
					}
				};
				
				worker.execute();
			}
		});
		
		b2 = new JButton("Reset");
		buttonPanel.add(b2);
		b2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(int r = 0; r < 20; r++) {
					for(int c = 0; c < 20; c++) {
						squares[r][c].reset();
					}
				}
			}
		});
		
		container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		
		container.add(p);
		container.add(buttonPanel);
		
		window.add(container);
		window.setVisible(true);
	}
	
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
		
		buttonPanel = new JPanel();
		buttonPanel.setPreferredSize(new Dimension(100, 100));
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
		
		b1 = new JButton("Start");
		buttonPanel.add(b1);
		
		b2 = new JButton("Reset");
		buttonPanel.add(b2);
		
		container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		
		container.add(p);
		container.add(buttonPanel);
		
		window.add(container);
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
	
	public Node findStart() {
		
		for(Node[] n1 : squares) {
			for(Node n : n1) {
				if(n.isStart()) {
					return n;
				}
			}
		}
		return null;
	}
	
	public Node findEnd() {
		
		for(Node[] n1 : squares) {
			for(Node n : n1) {
				if(n.isEnd()) {
					return n;
				}
			}
		}
		return null;
	}
	
	private ArrayList<Node> AStar(){
		start = findStart();
		end = findEnd();
		
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
						if(!(i < 0 || i >= 20) && !(j < 0 || j >= 20) && !(squares[i][j].isWall())) {
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
		/**
		Node s = new Node(true, false, 1, 1);
		Node e = new Node(false, true, 18, 15);
		
		Graph g = new Graph(s, e);
		 **/
		
		Graph g = new Graph();	
		
	}

}
