import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;

public class HomePanel extends JPanel {
    static final long serialVersionUID = 42L;
    JLabel balanceLabel;
    JLabel balance;
    JLabel message;
    JTextField target;
    JTextField amount;
    JButton transfer;
    JTable history;
    DefaultTableModel historyTM;
    transient Object[] headerRow;
    static final String NOINFO = "No information";

    HomePanel() {
        super(new BorderLayout());
        balanceLabel = new JLabel("Account balance: ");
        balance = new JLabel();
        balance.setText("100000 PLN");
        transfer = new JButton("Make transfer to >");
        target = new JTextField();

        historyTM = new DefaultTableModel();
        headerRow = new Object[] { "From", "To", "Amount", "Date" };
        history = new JTable(historyTM) {
            static final long serialVersionUID = 42L;

            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        historyTM.setDataVector(new Object[][] { { NOINFO, NOINFO, NOINFO, NOINFO, NOINFO } }, headerRow);
        JScrollPane scrollPane = new JScrollPane(history);

        JPanel p = new JPanel(new BorderLayout());

        JPanel balancePanel = new JPanel(new GridLayout(1, 2));
        balancePanel.add(balanceLabel);
        balancePanel.add(balance);

        JPanel transactionPanel = new JPanel(new GridLayout(4, 2));
        transactionPanel.add(new JLabel("Transaction"));
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

    public void refresh(Object[][] data) {
        if (data[0].length > 0)
            historyTM.setDataVector(data, headerRow);
        else
            historyTM.setDataVector(new Object[][] { { NOINFO, NOINFO, NOINFO, NOINFO, NOINFO } }, headerRow);
    }

    public void addActionListener(ActionListener l) {
        target.addActionListener(l);
        amount.addActionListener(l);
        transfer.addActionListener(l);
    }

    public void clear() {
        message.setText("");
        target.setText("");
        amount.setText("");
    }

    public void inform(String info) {
        message.setText(info);
    }
}
