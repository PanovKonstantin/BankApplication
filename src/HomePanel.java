import javax.swing.*;
import javax.swing.table.AbstractTableModel;

import java.awt.*;

public class HomePanel extends JPanel{
    JLabel balanceLabel;
    JLabel balance;
    JLabel message;
    JTextField target;
    JTextField amount;
    JButton transfer;
    JTable history;

    HomePanel() {
        super(new BorderLayout());
        balanceLabel = new JLabel("Account balance: ");
        balance = new JLabel();
        balance.setText("100000 PLN");
        transfer = new JButton("Make transfer to >");
        target = new JTextField();

        history = new JTable(new HistoryTableModel());
        JScrollPane scrollPane = new JScrollPane(history);
        history.setFillsViewportHeight(true);

        JPanel p = new JPanel(new BorderLayout());

        JPanel balancePanel = new JPanel(new GridLayout(1, 2));
        balancePanel.add(balanceLabel);
        balancePanel.add(balance);

        JPanel transactionPanel = new JPanel(new GridLayout(4, 2));
        transactionPanel.add(new JLabel("Transactoin"));
        transactionPanel.add(new JLabel(""));
        transactionPanel.add(new JLabel("Target account: "));
        transactionPanel.add(target);
        transactionPanel.add(new JLabel("Amount"));
        amount = new JTextField();
        transactionPanel.add(amount);
        message = new JLabel();
        transactionPanel.add(message);
        transactionPanel.add(transfer);

        p.add(balancePanel, BorderLayout.EAST);
        p.add(transactionPanel, BorderLayout.WEST);
        add(p, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

    }
}

class HistoryTableModel extends AbstractTableModel {

    String[] columnNames = { "Type", "From", "To", "Amount", "Date" };
    Object[][] data = { { "dasda", "asdas", "asdasd", 123213, "2112-21-41" },
            { "dasda", "asdas", "asdasd", 123213, "2112-21-41" }, { "dasda", "asdas", "asdasd", 123213, "2112-21-41" },
            { "dasda", "asdas", "asdasd", 123213, "2112-21-41" }, { "dasda", "asdas", "asdasd", 123213, "2112-21-41" },
            { "vxvc", "xqerz", "wfda", 2222, "0000-00-00" } };

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        return data[row][col];
    }

    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }
}