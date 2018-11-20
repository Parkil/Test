package test.ddexparse;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DDEXUniXmlParsing {
	static Logger logger = LoggerFactory.getLogger(DDEXUniXmlParsing.class);
	
	private Document getDocumnet(String path) throws Exception{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringElementContentWhitespace(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		
		return builder.parse(path);
	}
	
	/** DDEX 유니버셜 앨범정보 + 앨범 아티스트 반환
	 * @param doc
	 * @param xpath
	 * @param territoryCode
	 * @return
	 * @throws XPathExpressionException
	 * @throws ParseException
	 */
	public Map<String,Object> getAbmInfo(Document doc, XPath xpath, String territoryCode) throws XPathExpressionException, ParseException {
		Map<String,Object> abmInfoMap = new HashMap<String,Object>();
		
		Node abmRepNode = (Node)xpath.evaluate("//Release[@IsMainRelease='true']/ReferenceTitle/TitleText", doc, XPathConstants.NODE);
		abmInfoMap.put("ABM_REPNM",abmRepNode.getTextContent());
		
		Node abmSubRepNode = (Node)xpath.evaluate("//Release[@IsMainRelease='true']/ReferenceTitle/SubTitle", doc, XPathConstants.NODE);
		if(abmSubRepNode != null) {
			abmInfoMap.put("ABM_SUB_REPNM",abmSubRepNode.getTextContent());
		}
		
		Node abmGnrNode = (Node)xpath.evaluate("//Release[@IsMainRelease='true']/ReleaseDetailsByTerritory/TerritoryCode[text() = '"+territoryCode+"']/following-sibling::Genre/GenreText", doc, XPathConstants.NODE);
		if(abmGnrNode != null) {
			abmInfoMap.put("ABM_GNR",abmGnrNode.getTextContent());
		}
		
		Node abmIcpnNode = (Node)xpath.evaluate("//ReleaseId/ICPN[1]", doc, XPathConstants.NODE);
		abmInfoMap.put("ABM_ICPN",abmIcpnNode.getTextContent());
		
		Node abmRlsOpenDtNode = (Node)xpath.evaluate("//ReleaseDetailsByTerritory/TerritoryCode[text() = '"+territoryCode+"']/following-sibling::OriginalReleaseDate", doc, XPathConstants.NODE);
		abmInfoMap.put("ABM_RLS_DT",abmRlsOpenDtNode.getTextContent());
		abmInfoMap.put("ABM_OPEN_DT",abmRlsOpenDtNode.getTextContent());
		
		Node abmAgncyNode = (Node)xpath.evaluate("//ReleaseDetailsByTerritory/TerritoryCode[text() = '"+territoryCode+"']/following-sibling::LabelName", doc, XPathConstants.NODE);
		abmInfoMap.put("ABM_AGNCY",abmAgncyNode.getTextContent());
		
		Node abmImgNode = (Node)xpath.evaluate("//Image/ImageDetailsByTerritory//TerritoryCode[text() = '"+territoryCode+"']/following-sibling::TechnicalImageDetails/File/FileName", doc, XPathConstants.NODE);
		if(abmImgNode != null) {
			abmInfoMap.put("ABM_FILE",abmImgNode.getTextContent());
		}
		
		NodeList abmPrmtNodeList = (NodeList)xpath.evaluate("//ReleaseDeal/DealReleaseReference[text() = 'R0']/following-sibling::Deal/DealTerms", doc, XPathConstants.NODESET);
		abmInfoMap.put("ABM_PRMT",DDEXXmlUtil.getPrmtStr(abmPrmtNodeList, xpath));
		
		NodeList abmValiDateNode = (NodeList)xpath.evaluate("//ReleaseDeal/DealReleaseReference[text() = 'R0']/..//ValidityPeriod/StartDate", doc, XPathConstants.NODESET);
		abmInfoMap.put("ABM_VALI_DATE",DDEXXmlUtil.getMinDateStr(abmValiDateNode));
		
		NodeList abmStartDateNode = (NodeList)xpath.evaluate("//ReleaseDeal/DealReleaseReference[text() = 'R0']/..//ValidityPeriod/StartDate", doc, XPathConstants.NODESET);
		if(abmStartDateNode.getLength() != 0) {
			abmInfoMap.put("ABM_START_DATE",DDEXXmlUtil.getMaxDateStr(abmStartDateNode));
		}
		
		NodeList abmEndDateNode = (NodeList)xpath.evaluate("//ReleaseDeal/DealReleaseReference[text() = 'R0']/..//ValidityPeriod/EndDate", doc, XPathConstants.NODESET);
		if(abmEndDateNode.getLength() != 0) {
			abmInfoMap.put("ABM_END_DATE",DDEXXmlUtil.getMaxDateStr(abmEndDateNode));
			abmInfoMap.put("ABM_DELETE", "true");
		}
		
		List<Map<String,Object>> abmArtistList = new ArrayList<Map<String,Object>>();
		//앨범 아티스트 정보 추출
		int artistSeq = 1;
		Node artistNode = abmRepNode; //최초조건을 유지하기 위해서 임의의 현재 생성된 노드를 대입
		while(artistNode != null) {
			artistNode = (Node)xpath.evaluate("//Release[@IsMainRelease='true']/ReleaseDetailsByTerritory/TerritoryCode[text() = '"+territoryCode+"']/following-sibling::DisplayArtist[@SequenceNumber='"+artistSeq+"']", doc, XPathConstants.NODE);
			if(artistNode != null) {
				Map<String,Object> abmArtistMap = new HashMap<String,Object>();
				abmArtistMap.put("DSK_NO","0");
				abmArtistMap.put("TRCK_NO","0");
				
				Node artistNmNode = (Node)xpath.evaluate("//Release[@IsMainRelease='true']/ReleaseDetailsByTerritory/TerritoryCode[text() = '"+territoryCode+"']/following-sibling::DisplayArtist[@SequenceNumber='"+artistSeq+"']/PartyName/FullName", doc, XPathConstants.NODE);
				abmArtistMap.put("ARTST_NM", artistNmNode.getTextContent());
				
				Node artistRoleNode = (Node)xpath.evaluate("//Release[@IsMainRelease='true']/ReleaseDetailsByTerritory/TerritoryCode[text() = '"+territoryCode+"']/following-sibling::DisplayArtist[@SequenceNumber='"+artistSeq+"']/ArtistRole", doc, XPathConstants.NODE);
				abmArtistMap.put("ARTST_ROLE", artistRoleNode.getTextContent());
				
				abmArtistMap.put("ARTST_SEQ", artistSeq);
				abmArtistList.add(abmArtistMap);
			}
			
			artistSeq++;
		}
		
		List<Map<String,Object>> abmDtList = DDEXXmlUtil.getDtList(abmPrmtNodeList, xpath); //허가값 별 시작,종료일 반환
		
		for(Map<String,Object> map : abmDtList) {
			map.put("trckNo", "0");
			map.put("dskNo", "0");
		}
		
		Map<String,Object> retMap = new HashMap<String,Object>();
		retMap.put("abmInfo", abmInfoMap);
		retMap.put("abmArtistList", abmArtistList);
		retMap.put("abmDtList", abmDtList);
		
		return retMap;
	}
	
	
	/** DDEX XML 곡 정보+아티스트정보 반환
	 * @param doc
	 * @param xpath
	 * @param territoryCode
	 * @return
	 */
	public Map<String,List<Map<String,Object>>> getSngInfo(Document doc, XPath xpath, String territoryCode) throws XPathExpressionException, ParseException{
		Map<String,List<Map<String,Object>>> retMap = new HashMap<String,List<Map<String,Object>>>();
		
		//트랙별로 곡별 index와 곡 참조 ID를 검색하여 더이상 트랙이 검색되지 않을때까지 반복
		List<Map<String,Object>> sngInfoList = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> sngArtistList = new ArrayList<Map<String,Object>>();
		int listSize = 9999;
		int trackIdx = 1;
		
		List<Map<String,Object>> sngDtList = new ArrayList<Map<String,Object>>();
		while(listSize != 0) {
			NodeList sngNodeList = (NodeList)xpath.evaluate("//Release[@IsMainRelease='true']/ReleaseDetailsByTerritory/TerritoryCode[text() = '"+territoryCode+"']/following-sibling::ResourceGroup/ResourceGroup/SequenceNumber[text() = '"+trackIdx+"']/..//ResourceGroupContentItem/ResourceType[text() = 'SoundRecording']/..", doc, XPathConstants.NODESET);
			listSize = sngNodeList.getLength();
			
			System.out.println("xml[] trackNo["+trackIdx+"] listSize["+listSize+"]");
			for(int i=0 ; i<listSize ; i++) {
				Node contentItem = sngNodeList.item(i);
				NodeList contentItemList = contentItem.getChildNodes();
				
				Map<String,Object> map = new HashMap<String,Object>();
				for(int j=0,length = contentItemList.getLength() ; j<length ; j++) {
					Node contentItemSub = contentItemList.item(j);
					
					map.put("trackIdx", trackIdx);
					if(contentItemSub.getNodeType() == Node.ELEMENT_NODE) { //자식 노드중 element(tag) 노드만 검색
						map.put(contentItemSub.getNodeName(), contentItemSub.getTextContent());
					}
				}
				
				String assetId = (String)map.get("ReleaseResourceReference"); //곡자체에 대한 ID A1,A2...
				String assetRefId = (String)map.get("ResourceGroupContentItemReleaseReference"); //곡 발매정보를 가지고 있는 ID R1,R2....
				
				Node refTitleNode = (Node)xpath.evaluate("//SoundRecording/ResourceReference[text() = '"+assetId+"']/following-sibling::ReferenceTitle/TitleText", doc, XPathConstants.NODE);
				map.put("SNG_REPNM", refTitleNode.getTextContent());
				
				Node refSubTitleNode = (Node)xpath.evaluate("//SoundRecording/ResourceReference[text() = '"+assetId+"']/following-sibling::ReferenceTitle/SubTitle", doc, XPathConstants.NODE);
				if(refSubTitleNode != null) {
					map.put("SNG_SUB_REPNM", refSubTitleNode.getTextContent());
				}
				
				Node formalRepNmNode = (Node)xpath.evaluate("//SoundRecording/ResourceReference[text() = '"+assetId+"']/..//Title[@TitleType='FormalTitle']/TitleText", doc, XPathConstants.NODE);
				if(formalRepNmNode != null) {
					map.put("SNG_FORMAL_REPNM", formalRepNmNode.getTextContent());
				}
				
				Node displayRepNmNode = (Node)xpath.evaluate("//SoundRecording/ResourceReference[text() = '"+assetId+"']/..//Title[@TitleType='DisplayTitle']/TitleText", doc, XPathConstants.NODE);
				if(displayRepNmNode != null) {
					map.put("SNG_DISPLAY_REPNM", displayRepNmNode.getTextContent());
				}
				
				Node gnrNode = (Node)xpath.evaluate("//SoundRecording/ResourceReference[text() = '"+assetId+"']/..//Genre/GenreText", doc, XPathConstants.NODE);
				if(gnrNode != null) {
					map.put("SNG_GNR", gnrNode.getTextContent());
				}
				
				Node sngIsrcNode = (Node)xpath.evaluate("//SoundRecording/ResourceReference[text() = '"+assetId+"']/preceding-sibling::SoundRecordingId/ISRC", doc, XPathConstants.NODE);
				if(sngIsrcNode != null) {
					map.put("ISRC", sngIsrcNode.getTextContent());
				}
				
				Node sngFileNode = (Node)xpath.evaluate("//SoundRecording/ResourceReference[text() = '"+assetId+"']/following-sibling::SoundRecordingDetailsByTerritory/TerritoryCode[text() = '"+territoryCode+"']/following-sibling::TechnicalSoundRecordingDetails/AudioCodecType[text() = 'MP3']/following-sibling::File/FileName", doc, XPathConstants.NODE);
				if(sngFileNode != null) {
					map.put("SNG_FILE", sngFileNode.getTextContent());
				}
				
				NodeList sngPrmtNodeList = (NodeList)xpath.evaluate("//ReleaseDeal/DealReleaseReference[text() = '"+assetRefId+"']/following-sibling::Deal/DealTerms", doc, XPathConstants.NODESET);
				map.put("SNG_PRMT", DDEXXmlUtil.getPrmtStr(sngPrmtNodeList, xpath));
				
				List<Map<String,Object>> tempSngDtList = DDEXXmlUtil.getDtList(sngPrmtNodeList, xpath); //곡 허가값 별 시작,종료일 반환
				
				for(Map<String,Object> tempmap : tempSngDtList) {
					tempmap.put("trckNo", map.get("SequenceNumber"));
					tempmap.put("dskNo", trackIdx);
				}
				
				sngDtList.addAll(tempSngDtList);
				
				NodeList sngStartDateNode = (NodeList)xpath.evaluate("//ReleaseDeal/DealReleaseReference[text() = '"+assetRefId+"']/..//ValidityPeriod/StartDate", doc, XPathConstants.NODESET);
				if(sngStartDateNode.getLength() != 0) {
					map.put("SNG_START_DATE", DDEXXmlUtil.getMaxDateStr(sngStartDateNode));
				}
				
				NodeList sngEndDateNode = (NodeList)xpath.evaluate("//ReleaseDeal/DealReleaseReference[text() = '"+assetRefId+"']/..//ValidityPeriod/EndDate", doc, XPathConstants.NODESET);
				if(sngEndDateNode.getLength() != 0) {
					map.put("SNG_END_DATE", DDEXXmlUtil.getMaxDateStr(sngEndDateNode));
					map.put("SNG_DELETE", "true");
				}
				sngInfoList.add(map);
				
				//곡 아티스트 정보 추출
				int artistSeq = 1;
				Node artistNode = contentItem; //최초조건을 유지하기 위해서 임의의 현재 생성된 노드를 대입
				while(artistNode != null) {
					artistNode = (Node)xpath.evaluate("//SoundRecording/ResourceReference[text() = '"+assetId+"']/..//DisplayArtist[@SequenceNumber='"+artistSeq+"']", doc, XPathConstants.NODE);
					
					if(artistNode != null) {
						Map<String,Object> sngArtistMap = new HashMap<String,Object>();
						sngArtistMap.put("assetId", assetId);
						sngArtistMap.put("assetRefId", assetRefId);
						sngArtistMap.put("DSK_NO", map.get("trackIdx"));
						sngArtistMap.put("TRCK_NO", map.get("SequenceNumber"));
						
						Node artistNmNode = (Node)xpath.evaluate("//SoundRecording/ResourceReference[text() = '"+assetId+"']/..//DisplayArtist[@SequenceNumber='"+artistSeq+"']/PartyName/FullName", doc, XPathConstants.NODE);
						sngArtistMap.put("ARTST_NM", artistNmNode.getTextContent());
						
						Node artistRoleNode = (Node)xpath.evaluate("//SoundRecording/ResourceReference[text() = '"+assetId+"']/..//DisplayArtist[@SequenceNumber='"+artistSeq+"']/ArtistRole", doc, XPathConstants.NODE);
						sngArtistMap.put("ARTST_ROLE", artistRoleNode.getTextContent());
						
						sngArtistMap.put("ARTST_SEQ", artistSeq);
						sngArtistList.add(sngArtistMap);
					}
					
					artistSeq++;
				}
			}
			trackIdx++;
		}
		
		retMap.put("sngDtList", sngDtList);
		retMap.put("sngInfoList", sngInfoList);
		retMap.put("sngArtistList", sngArtistList);
		
		return retMap;
	}
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception{
		Document doc = new DDEXUniXmlParsing().getDocumnet("d:/UMG_Metadata_mnetgsc_00008811173326_2018-10-25_16-40-17.xml");
		XPath xpath = XPathFactory.newInstance().newXPath();
		
		//지역코드 설정
		Node territoryChkNode = (Node)xpath.evaluate("//Release[@IsMainRelease='true']/ReleaseDetailsByTerritory/TerritoryCode[text() = 'KR']", doc, XPathConstants.NODE);
		String territoryCode = "KR";
		
		if(territoryChkNode == null) {
			territoryCode = "Worldwide";
		}
		
		DDEXUniXmlParsing parse = new DDEXUniXmlParsing();
		Map<String,Object> abmRetMap = parse.getAbmInfo(doc, xpath, territoryCode);
		Map<String,List<Map<String,Object>>> sngRetMap = parse.getSngInfo(doc, xpath, territoryCode);
		
		Map<String,Object> abmInfoMap = (Map<String,Object>)abmRetMap.get("abmInfo");
		List<Map<String,Object>> abmArtistList = (List<Map<String,Object>>)abmRetMap.get("abmArtistList");
		List<Map<String,Object>> abmDtList = (List<Map<String,Object>>)abmRetMap.get("abmDtList");
		
		List<Map<String,Object>> sngInfoList = (List<Map<String,Object>>)sngRetMap.get("sngInfoList");
		List<Map<String,Object>> sngArtistList = (List<Map<String,Object>>)sngRetMap.get("sngArtistList");
		List<Map<String,Object>> sngDtList = (List<Map<String,Object>>)sngRetMap.get("sngDtList");
		
		abmArtistList.addAll(sngArtistList);
		abmDtList.addAll(sngDtList);
		
		System.out.println(abmInfoMap);
		
		for(Map<String,Object> map : sngInfoList) {
			System.out.println(map);
			//System.out.println(map.get("trackIdx")+"/"+map.get("SequenceNumber")+"/"+map.get("ReleaseResourceReference")+"/"+map.get("SNG_FILE")+"map);
		}
		System.out.println();
		for(Map<String,Object> map : abmArtistList) {
			//System.out.println(map);
			System.out.println(map.get("assetId")+"/"+map.get("assetRefId")+"/"+map.get("DSK_NO")+"/"+map.get("TRCK_NO")+"/"+map.get("ARTST_SEQ")+"/"+map.get("ARTST_NM")+"/"+map.get("ARTST_ROLE"));
		}
		System.out.println();
		for(Map<String,Object> map : abmDtList) {
			System.out.println(map);
		}
	}
}
