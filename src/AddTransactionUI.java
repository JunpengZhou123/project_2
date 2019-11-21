import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Calendar;

class AddTransactionUI extends BaseUI {
    // Info strings for extraordinary circumstances
    private static final String INFO_INVALID = "[Invalid]";
    private static final String INFO_NOT_FOUND = "[Not Found]";
    // Column IDs for `labelText`
    private static final int LABEL_TEXT_FIELD = 0;
    private static final int LABEL_TEXT_INFO = 1;
    // Row IDs for `fields`
    private static final int FIELDS_UUID = 0;
    private static final int FIELDS_CUST_UUID = 1;
    private static final int FIELDS_PROD_UPC = 2;
    private static final int FIELDS_QTY = 3;
    // Row IDs for `labels`
    private static final int LABELS_DATE = 0;
    private static final int LABELS_CUST_NAME = 1;
    private static final int LABELS_PROD_NAME = 2;
    private static final int LABELS_PRICE = 3;
    // Row IDs for `auxLabels`
    private static final int AUX_SUBTOTAL = 0;
    private static final int AUX_TAX = 1;
    private static final int AUX_TOTAL = 2;

    private static final String[][] labelText = {
            {"UUID", "Timestamp: "},
            {"Customer UUID", "Customer Name: "},
            {"Product UPC", "Product Name: "},
            {"Quantity", "Price: "}};
    private JTextField[] fields = new JTextField[labelText.length];
    private JLabel[] labels = new JLabel[labelText.length];

    private static final String[] auxLabelText = {"Subtotal: $",
            "\tTax: $", "\tTotal: $"};
    private JLabel[] auxLabels = new JLabel[auxLabelText.length];

    private CustomerModel c;
    private ProductModel p;
    private TransactionModel t;

