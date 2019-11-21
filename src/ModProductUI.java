import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ModProductUI extends BaseUI {
    private static final String[] labels = {"UPC", "Name", "Price",
            "Quantity", "Vendor", "Description", "Tax Schedule"};
    private JTextField[] fields = new JTextField[labels.length];

    ModProductUI() {
        super("Modify Product", 400, 400);

        for (int i = 0; i < fields.length; i++) {
            fields[i] = new JTextField(TEXT_FIELD_WIDTH);
            JPanel line = new JPanel(new FlowLayout());
            line.add(new JLabel(labels[i]));
            line.add(fields[i]);
            pane.add(line);
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
        pane.add(buttons);

        view.setVisible(true);
    }

    class LoadButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            String sUPC = fields[0].getText();
            int UPC;
            if (sUPC.length() == 0) {
                JOptionPane.showMessageDialog(null,
                        "UPC cannot be empty");
                return;
            }
            try {
                UPC = Integer.parseInt(sUPC);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null,
                        "Invalid UPC");
                return;
            }
            ProductModel p = Driver.db.loadProduct(UPC);
            if (p == null) {
                JOptionPane.showMessageDialog(null,
                        "Product not found");
                return;
            }

            // Populate text fields at the proper time
            Runnable fillFields = new Runnable() {
                @Override
                public void run() {
                    fields[1].setText(p.name);
                    fields[2].setText("" + p.price);
                    fields[3].setText("" + p.qty);
                    fields[4].setText(p.vendor);
                    fields[5].setText(p.description);
                    fields[6].setText("" + p.taxSch);
                }
            };
            SwingUtilities.invokeLater(fillFields);
        }
    }

    class SaveButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            ProductModel p = new ProductModel();

            String[] textFields = new String[fields.length];
            for (int i = 0; i < textFields.length; i++) {
                textFields[i] = fields[i].getText();
            }

            // UPC
            if (textFields[0].length() == 0) {
                JOptionPane.showMessageDialog(null,
                        "UPC cannot be empty");
                return;
            }
            try {
                p.UPC = Integer.parseInt(textFields[0]);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null,
                        "Invalid UPC");
                return;
            }
            // Name
            if (textFields[1].length() == 0) {
                JOptionPane.showMessageDialog(null,
                        "Product name cannot be empty");
                return;
            }
            p.name = textFields[1];
            // Price
            try {
                p.price = Double.parseDouble(textFields[2]);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null,
                        "Invalid price");
                return;
            }
            // Quantity
            try {
                p.qty = Integer.parseInt(textFields[3]);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null,
                        "Invalid quantity");
                return;
            }
            // Vendor
            p.vendor = textFields[4];
            // Description
            p.description = textFields[5];
            // Tax Schedule
            try {
                p.taxSch = Double.parseDouble(textFields[6]);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null,
                        "Invalid Tax Schedule");
                return;
            }

            switch (Driver.db.saveProduct(p)) {
                case DBClient.ERR_DUPLICATE:
                    JOptionPane.showMessageDialog(null,
                            "UPC is a duplicate. Product add failed.");
                    break;
                case DBClient.SAVE_OK:
                    view.dispose();
                    System.out.println("[INFO] Product modified");
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
