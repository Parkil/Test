package test.xml;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;


/*
 * XPath sibling옵션 + URL에서 XML을 가져와서 처리하는 예제
 * 
 * Xpath start-with
 * [starts-with(@id,'gbqfsa')]
 * 
 * contains
 * [contains(@id,'fsa')]
 * 
 * following-sibling과 preceding-sibling의 차이
 * ex)
 * <basin>
		<codeName>종로구</codeName>
		<code>11010</code>
		<lgtCnt>9</lgtCnt>
		<averImpact>18.03</averImpact>
		<maxImpact>32.90</maxImpact>
		<minImpact> 5.60</minImpact>
	</basin>
	
	node검색조건 뒤에 following-sibling / preceding-sibling을 붙이게 되는데
	node검색조건 위에 있으면 preceding-sibling 아래에 있으면 following-sibling으로 검색을 해야 한다.
	
	예를 들어 //basin/code 로 1차검색을 하고 codeName 데이터를 얻고 싶으면 preceding-sibling, lgtCnt,averImpact,maxImpact,minImpact 데이터를
	얻고 싶으면 following-sibling으로 검색을 해야 한다.
 * 
 */
public class XPathSiblingTest {
	public static void main(String[] args) throws Exception{
		
		String xmlurl1 = "http://211.118.10.110:62380/cgi-bin/lgtn_text_zoom?C&M&Y&201401010101&0&";
		String xmlurl2 = "http://211.118.10.110:62380/cgi-bin/lgtn_text_zoom?S&M&Y&201401010101&0&";
		
		URL url = new URL(xmlurl2);
		InputStream is = url.openStream();
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringElementContentWhitespace(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(is);
		
		XPath xpath = XPathFactory.newInstance().newXPath();
		
		//시도 Xpath검색 문자열
		String format_sido_key_str = "//basin/codeName[text() != '%1$s']";
		String format_sido_val_str = "/following-sibling::%1$s";
		
		//시군구 XPath검색 문자열
		String format_gungu_key_str = "//basin/code[starts-with(text(),'%1$s')]/preceding-sibling::codeName[text() != '%2$s']";
		String format_gungu_val_str = "/%1$s::%2$s";
		
		String xpathKey = "";
		String xpathVal = "";
		
		/*
		시도 표시 로직
		NodeList keyList = (NodeList)xpath.evaluate("//basin/codeName[text() != '합계']", doc, XPathConstants.NODESET);
		NodeList valList = (NodeList)xpath.evaluate("//basin/codeName[text() != '합계']/following-sibling::minImpact", doc, XPathConstants.NODESET);
		
		HashMap<String,String> tempMap = new HashMap<String,String>();
		
		for(int i = 0 ; i<keyList.getLength() ; i++) {
			Node key = keyList.item(i);
			Node val = valList.item(i);
			
			tempMap.put(key.getTextContent(), val.getTextContent());
		}
		
		TreeMap<String,String> resultMap = new TreeMap<String,String>(tempMap);
		
		System.out.println(resultMap.toString());
		*/
		
		
		/*시군구 테스트*/
		System.out.println(String.format(format_sido_key_str, "합계"));
		System.out.println(String.format(format_sido_val_str, "averImpact"));
		System.out.println(String.format(format_gungu_key_str, "11" , "11000"));
		System.out.println(String.format(format_gungu_val_str, "preceding-sibling","minImpact"));
		
		xpathKey = String.format(format_gungu_key_str, "11" , "합계");
		xpathVal = String.format(format_gungu_key_str, "11" , "합계")+String.format(format_gungu_val_str, "following-sibling","minImpact");
		
		NodeList keyList = (NodeList)xpath.evaluate(xpathKey, doc, XPathConstants.NODESET);
		NodeList valList = (NodeList)xpath.evaluate(xpathVal, doc, XPathConstants.NODESET);

		HashMap<String,String> tempMap = new HashMap<String,String>();
		
		for(int i = 0 ; i<keyList.getLength() ; i++) {
			Node key = keyList.item(i);
			Node val = valList.item(i);
			
			tempMap.put(key.getTextContent(), val.getTextContent());
		}
		
		TreeMap<String,String> resultMap = new TreeMap<String,String>(tempMap);
		
		System.out.println(resultMap.toString());
	}
}
