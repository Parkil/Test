package test.xml;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class GenerateXML {
	public static void main(String argv[]) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			//기본 노드 ==================
			//VAST
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("VAST");
			rootElement.setAttribute("version", "3.0");
			doc.appendChild(rootElement);

			//AD
			Element Ad = doc.createElement("Ad");
			Ad.setAttribute("id", "824");
			Ad.setAttribute("sequence", "1");
			rootElement.appendChild(Ad);
			
			Element InLine = doc.createElement("InLine");
			Ad.appendChild(InLine);
			
			Element AdSystem = doc.createElement("AdSystem");
			AdSystem.appendChild(doc.createTextNode("NetInsight VIDEO AD SERVER"));
			InLine.appendChild(AdSystem);
			
			Element AdTitle = doc.createElement("AdTitle");
			AdTitle.appendChild(doc.createTextNode("NetInsight VIDEO AD"));
			InLine.appendChild(AdTitle);
			
			Element Impression = doc.createElement("Impression");
			InLine.appendChild(Impression);
			
			Element Creatives = doc.createElement("Creatives");
			InLine.appendChild(Creatives);
			
			Element Creative = doc.createElement("Creative");
			Creatives.appendChild(Creative);
			
			//Impression ==================
			Impression.appendChild(doc.createTextNode("<![CDATA["+"http://localhost:8080"+"]]>"));
			//-Impression ==================
			
			//Creative ==================
			Element Linear = doc.createElement("Linear");
			Linear.setAttribute("skipoffset", "00:00:15");
			Creative.appendChild(Linear);
			
			Element Duration = doc.createElement("Duration");
			Duration.appendChild(doc.createTextNode("00:00:15"));
			Linear.appendChild(Duration);
			
			Element TrackingEvents = doc.createElement("TrackingEvents");
			Linear.appendChild(TrackingEvents);
			
			Element VideoClicks = doc.createElement("VideoClicks");
			Linear.appendChild(VideoClicks);
			
			Element MediaFiles = doc.createElement("MediaFiles");
			Linear.appendChild(MediaFiles);
			
			//-Creative ==================
			
			//기본 노드 ==================
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);

			//StreamResult result = new StreamResult(new File("C:\\file.xml"));

			// Output to console for testing
			//StreamResult result = new StreamResult(System.out);
			
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			
			transformer.transform(source, result);
			String strResult = writer.toString();
			
			System.out.println(strResult);
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}
}
