package test.javatree;

import java.io.Serializable;
import java.util.Iterator;

import test.serialization.Object1;
import test.serialization.SerializationTest;

/*
 * Ʈ�� ���� ����
 */
public class TestExample {
	public static void main(String[] args) {
		/*
		Tree tree = new Tree();
		
		System.out.println(tree instanceof Serializable);
		
		//�ι�° �Ķ���Ͱ� �θ� �Ķ������
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
		
		
		test.save(tree);
		*/
		SerializationTest test = new SerializationTest();
		Tree recover_tree = (Tree)test.load();
		recover_tree.display("Harry");
		//System.out.println(recover.test);
		//System.out.println(recover.test2);
		
		/*
		for(Node node : tree.getNodeListByLevel(3)) {
			System.out.println(node.getIdentifier());
		}
		
		System.out.println();
		System.out.println();
		System.out.println("===========================");
		for(Node node : tree.getNodeListToRoot("Mark")) {
			System.out.println(node.getIdentifier());
		}*/
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
