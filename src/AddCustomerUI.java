import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class AddCustomerUI extends BaseUI {
    private static final String[] labels = {"UUID", "Name", "Address",
            "Payment", "Phone"};
    private JTextField[] fields = new JTextField[labels.length];

    AddCustomerUI() {
        super("New Customer", 400, 300);

        for (int i = 0; i < fields.length; i++) {
            fields[i] = new JTextField(TEXT_FIELD_WIDTH);
            JPanel line = new JPanel(new FlowLayout());
            line.add(new JLabel(labels[i]));
            line.add(fields[i]);
            view.getContentPane().add(line);
        }

        JButton btnAdd = new JButton("Add");
        btnAdd.addActionListener(new AddButtonListener());

        JPanel buttons = new JPanel(new FlowLayout());
        buttons.add(btnCancel);
        buttons.add(btnAdd);
        view.getContentPane().add(buttons);

        view.setVisible(true);
    }

    class AddButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            CustomerModel c = new CustomerModel();

            String[] textFields = new String[fields.length];
            for (int i = 0; i < textFields.length; i++) {
                textFields[i] = fields[i].getText();
            }

            // UUID
            if (textFields[0].length() == 0) {
                JOptionPane.showMessageDialog(null,
                        "UUID cannot be empty");
                return;
            }
            try {
                c.UUID = Integer.parseInt(textFields[0]);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null,
                        "Invalid UUID");
                return;
            }
            // Name
            if (textFields[1].length() == 0) {
                JOptionPane.showMessageDialog(null,
                        "Customer name cannot be empty");
                return;
            }
            c.name = textFields[1];
            c.address = textFields[2];
            c.payment = textFields[3];
            c.phone = textFields[4];

            switch (Driver.db.saveCustomer(c)) {
                case DBClient.ERR_DUPLICATE:
                    JOptionPane.showMessageDialog(null,
                            "UUID is a duplicate. Customer add " +
                                    "failed.");
                    break;
                case DBClient.SAVE_OK:
                    view.dispose();
                    System.out.println("[INFO] New customer saved");
                    break;
                default:
                    System.err.println("[ERR] Unreachable state");
            }
        }
    }
}
