package com.rinbowxp.app;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class Keyboard extends JPanel{
    // Source - https://stackoverflow.com/a/13613236
    // Posted by FThompson, modified by community. See post 'Timeline' for change history
    // Retrieved 2026-02-10, License - CC BY-SA 3.0
    private ResourceManager resourceManager = new ResourceManager();

    String row1 = "QWERTYUIOP";
    String row2 = "ASDFGHJKL";
    String row3 = "ZXCVBNM";
    String[] rows = { row1, row2, row3};

    public Keyboard() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setOpaque(false);
        // Padding between Keyboard panel and JPanel border
        setBorder(BorderFactory.createEmptyBorder(7, 45, 0, 0));

        JPanel keyboardPanel = new JPanel();
        keyboardPanel.setLayout(new GridBagLayout());
        keyboardPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.gridx = 0;
        gbc.gridy = 0;
        
        // Padding between each key in a single row
        JPanel row1Panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        row1Panel.setOpaque(false);
        for (char key : row1.toCharArray()) {
            JButton button = new JButton(Character.toString(key));
            button.setFont(resourceManager.getCousineRegular());
            button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createRaisedBevelBorder(),
                    BorderFactory.createEmptyBorder(2, 2, 2, 2)
            ));
            button.setOpaque(true);
            button.setPreferredSize(new java.awt.Dimension(40, 40));
            row1Panel.add(button);
        }
        keyboardPanel.add(row1Panel, gbc);

        // Padding between each key in a single row
        JPanel row2Panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        // Padding between Keyboard row and Keyboard JPanel border (Also padding between row 1 and row 2)
        row2Panel.setBorder(BorderFactory.createEmptyBorder(5, 8, 0, 0));
        row2Panel.setOpaque(false);
        for (char key : row2.toCharArray()) {
            JButton button = new JButton(Character.toString(key));
             button.setFont(resourceManager.getCousineRegular());
            button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createRaisedBevelBorder(),
                    BorderFactory.createEmptyBorder(2, 2, 2, 2)            ));
            button.setOpaque(true);
            button.setPreferredSize(new java.awt.Dimension(40, 40));
            row2Panel.add(button);
        }
        gbc.gridy = 1;
        keyboardPanel.add(row2Panel, gbc);

        // Padding between each key in a single row
        JPanel row3Panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        // Padding between Keyboard row and Keyboard JPanel border (Also padding between row 2 and row 3)
        row3Panel.setBorder(BorderFactory.createEmptyBorder(7, 17, 0, 0));
        row3Panel.setOpaque(false);
        for (char key : row3.toCharArray()) {
            JButton button = new JButton(Character.toString(key));
            button.setPreferredSize(new java.awt.Dimension(40, 40));
             button.setFont(resourceManager.getCousineRegular());
            button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createRaisedBevelBorder(),
                    BorderFactory.createEmptyBorder(2, 2, 2, 2)
            ));
            button.setOpaque(true);
            row3Panel.add(button);
        }
        gbc.gridy = 2;
        keyboardPanel.add(row3Panel, gbc);

        add(keyboardPanel);
    }

}
