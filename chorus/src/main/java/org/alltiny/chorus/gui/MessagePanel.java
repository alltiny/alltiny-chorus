package org.alltiny.chorus.gui;

import org.alltiny.chorus.model.app.AppMessage;

import javax.swing.*;
import java.awt.*;

/**
 * This panel renders a single {@link org.alltiny.chorus.model.app.AppMessage}.
 */
public class MessagePanel extends JPanel {

    private static final int INDICATOR_WIDTH = 3; // px

    public MessagePanel(AppMessage message) {
        super(new GridBagLayout());

        final JLabel text = new JLabel(message.getText());
        text.setFont(new Font("Monospaced", Font.PLAIN, 12));
        text.setBackground(new Color(224, 224, 224));
        text.setOpaque(true);

        this.setMinimumSize(new Dimension((int)text.getPreferredSize().getWidth() + INDICATOR_WIDTH, (int)text.getPreferredSize().getHeight()));
        this.setPreferredSize(new Dimension((int)text.getPreferredSize().getWidth() + INDICATOR_WIDTH, (int)text.getPreferredSize().getHeight()));
        this.setMaximumSize(new Dimension(-1, (int)text.getPreferredSize().getHeight()));

        this.add(text, new GridBagConstraints(0,0,1,0,1,0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(0,INDICATOR_WIDTH,0,0),0,0));

        switch (message.getType()) {
            case SUCCESS: setBackground(Color.GREEN); break;
            case ERROR: setBackground(Color.RED); break;
            case NEUTRAL: setBackground(Color.GRAY); break;
            default: setBackground(Color.BLACK);
        }


    }
}
