package Swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RoundedMenuItem extends JMenuItem {
    private int arcWidth = 10;
    private int arcHeight = 10;
    private boolean isHovered = false;

    public RoundedMenuItem(String text) {
        super(text);
        setOpaque(false); // Tắt nền mặc định để tự vẽ
        setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        setBackground(Color.WHITE); // Màu nền mặc định

        // Xóa kiểu mặc định của JMenuItem để nó không can thiệp vào màu nền
        setUI(new javax.swing.plaf.basic.BasicMenuItemUI() {
            @Override
            protected void paintBackground(Graphics g, JMenuItem menuItem, Color bgColor) {
                // Không vẽ nền mặc định
            }
        });

        // Lắng nghe sự kiện di chuột vào và ra
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }
        });

        // Xử lý khi menu item được chọn (bằng bàn phím hoặc chuột)
        addChangeListener(e -> {
            isHovered = getModel().isArmed(); // Kiểm tra xem item có đang được chọn không
            repaint();
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Màu khi hover: #00785C
        Color backgroundColor = isHovered ? new Color(0x00785C) : getBackground();
        
        // Vẽ nền bo góc
        g2.setColor(backgroundColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arcWidth, arcHeight);

        // Vẽ border
        g2.setColor(Color.GRAY);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arcWidth, arcHeight);

        g2.dispose();
        
        super.paintComponent(g); // Gọi sau khi vẽ
    }
}
