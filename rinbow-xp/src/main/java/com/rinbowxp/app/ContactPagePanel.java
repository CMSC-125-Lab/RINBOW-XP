package com.rinbowxp.app;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
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


public class ContactPagePanel extends JPanel implements MouseListener{
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private Image bg_image;
    private JPanel upperPanel, lowerPanel;
    private JLabel title, homePageButton, contentPageButton, contactPageButton, settingsButton, exitButtonLabel, minimizeButtonLabel;
    private Font customFont = new Font("Arial", Font.PLAIN, 21);
    private Font boldCustomFont = new Font("Arial", Font.BOLD, 21);
    private Font titleFont = new Font("Arial", Font.BOLD, 56);
    private ImageIcon exitButton, exitButtonClicked, minimizeButton, minimizeButtonClicked;

    private JLabel github1, github2, github3, github4, github5, github6;
    private JLabel email1, email2, email3, email4;
    private Dimension frameDimension;

    private ResourceManager resourceManager;

    public ContactPagePanel(CardLayout cardLayout, JPanel cardPanel, ResourceManager resourceManager,
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
        contactPageButton.setFont(boldCustomFont);
        contactPageButton.addMouseListener(this);

        settingsButton = new JLabel("Settings");
        settingsButton.setForeground(java.awt.Color.black);
        settingsButton.setFont(customFont);
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
        navPanel.add(settingsButton);        // Settings

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
        GridBagConstraints gbc = new GridBagConstraints();
        lowerPanel = new JPanel();
        lowerPanel.setOpaque(false);
        lowerPanel.setLayout(new GridBagLayout());

        JLabel title = new JLabel("CONTACT");
        title.setForeground(new Color(0x0057cc));
        title.setFont(titleFont);

        Font smallerFont = customFont.deriveFont(Font.PLAIN, (int) (frameDimension.getHeight()/36.7));
        // For Contact 1
        JLabel name1 = new JLabel("ROLF GARCES");
        github1 = new JLabel("rolpppp");
        email1 = new JLabel("rgarces@up.edu.ph");
        name1.setFont(smallerFont);
        github1.setFont(smallerFont);
        email1.setFont(smallerFont);
        github1.addMouseListener(this);
        email1.addMouseListener(this);

        // For Contact 2
        JLabel name2 = new JLabel("NORMAN EULIN");
        github2 = new JLabel("normaneulin");
        email2 = new JLabel("nceulin1@up.edu.ph");
        name2.setFont(smallerFont);
        github2.setFont(smallerFont);
        email2.setFont(smallerFont);
        github2.addMouseListener(this);
        email2.addMouseListener(this);

        // For Contact 3
        JLabel name3 = new JLabel("GIAN TONGZON");
        github3 = new JLabel("giangelo19");
        email3 = new JLabel("gatongzon@up.edu.ph");
        name3.setFont(smallerFont);
        github3.setFont(smallerFont);
        email3.setFont(smallerFont);
        github3.addMouseListener(this);
        email3.addMouseListener(this);

        // For Contact 4
        JLabel name4 = new JLabel("VANCE ORTEGA");
        github4 = new JLabel("MoresVance");
        email4 = new JLabel("mcortega@up.edu.ph");
        name4.setFont(smallerFont);
        github4.setFont(smallerFont);
        email4.setFont(smallerFont);
        github4.addMouseListener(this);
        email4.addMouseListener(this);

        gbc.insets = new Insets(0, 0, (int) (frameDimension.getHeight()/120), 0);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.ipady = 20;

        lowerPanel.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.gridy = 1;
        gbc.ipady = 15;

        gbc.insets = new Insets((int) (frameDimension.getHeight()/49), (int) (frameDimension.getWidth()/55), 0, (int) (frameDimension.getWidth()/55));
        lowerPanel.add(name1, gbc);

        gbc.gridx = 1;
        lowerPanel.add(github1, gbc);

        gbc.gridx = 2;
        lowerPanel.add(email1, gbc);

        gbc.insets = new Insets((int) (frameDimension.getHeight()/73), (int) (frameDimension.getWidth()/55), 0, (int) (frameDimension.getWidth()/55));
        gbc.gridy = 2;

        gbc.gridx = 0;
        lowerPanel.add(name2, gbc);

        gbc.gridx = 1;
        lowerPanel.add(github2, gbc);

        gbc.gridx = 2;
        lowerPanel.add(email2, gbc);

        gbc.gridy = 3;

        gbc.gridx = 0;
        lowerPanel.add(name3, gbc);

        gbc.gridx = 1;
        lowerPanel.add(github3, gbc);

        gbc.gridx = 2;
        lowerPanel.add(email3, gbc);

        gbc.gridy = 4;

        gbc.gridx = 0;
        lowerPanel.add(name4, gbc);

        gbc.gridx = 1;
        lowerPanel.add(github4, gbc);

        gbc.gridx = 2;
        lowerPanel.add(email4, gbc);

        gbc.gridy = 5;
        gbc.weighty = 1.0;
        lowerPanel.add(new JLabel(""), gbc);

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
         else if (e.getSource() == email1) {
            Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
            if (desktop != null && desktop.isSupported(Desktop.Action.MAIL)) {
                try {
                    desktop.browse(new URI("https://github.com/rolpppp"));
                }
                catch (Exception e1) {
                    System.out.println("Desktop browsing Failed.");
                    e1.printStackTrace();
                }
            }
        }
        else if (e.getSource() == github2){
            Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
            if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                try {
                    desktop.browse(new URI("https://github.com/normaneulin"));
                }
                catch (Exception e1) {
                    System.out.println("Desktop browsing Failed.");
                    e1.printStackTrace();
                }
            }

        }
        else if (e.getSource() == github3){
            Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
            if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                try {
                    desktop.browse(new URI("https://github.com/giangelo19"));
                }
                catch (Exception e1) {
                    System.out.println("Desktop browsing Failed.");
                    e1.printStackTrace();
                }
            }

        }
        else if (e.getSource() == github4){
            Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
            if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                try {
                    desktop.browse(new URI("https://github.com/MoresVance"));
                } catch (Exception e1) {
                    System.out.println("Desktop browsing Failed.");
                    e1.printStackTrace();
                }
            }

        } else if (e.getSource() == title){
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
        if (e.getSource() == contentPageButton) {
            contentPageButton.setFont(boldCustomFont);
        }
        else if (e.getSource() == contactPageButton) {
            contactPageButton.setFont(customFont);
        }
        else if (e.getSource() == homePageButton) {
            homePageButton.setFont(boldCustomFont);

        }else if (e.getSource() == exitButtonLabel) {
            exitButtonLabel.setIcon(exitButtonClicked);
        } else if (e.getSource() == settingsButton) {
            settingsButton.setFont(boldCustomFont);
        }
        else if (e.getSource() == minimizeButtonLabel) {
            minimizeButtonLabel.setIcon(minimizeButtonClicked);
        }
        else if (e.getSource() == github1 || e.getSource() == github2 || e.getSource() == github3 || e.getSource() == github4 || e.getSource() == github5 || e.getSource() == github6) {
            ((Component) e.getSource()).setForeground(java.awt.Color.blue);
        } 

    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (e.getSource() == contentPageButton) {
            contentPageButton.setFont(customFont);
        }
        else if (e.getSource() == contactPageButton) {
            contactPageButton.setFont(boldCustomFont);
        }
        else if (e.getSource() == homePageButton){
            homePageButton.setFont(customFont);

        } else if (e.getSource() == settingsButton) {
            settingsButton.setFont(customFont);
        }
        else if (e.getSource() == exitButtonLabel) {
            exitButtonLabel.setIcon(exitButton);
        }
        else if (e.getSource() == minimizeButtonLabel) {
            minimizeButtonLabel.setIcon(minimizeButton);       
        }
        else if (e.getSource() == github1 || e.getSource() == github2 || e.getSource() == github3 || e.getSource() == github4 || e.getSource() == github5 || e.getSource() == github6) {
            ((Component) e.getSource()).setForeground(java.awt.Color.black);
        } 
    }
}
