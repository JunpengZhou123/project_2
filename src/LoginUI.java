import javax.swing.*;
import java.awt.*;

class LoginUI extends BaseUI {
    LoginUI() {
        super("Login", 400, 200);

        JButton btnLogin = new JButton("Login");
        JButton btnExit = new JButton("Exit");
        JTextField txtUsername = new JTextField(TEXT_FIELD_WIDTH);
        JTextField txtPassword = new JTextField(TEXT_FIELD_WIDTH);

        JPanel line = new JPanel();
        line.add(new JLabel("Username"));
        line.add(txtUsername);
        pane.add(line);

        line = new JPanel();
        line.add(new JLabel("Password"));
        line.add(txtPassword);
        pane.add(line);

        line = new JPanel();
        line.add(btnExit);
        line.add(btnLogin);
        pane.add(line);

        view.setVisible(true);
    }
}
