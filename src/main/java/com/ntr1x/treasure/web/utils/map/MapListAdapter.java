//package com.ntr1x.treasure.web.utils.map;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import javax.xml.bind.annotation.adapters.XmlAdapter;
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//
//import com.ntr1x.treasure.web.model.purchase.GoodEntityList;
//
//public class MapListAdapter extends XmlAdapter<Element, Map<String, GoodEntityList>> {
//
//	private DocumentBuilder documentBuilder;
//
//	public MapListAdapter() throws Exception {
//		documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//	}
//
//	@Override
//	public Element marshal(Map<String, GoodEntityList> map) throws Exception {
//		Document document = documentBuilder.newDocument();
//		Element rootElement = document.createElement("data");
//		document.appendChild(rootElement);
//		for(Map.Entry<String, GoodEntityList> entry : map.entrySet()) {
//			Element childElement = document.createElement(entry.getKey());
//			childElement.setTextContent("goods");
//			rootElement.appendChild(childElement);
//		}
//		return rootElement;
//	}
//
//	@Override
//	public Map<String, GoodEntityList> unmarshal(Element rootElement) throws Exception {
//		NodeList nodeList = rootElement.getChildNodes();
//		Map<String,GoodEntityList> map = new HashMap<>(nodeList.getLength());
//		for(int x=0; x<nodeList.getLength(); x++) {
//			Node node = nodeList.item(x);
//			if(node.getNodeType() == Node.ELEMENT_NODE) {
////				map.put(node.getNodeName(), node.getTextContent());
//				map.put(node.getNodeName(), new GoodEntityList());
//			}
//		}
//		return map;
//	}
//
//}
