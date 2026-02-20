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
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class SettingsPage extends JPanel implements MouseListener{
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private Image bg_image;
    private JPanel upperPanel, lowerPanel;
    private JLabel title, homePageButton, contentPageButton, contactPageButton, settingsButton, exitButtonLabel, minimizeButtonLabel;
    private Font customFont = new Font("Arial", Font.PLAIN, 21);
    private Font boldCustomFont = new Font("Arial", Font.BOLD, 21);
    private Font titleFont = new Font("Arial", Font.BOLD, 56);
    private ImageIcon exitButton, exitButtonClicked, minimizeButton, minimizeButtonClicked;
    private JLabel toggleSound, toggleSoundFX;
    private JSlider volumeSlider;
    private float newVolume;
    
    private Dimension frameDimension;

    private ResourceManager resourceManager;

    public SettingsPage(CardLayout cardLayout, JPanel cardPanel, ResourceManager resourceManager,
                            Dimension frameDimension){
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
        this.resourceManager = resourceManager;
        this.frameDimension = frameDimension;

        bg_image = resourceManager.getImageIcon("Contact Panel BG").getImage();

        this.setLayout(new BorderLayout());

        customFont = resourceManager.getCousineRegular();
        customFont = customFont.deriveFont(Font.PLAIN, (int) frameDimension.getHeight()/25);
        boldCustomFont = resourceManager.getCousineBold();
        boldCustomFont = boldCustomFont.deriveFont(Font.BOLD, (int) frameDimension.getHeight()/25);
        titleFont = resourceManager.getAnonymousProBold();
        titleFont = titleFont.deriveFont(Font.BOLD, (int) frameDimension.getHeight()/10);

        setUpperPanel();
        setLowerPanel();

        // add the upper and lower panels
        add(upperPanel, BorderLayout.NORTH);
        add(lowerPanel, BorderLayout.CENTER);

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

        homePageButton = new JLabel("Home");
        homePageButton.setForeground(java.awt.Color.black);
        homePageButton.setFont(customFont);
        homePageButton.addMouseListener(this);

        contentPageButton = new JLabel("Rules");
        contentPageButton.setForeground(java.awt.Color.black);
        contentPageButton.setFont(customFont);
        contentPageButton.addMouseListener(this);

        contactPageButton = new JLabel("Developers");
        contactPageButton.setForeground(java.awt.Color.black);
        contactPageButton.setFont(customFont);
        contactPageButton.addMouseListener(this);
        
        settingsButton = new JLabel("Settings");
        settingsButton.setForeground(java.awt.Color.black);
        settingsButton.setFont(boldCustomFont);
        settingsButton.addMouseListener(this);

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

        navPanel.add(homePageButton);       // Home
        navPanel.add(contentPageButton);      // Rules
        navPanel.add(contactPageButton);     // Contact
        navPanel.add(settingsButton);       // Settings

        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;

        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets((int) frameDimension.getHeight()/16, (int) (frameDimension.getWidth()/27.5), 0, 0);
        upperPanel.add(title, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0; 
        upperPanel.add(new JLabel(""), gbc);

        gbc.gridx = 2;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets((int) frameDimension.getHeight()/18, 0, 0, 10);
        upperPanel.add(minimizeButtonLabel, gbc);

        gbc.gridx = 3;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets((int) frameDimension.getHeight()/18, 0, 0, (int) (frameDimension.getWidth()/27.5));
        upperPanel.add(exitButtonLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(
                (int)(frameDimension.getHeight() / 28),
                (int)(frameDimension.getWidth() / 80),
                (int)(frameDimension.getHeight() / 10.4),
                0
        );

        upperPanel.add(navPanel, gbc);
    }

    private void setLowerPanel() {
        lowerPanel = new JPanel();
        lowerPanel.setOpaque(false);
        lowerPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0.3; // Allocates space and pushes components below it down
        gbc.fill = GridBagConstraints.VERTICAL;
        JPanel topSpacer = new JPanel();
        topSpacer.setOpaque(false);
        lowerPanel.add(topSpacer, gbc);

        // Reset weighty for content components
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridwidth = 1;


        JLabel title = new JLabel("Sound Settings");
        title.setForeground(new Color(0x0057cc));
        title.setFont(titleFont.deriveFont(64f));

        
        gbc.gridy = 1;
        // Add vertical insets above and below the title
        gbc.insets = new Insets(0, 0, (int) (frameDimension.getHeight()/ 20), 0);
        lowerPanel.add(title, gbc);


        gbc.gridy = 2; 
        
        // Reset insets or set new ones as needed for spacing between title and toggle label
        gbc.insets = new Insets((int) (frameDimension.getHeight()/ 20), 0, (int) (frameDimension.getHeight()/ 73), 0); 

        toggleSound = new JLabel("TOGGLE BG MUSIC: ON");
        toggleSound.setForeground(java.awt.Color.black);
        toggleSound.setFont(customFont);
        toggleSound.addMouseListener(this);
        

        lowerPanel.add(toggleSound, gbc); 
        
        toggleSoundFX = new JLabel("TOGGLE SOUND FX: ON");
        toggleSoundFX.setForeground(java.awt.Color.black);
        toggleSoundFX.setFont(customFont);
        toggleSoundFX.addMouseListener(this);

        gbc.gridy = 3; 
        gbc.insets = new Insets((int) (frameDimension.getHeight()/ 30), 0, (int) (frameDimension.getHeight()/ 73), 0); 

        lowerPanel.add(toggleSoundFX, gbc);

        volumeSlider = new JSlider(0, 100, 80); 
        volumeSlider.setOpaque(false);
        volumeSlider.setPreferredSize(new Dimension((int) (frameDimension.getWidth() / 3.5), (int) (frameDimension.getHeight() / 20)));
        
        // Add a ChangeListener to update both BGM and SFX volume as the slider moves
        volumeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                newVolume = volumeSlider.getValue() / 100.0f;
                SoundManager.getInstance().setMasterVolume(newVolume);
                System.out.println("Volume set to: " + newVolume);
            }
        });

        gbc.gridy = 4; // New row for the slider
        gbc.insets = new Insets((int) (frameDimension.getHeight()/ 73), 0, (int) (frameDimension.getHeight()/ 5), 0);
        lowerPanel.add(volumeSlider, gbc);


        gbc.gridy = 5;
        gbc.weighty = 1.0; 
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.insets = new Insets(0, 0, 0, 0);
        JPanel bottomFiller = new JPanel();
        bottomFiller.setOpaque(false);
        lowerPanel.add(bottomFiller, gbc);
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
        else if (e.getSource() == contentPageButton) {
            cardLayout.show(cardPanel, "Rules Page");
        }
        else if (e.getSource() == contactPageButton) {
            cardLayout.show(cardPanel, "Contact Page");
        }
        else if (e.getSource() == settingsButton) {
            cardLayout.show(cardPanel, "Settings Page");
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
        } if (e.getSource() == toggleSound) {
            boolean nowEnabled = !SoundManager.getInstance().isBGMEnabled();
            SoundManager.getInstance().setBGMEnabled(nowEnabled);
            toggleSound.setText("TOGGLE BG MUSIC: " + (nowEnabled ? "ON" : "OFF"));
        } else if (e.getSource() == toggleSoundFX) {
            boolean nowEnabled = !SoundManager.getInstance().isSFXEnabled();
            SoundManager.getInstance().setSFXEnabled(nowEnabled);
            toggleSoundFX.setText("TOGGLE SOUND FX: " + (nowEnabled ? "ON" : "OFF"));
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getSource() == homePageButton
                || e.getSource() == contentPageButton
                || e.getSource() == contactPageButton
                || e.getSource() == settingsButton
                || e.getSource() == toggleSound
                || e.getSource() == toggleSoundFX) {
            SoundManager.getInstance().playSFX(SoundManager.SFX.KEY_TYPE);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (e.getSource() == contentPageButton) {
            contentPageButton.setFont(boldCustomFont);
        }
        else if (e.getSource() == contactPageButton) {
            contactPageButton.setFont(boldCustomFont);
        }
        else if (e.getSource() == homePageButton) {
            homePageButton.setFont(boldCustomFont);

        }else if (e.getSource() == settingsButton) {
            settingsButton.setFont(customFont);
        }
        else if (e.getSource() == exitButtonLabel) {
            exitButtonLabel.setIcon(exitButtonClicked);
        }
        else if (e.getSource() == minimizeButtonLabel) {
            minimizeButtonLabel.setIcon(minimizeButtonClicked);
        }else if (e.getSource() == toggleSound) {
            toggleSound.setFont(boldCustomFont);
        } else if (e.getSource() == toggleSoundFX) {
            toggleSoundFX.setFont(boldCustomFont);
        }

    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (e.getSource() == contentPageButton) {
            contentPageButton.setFont(customFont);
        }
        else if (e.getSource() == contactPageButton) {
            contactPageButton.setFont(customFont);
        }
        else if (e.getSource() == homePageButton){
            homePageButton.setFont(customFont);
        } else if (e.getSource() == settingsButton) {
            settingsButton.setFont(boldCustomFont);
        }
        else if (e.getSource() == exitButtonLabel) {
            exitButtonLabel.setIcon(exitButton);
        }
        else if (e.getSource() == minimizeButtonLabel) {
            minimizeButtonLabel.setIcon(minimizeButton);       
        } else if (e.getSource() == toggleSound) {
            toggleSound.setFont(customFont);
        } else if (e.getSource() == toggleSoundFX) {
            toggleSoundFX.setFont(customFont);
        }
    }
}
