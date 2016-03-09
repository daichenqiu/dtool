package org.dcq.crawl.model;  
  
import java.util.LinkedList;  
  
@SuppressWarnings("unchecked")  
public class Queue {  
  
    private LinkedList queue = new LinkedList();    
      
    public void enQueue(Object t) {  
        queue.add(t);  
    }  
  
    public Object deQueue() {  
        return queue.removeFirst();  
    }  
      
    public boolean contains(Object t) {  
        return queue.contains(t);  
    }  
      
    public boolean empty() {  
        return queue.isEmpty();  
    }  
}  