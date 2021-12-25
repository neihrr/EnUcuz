package enUcuz.launcher;
import javax.swing.*;

import java.awt.*;

public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainFrame mySwing = new MainFrame();
                mySwing.setVisible(true);
                mySwing.setResizable(false);
                Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
                int x = (int) ((dimension.getWidth() - mySwing.getWidth()) / 2);
                int y = (int) ((dimension.getHeight() - mySwing.getHeight()) / 2);
                mySwing.setLocation(x, y);
            }
        });
    }
}
