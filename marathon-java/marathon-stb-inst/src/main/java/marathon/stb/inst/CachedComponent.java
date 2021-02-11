package marathon.stb.inst;

import javax.swing.JComponent;
import javax.swing.border.Border;

public class CachedComponent <T extends JComponent>{

	private T component;
	
	private Border originalBorder;

	public CachedComponent(T component, Border originalBorder) {
		super();
		this.component = component;
		this.originalBorder = originalBorder;
	}

	public T getComponent() {
		return component;
	}

	public void setComponent(T component) {
		this.component = component;
	}

	public Border getOriginalBorder() {
		return originalBorder;
	}

	public void setOriginalBorder(Border originalBorder) {
		this.originalBorder = originalBorder;
	}
	
}
