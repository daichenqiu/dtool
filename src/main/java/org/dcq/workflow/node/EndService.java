package org.dcq.workflow.node;

import org.dcq.workflow.annotation.handle;
import org.dcq.workflow.utils.WorkflowConstant;

public class EndService {
	
	@handle
	public String end(){
		System.out.println(this.getClass().getName());
		return WorkflowConstant.SUCCESS;
	}
}