import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by neil on 7/12/17.
 */
public class file_selector {
    public JPanel panel1;
    public File file;
    JButton button1;

    public file_selector() {
        final JFileChooser fc = new JFileChooser();
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnVal = fc.showOpenDialog(null);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    file = fc.getSelectedFile();
                    System.out.println("You chose to open " + file.getName());
                }

            }
        });
    }

    public File getFile() {
        return file;
    }
}
