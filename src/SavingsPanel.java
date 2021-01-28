import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import java.awt.*;
import java.awt.event.*;

public class SavingsPanel extends JPanel {
    static final long serialVersionUID = 42L;
    JLabel amountLabel;
    JTextField amount;
    JButton transferTo;
    JButton transferFrom;
    JTable savings;
    DefaultTableModel savingsTM;
    ButtonGroup bg;
    transient Object[] headerRow;
    static final String SELECT = "Select";
    static final String NOINFO = "No information";

    SavingsPanel() {
        super(new BorderLayout());

        transferTo = new JButton("Transfer to selected account");
        transferFrom = new JButton("Transfer from selected account");
        amountLabel = new JLabel("Amount");
        amount = new JTextField();
        savingsTM = new DefaultTableModel();
        headerRow = new Object[] {"Number", "Balance", "Percent", SELECT};
        bg = new ButtonGroup();

        savings = new JTable(savingsTM) {
            static final long serialVersionUID = 42L;

            @Override
            public void tableChanged(TableModelEvent tme) {
                super.tableChanged(tme);
                repaint();
            }

            @Override
            public boolean isCellEditable(int row, int col) {return col == 3;}
        };
        JScrollPane sp = new JScrollPane(savings);
        add(sp, BorderLayout.CENTER);

        JPanel p = new JPanel(new GridLayout(2, 2));
        p.add(amountLabel);
        p.add(amount);
        p.add(transferTo);
        p.add(transferFrom);
        add(p, BorderLayout.NORTH);

    }

    public void addActionListener(ActionListener l) {
        transferTo.addActionListener(l);
    }

    public void refresh(Object[][] data) {
        if (data.length == 0) savingsTM.setDataVector(new Object[][] {{NOINFO, NOINFO, NOINFO, NOINFO}}, headerRow);
        else {
            for (int i = 0; i < savings.getRowCount(); i++){
                bg.remove((JRadioButton) savings.getValueAt(i, 3));
            }
            for (int i = 0; i < data.length; i++) {
                data[i][3] = new JRadioButton();
                bg.add((JRadioButton) data[i][3]);
            }
            savingsTM.setDataVector(data, headerRow);
            savings.getColumn(SELECT).setCellRenderer(new RadioButtonRenderer());
            savings.getColumn(SELECT).setCellEditor(new RadioButtonEditor(new JCheckBox()));
        }
    }
}

class RadioButtonRenderer implements TableCellRenderer {
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        if (value == null)
            return null;
        return (Component) value;
    }
}

class RadioButtonEditor extends DefaultCellEditor implements ItemListener {
    static final long serialVersionUID = 42L;
    private JRadioButton button;

    public RadioButtonEditor(JCheckBox checkBox) {
        super(checkBox);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (value == null)
            return null;
        button = (JRadioButton) value;
        button.addItemListener(this);
        return (Component) value;
    }

    @Override
    public Object getCellEditorValue() {
        button.removeItemListener(this);
        return button;
    }

    public void itemStateChanged(ItemEvent e) {
        super.fireEditingStopped();
    }
}