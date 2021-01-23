import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import java.awt.*;
import java.awt.event.*;

public class SavingsPanel extends JPanel implements ActionListener {
    JLabel amountLabel;
    JTextField amount;
    JButton transferTo;
    JButton transferFrom;
    JTable savings;

    SavingsPanel() {
        super(new BorderLayout());

        transferTo = new JButton("Transfer to selected account");
        transferFrom = new JButton("Transfer from selected account");
        amountLabel = new JLabel("Amount");
        amount = new JTextField();

        DefaultTableModel savingsTM = new DefaultTableModel();
        savingsTM.setDataVector(
                new Object[][] { { "Konto 1", 123123, 1000, 3, new JRadioButton() },
                        { "Konto 2", 1123, 13000, 11, new JRadioButton() } },
                new Object[] { "Name", "Number", "Balance", "Percent", "Select" });
        savings = new JTable(savingsTM) {
            @Override
            public void tableChanged(TableModelEvent tme) {
                super.tableChanged(tme);
                repaint();
            }
        };
        ButtonGroup bg = new ButtonGroup();
        bg.add((JRadioButton) savingsTM.getValueAt(0, 4));
        bg.add((JRadioButton) savingsTM.getValueAt(1, 4));
        savings.getColumn("Select").setCellRenderer(new RadioButtonRenderer());
        savings.getColumn("Select").setCellEditor(new RadioButtonEditor(new JCheckBox()));
        JScrollPane sp = new JScrollPane(savings);
        add(sp, BorderLayout.CENTER);
        transferTo.addActionListener(this);
        transferFrom.addActionListener(this);

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

    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Transfer to selected account":
                break;
            case "Transfer from selected account":
                break;
            default:
                break;
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