package org.dcq.workflow.xmlreader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.dcq.workflow.model.DriverNode;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class NodeXmlDriver extends DefaultHandler{
	private static SAXParser parser;
	private List<DriverNode> nodes = new ArrayList<DriverNode>();

	private DriverNode node = null;
	private String preTag = null;//作用是记录解析时的上一个节点名称
	private String preAttrType = null;//作用是记录解析时的上一个节点名称
	
	public NodeXmlDriver(){
		init();
	}
	public static synchronized void init(){
		try {
			if (parser == null){
				SAXParserFactory factory = SAXParserFactory.newInstance();
				parser = factory.newSAXParser();
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public List<DriverNode> getNodes(InputStream xmlStream) throws Exception{
		NodeXmlDriver driver = new NodeXmlDriver();
		parser.parse(xmlStream, driver);
		return driver.getNodes();
	}
    
	
	public List<DriverNode> getNodes() throws Exception{
		return nodes;
	}
	
	@Override
	public void startDocument() throws SAXException {
		nodes = new ArrayList<DriverNode>();
	}
	
	@Override
	public void startElement (String uri, String localName,String qName, Attributes attributes)
	throws SAXException{
		if("node".equals(qName)){
			node = new DriverNode();
			node.setId(attributes.getValue("id"));
		}
		preTag = qName;//将正在解析的节点名称赋给preTag
		preAttrType = attributes.getValue("type");
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if("node".equals(qName)){
			nodes.add(node);
			node = null;
		}
		preTag = null;/**当解析结束时置为空。这里很重要，例如，当图中画3的位置结束后，会调用这个方法
		，如果这里不把preTag置为null，根据startElement(....)方法，preTag的值还是book，当文档顺序读到图
		中标记4的位置时，会执行characters(char[] ch, int start, int length)这个方法，而characters(....)方
		法判断preTag!=null，会执行if判断的代码，这样就会把空值赋值给book，这不是我们想要的。*/
		preAttrType = null;
	}
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if(preTag!=null){
			String content = new String(ch,start,length);
			if("result".equals(preTag)){
				node.addResults(preAttrType, content);
			}
		}
	}
}
