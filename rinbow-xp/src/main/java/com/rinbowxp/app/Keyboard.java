package com.rinbowxp.app;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Component;
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
import javax.swing.JTextArea;

public class Keyboard extends JPanel{
    // Source - https://stackoverflow.com/a/13613236
    // Posted by FThompson, modified by community. See post 'Timeline' for change history
    // Retrieved 2026-02-10, License - CC BY-SA 3.0
    private ResourceManager resourceManager = new ResourceManager();
    private JTextArea textArea;

    String row1 = "QWERTYUIOP";
    String row2 = "ASDFGHJKL";
    String row3 = "ZXCVBNM";
    String[] rows = { row1, row2, row3};
    
    private static final String KEYBOARD_IMAGES_PATH = "keyboard/";

    public Keyboard() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        setOpaque(false);
        // Don't set preferred size - let it adapt to container
        // setPreferredSize(new Dimension(550, 200));

        JPanel keyboardPanel = new JPanel();
        keyboardPanel.setLayout(new GridBagLayout());
        keyboardPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.gridx = 0;
        gbc.gridy = 0;
        
        // Padding between each key in a single row
        JPanel row1Panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 7, 0));
        // Padding between Keyboard row and Keyboard JPanel border
        row1Panel.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0)); // Padding between Keyboard row and Keyboard JPanel border
        row1Panel.setOpaque(false);
        for (int i = 0; i < row1.length(); i++) {
            JButton button = createKeyButton(row1.charAt(i));
            row1Panel.add(button);
        }
        keyboardPanel.add(row1Panel, gbc);

        // Padding between each key in a single row
        JPanel row2Panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 7, 0));
        // Padding between Keyboard row and Keyboard JPanel border (Also padding between row 1 and row 2)
        row2Panel.setBorder(BorderFactory.createEmptyBorder(10, 13, 0, 0));
        row2Panel.setOpaque(false);
        for (int i = 0; i < row2.length(); i++) {
            JButton button = createKeyButton(row2.charAt(i));
            row2Panel.add(button);
        }
        gbc.gridy = 1;
        keyboardPanel.add(row2Panel, gbc);

        // Padding between each key in a single row
        JPanel row3Panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 9, 0));
        // Padding between Keyboard row and Keyboard JPanel border (Also padding between row 2 and row 3)
        row3Panel.setBorder(BorderFactory.createEmptyBorder(12, 38, 0, 0));
        row3Panel.setOpaque(false);
        for (int i = 0; i < row3.length(); i++) {
            JButton button = createKeyButton(row3.charAt(i));
            row3Panel.add(button);
        }
        gbc.gridy = 2;
        keyboardPanel.add(row3Panel, gbc);

        add(keyboardPanel);
    }

    private JButton createKeyButton(char keyChar) {
        String idleImagePath = KEYBOARD_IMAGES_PATH + keyChar + ".png";
        String clickedImagePath = KEYBOARD_IMAGES_PATH + keyChar + "-clicked.png";
        
        // Load images
        ImageIcon idleImage = new ImageIcon(resourceManager.getURLFromFiles(idleImagePath));
        ImageIcon clickedImage = new ImageIcon(resourceManager.getURLFromFiles(clickedImagePath));
        
        JButton button = new JButton() {
            private boolean isPressed = false;
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw the appropriate image based on button state
                ImageIcon currentImage = isPressed || getModel().isArmed() ? clickedImage : idleImage;
                g2d.drawImage(currentImage.getImage(), 0, 0, getWidth(), getHeight(), this);
                g2d.dispose();
                
            }
        };
        
        if ("zxcvbnm".contains(Character.toString(keyChar).toLowerCase())) {
            System.out.print("Key: " + keyChar + " - ");
            button.setPreferredSize(new java.awt.Dimension(44,44));
        } else {
            button.setPreferredSize(new java.awt.Dimension(45, 45));
        }
        
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setRolloverEnabled(true);
        
        // Add action to type character into text area
        button.addActionListener(e -> {
            if (textArea != null) {
                textArea.append(String.valueOf(keyChar));
            }
        });
        
        // Track button press state
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                ((JButton) e.getSource()).putClientProperty("pressed", true);
                ((Component) e.getSource()).repaint();
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                ((JButton) e.getSource()).putClientProperty("pressed", false);
                ((Component) e.getSource()).repaint();
            }
        });
        
        return button;
    }

    public void setTextArea(JTextArea textArea) {
        this.textArea = textArea;
    }

}

