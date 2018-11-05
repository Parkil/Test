package test.xml;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
/*
 * XML Node�� �˻��ϴ� ���Խ��� ������ XPath�� �̿��Ͽ� Node�� �˻��ϴ� ����
 * 
 * XPath syntax
 * [node��] : ���� �ش��ϴ� ��� ���
 * [/]		: root node�˻�
 * [//]		: ��ġ�� ��� ���� �ش��ϴ� node�� �˻�
 * [.]		: ���� ��ġ�� �ִ� ���
 * [..]		: ���� ��ġ���� ������ �ִ� ���
 * [@~]		: �Ӽ����� ��带 �˻�
 * 
 * ����
 * <�ֻ��� ��带 �˻��ϴ°� �ƴ϶� �߰��� �ִ� ��� ��带 �˻��Ҷ����� �տ� //�� ���δ�>
 * XML���� step1�̶� node�� �˻� : step1 
 * 
 * step1���� step2 ��� �˻� : step1/step2
 * 
 * XML���� name�Ӽ��� ������ ��� �˻� : //@name
 * 
 * ��� ����Ʈ�� Ư����ġ�� �� �˻� : //step1/step2[1]
 * - ������ IE5,6,7,8,9������ ù��° ��尡 [0]������ W3Cǥ�ؿ����� ù��° ��尡[1]�� �̸� �ذ��ϱ� ���ؼ��� XPath�� SelectionLanguage�� ����(web�� javascript���� �������� �ʴ��̻��� �Ű澵 �ʿ䰡 ����)
 * 
 * ��� ����Ʈ�� ��������ġ �� �˻� : //step1/step2[last()]
 * 
 * ��� ����Ʈ�� Ư�� index�� �� �˻� : //step1/step2[position() < 3]
 * 
 * Ư�� �Ӽ����� ���� ��� �˻� : //step1[@name='Ư����']
 * 
 * Ư�� �Ӽ����� ���� ����� �˻� : //*[@name='Ư����']
 * 
 * Ư�������� �����ϴ� ��� �˻�(step2 ��� ���� price ��尪�� 35�̻��� ������ ����) : //step1/step2[price > 35]
 * //step1/step2[price > 35]/step3 �̷������ε� ��밡��
 * 
 * ��尪�� Ư�����ڿ��� �����ϴ� ��常 �˻� : //*[contains(@path,'RDR')] (path�� RDR�̶� ���� ���ԵǴ� ��常 �˻�)
 * 
 * ������ ����� ������� �˻�
 * /AAA/BBB/following-sibling::*
 * 
 * ������ �ؽ�Ʈ�� ������ �˻�
 * /AAA/BBB[contains(text(),'ZZZ')]
 * 
 * Ư�� Node�� �˻� <Name>,<Name-aaa>,<Name-222>
 * //*[contains(local-name(), 'Name')]
 * 
 * �Ʒ� �������� /AAA/BBB/following-sibling::*�� �����Ұ�� BBB�� ������ ������ �ִ� ����� XXX,CCC��尡 �˻��ȴ�.
 * ex)
 *	<AAA> 
		<BBB> 
			<CCC/> 
			<DDD/> 
		</BBB> 
		<XXX> 
			<DDD> 
				<EEE/> 
			<DDD/> 
			<CCC/> 
			<FFF/> 
			<FFF> 
				<GGG/> 
			</FFF> 
			</DDD> 
		</XXX> 
		<CCC> 
			<DDD/> 
		</CCC> 
	</AAA>
 */
public class XPathTest {
	/*
	 * �� ��庰�� �ֻ��� ~ ���������� ���ڿ��� ��ȯ�Ѵ�.
	 */
	public String getPathName(Node node) {
		StringBuffer sb = new StringBuffer();
		List<String> list = new ArrayList<String>();
		
		String parentName = "";
		Node parent = null;
		while(parentName != "root") {
			parent = node.getParentNode();
			parentName = parent.getNodeName();
			
			if(parentName.intern() != "root".intern()) {
				list.add(parent.getAttributes().getNamedItem("name").getNodeValue());
			}
			
			node = parent;
		}
		
		Collections.reverse(list);
		
		for(String str : list) {
			sb.append(str);
			sb.append(" > ");
		}
		
		return sb.toString();
	}
	
