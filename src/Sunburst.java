import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Tor Gammelgard
 * @version 2015-12-01
 */
public class Sunburst extends JPanel {

    Color color;
    int size;
    Timer timer;

    public Sunburst(int x, int y) {
        super();
        setLocation(x, y);
        setSize(0, 0);
        color = new Color(200, 0, 0, 255);
        timer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                size += 2;
                setBounds(getX() - 1, getY() - 1, size, size);
                int alpha = color.getAlpha();
                alpha -= 4;
                if (alpha > 0)
                    color = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
                else
                    timer.stop();
            }
        }){
            @Override
            public void stop() {
                super.stop();
                System.out.println("Stopped");
            }
        };

        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(color);
        g2.fillArc(0, 0, getWidth(), getHeight(), 0, 360);
    }
}
