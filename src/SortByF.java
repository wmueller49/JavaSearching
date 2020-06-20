import java.util.Comparator;

class SortByF implements Comparator<Node> {

	public Node end;
	
	public int compare(Node n1, Node n2) {
		return n1.distanceFromEnd(end) - n2.distanceFromEnd(end);
	}

}
