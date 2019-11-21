import javax.swing.*;
import java.awt.*;

class AddAdminUI extends BaseUI {

    AddAdminUI() {
        super("New Admin", 400, 200);

        JTextField txtUsername = new JTextField(TEXT_FIELD_WIDTH);
        JTextField txtPassword = new JTextField(TEXT_FIELD_WIDTH);
        JButton btnAdd = new JButton("Add");

        JPanel line = new JPanel(new FlowLayout());
        line.add(new JLabel("Username"));
        line.add(txtUsername);
        pane.add(line);

        line = new JPanel(new FlowLayout());
        line.add(new JLabel("Password"));
        line.add(txtPassword);
        pane.add(line);

        line = new JPanel();
        line.add(btnCancel);
        line.add(btnAdd);
        pane.add(line);

        view.setVisible(true);
    }
}
