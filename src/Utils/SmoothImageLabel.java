package Utils;

import Utils.ImageUtils;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

public class SmoothImageLabel extends JLabel {
    private BufferedImage original;

    public SmoothImageLabel(String resourcePath) {
        setHorizontalAlignment(CENTER);
        setVerticalAlignment(CENTER);
        try {
            original = ImageIO.read(getClass().getResource(resourcePath));
        } catch (Exception e) {
            System.err.println("Không đọc được ảnh: " + resourcePath + " -> " + e.getMessage());
        }

        // scale lại khi label thay đổi size
        addComponentListener(new ComponentAdapter() {
            @Override public void componentResized(ComponentEvent e) { refreshIcon(); }
            @Override public void componentShown(ComponentEvent e) { refreshIcon(); }
        });
    }

    private void refreshIcon() {
        if (original == null) return;
        int w = getWidth(), h = getHeight();
        if (w <= 0 || h <= 0) return;
        setIcon(new ImageIcon(ImageUtils.scaleSmooth(original, w, h)));
    }
}
