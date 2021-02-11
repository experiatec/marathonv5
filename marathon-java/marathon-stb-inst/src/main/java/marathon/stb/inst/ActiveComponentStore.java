package marathon.stb.inst;

import java.util.EmptyStackException;
import java.util.Stack;

import javax.swing.JComponent;

public class ActiveComponentStore {

	private static ActiveComponentStore activeComponentStore = null;
	
	private Stack<CachedComponent<JComponent>> componentStack = new Stack<>();
	
	private ActiveComponentStore() {}
	
    // static method to create instance of Singleton class 
    public static ActiveComponentStore getInstance() 
    { 
        if (activeComponentStore == null) 
        	activeComponentStore = new ActiveComponentStore(); 
  
        return activeComponentStore; 
    }

	public void push(CachedComponent<JComponent> component) {
		componentStack.push(component);
	}
	
	public CachedComponent<JComponent> pop() {
		CachedComponent<JComponent> comp = null;
		try {
			comp = componentStack.pop();
		} catch(EmptyStackException e) {
			//No es importante
		}
		return comp;
	}
    
}
