package test.javatree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class Tree implements Serializable{

	private static final long serialVersionUID = 8488344715716646215L;

	private final static int ROOT = 0;

	private HashMap<String, Node> nodes;
	private TraversalStrategy traversalStrategy;

	// Constructors
	public Tree() {
		this(TraversalStrategy.DEPTH_FIRST);
	}

	public Tree(TraversalStrategy traversalStrategy) {
		this.nodes = new HashMap<String, Node>();
		this.traversalStrategy = traversalStrategy;
	}

	// Properties
	public HashMap<String, Node> getNodes() {
		return nodes;
	}

	public TraversalStrategy getTraversalStrategy() {
		return traversalStrategy;
	}

	public void setTraversalStrategy(TraversalStrategy traversalStrategy) {
		this.traversalStrategy = traversalStrategy;
	}

	// Public interface
	public Node addNode(String identifier) {
		return this.addNode(identifier, null);
	}

	public Node addNode(String identifier, String parent) {
		Node node = new Node(identifier);
		nodes.put(identifier, node);
		
		int level = 0; //node level

		if (parent != null) { //parent�� null�ΰ��� root ����ϰ����
			level = nodes.get(parent).getLevel()+1;
			nodes.get(parent).addChild(identifier);
		}
		
		node.setLevel(level);
		node.setParent_identifier(parent);
		
		return node;
	}

	public void display(String identifier) {
		this.display(identifier, ROOT);
	}

	public void display(String identifier, int depth) {
		ArrayList<String> children = nodes.get(identifier).getChildren();
		
		if (depth == ROOT) {
			System.out.println(nodes.get(identifier).getIdentifier()+"=="+nodes.get(identifier).getLevel());
		} else {
			String tabs = String.format("%0" + depth + "d", 0).replace("0", "    "); // 4 spaces
			System.out.println(tabs + nodes.get(identifier).getIdentifier()+"=="+nodes.get(identifier).getLevel());
		}
		depth++;
		for (String child : children) {
			// Recursive call
			this.display(child, depth);
		}
	}

	public Iterator<Node> iterator(String identifier) {
		return this.iterator(identifier, traversalStrategy);
	}

	public Iterator<Node> iterator(String identifier,
			TraversalStrategy traversalStrategy) {
		return traversalStrategy == TraversalStrategy.BREADTH_FIRST
				? new BreadthFirstTreeIterator(nodes, identifier)
				: new DepthFirstTreeIterator(nodes, identifier);
	}
	
	/** �Է��� Level�� �ش��ϴ� Node����Ʈ�� ��ȯ�Ѵ�.
	 * @param level
	 * @return
	 */
	public List<Node> getNodeListByLevel(int level) {
		List<Node> ret_list = new ArrayList<Node>();
		
		for(Entry<String,Node> entry : nodes.entrySet()) {
			if(entry.getValue().getLevel() == level) {
				ret_list.add(entry.getValue());
			}
		}
		
		return ret_list;
	}
	
	/** Ư�� Node�� ���� Root������ ��ο� �ش��ϴ� Node�� ��ȯ�Ѵ�.
	 * @param identifier
	 * @return
	 */
	public List<Node> getNodeListToRoot(String identifier) {
		List<Node> ret_list = new ArrayList<Node>();
		
		Node temp = nodes.get(identifier);
		ret_list.add(temp);
		
		while(temp.getLevel() != 0 && temp != null) {
			temp = nodes.get(temp.getParent_identifier());
			ret_list.add(temp);
		}
		
		return ret_list;
	}
}
