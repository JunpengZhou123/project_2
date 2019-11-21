import javax.swing.*;
import java.awt.*;

class ModAdminUI extends BaseUI {
    ModAdminUI() {
        super("Modify Admin", 400, 200);

        JTextField txtUsername = new JTextField(TEXT_FIELD_WIDTH);
        JTextField txtPassword = new JTextField(TEXT_FIELD_WIDTH);
        JButton btnLoad = new JButton("Load");
        JButton btnSave = new JButton("Save");
        JButton btnRemove = new JButton("Remove");

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
        line.add(btnLoad);
        line.add(btnSave);
        line.add(btnRemove);
        pane.add(line);

        view.setVisible(true);
    }
}
