package test.javatree;

import java.util.Iterator;

/*
 * 트리 실행 예제
 */
public class TestExample {
	public static void main(String[] args) {
		Tree tree = new Tree();
		
		//두번째 파라메터가 부모 파라메터임
		tree.addNode("Harry");
		tree.addNode("Jane", "Harry");
		tree.addNode("Bill", "Harry");
		tree.addNode("Joe", "Jane");
		tree.addNode("Diane", "Jane");
		tree.addNode("George", "Diane");
		tree.addNode("Mary", "Diane");
		tree.addNode("Jill", "George");
		tree.addNode("Carol", "Jill");
		tree.addNode("Grace", "Bill");
		tree.addNode("Mark", "Jane");

		tree.display("Harry");
		
		for(Node node : tree.getNodeListByLevel(3)) {
			System.out.println(node.getIdentifier());
		}
		
		System.out.println();
		System.out.println();
		System.out.println("===========================");
		for(Node node : tree.getNodeListToRoot("Mark")) {
			System.out.println(node.getIdentifier());
		}
		/*
		System.out.println("\n***** DEPTH-FIRST ITERATION *****");

		Iterator<Node> depthIterator = tree.iterator("Harry");

		while (depthIterator.hasNext()) {
			Node node = depthIterator.next();
			System.out.println(node.getIdentifier());
		}

		System.out.println("\n***** BREADTH-FIRST ITERATION *****");

		Iterator<Node> breadthIterator = tree.iterator("Harry",TraversalStrategy.BREADTH_FIRST);

		while (breadthIterator.hasNext()) {
			Node node = breadthIterator.next();
			System.out.println(node.getIdentifier());
		}*/
	}
}
