package org.dcq.dtest;

import java.io.InputStream;
import java.util.List;

import org.dcq.workflow.model.DriverNode;
import org.dcq.workflow.xmlreader.NodeXmlDriver;
import org.junit.Test;

public class ParseTest{
	@Test
	public void testNode(){
		try {
			InputStream input = this.getClass().getClassLoader().getResourceAsStream("node.xml");
			List<DriverNode> nodes = NodeXmlDriver.getNodes(input);
			
			
			for(DriverNode node : nodes){
				System.out.println(node.toString());
			}
		} catch (Exception e) {
			System.out.println(e);
			// TODO: handle exception
		}
	}
	
}