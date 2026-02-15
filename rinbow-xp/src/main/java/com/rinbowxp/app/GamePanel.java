package com.rinbowxp.app;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URI;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class GamePanel extends JPanel implements MouseListener{
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private Image bg_image;
    private JPanel upperPanel, lowerPanel, lowerBorderPanel;
    private JLabel title, homePageButton, exitButtonLabel, minimizeButtonLabel, gamedescriptionButton;
    private Font customFont = new Font("Arial", Font.PLAIN, 21);
    private Font boldCustomFont = new Font("Arial", Font.BOLD, 21);
    private Font titleFont = new Font("Arial", Font.BOLD, 56);
    private ImageIcon exitButton, exitButtonClicked, minimizeButton, minimizeButtonClicked;

    private Dimension frameDimension;

    private ResourceManager resourceManager;
    private SpriteTransition spriteTransition;

    public GamePanel(CardLayout cardLayout, JPanel cardPanel, ResourceManager resourceManager,
                            Dimension frameDimension){
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
        this.resourceManager = resourceManager;
        this.frameDimension = frameDimension;

        bg_image = resourceManager.getImageIcon("Contact Panel BG").getImage();
        
        // Initialize sprite transition
        spriteTransition = new SpriteTransition(resourceManager);

        // PNG transition speed
        spriteTransition.setTransitionSpeed(20);

        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        customFont = resourceManager.getCousineRegular();
        customFont = customFont.deriveFont(Font.PLAIN, (int) frameDimension.getHeight()/25);
        boldCustomFont = resourceManager.getCousineBold();
        boldCustomFont = boldCustomFont.deriveFont(Font.BOLD, (int) frameDimension.getHeight()/25);
        titleFont = resourceManager.getAnonymousProBold();
        titleFont = titleFont.deriveFont(Font.BOLD, (int) frameDimension.getHeight()/10);

        setUpperPanel();
        setLowerPanel();
        setLowerBorderPanel();

        // add the upper and lower panels
        add(upperPanel, BorderLayout.NORTH);
        add(lowerPanel, BorderLayout.CENTER);
        add(lowerBorderPanel, BorderLayout.SOUTH);

    }

    private void setUpperPanel() {
        GridBagConstraints gbc = new GridBagConstraints();
        upperPanel = new JPanel();
        upperPanel.setOpaque(false);
        upperPanel.setLayout(new GridBagLayout());

        title = new JLabel("RINBOWS XP OS");
        title.setForeground(java.awt.Color.lightGray);
        title.setFont(boldCustomFont);
        title.addMouseListener(this);

        homePageButton = new JLabel("Back To Home");
        homePageButton.setForeground(java.awt.Color.black);
        homePageButton.setFont(customFont);
        homePageButton.addMouseListener(this);

        exitButton = resourceManager.getImageIcon("Exit Button");
        Image exitButtonResized = exitButton.getImage().getScaledInstance((int) frameDimension.getWidth()/25, (int) frameDimension.getWidth()/25, Image.SCALE_DEFAULT);
        exitButton = new ImageIcon(exitButtonResized);

        minimizeButton = resourceManager.getImageIcon("Minimize Button");
        Image minimizeButtonResized = minimizeButton.getImage().getScaledInstance((int) frameDimension.getWidth()/25, (int) frameDimension.getWidth()/25, Image.SCALE_DEFAULT);
        minimizeButton = new ImageIcon(minimizeButtonResized);

        exitButtonClicked = resourceManager.getImageIcon("Exit Button Clicked");
        Image exitButtonClickedResized = exitButtonClicked.getImage().getScaledInstance((int) frameDimension.getWidth()/25, (int) frameDimension.getWidth()/25, Image.SCALE_DEFAULT);
        exitButtonClicked = new ImageIcon(exitButtonClickedResized);

        minimizeButtonClicked = resourceManager.getImageIcon("Minimize Button Clicked");
        Image minimizeButtonClickedResized = minimizeButtonClicked.getImage().getScaledInstance((int) frameDimension.getWidth()/25, (int) frameDimension.getWidth()/25, Image.SCALE_DEFAULT);
        minimizeButtonClicked = new ImageIcon(minimizeButtonClickedResized);

        exitButtonLabel = new JLabel(exitButton);
        exitButtonLabel.addMouseListener(this);

        minimizeButtonLabel = new JLabel(minimizeButton);
        minimizeButtonLabel.addMouseListener(this);

        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        navPanel.setOpaque(false);

        navPanel.add(homePageButton);       // HomePage Button

        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;

        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets((int) frameDimension.getHeight()/50, (int) (frameDimension.getWidth()/27.5), 0, 0);
        upperPanel.add(title, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0; 
        upperPanel.add(new JLabel(""), gbc);

        gbc.gridx = 2;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets((int) frameDimension.getHeight()/28, 0, 0, 10);
        upperPanel.add(minimizeButtonLabel, gbc);

        gbc.gridx = 3;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets((int) frameDimension.getHeight()/28, 0, 0, (int) (frameDimension.getWidth()/27.5));
        upperPanel.add(exitButtonLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(
            (int)(frameDimension.getHeight() / 30),
            (int)(frameDimension.getWidth() / 80),
            (int)(frameDimension.getHeight() / 40),
            0
        );

        upperPanel.add(navPanel, gbc);
        upperPanel.setBorder(BorderFactory.createEmptyBorder((int)(frameDimension.getHeight() / 45), 0, 0, 0));
        
    }

    private void setLowerPanel() {
    lowerPanel = new JPanel();
    lowerPanel.setLayout(new GridBagLayout());
    lowerPanel.setOpaque(false);

    lowerPanel.setBorder(BorderFactory.createEmptyBorder(
        0,
        (int)(frameDimension.getWidth() / 45),
        0,
        (int)(frameDimension.getWidth() / 45)
    ));

    // Left Panel (60%)
    JPanel leftPanel = new JPanel();
    leftPanel.setOpaque(false);
    leftPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
    leftPanel.setLayout(new GridBagLayout());

    GridBagConstraints leftGbc = new GridBagConstraints();
    leftGbc.gridx = 0;
    leftGbc.fill = GridBagConstraints.BOTH;
    leftGbc.weightx = 1.0;

    // Monitor Panel (NORTH) - fills all available space
    JPanel monitorPanel = new JPanel();
    monitorPanel.setOpaque(false);
    monitorPanel.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
    leftGbc.gridy = 0;
    leftGbc.weighty = 1.0;  // Expand to fill all space
    leftGbc.fill = GridBagConstraints.BOTH;  // Fill both horizontally and vertically
    leftPanel.add(monitorPanel, leftGbc);

    // Keyboard Panel (SOUTH) - only as tall as keyboard
    JPanel keyboardPanel = new JPanel();
    keyboardPanel.setOpaque(false);
    keyboardPanel.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
    keyboardPanel.setLayout(new GridBagLayout());
    GridBagConstraints kbc = new GridBagConstraints();
    kbc.anchor = GridBagConstraints.CENTER;
    keyboardPanel.add(new Keyboard(1.5, spriteTransition), kbc);  // Pass spriteTransition to Keyboard
    leftGbc.gridy = 1;
    leftGbc.weighty = 0;  // Don't expand vertically - only keyboard's height
    leftGbc.fill = GridBagConstraints.HORIZONTAL;
    leftPanel.add(keyboardPanel, leftGbc);

    // Right Panel (40%) - Square with height = lowerPanel height
    JPanel rightPanel = new JPanel() {
        @Override
        public Dimension getPreferredSize() {
            int height = lowerPanel.getHeight();
            return new Dimension(height, height);
        }
    };
    rightPanel.setOpaque(false);
    rightPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
    rightPanel.setLayout(new java.awt.BorderLayout());
    
    // Add sprite display button - transparent, non-clickable, scales via paintIcon to preserve GIF animation
    JButton spriteButton = new JButton() {
        @Override
        protected void paintComponent(Graphics g) {
            // Do NOT call super.paintComponent(g) to avoid filling background
            setOpaque(false);
            ImageIcon currentSprite = (ImageIcon) getIcon();
            if (currentSprite == null) return;

            int imgWidth = currentSprite.getIconWidth();
            int imgHeight = currentSprite.getIconHeight();
            int panelWidth = getWidth();
            int panelHeight = getHeight();
            if (panelWidth <= 0 || panelHeight <= 0 || imgWidth <= 0 || imgHeight <= 0) return;

            double scale = Math.min((double) panelWidth / imgWidth, (double) panelHeight / imgHeight) * 0.95;
            int scaledWidth = (int) Math.max(1, Math.round(imgWidth * scale));
            int scaledHeight = (int) Math.max(1, Math.round(imgHeight * scale));
            int x = (panelWidth - scaledWidth) / 2;
            int y = (panelHeight - scaledHeight) / 2;

            Graphics2D g2d = (Graphics2D) g.create();
            try {
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.translate(x, y);
                g2d.scale(scaledWidth / (double) imgWidth, scaledHeight / (double) imgHeight);
                // Paint the icon using ImageIcon so GIF frames animate
                currentSprite.paintIcon(this, g2d, 0, 0);
            } finally {
                g2d.dispose();
            }
        }
    };
    // Make the button visually passive and non-interactive
    spriteButton.setOpaque(false);
    spriteButton.setContentAreaFilled(false);
    spriteButton.setBorderPainted(false);
    spriteButton.setFocusPainted(false);
    spriteButton.setFocusable(false);
    spriteButton.setRolloverEnabled(false);
    // Do not add any action listeners; clicks do nothing
    rightPanel.add(spriteButton, java.awt.BorderLayout.CENTER);

    // Update the button's icon whenever the sprite changes
    Runnable updateSprite = () -> {
        ImageIcon icon = spriteTransition.getCurrentImage();
        if (icon != null) {
            spriteButton.setIcon(icon);
            spriteButton.repaint();
        }
    };
    spriteTransition.setOnFrameChange(updateSprite);
    javax.swing.SwingUtilities.invokeLater(updateSprite);

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridy = 0;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.weighty = 1.0;

    gbc.gridx = 0;
    gbc.weightx = 0.53;
    lowerPanel.add(leftPanel, gbc);

    gbc.gridx = 1;
    gbc.weightx = 0;  // Don't expand horizontally
    gbc.weighty = 0;  // Don't expand vertically
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.fill = GridBagConstraints.NONE;  // Don't stretch
    lowerPanel.add(rightPanel, gbc);

}

    private void setLowerBorderPanel() {
        lowerBorderPanel = new JPanel();
        lowerBorderPanel.setOpaque(false);
        lowerBorderPanel.setPreferredSize(new Dimension(0, (int)(frameDimension.getHeight() /10)));
        lowerBorderPanel.setBorder(BorderFactory.createEmptyBorder(
            (int)(frameDimension.getHeight() / 64),
            (int)(frameDimension.getWidth() / 32),
            (int)(frameDimension.getHeight() / 64),
            (int)(frameDimension.getWidth() / 32)
        ));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // don't proceed if bg_image is null
        if (bg_image == null) {
            System.out.println("Background Image failed to Load");
            return;
        }


        // proceed if bg_image is not null
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawImage(bg_image, 0, 0, getWidth(), getHeight(), this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == minimizeButtonLabel) {
            System.out.println("Minimize Button Pressed");
            java.awt.Window window = javax.swing.SwingUtilities.getWindowAncestor(this);
            if (window instanceof JFrame) {
            ((JFrame) window).setState(JFrame.ICONIFIED);
            }
        }
        else if (e.getSource() == exitButtonLabel) {
            System.out.println("Exit Button Pressed");
            System.exit(0);
        }
        else if (e.getSource() == homePageButton) {
            cardLayout.show(cardPanel, "Home Page");
        }
         else if (e.getSource() == title){
            Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
            if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                try {
                    desktop.browse(new URI("https://github.com/CMSC-125-Lab/RINBOW-XP"));
                }
                catch (Exception e1) {
                    System.out.println("Desktop browsing Failed.");
                    e1.printStackTrace();
                }
            }
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (e.getSource() == homePageButton) {
            homePageButton.setFont(boldCustomFont);

        }else if (e.getSource() == exitButtonLabel) {
            exitButtonLabel.setIcon(exitButtonClicked);
        }
        else if (e.getSource() == minimizeButtonLabel) {
            minimizeButtonLabel.setIcon(minimizeButtonClicked);
        }

    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (e.getSource() == homePageButton){
            homePageButton.setFont(customFont);

        }else if(e.getSource() == gamedescriptionButton){
            gamedescriptionButton.setFont(customFont);
        }
        else if (e.getSource() == exitButtonLabel) {
            exitButtonLabel.setIcon(exitButton);
        }
        else if (e.getSource() == minimizeButtonLabel) {
            minimizeButtonLabel.setIcon(minimizeButton);       
        }
    }
}


