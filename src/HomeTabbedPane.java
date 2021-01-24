import javax.swing.*;

public class HomeTabbedPane extends JTabbedPane {
    static final long serialVersionUID = 42L;
    HomePanel home;
    InfoPanel info;
    SavingsPanel savings;

    HomeTabbedPane() {
        super();
        setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        home = new HomePanel();
        add("Home", home);

        info = new InfoPanel();
        add("Information", info);


        savings = new SavingsPanel();
        savings.addActionListener(e -> {
        });

        add("Savings", savings);

    }
}