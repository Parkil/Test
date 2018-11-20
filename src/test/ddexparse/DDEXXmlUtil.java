package test.ddexparse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;

public class DDEXXmlUtil {
	
	/**노드리스트에 포함된 날자중 가장큰 날자를 반환
	 * @param timeList
	 * @return
	 * @throws ParseException
	 */
	public static String getMaxDateStr(NodeList timeList) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		String maxTimeStr = "";
		long maxTime = Long.MIN_VALUE;
		for(int i=0 ; i<timeList.getLength() ; i++) {
			String textContent = timeList.item(i).getTextContent();
			long timeMillis = sdf.parse(textContent).getTime();
			
			if(timeMillis > maxTime) {
				maxTimeStr = textContent; 
				maxTime = timeMillis;
			}
		}
		
		return maxTimeStr;
	}
	
	/**노드리스트에 포함된 날자중 가장 작은 날자를 반환
	 * @param timeList
	 * @return
	 * @throws ParseException
	 */
	public static String getMinDateStr(NodeList timeList) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		String minTimeStr = "";
		long minTime = Long.MAX_VALUE;
		for(int i=0 ; i<timeList.getLength() ; i++) {
			String textContent = timeList.item(i).getTextContent();
			long timeMillis = sdf.parse(textContent).getTime();
			
			if(minTime > timeMillis) {
				minTimeStr = textContent; 
				minTime = timeMillis;
			}
		}
		
		return minTimeStr;
	}
	
	/** 리스트의 값을 기준으로 허가 문자열 생성
	 * @param prmtList
	 * @param xpath
	 * @return
	 * @throws XPathExpressionException
	 */
	public static String getPrmtStr(NodeList prmtList, XPath xpath) throws XPathExpressionException {
		ArrayList<String> tempList = new ArrayList<String>();
		
		for(int i = 0 ; i<prmtList.getLength() ; i++) {
			Node prmt = prmtList.item(i);
			Node comModelTypeNode = (Node)xpath.evaluate(".//CommercialModelType", prmt, XPathConstants.NODE);
			String CommercialModelType = (comModelTypeNode != null) ? comModelTypeNode.getTextContent() : "";
			
			NodeList useTypeNodeList = (NodeList)xpath.evaluate(".//UseType", prmt, XPathConstants.NODESET);
			
			ArrayList<String> useTypeList = new ArrayList<String>();
			for(int j = 0 ; j<useTypeNodeList.getLength() ; j++) {
				useTypeList.add(useTypeNodeList.item(j).getTextContent());
			}

			if("".equals(CommercialModelType) == false && useTypeNodeList.getLength() != 0) {
				tempList.add(CommercialModelType+"("+StringUtils.join(useTypeList, "♪")+")");
			}
		}
		
		if(tempList.size() == 0) {
			return null;
		}else {
			return StringUtils.join(tempList, "♩");
		}
	}
	
	/** 허가 문자열+시작일,종료일 정보 리스트 반환
	 * @param prmtList
	 * @param xpath
	 * @return
	 * @throws XPathExpressionException
	 */
	public static List<Map<String,Object>> getDtList(NodeList prmtList, XPath xpath) throws XPathExpressionException {
		ArrayList<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		
		for(int i = 0 ; i<prmtList.getLength() ; i++) {
			Node prmt = prmtList.item(i);
			Node comModelTypeNode = (Node)xpath.evaluate(".//CommercialModelType", prmt, XPathConstants.NODE);
			String CommercialModelType = (comModelTypeNode != null) ? comModelTypeNode.getTextContent() : "";
			
			NodeList useTypeNodeList = (NodeList)xpath.evaluate(".//UseType", prmt, XPathConstants.NODESET);
			
			ArrayList<String> useTypeList = new ArrayList<String>();
			for(int j = 0 ; j<useTypeNodeList.getLength() ; j++) {
				useTypeList.add(useTypeNodeList.item(j).getTextContent());
			}
			
			Node startDtNode = (Node)xpath.evaluate(".//StartDate", prmt, XPathConstants.NODE);
			Node endDtNode = (Node)xpath.evaluate(".//EndDate", prmt, XPathConstants.NODE);
			
			String startDate = (startDtNode != null) ? startDtNode.getTextContent() : null;
			String endDate = (endDtNode != null) ? endDtNode.getTextContent() : null;
			
			if("".equals(CommercialModelType) == false && useTypeNodeList.getLength() != 0) {
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("prmtStr", CommercialModelType+"("+StringUtils.join(useTypeList, " / ")+")");
				map.put("startDate", startDate);
				map.put("endDate", endDate);
				list.add(map);
			}
		}
		
		return list;
	}
}
