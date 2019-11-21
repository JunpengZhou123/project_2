import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ModCustomerUI extends BaseUI {
    private static final String[] labels = {"UUID", "Name", "Address",
            "Payment", "Phone"};
    private JTextField[] fields = new JTextField[labels.length];

    ModCustomerUI() {
        super("Modify Customer", 400, 300);

        for (int i = 0; i < fields.length; i++) {
            fields[i] = new JTextField(TEXT_FIELD_WIDTH);
            JPanel line = new JPanel(new FlowLayout());
            line.add(new JLabel(labels[i]));
            line.add(fields[i]);
            view.getContentPane().add(line);
        }

        JButton btnLoad = new JButton("Load");
        JButton btnSave = new JButton("Save");
        JButton btnRemove = new JButton("Remove");
        btnLoad.addActionListener(new LoadButtonListener());
        btnSave.addActionListener(new SaveButtonListener());
        btnRemove.addActionListener(new RemoveButtonListener());

        JPanel buttons = new JPanel(new FlowLayout());
        buttons.add(btnCancel);
        buttons.add(btnLoad);
        buttons.add(btnSave);
        buttons.add(btnRemove);
        view.getContentPane().add(buttons);

        view.setVisible(true);
    }

    class LoadButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            String sUUID = fields[0].getText();
            int UUID;
            if (sUUID.length() == 0) {
                JOptionPane.showMessageDialog(null,
                        "UUID cannot be empty");
                return;
            }
            try {
                UUID = Integer.parseInt(sUUID);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null,
                        "Invalid UUID");
                return;
            }
            CustomerModel c = Driver.db.loadCustomer(UUID);
            if (c == null) {
                JOptionPane.showMessageDialog(null,
                        "Customer not found");
                return;
            }

            // Populate text fields at the proper time
            Runnable fillFields = new Runnable() {
                @Override
                public void run() {
                    fields[1].setText(c.name);
                    fields[2].setText(c.address);
                    fields[3].setText(c.payment);
                    fields[4].setText(c.phone);
                }
            };
            SwingUtilities.invokeLater(fillFields);
        }
    }

    class SaveButtonListener implements ActionListener {
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
                    System.out.println("[INFO] Customer modified");
                    break;
                default:
                    System.err.println("[ERR] Unreachable state");
            }
        }
    }

    class RemoveButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            // TODO: Implement removal
        }
    }
}
