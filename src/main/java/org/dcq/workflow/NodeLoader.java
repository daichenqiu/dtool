package org.dcq.workflow;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dcq.workflow.annotation.handle;
import org.dcq.workflow.model.DriverNode;
import org.dcq.workflow.utils.WorkflowConstant;
import org.dcq.workflow.xmlreader.NodeXmlDriver;

public class NodeLoader {
	
	private static List<DriverNode> nodes = new LinkedList<DriverNode>();
	
	public static void main(String[] args) {
		NodeLoader t = new NodeLoader();
		t.load("node.xml");
	}
	
	/**
	 * 
	 * @param resource
	 */
	public void load(String resource){
			InputStream input = this.getClass().getClassLoader().getResourceAsStream(resource);
			load(input);
	}
	
	public void load(InputStream input){
		nodes = NodeXmlDriver.getNodes(input);
		if(nodes.size()!=0){
			DriverNode startNode = nodes.get(0);
			invokeNode(startNode);
		}
	}
	
	/**
	 * 
	 * @param DriverNode
	 */
	public void invokeNode(DriverNode node){
		try {
			if(StringUtils.isNoneBlank(node.getInvokeName())){
				Class<?> claz = Class.forName(node.getInvokeName());
				Method method = null;
				for(Method m:claz.getDeclaredMethods()){
					if(m.getAnnotation(handle.class) != null){
						method = m;
					}
				}
				String result = String.valueOf(method.invoke(claz.newInstance()));
				
				for (Annotation anno : method.getAnnotations()) {
					System.out.println(anno.toString());
				}
				
				if(WorkflowConstant.SUCCESS.equalsIgnoreCase(result)){
					System.out.println(node.getId() + " return with result: " + WorkflowConstant.SUCCESS);
				}else if (WorkflowConstant.FAILURE.equalsIgnoreCase(result)){
					System.out.println(node.getId() + " return with result: " + WorkflowConstant.FAILURE);
				}else if(WorkflowConstant.REDIRECT.equalsIgnoreCase(result)){
					System.out.println(node.getId() + " return with result: " + WorkflowConstant.REDIRECT);
					redirect(node);
				}else{
					System.out.println(node.getId() + " no matching result");
				}
			}
		} catch (ClassNotFoundException e) {
			System.out.println("no such class");
			e.printStackTrace();
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			System.out.println("arguments wrong");
			e.printStackTrace();
		}
		
	}
	
	public void redirect(DriverNode node){
		if(node.getResults().containsKey(WorkflowConstant.REDIRECT)){
			String nodeId = node.getResults().get(WorkflowConstant.REDIRECT);
			for(DriverNode element:nodes){
				if(element.getId()!=null && element.getId().equalsIgnoreCase(nodeId)){
					invokeNode(element);
				}
			}
		}
	}
	

}
