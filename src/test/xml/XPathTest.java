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
 * XML Node를 검색하는 정규식의 일종인 XPath를 이용하여 Node를 검색하는 예제
 * 
 * XPath syntax
 * [node명] : 노드명에 해당하는 모든 노드
 * [/]		: root node검색
 * [//]		: 위치에 상관 없이 해당하는 node를 검색
 * [.]		: 현재 위치에 있는 노드
 * [..]		: 현재 위치에서 상위에 있는 노드
 * [@~]		: 속성으로 노드를 검색
 * 
 * 예제
 * <최상위 노드를 검색하는게 아니라 중간에 있는 모든 노드를 검색할때에는 앞에 //를 붙인다>
 * XML에서 step1이란 node를 검색 : step1 
 * 
 * step1내의 step2 노드 검색 : step1/step2
 * 
 * XML에서 name속성을 가지는 노드 검색 : //@name
 * 
 * 노드 리스트중 특정위치의 값 검색 : //step1/step2[1]
 * - 주의점 IE5,6,7,8,9에서는 첫번째 노드가 [0]이지만 W3C표준에서는 첫번째 노드가[1]임 이를 해결하기 위해서는 XPath에 SelectionLanguage를 설정
 * 
 * 노드 리스트중 마지막위치 값 검색 : //step1/step2[last()]
 * 
 * 노드 리스트중 특정 index전 값 검색 : //step1/step2[position() < 3]
 * 
 * 특정 속성명을 가진 노드 검색 : //step1[@name='특정값']
 * 
 * 특정 속성명을 가진 모든노드 검색 : //*[@name='특정값']
 * 
 * 특정조건을 만족하는 노드 검색(step2 노드 안의 price 노드값이 35이상인 데이터 추출) : //step1/step2[price > 35]
 * //step1/step2[price > 35]/step3 이런식으로도 사용가능
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
