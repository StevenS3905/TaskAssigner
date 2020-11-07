package components;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

/* * The following code is adapted from Java Forums - JCheckBox in JComboBox URL: http://forum.java.sun.com/thread.jspa?forumID=257&threadID=364705 Date of Access: July 28 2005 */
import javax.swing.ComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class JComboCheckBox extends JComboBox<JCheckBox> {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

/**
	 * 
	 */

public JComboCheckBox() { addStuff(); }
  public JComboCheckBox(JCheckBox[] items) { super(items); addStuff(); }
  public JComboCheckBox(Vector<JCheckBox> items) { super(items); addStuff();}
  public JComboCheckBox(ComboBoxModel<JCheckBox> aModel) { super(aModel); addStuff(); }
  private void addStuff() {
    setRenderer(new ComboBoxRenderer());
    addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae)
      {
    	  itemSelected();
      }
    });
  }
  
  /*@Override
  public void actionPerformed(ActionEvent sdf)
  {
      setSelectedItem(getEditor().getItem());
  }
  /*
  public boolean doAccessibleAction(int actionIndex)
  {
     setPopupVisible(true);
     boolean actionPerformed = false;
     if (actionIndex == 0)
       {
         actionPerformed = true;
       }
     return actionPerformed;
  }
  */
  private void itemSelected() {
    if (getSelectedItem() instanceof JCheckBox) {
      JCheckBox jcb = (JCheckBox)getSelectedItem();
      jcb.setSelected(!jcb.isSelected());
    }
    setPopupVisible(true);
    showPopup();
    setPopupVisible(true);
  }
  
  class ComboBoxRenderer implements ListCellRenderer<Object>
  {	  
    private JLabel defaultLabel;
    public ComboBoxRenderer() { setOpaque(false); }
    public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus)
    {
          if (value instanceof Component)
      {
        Component c = (Component)value;
        if (isSelected)
        {
          c.setBackground(list.getSelectionBackground());
          c.setForeground(list.getSelectionForeground());
        }
        else
        {
          c.setBackground(list.getBackground());
          c.setForeground(list.getForeground());
        }
        return c;
      } 
      else
      {
        if (defaultLabel==null)
        	defaultLabel =
        	new JLabel(
        	String.valueOf(value));
        else defaultLabel.setText(String.valueOf(value));
        return defaultLabel;
      }
    }
  }
}