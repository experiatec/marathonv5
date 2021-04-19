package marathon.stb.inst;


import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButton;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;

public class CustomMouseAdapter extends MouseAdapter {
	public static final Logger LOGGER = Logger.getLogger(CustomMouseAdapter.class.getName());
	
	@Override
	public void mousePressed(MouseEvent me) {
		CachedComponent<JComponent> lastActivatedComponent = ActiveComponentStore.getInstance().pop();
		if (lastActivatedComponent != null) {
			LOGGER.fine("Restoring component border...");
			lastActivatedComponent.getComponent().setBorder(lastActivatedComponent.getOriginalBorder());
			repaint(lastActivatedComponent.getComponent());
			setDecoratedBorder((JComponent)me.getSource());
		} else {
			setDecoratedBorder((JComponent)me.getSource());
		}
	}

	private void setDecoratedBorder(JComponent comp) {
		LOGGER.fine("Decorating current component...");
		if (comp instanceof BasicInternalFrameTitlePane) {
			comp = (JComponent)comp.getParent();
		}
		Border border = comp.getBorder();
		ActiveComponentStore.getInstance().push(new CachedComponent<JComponent>(comp, border));
		Border margin = new javax.swing.border.LineBorder(java.awt.Color.red, 1);
		comp.setBorder(new CompoundBorder(margin, border));
		repaint(comp);
	}
	
	private void repaint(JComponent comp) {
		if (comp instanceof JCheckBox) {
			((JCheckBox)comp).setBorderPainted(true);
		} else if (comp instanceof JRadioButton) {
			((JRadioButton)comp).setBorderPainted(true);
		} else if (comp instanceof JRadioButton) {
			((JMenu)comp).setBorderPainted(true);
		} else if (comp instanceof JMenuBar) {
			((JMenuBar)comp).setBorderPainted(true);
		} else if (comp instanceof JMenuItem) {
			((JMenuItem)comp).setBorderPainted(true);
		}
	}
}
