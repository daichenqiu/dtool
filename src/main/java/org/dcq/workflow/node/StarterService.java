package org.dcq.workflow.node;

import org.dcq.workflow.annotation.handle;
import org.dcq.workflow.utils.WorkflowConstant;

public class StarterService {
	
	@handle
	public String drivenByHandle(){
		System.out.println(this.getClass().getName());
		return WorkflowConstant.REDIRECT;
	}
}
