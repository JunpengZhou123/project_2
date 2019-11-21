import javax.swing.*;
import java.awt.*;
import java.util.ArrayDeque;

class ListTransactionUI extends BaseUI {
    ListTransactionUI() {
        super("Purchase History", 400, 250);

        // TODO: Get actual purchase history from db server
        ArrayDeque<TransactionModel> rawHist = new ArrayDeque<>();
        DefaultListModel<String> listData = new DefaultListModel<>();
        for (TransactionModel t : rawHist) {
            listData.addElement(t.toString());
        }

        JList purchases = new JList(listData);
        JScrollPane scrollPane = new JScrollPane(purchases,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        pane.add(scrollPane);

        JButton btnModTransaction = new JButton("Modify");
        JButton btnRemTransaction = new JButton("Remove");

        JPanel line = new JPanel();
        line.add(btnCancel);
        line.add(btnModTransaction);
        line.add(btnRemTransaction);
        pane.add(line);

        view.setVisible(true);
    }
}
