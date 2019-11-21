import javax.swing.*;
import java.awt.*;

class CustomerUI extends BaseUI {
    CustomerUI() {
        super("Main Menu", 400, 150);

        JButton btnAddTransaction = new JButton("Make Purchase");
        JButton btnListTransaction = new JButton("View Purchase History");
        JButton btnLogout = new JButton("Logout");

        JPanel line = new JPanel();
        line.add(btnAddTransaction);
        line.add(btnListTransaction);
        pane.add(line);

        line = new JPanel();
        line.add(btnLogout);
        pane.add(line);

        view.setVisible(true);
    }
}
