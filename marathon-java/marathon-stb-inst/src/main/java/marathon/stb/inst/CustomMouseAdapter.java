package marathon.stb.inst;


import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;

import javax.swing.JComponent;

public class CustomMouseAdapter extends MouseAdapter {
	public static final Logger LOGGER = Logger.getLogger(CustomMouseAdapter.class.getName());
	
	@Override
	public void mouseClicked(MouseEvent me) {
		JComponent lastActivatedComponent = ActiveComponentStore.getInstance().pop();
		if (lastActivatedComponent != null) {
			LOGGER.info("Removing border...");
			lastActivatedComponent.setBorder(javax.swing.BorderFactory.createEmptyBorder());
			storeComponent(me);
		} else {
			storeComponent(me);
		}
	}

	private void storeComponent(MouseEvent me) {
		LOGGER.info("Storing current component...");
		JComponent comp = ((javax.swing.JComponent)me.getSource());
		ActiveComponentStore.getInstance().push(comp);
		comp.setBorder(new javax.swing.border.LineBorder(java.awt.Color.red, 1));
	}
}
