package test.xml;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
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
 * - ������ IE5,6,7,8,9������ ù��° ��尡 [0]������ W3Cǥ�ؿ����� ù��° ��尡[1]�� �̸� �ذ��ϱ� ���ؼ��� XPath�� SelectionLanguage�� ����
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
 */
public class XPathTest {
	public static void main(String[] args){
		try{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setIgnoringElementContentWhitespace(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse("d:/data_tree.xml");
			
			XPath xpath = XPathFactory.newInstance().newXPath();
		
			NodeList nList = (NodeList)xpath.evaluate("//@name", doc, XPathConstants.NODESET);
			
			System.out.println(nList.getLength());
			
			for(int i = 0 ; i<nList.getLength() ; i++) {
				Node node = nList.item(i);
				System.out.println(node);
				//System.out.println(node.getAttributes().getNamedItem("name").getNodeValue());
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
