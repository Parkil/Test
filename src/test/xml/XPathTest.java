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
 * - 주의점 IE5,6,7,8,9에서는 첫번째 노드가 [0]이지만 W3C표준에서는 첫번째 노드가[1]임 이를 해결하기 위해서는 XPath에 SelectionLanguage를 설정(web상 javascript에서 실행하지 않는이상은 신경쓸 필요가 없음)
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
 * 
 * 노드값이 특정문자열을 포함하는 노드만 검색 : //*[contains(@path,'RDR')] (path에 RDR이란 값이 포함되는 노드만 검색)
 * 
 * 선택한 노드의 형제노드 검색
 * /AAA/BBB/following-sibling::*
 * 
 * 노드안의 텍스트를 가지고 검색
 * /AAA/BBB[contains(text(),'ZZZ')]
 * 
 * 특정 Node명만 검색 <Name>,<Name-aaa>,<Name-222>
 * //*[contains(local-name(), 'Name')]
 * 
 * 아래 예제에서 /AAA/BBB/following-sibling::*을 실행할경우 BBB와 동일한 레벨에 있는 노드인 XXX,CCC노드가 검색된다.
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
	 * 각 노드별로 최상위 ~ 상위까지의 문자열을 반환한다.
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
	
	//Document 객체 반환
	public Document getDocumnet() throws Exception{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringElementContentWhitespace(true);
		DocumentBuilder builder = factory.newDocumentBuilder();

		return builder.parse("d:/data_tree.xml");
	}
	
	//XML수정내용을 XML파일에 반영
	public void applyXML(Document doc) throws Exception {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult("d:/data_tree.xml");
		transformer.transform(source, result);
	}
	
	//Node List를 arrayList로 변환
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
	
	//Node에 속성 추가
	public void addAttribute(Node node) {
		Element e = (Element)node;
		e.setAttribute("test", "test");
	}
	
	//Node 추가
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
			 * 단일 노드를 검색하려면 XPathConstants.NODE 노드 집합을 검색하려면 XPathConstants.NODESET을 설정
			 * 만일 NodeList를 검색하는데 XPathConstants.NODE로 설정을 하면 데이터가 추출되지 않는다 주의할것
			 */
			//NodeList nList = (NodeList)xpath.evaluate("//*[@type='IMG']", doc, XPathConstants.NODESET);
			//NodeList nList = (NodeList)xpath.evaluate("//*[contains(@path,'RDR')]", doc, XPathConstants.NODESET);
			//NodeList nList = (NodeList)xpath.evaluate("//*[@name='실황감시']/*[@name='합성']/*[@name='수정테스트']", doc, XPathConstants.NODESET);
			NodeList nList = (NodeList)xpath.evaluate("//*[@name='실황감시11']/*[@name='합성']/*[@name='실시간(일기현상)']", doc, XPathConstants.NODESET);
			
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
				 * node에 속성 추가
				 */
				//Node node = nList.item(i);
				//NamedNodeMap nodemap = node.getAttributes();
				//Element e = (Element)node;
				//e.setAttribute("test", "test");
				
				/*
				 * node 추가
				 */
				//Element age = doc.createElement("child");
				//node.getParentNode().appendChild(age);
				
				
				//nodemap.getNamedItem("name").setTextContent("실시간(실황감시)");
			}
			
			//test.applyXML(doc);
			
			//System.out.println(list);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
