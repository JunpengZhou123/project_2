import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class AdminUI extends BaseUI {
    AdminUI() {
        super("Administration", 400, 200);
        JButton btnAddCustomer = new JButton("Add Customer");
        JButton btnModCustomer = new JButton("Modify Customer");
        JButton btnAddAdmin = new JButton("Add Admin");
        JButton btnModAdmin = new JButton("Modify Admin");
        JButton btnAddProduct = new JButton("Add Product");
        JButton btnModProduct = new JButton("Modify Product");
        JButton btnAddTransaction = new JButton("Add Transaction");
        JButton btnModTransaction = new JButton("Modify Transaction");
        JButton btnLogout = new JButton("Logout");

        JPanel line = new JPanel();
        line.add(btnAddCustomer);
        line.add(btnModCustomer);
        pane.add(line);

        // TODO: Implement admin login
//        line = new JPanel();
//        line.add(btnAddAdmin);
//        line.add(btnModAdmin);
//        pane.add(line);

        line = new JPanel();
        line.add(btnAddProduct);
        line.add(btnModProduct);
        pane.add(line);

        line = new JPanel();
        line.add(btnAddTransaction);
        line.add(btnModTransaction);
        pane.add(line);

        line = new JPanel();
//        line.add(btnLogout); // TODO: Implement logout (see above)
        line.add(btnCancel);
        pane.add(line);

        btnAddCustomer.addActionListener(new AddCustomerButtonListener());
        btnAddProduct.addActionListener(new AddProductButtonListener());
        btnAddTransaction.addActionListener(new AddTransactionButtonListener());
        btnModCustomer.addActionListener(new ModCustomerButtonListener());
        btnModProduct.addActionListener(new ModProductButtonListener());
        btnModTransaction.addActionListener(new ModTransactionButtonListener());

        view.setVisible(true);
    }

    class AddCustomerButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            new AddCustomerUI();
        }
    }
    class AddProductButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            new AddProductUI();
        }
    }
    class AddTransactionButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            new AddTransactionUI();
        }
    }
    class ModCustomerButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            new ModCustomerUI();
        }
    }
    class ModProductButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            new ModProductUI();
        }
    }
    class ModTransactionButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            new ModTransactionUI();
        }
    }
}