	//Document ��ü ��ȯ
	public Document getDocumnet() throws Exception{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringElementContentWhitespace(true);
		DocumentBuilder builder = factory.newDocumentBuilder();

		return builder.parse("d:/data_tree.xml");
	}
	
	//XML���������� XML���Ͽ� �ݿ�
	public void applyXML(Document doc) throws Exception {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult("d:/data_tree.xml");
		transformer.transform(source, result);
	}
	
	//Node List�� arrayList�� ��ȯ
	public List<HashMap<String,String>> getXmlList(NodeList nList) {
		List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
		for(int i = 0 ; i<nList.getLength() ; i++) {
			HashMap<String,String> map = new HashMap<String,String>();
			Node node = nList.item(i);
			NamedNodeMap nodemap = node.getAttributes();
			
			if(nodemap.getNamedItem("path") == null) {
				continue;
			}
			
			map.put("name", nodemap.getNamedItem("name").getNodeValue());
			map.put("type", nodemap.getNamedItem("type").getNodeValue());
			map.put("path", nodemap.getNamedItem("path").getNodeValue());
			map.put("rootpath", getPathName(node));
			
			list.add(map);
		}
		
		return list;
	}
	
	//Node�� �Ӽ� �߰�
	public void addAttribute(Node node) {
		Element e = (Element)node;
		e.setAttribute("test", "test");
	}
	
	//Node �߰�
	public void addNode(Document doc,Node node) {
		Element age = doc.createElement("child");
		node.getParentNode().appendChild(age);
	}
	
	public static void main(String[] args){
		XPathTest test = new XPathTest();
		List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
		
		try{
			Document doc = test.getDocumnet();
			
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			
			/*
			 * ���� ��带 �˻��Ϸ��� XPathConstants.NODE ��� ������ �˻��Ϸ��� XPathConstants.NODESET�� ����
			 * ���� NodeList�� �˻��ϴµ� XPathConstants.NODE�� ������ �ϸ� �����Ͱ� ������� �ʴ´� �����Ұ�
			 */
			//NodeList nList = (NodeList)xpath.evaluate("//*[@type='IMG']", doc, XPathConstants.NODESET);
			//NodeList nList = (NodeList)xpath.evaluate("//*[contains(@path,'RDR')]", doc, XPathConstants.NODESET);
			//NodeList nList = (NodeList)xpath.evaluate("//*[@name='��Ȳ����']/*[@name='�ռ�']/*[@name='�����׽�Ʈ']", doc, XPathConstants.NODESET);
			NodeList nList = (NodeList)xpath.evaluate("//*[@name='��Ȳ����11']/*[@name='�ռ�']/*[@name='�ǽð�(�ϱ�����)']", doc, XPathConstants.NODESET);
			
			System.out.println(nList.getLength());
			
			
			for(int i = 0 ; i<nList.getLength() ; i++) {
				Node node = nList.item(i);
				System.out.println(node);
				System.out.println(node.getAttributes().getNamedItem("type").getNodeValue());
				/*
				HashMap<String,String> map = new HashMap<String,String>();
				Node node = nList.item(i);
				NamedNodeMap nodemap = node.getAttributes();
				
				if(nodemap.getNamedItem("path") == null) {
					continue;
				}
				
				map.put("name", nodemap.getNamedItem("name").getNodeValue());
				map.put("type", nodemap.getNamedItem("type").getNodeValue());
				map.put("path", nodemap.getNamedItem("path").getNodeValue());
				map.put("rootpath", test.getPathName(node));
				
				list.add(map);*/
				
				/*
				 * node�� �Ӽ� �߰�
				 */
				//Node node = nList.item(i);
				//NamedNodeMap nodemap = node.getAttributes();
				//Element e = (Element)node;
				//e.setAttribute("test", "test");
				
				/*
				 * node �߰�
				 */
				//Element age = doc.createElement("child");
				//node.getParentNode().appendChild(age);
				
				
				//nodemap.getNamedItem("name").setTextContent("�ǽð�(��Ȳ����)");
			}
			
			//test.applyXML(doc);
			
			//System.out.println(list);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
