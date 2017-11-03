package temp;

import java.io.StringWriter;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class GenerateAdRequestXML {
	//나중에 광고관련 데이터가 입력되는 노드
	private Element Impression;
	private Element TrackingEvents;
	private Element VideoClicks;
	private Element MediaFiles;
	
	private Document doc;
	
	/**광고 xml 기본 템플릿 생성
	 * @param ad_id 광고 아이디
	 * @param sequence 광고 순차
	 * @param skipoffset 광고 최소 시청 시간
	 * @param duration 광고 기간
	 * @throws ParserConfigurationException xml생성 도중 오류가 발생할 경우
	 */
	private void generateBasicTemplate(String ad_id, String sequence, String skipoffset, String duration) throws ParserConfigurationException {
		if(!chkNumStr(ad_id)) {
			throw new InvalidParameterException("ad_id must numeric string");
		}
		
		if(!chkNumStr(sequence)) {
			throw new InvalidParameterException("sequence must numeric string");
		}
		
		if(!chkTimeStr(skipoffset)) {
			throw new InvalidParameterException("skipoffset must time string ex)01:23:22");
		}
		
		if(!chkTimeStr(duration)) {
			throw new InvalidParameterException("duration must time string ex)01:23:22");
		}
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		
		doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("VAST");
		rootElement.setAttribute("version", "3.0");
		doc.appendChild(rootElement);

		//AD
		Element Ad = doc.createElement("Ad");
		Ad.setAttribute("id", ad_id);
		Ad.setAttribute("sequence", sequence);
		rootElement.appendChild(Ad);
		
		Element InLine = doc.createElement("InLine");
		Ad.appendChild(InLine);
		
		Element AdSystem = doc.createElement("AdSystem");
		AdSystem.appendChild(doc.createTextNode("NetInsight VIDEO AD SERVER"));
		InLine.appendChild(AdSystem);
		
		Element AdTitle = doc.createElement("AdTitle");
		AdTitle.appendChild(doc.createTextNode("NetInsight VIDEO AD"));
		InLine.appendChild(AdTitle);
		
		Impression = doc.createElement("Impression");
		InLine.appendChild(Impression);
		
		Element Creatives = doc.createElement("Creatives");
		InLine.appendChild(Creatives);
		
		Element Creative = doc.createElement("Creative");
		Creatives.appendChild(Creative);
		
		//Creative ==================
		Element Linear = doc.createElement("Linear");
		Linear.setAttribute("skipoffset", skipoffset);
		Creative.appendChild(Linear);
		
		Element Duration = doc.createElement("Duration");
		Duration.appendChild(doc.createTextNode(duration));
		Linear.appendChild(Duration);
		
		TrackingEvents = doc.createElement("TrackingEvents");
		Linear.appendChild(TrackingEvents);
		
		VideoClicks = doc.createElement("VideoClicks");
		Linear.appendChild(VideoClicks);
		
		MediaFiles = doc.createElement("MediaFiles");
		Linear.appendChild(MediaFiles);
		//-Creative ==================
	}
	
	
	/** Impression tracking url 입력
	 * @param impressionUrl tracking url
	 */
	private void setImpression(String impressionUrl) {
		Impression.appendChild(doc.createCDATASection(impressionUrl));
	}
	
	
	/** 광고 동영상 정보 입력
	 * @param mediaUrl 광고 동영사 url
	 * @param delivery 
	 * @param type 비디오 타입
	 * @param width 동영상 넓이
	 * @param height 동영상 높이
	 */
	private void setMediaFile(String mediaUrl, String delivery, String type, String width, String height) {
		if(!chkNumStr(width)) {
			throw new InvalidParameterException("width must numeric string");
		}
		
		if(!chkNumStr(height)) {
			throw new InvalidParameterException("height must numeric string");
		}
		
		Element MediaFile = doc.createElement("MediaFile");
		MediaFile.setAttribute("delivery", delivery);
		MediaFile.setAttribute("type", type);
		MediaFile.setAttribute("width", width);
		MediaFile.setAttribute("height", height);
		MediaFiles.appendChild(MediaFile);
		
		MediaFile.appendChild(doc.createCDATASection(mediaUrl));
	}
	
	/**광고영상 클릭 데이터 입력
	 * @param ClickThroughUrl
	 * @param ClickTrackingUrl
	 */
	private void setVideoClick(String ClickThroughUrl, String ClickTrackingUrl) {
		Element ClickThrough = doc.createElement("ClickThrough");
		ClickThrough.appendChild(doc.createCDATASection(ClickThroughUrl));
		
		Element ClickTracking = doc.createElement("ClickTracking");
		ClickTracking.appendChild(doc.createCDATASection(ClickTrackingUrl));
		
		VideoClicks.appendChild(ClickThrough);
		VideoClicks.appendChild(ClickTracking);
	}
	
	private void setTrackingEvents(Map<String,String> event_map, String progress_offset) {
		if(!chkTimeStr(progress_offset)) {
			throw new InvalidParameterException("skipoffset must time string ex)01:23:22");
		}
		
		String key_arr[] = {"start","firstQuartile","midpoint","thirdQuartile","complete","skip","progress"};
		
		Element Tracking = null;
		for(String key : key_arr) {
			Tracking = doc.createElement("Tracking");
			Tracking.setAttribute("event", key);
			
			if("progress".equals(key)) {
				Tracking.setAttribute("offset", progress_offset);
			}
			
			Tracking.appendChild(doc.createCDATASection(event_map.get(key)));
			TrackingEvents.appendChild(Tracking);
		}
	}
	
	/**생성된 xml을 문자열 형식으로 반환
	 * @return xml문자열
	 * @throws TransformerException document객체가 존재하지 않거나 xml문자열 변환시 오류가 발생한 경우
	 */
	private String getXmlStr() throws TransformerException {
		if(doc == null)  {
			throw new TransformerException("document is null");
		}
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		
		transformer.transform(source, result);
		return writer.toString();
	}
	/**시간 문자열 유효성 검사
	 * @param time_str 시간 문자열
	 * @return 시간문자열이 유효한지 여부
	 */
	private boolean chkTimeStr(String time_str) {
		if(time_str == null) {
			return false;
		}
		
		Pattern p = Pattern.compile("^([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
		Matcher m = p.matcher(time_str);
		return m.matches();
	}
	
	/**숫자 문자열 유효성 검사
	 * @param num_str 숫자 문자열
	 * @return 숫자 문자열이 유효한지 여부
	 */
	private boolean chkNumStr(String num_str) {
		if(num_str == null) {
			return false;
		}
		
		Pattern p = Pattern.compile("^\\d{1,10}$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
		Matcher m = p.matcher(num_str);
		return m.matches();
	}
	
	@SuppressWarnings("unchecked")
	public String genereateAdRequestXML(Map<String,Object> param_map) {
		String ad_id = (String)param_map.get("ad_id");
		String sequence = (String)param_map.get("sequence");
		String skipoffset = (String)param_map.get("skipoffset");
		String duration = (String)param_map.get("duration");
		String impressionUrl = (String)param_map.get("impressionUrl");
		String mediaUrl = (String)param_map.get("mediaUrl");
		String delivery = (String)param_map.get("delivery");
		String type = (String)param_map.get("type");
		String width = (String)param_map.get("width");
		String height = (String)param_map.get("height");
		String ClickThroughUrl = (String)param_map.get("ClickThroughUrl");
		String ClickTrackingUrl = (String)param_map.get("ClickTrackingUrl");
		String progress_offset = (String)param_map.get("progress_offset");
		
		Map<String,String> tracking_map = (Map<String,String>)param_map.get("tracking_map");
		
		try {
			generateBasicTemplate(ad_id, sequence, skipoffset, duration);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setImpression(impressionUrl);
		setMediaFile(mediaUrl, delivery, type, width, height);
		setVideoClick(ClickThroughUrl, ClickTrackingUrl);
		setTrackingEvents(tracking_map, progress_offset);
		
		String ret_xml_str = "";
		try {
			ret_xml_str =  getXmlStr();
		} catch (TransformerException e) {
			e.printStackTrace();
			return null;
		}
		
		return ret_xml_str;
	}

	public static void main(String argv[]) {
		Map<String,Object> param_map = new HashMap<String,Object>();
		param_map.put("ad_id","824");
		param_map.put("sequence","1");
		param_map.put("skipoffset","00:00:15");
		param_map.put("duration","00:00:15");
		param_map.put("impressionUrl","http://mlink-vad.netinsight.co.kr/NetInsight/event/imp/3tQcLtlXl8m7Q23LXKUPYnBDB2sEDMNhBdZmogQMbCCHAbZQM25LN2N3dVF0aVh4U1RudzdJS1NBAstB1n6x0fA1XQO2QnhTTERveUJROXFhRHk1cmtiR2xHZwawanRiYy92b2Qvdm9kQHZvZAfNAzgIzQ60Cd4AEqRkdmNtwKNwYXmhMKVwdGltZaMxNjWlY2xwaWSuSjAxX05WMTAxNzA3MjSlcHJvaWSuSjAxX1BSMTAwMDA0MDOlY3RudW2hMKRjYXRlojAxo2duZKFPpGNobmyjSjAxpXZ0eXBloTGkc2l0ZaRKVEJDpmFkdHlwZaEwomlwqzEuMjA5LjkuMTg0pGR2Y3SiUEOkc2VjdKMwMDWib3OnV0lORE9XU6VvbmFpcqEwo2FnZaI5OQ");
		param_map.put("mediaUrl","http://api.wecandeo.com/video?k=BOKNS9AQWrEknEjK0uFFQNHd023xknvm6MU7ccpHFr7dIPmAKMhZRwieie");
		param_map.put("delivery","progressive");
		param_map.put("type","video/mp4");
		param_map.put("width","1280");
		param_map.put("height","720");
		param_map.put("ClickThroughUrl","http://www.tria4beauty.co.kr/Goods");
		param_map.put("ClickTrackingUrl","http://mlink-vad.netinsight.co.kr/NetInsight/event/clk/3tQcLtlXl8m7Q23LXKUPYnBDB2sEDMNhBdZmogQMbCCHAbZQM25LN2N3dVF0aVh4U1RudzdJS1NBAstB1n6x0fA1XQO2QnhTTERveUJROXFhRHk1cmtiR2xHZwawanRiYy92b2Qvdm9kQHZvZAfNAzgIzQ60Cd4AEqRkdmNtwKNwYXmhMKVwdGltZaMxNjWlY2xwaWSuSjAxX05WMTAxNzA3MjSlcHJvaWSuSjAxX1BSMTAwMDA0MDOlY3RudW2hMKRjYXRlojAxo2duZKFPpGNobmyjSjAxpXZ0eXBloTGkc2l0ZaRKVEJDpmFkdHlwZaEwomlwqzEuMjA5LjkuMTg0pGR2Y3SiUEOkc2VjdKMwMDWib3OnV0lORE9XU6VvbmFpcqEwo2FnZaI5OQ");
		param_map.put("progress_offset","00:00:00");
		
		Map<String,String> tracking_map = new HashMap<String,String>();
		tracking_map.put("start", "http://mlink-vad.netinsight.co.kr/NetInsight/event/str/3tQcLtlXl8m7Q23LXKUPYnBDB2sEDMNhBdZmogQMbCCHAbZQM25LN2N3dVF0aVh4U1RudzdJS1NBAstB1n6x0fA1XQO2QnhTTERveUJROXFhRHk1cmtiR2xHZwawanRiYy92b2Qvdm9kQHZvZAfNAzgIzQ60Cd4AEqRkdmNtwKNwYXmhMKVwdGltZaMxNjWlY2xwaWSuSjAxX05WMTAxNzA3MjSlcHJvaWSuSjAxX1BSMTAwMDA0MDOlY3RudW2hMKRjYXRlojAxo2duZKFPpGNobmyjSjAxpXZ0eXBloTGkc2l0ZaRKVEJDpmFkdHlwZaEwomlwqzEuMjA5LjkuMTg0pGR2Y3SiUEOkc2VjdKMwMDWib3OnV0lORE9XU6VvbmFpcqEwo2FnZaI5OQ");
		tracking_map.put("firstQuartile", "http://mlink-vad.netinsight.co.kr/NetInsight/event/p25/3tQcLtlXl8m7Q23LXKUPYnBDB2sEDMNhBdZmogQMbCCHAbZQM25LN2N3dVF0aVh4U1RudzdJS1NBAstB1n6x0fA1XQO2QnhTTERveUJROXFhRHk1cmtiR2xHZwawanRiYy92b2Qvdm9kQHZvZAfNAzgIzQ60Cd4AEqRkdmNtwKNwYXmhMKVwdGltZaMxNjWlY2xwaWSuSjAxX05WMTAxNzA3MjSlcHJvaWSuSjAxX1BSMTAwMDA0MDOlY3RudW2hMKRjYXRlojAxo2duZKFPpGNobmyjSjAxpXZ0eXBloTGkc2l0ZaRKVEJDpmFkdHlwZaEwomlwqzEuMjA5LjkuMTg0pGR2Y3SiUEOkc2VjdKMwMDWib3OnV0lORE9XU6VvbmFpcqEwo2FnZaI5OQ");
		tracking_map.put("midpoint", "http://mlink-vad.netinsight.co.kr/NetInsight/event/p50/3tQcLtlXl8m7Q23LXKUPYnBDB2sEDMNhBdZmogQMbCCHAbZQM25LN2N3dVF0aVh4U1RudzdJS1NBAstB1n6x0fA1XQO2QnhTTERveUJROXFhRHk1cmtiR2xHZwawanRiYy92b2Qvdm9kQHZvZAfNAzgIzQ60Cd4AEqRkdmNtwKNwYXmhMKVwdGltZaMxNjWlY2xwaWSuSjAxX05WMTAxNzA3MjSlcHJvaWSuSjAxX1BSMTAwMDA0MDOlY3RudW2hMKRjYXRlojAxo2duZKFPpGNobmyjSjAxpXZ0eXBloTGkc2l0ZaRKVEJDpmFkdHlwZaEwomlwqzEuMjA5LjkuMTg0pGR2Y3SiUEOkc2VjdKMwMDWib3OnV0lORE9XU6VvbmFpcqEwo2FnZaI5OQ");
		tracking_map.put("thirdQuartile", "http://mlink-vad.netinsight.co.kr/NetInsight/event/p75/3tQcLtlXl8m7Q23LXKUPYnBDB2sEDMNhBdZmogQMbCCHAbZQM25LN2N3dVF0aVh4U1RudzdJS1NBAstB1n6x0fA1XQO2QnhTTERveUJROXFhRHk1cmtiR2xHZwawanRiYy92b2Qvdm9kQHZvZAfNAzgIzQ60Cd4AEqRkdmNtwKNwYXmhMKVwdGltZaMxNjWlY2xwaWSuSjAxX05WMTAxNzA3MjSlcHJvaWSuSjAxX1BSMTAwMDA0MDOlY3RudW2hMKRjYXRlojAxo2duZKFPpGNobmyjSjAxpXZ0eXBloTGkc2l0ZaRKVEJDpmFkdHlwZaEwomlwqzEuMjA5LjkuMTg0pGR2Y3SiUEOkc2VjdKMwMDWib3OnV0lORE9XU6VvbmFpcqEwo2FnZaI5OQ");
		tracking_map.put("complete", "http://mlink-vad.netinsight.co.kr/NetInsight/event/cmp/3tQcLtlXl8m7Q23LXKUPYnBDB2sEDMNhBdZmogQMbCCHAbZQM25LN2N3dVF0aVh4U1RudzdJS1NBAstB1n6x0fA1XQO2QnhTTERveUJROXFhRHk1cmtiR2xHZwawanRiYy92b2Qvdm9kQHZvZAfNAzgIzQ60Cd4AEqRkdmNtwKNwYXmhMKVwdGltZaMxNjWlY2xwaWSuSjAxX05WMTAxNzA3MjSlcHJvaWSuSjAxX1BSMTAwMDA0MDOlY3RudW2hMKRjYXRlojAxo2duZKFPpGNobmyjSjAxpXZ0eXBloTGkc2l0ZaRKVEJDpmFkdHlwZaEwomlwqzEuMjA5LjkuMTg0pGR2Y3SiUEOkc2VjdKMwMDWib3OnV0lORE9XU6VvbmFpcqEwo2FnZaI5OQ");
		tracking_map.put("skip", "http://mlink-vad.netinsight.co.kr/NetInsight/event/skp/3tQcLtlXl8m7Q23LXKUPYnBDB2sEDMNhBdZmogQMbCCHAbZQM25LN2N3dVF0aVh4U1RudzdJS1NBAstB1n6x0fA1XQO2QnhTTERveUJROXFhRHk1cmtiR2xHZwawanRiYy92b2Qvdm9kQHZvZAfNAzgIzQ60Cd4AEqRkdmNtwKNwYXmhMKVwdGltZaMxNjWlY2xwaWSuSjAxX05WMTAxNzA3MjSlcHJvaWSuSjAxX1BSMTAwMDA0MDOlY3RudW2hMKRjYXRlojAxo2duZKFPpGNobmyjSjAxpXZ0eXBloTGkc2l0ZaRKVEJDpmFkdHlwZaEwomlwqzEuMjA5LjkuMTg0pGR2Y3SiUEOkc2VjdKMwMDWib3OnV0lORE9XU6VvbmFpcqEwo2FnZaI5OQ");
		tracking_map.put("progress", "http://mlink-vad.netinsight.co.kr/NetInsight/event/prg/n94loLjllQJ6jgmjisB0DHa2yHbqGf4Bc74tjOjqcJuIAbZQM25LN2N3dVF0aVh4U1RudzdJS1NBAstB1n6x0fA1XQO2QnhTTERveUJROXFhRHk1cmtiR2xHZwawanRiYy92b2Qvdm9kQHZvZAfNAzgIzQ60Cd4AEqRkdmNtwKNwYXmhMKVwdGltZaMxNjWlY2xwaWSuSjAxX05WMTAxNzA3MjSlcHJvaWSuSjAxX1BSMTAwMDA0MDOlY3RudW2hMKRjYXRlojAxo2duZKFPpGNobmyjSjAxpXZ0eXBloTGkc2l0ZaRKVEJDpmFkdHlwZaEwomlwqzEuMjA5LjkuMTg0pGR2Y3SiUEOkc2VjdKMwMDWib3OnV0lORE9XU6VvbmFpcqEwo2FnZaI5OQqBoW-hMA");
		
		param_map.put("tracking_map", tracking_map);
		
		GenerateAdRequestXML test = new GenerateAdRequestXML();
		String xml_str = test.genereateAdRequestXML(param_map);
		System.out.println(xml_str);
	}
}
