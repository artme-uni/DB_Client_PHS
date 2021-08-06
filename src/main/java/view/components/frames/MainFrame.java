package view.components.frames;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame{
    private static final int BORDER_PERCENTAGE = 3;

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainPanel = new JPanel(cardLayout);

    public MainFrame(String frameName, double widthProportion, double heightProportion, double scale) {
        configureFrame(frameName);
        addMainPanel(widthProportion, heightProportion, scale);
        pack();

        setVisible(true);
        setLocationRelativeTo(null);
    }

    private void configureFrame(String frameName) {
        setTitle(frameName);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setFocusable(true);
    }

    private void addMainPanel(double widthProportion, double heightProportion, double scale) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double height = screenSize.height * scale;
        double width = screenSize.height * widthProportion / heightProportion * scale;
        mainPanel.setPreferredSize(new Dimension((int) width, (int) height));

        int borderSize = (int) (width * BORDER_PERCENTAGE / 100);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(borderSize, borderSize, borderSize, borderSize));

        add(mainPanel);
    }

    public void addPanel(JPanel panel, String panelName){
        mainPanel.add(panel, panelName);
        setVisible(true);
    }

    public void showPanel(String panelName){
        cardLayout.show(mainPanel, panelName);
    }
}
