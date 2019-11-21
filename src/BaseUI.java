import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

abstract class BaseUI {
    static final int TEXT_FIELD_WIDTH = 20;
    JFrame view;
    Container pane;
    String dialogName;
    JButton btnCancel;

    BaseUI(String name, int sizeW, int sizeH) {
        dialogName = name;
        System.out.println("[INFO] Displaying " + dialogName + " dialog.");
        view = new JFrame();
        view.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        view.setTitle(dialogName);
        view.setSize(sizeW, sizeH);
        pane = view.getContentPane();
        pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));

        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new CancelButtonListener());
    }

    class CancelButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            view.dispose();
            System.out.println("[INFO] " + dialogName + " dialog dismissed");
        }
    }
}