    AddTransactionUI() {
        super("New Purchase", 600, 300);

        Container pane = view.getContentPane();
        pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));

        for (int i = 0; i < labelText.length; i++) {
            fields[i] = new JTextField(TEXT_FIELD_WIDTH);
            labels[i] = new JLabel(labelText[i][LABEL_TEXT_INFO]);
            JPanel line = new JPanel(new FlowLayout());
            line.add(new JLabel(labelText[i][LABEL_TEXT_FIELD]));
            line.add(fields[i]);
            line.add(labels[i]);
            view.getContentPane().add(line);
        }

        JPanel auxLine = new JPanel(new FlowLayout());
        for (int i = 0; i < auxLabelText.length; i++) {
            auxLabels[i] = new JLabel(auxLabelText[i]);
            auxLine.add(auxLabels[i]);
        }
        view.getContentPane().add(auxLine);

        // Set up info displays
        t = new TransactionModel();
        t.date = Calendar.getInstance().getTime().toString();
        labels[LABELS_DATE].setText(labelText[LABELS_DATE][LABEL_TEXT_INFO] +
                t.date);
        fields[FIELDS_CUST_UUID].addFocusListener(new CustUUIDFocusListener());
        fields[FIELDS_PROD_UPC].addFocusListener(new ProdUPCFocusListener());
        fields[FIELDS_QTY].getDocument().addDocumentListener(
                new QtyChangeListener());

        JButton btnAdd = new JButton("Add");
        btnAdd.addActionListener(new AddButtonListener());

        JPanel buttons = new JPanel(new FlowLayout());
        buttons.add(btnCancel);
        buttons.add(btnAdd);
        pane.add(buttons);

        view.setVisible(true);
    }

    class AddButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            String UUID = fields[FIELDS_UUID].getText();
            if (UUID.length() == 0) {
                JOptionPane.showMessageDialog(null,
                        "UUID cannot be empty");
                return;
            }
            try {
                t.UUID = Integer.parseInt(UUID);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null,
                        "Invalid UUID");
                return;
            }
            if (p == null) {
                JOptionPane.showMessageDialog(null,
                        "Invalid product");
                return;
            }
            if (c == null) {
                JOptionPane.showMessageDialog(null,
                        "Invalid customer");
                return;
            }

            switch (Driver.db.saveTransaction(t)) {
                case DBClient.ERR_DUPLICATE:
                    JOptionPane.showMessageDialog(null,
                            "UUID is a duplicate. Transaction " +
                                    "add failed.");
                    break;
                case DBClient.SAVE_OK:
                    Driver.printer.print("BILL OF SALE", c, p, t,
                            "");
                    view.dispose();
                    System.out.println("[INFO] New Transaction saved");
                    break;
                default:
                    System.err.println("[ERR] Unreachable State");
            }
        }
    }

    class CustUUIDFocusListener implements FocusListener {
        public void focusGained(FocusEvent event) {
            // pass
        }
        public void focusLost(FocusEvent event) {
            JLabel f = labels[LABELS_CUST_NAME];
            String l = labelText[LABELS_CUST_NAME][LABEL_TEXT_INFO];
            String UUID = fields[FIELDS_CUST_UUID].getText();

            if (UUID.length() == 0) {
                f.setText(l);
                return;
            }
            try {
                t.custUUID = Integer.parseInt(UUID);
            } catch (NumberFormatException e) {
                f.setText(l + INFO_INVALID);
                return;
            }
            c = Driver.db.loadCustomer(t.custUUID);
            if (c == null) {
                f.setText(l + INFO_NOT_FOUND);
                return;
            }
            f.setText(l + c.name);
        }
    }

    class ProdUPCFocusListener implements FocusListener {
        public void focusGained(FocusEvent event) {
            // pass
        }
        public void focusLost(FocusEvent event) {
            JLabel f = labels[LABELS_PROD_NAME];
            JLabel f2 = labels[LABELS_PRICE];
            String l = labelText[LABELS_PROD_NAME][LABEL_TEXT_INFO];
            String l2 = labelText[LABELS_PRICE][LABEL_TEXT_INFO];
            String UPC = fields[FIELDS_PROD_UPC].getText();

            if (UPC.length() == 0) {
                f.setText(l);
                f2.setText(l2);
                return;
            }
            try {
                t.prodUPC = Integer.parseInt(UPC);
            } catch (NumberFormatException e) {
                f.setText(l + INFO_INVALID);
                f2.setText(l2);
                return;
            }
            p = Driver.db.loadProduct(t.prodUPC);
            if (p == null) {
                f.setText(l + INFO_NOT_FOUND);
                f2.setText(l2);
                return;
            }
            f.setText(l + p.name);
            f2.setText(l2 + p.price);
            t.price = p.price;
        }
    }

    class QtyChangeListener implements DocumentListener {
        public void insertUpdate(DocumentEvent event) {
            update();
        }
        public void removeUpdate(DocumentEvent event) {
            update();
        }
        public void changedUpdate(DocumentEvent event) {
            update();
        }
        private void update() {
            JLabel f = labels[LABELS_PRICE];
            String l = labelText[LABELS_PRICE][LABEL_TEXT_INFO];
            String qty = fields[FIELDS_QTY].getText();

            if (p == null || qty.length() == 0) {
                updateTotals(0);
                return;
            }
            try {
                t.qty = Integer.parseInt(qty);
            } catch (NumberFormatException e) {
                t.qty = 0; // Handled by if statement immediately below
            }
            if (t.qty <= 0) {
                updateTotals(0);
                return;
            }

            if (t.qty > p.qty) { // Insufficient stock
                JOptionPane.showMessageDialog(null,
                        "Insufficient stock for this item. " +
                                "Quantity has been reduced to " + p.qty +
                                " to match current stock.",
                        "Insufficient Stock",
                        JOptionPane.INFORMATION_MESSAGE);

                // Set text in field, must be done after lock is released
                Runnable setFillableQty = new Runnable() {
                    @Override
                    public void run() {
                        fields[FIELDS_QTY].setText("" + p.qty);
                    }
                };
                SwingUtilities.invokeLater(setFillableQty);

                t.qty = p.qty;
            }

            updateTotals();
        }
        private void updateTotals() {
            updateTotals(t.qty);
        }
        private void updateTotals(int qty) {
            if (qty == 0 || p == null) {
                t.subtotal = t.tax = t.total = 0;
            } else {
                t.subtotal = qty * p.price;
                // Note that `p.taxSch` is a percentage
                t.tax = t.subtotal * p.taxSch / 100;
                t.total = t.subtotal + t.tax;
            }

            auxLabels[AUX_SUBTOTAL].setText(auxLabelText[AUX_SUBTOTAL] +
                    String.format("%.2f", t.subtotal));
            auxLabels[AUX_TAX].setText(auxLabelText[AUX_TAX] +
                    String.format("%.2f", t.tax));
            auxLabels[AUX_TOTAL].setText(auxLabelText[AUX_TOTAL] +
                    String.format("%.2f", t.total));
        }
    }
}
