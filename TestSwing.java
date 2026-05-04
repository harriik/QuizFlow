import javax.swing.*;
import java.awt.*;
public class TestSwing {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Test");
            JPanel p = new JPanel();
            p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
            JLabel l = new JLabel("<html><div style='width: 500px;'>Test Question</div></html>");
            p.add(l);
            f.add(p);
            f.pack();
            System.out.println("Label size: " + l.getSize() + " pref: " + l.getPreferredSize());
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setVisible(true);
            Timer t = new Timer(2000, e -> System.exit(0));
            t.setRepeats(false);
            t.start();
        });
    }
}
