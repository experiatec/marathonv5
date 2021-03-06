package marathon.stb.inst;


import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;

import javax.swing.JComponent;

public class CustomMouseAdapter extends MouseAdapter {
	public static final Logger LOGGER = Logger.getLogger(CustomMouseAdapter.class.getName());
	
	@Override
	public void mouseClicked(MouseEvent me) {
		CachedComponent<JComponent> lastActivatedComponent = ActiveComponentStore.getInstance().pop();
		if (lastActivatedComponent != null) {
			LOGGER.fine("Restoring component border...");
			lastActivatedComponent.getComponent().setBorder(lastActivatedComponent.getOriginalBorder());
			storeComponent(me);
		} else {
			storeComponent(me);
		}
	}

	private void storeComponent(MouseEvent me) {
		LOGGER.fine("Storing current component...");
		JComponent comp = ((javax.swing.JComponent)me.getSource());
		ActiveComponentStore.getInstance().push(new CachedComponent<JComponent>(comp, comp.getBorder()));
		comp.setBorder(new javax.swing.border.LineBorder(java.awt.Color.red, 1));
	}
}
