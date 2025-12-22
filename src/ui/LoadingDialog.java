package ui;

import javax.swing.*;
import java.awt.*;
// Added because it takes a long time to wait for LLM's response.
@SuppressWarnings("serial")
public class LoadingDialog extends JDialog {

    public LoadingDialog(JFrame parent, String message) {
        super(parent, false);

        setUndecorated(true);
        setLayout(new BorderLayout(10, 10));
        setSize(300, 100);
        setLocationRelativeTo(parent);

        JLabel label = new JLabel(message, SwingConstants.CENTER);
        JProgressBar bar = new JProgressBar();
        bar.setIndeterminate(true);

        add(label, BorderLayout.NORTH);
        add(bar, BorderLayout.CENTER);
    }
}
