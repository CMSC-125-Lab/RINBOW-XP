package com.rinbowxp.app;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class Keyboard extends JPanel{
    // Source - https://stackoverflow.com/a/13613236
    // Posted by FThompson, modified by community. See post 'Timeline' for change history
    // Retrieved 2026-02-10, License - CC BY-SA 3.0
    private ResourceManager resourceManager = new ResourceManager();
    private ImageIcon keyboardIcon = resourceManager.getImageIcon("Keyboard");

    String row1 = "QWERTYUIOP";
    String row2 = "ASDFGHJKL";
    String row3 = "ZXCVBNM";
    String[] rows = { row1, row2, row3};
    private double scale;
    private SpriteTransition spriteTransition;

    public Keyboard() {
        this(1.8);  // Default scale
    }

    public Keyboard(double scale) {
        this(scale, null);  // No sprite transition
    }

    public Keyboard(double scale, SpriteTransition spriteTransition) {
        this.scale = scale;
        this.spriteTransition = spriteTransition;
        setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        setOpaque(false);
        int scaledWidth = (int) Math.round(keyboardIcon.getIconWidth() * scale);
        int scaledHeight = (int) Math.round(keyboardIcon.getIconHeight() * scale);
        int buttonSize = (int) Math.round(18 * scale);
        System.out.println("Scaled dimensions: " + scaledWidth + "x" + scaledHeight);
        setPreferredSize(new java.awt.Dimension(scaledWidth, scaledHeight));

        JPanel keyboardPanel = new JPanel();
        keyboardPanel.setLayout(new GridBagLayout());
        keyboardPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.gridx = 0;
        gbc.gridy = 0;
        
        // Padding between each key in a single row
        JPanel row1Panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        // Padding between Keyboard row and Keyboard JPanel border
        row1Panel.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0)); // Padding between Keyboard row and Keyboard JPanel border
        row1Panel.setOpaque(false);
        row1Panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        for (int i = 0; i < row1.length(); i++) {
            JButton button = createKeyButton(buttonSize);
            row1Panel.add(button);
        }
        keyboardPanel.add(row1Panel, gbc);

        // Padding between each key in a single row
        JPanel row2Panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        // Padding between Keyboard row and Keyboard JPanel border (Also padding between row 1 and row 2)
        row2Panel.setBorder(BorderFactory.createEmptyBorder(10, 13, 0, 0));
        row2Panel.setOpaque(false);
        row2Panel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
        for (int i = 0; i < row2.length(); i++) {
            JButton button = createKeyButton(buttonSize);
            row2Panel.add(button);
        }
        gbc.gridy = 1;
        keyboardPanel.add(row2Panel, gbc);

        // Padding between each key in a single row
        JPanel row3Panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        // Padding between Keyboard row and Keyboard JPanel border (Also padding between row 2 and row 3)
        row3Panel.setBorder(BorderFactory.createEmptyBorder(12, 38, 0, 0));
        row3Panel.setOpaque(false);
        row3Panel.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
        for (int i = 0; i < row3.length(); i++) {
            JButton button = createKeyButton(buttonSize);
            row3Panel.add(button);
        }
        gbc.gridy = 2;
        keyboardPanel.add(row3Panel, gbc);

        add(keyboardPanel);
    }

    private JButton createKeyButton(int size) {
        JButton button = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                float alpha = getModel().isRollover() ? 0.65f : 0.0f;
                g2d.setColor(new Color(130, 130, 130, Math.round(255 * alpha)));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        button.setPreferredSize(new java.awt.Dimension(size, size));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setRolloverEnabled(true);
        button.setBorder(new RoundedBorder(10));
        
        // Add listener to advance sprite transition when button is clicked
        if (spriteTransition != null) {
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    spriteTransition.next();

                }
            });
        }
        
        return button;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Calls the original paintComponent
        // Draw the image onto the panel
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawImage(keyboardIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
    }

}

