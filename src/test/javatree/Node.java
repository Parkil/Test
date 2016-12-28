package test.javatree;

import java.util.ArrayList;

/*
 * tree node
 */
public class Node {

	private String identifier; //���ĺ���
	private String parent_identifier; //�θ�ĺ���
	private int level ; //��� ����
	private ArrayList<String> children; //�ڽĳ���
	private Object attach; //��忡 �ٴ� ÷�� ��ü

	public Node(String identifier) {
		this.identifier = identifier;
		children = new ArrayList<String>();
	}
	
	public Node(String identifier, String parent_identifier) {
		this.identifier = identifier;
		this.parent_identifier = parent_identifier;
		children = new ArrayList<String>();
	}
	
	public Node(String identifier, String parent_identifier, int level) {
		this.identifier = identifier;
		this.parent_identifier = parent_identifier;
		this.level = level;
		children = new ArrayList<String>();
	}
	
	public Node(String identifier, String parent_identifier, int level, Object attach) {
		this.identifier = identifier;
		this.parent_identifier = parent_identifier;
		this.level = level;
		this.attach = attach;
		children = new ArrayList<String>();
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getParent_identifier() {
		return parent_identifier;
	}

	public void setParent_identifier(String parent_identifier) {
		this.parent_identifier = parent_identifier;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public ArrayList<String> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<String> children) {
		this.children = children;
	}

	public Object getAttach() {
		return attach;
	}

	public void setAttach(Object attach) {
		this.attach = attach;
	}
	
	// Public interface
	public void addChild(String identifier) {
		children.add(identifier);
	}
}