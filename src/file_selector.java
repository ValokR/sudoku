import javax.swing.*;
import java.util.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;

/**
 * Created by neil on 7/12/17.
 */
public class file_selector {
    final JFileChooser fc = new JFileChooser();
    public JPanel panel1;
    public File file;
    private JButton button1;

    public file_selector() {
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int returnVal = fc.showOpenDialog(panel1);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                }
            }
        });
    }
}
