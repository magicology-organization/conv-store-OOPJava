package Swing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import javax.swing.JPanel;

public class TopRoundedPanel extends JPanel {

    public TopRoundedPanel() {
        setOpaque(false); // Đảm bảo panel không tự động vẽ nền mặc định
    }

    @Override
    public void paint(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        
        // Vẽ hình chữ nhật bo góc trên
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15); // Bo cả bốn góc
        
        // Vẽ hình chữ nhật không bo góc dưới
        g2.fillRect(0, 15, getWidth(), getHeight() - 15); // Phần dưới không bo góc

        g2.dispose();
        super.paint(grphcs);
    }
}