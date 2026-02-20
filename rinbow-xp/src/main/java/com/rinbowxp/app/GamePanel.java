package com.rinbowxp.app;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
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
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import com.rinbowxp.app.game_logic.GameSession;

public class GamePanel extends JPanel implements MouseListener {
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private Image bg_image;
    private JPanel upperPanel, lowerPanel, footerPanel, contentPanel;
    private JLabel title, homePageButton, clueButton, exitButtonLabel, minimizeButtonLabel, gamedescriptionButton, clueLabel;
    private Font customFont = new Font("Arial", Font.PLAIN, 21);
    private Font boldCustomFont = new Font("Arial", Font.BOLD, 21);
    private Font titleFont = new Font("Arial", Font.BOLD, 56);
    private ImageIcon exitButton, exitButtonClicked, minimizeButton, minimizeButtonClicked;
    private Keyboard keyboard;
    private Dimension frameDimension;

    private ResourceManager resourceManager;
    private SpriteTransition spriteTransition;
    private JButton spriteButton;
    private GameSession gameSession;
    private JLabel wordDisplayLabel;
    private JPanel clueOverlay;
    private JLabel clueOverlayText;
    private JLayeredPane layeredPane;

    public GamePanel(CardLayout cardLayout, JPanel cardPanel, GameResultPage gameResultPage, ResourceManager resourceManager,
                     Dimension frameDimension) {
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
        this.resourceManager = resourceManager;
        this.frameDimension = frameDimension;
        gameResultPage.setGamePanelReference(this);

        bg_image = resourceManager.getImageIcon("Contact Panel BG").getImage();

        // Initialize sprite transition
        spriteTransition = new SpriteTransition(resourceManager);

        // Initialize game session
        gameSession = new GameSession(cardLayout, cardPanel, gameResultPage, spriteTransition);

        customFont = resourceManager.getCousineRegular();
        customFont = customFont.deriveFont(Font.PLAIN, (int) frameDimension.getHeight() / 25);
        boldCustomFont = resourceManager.getCousineBold();
        boldCustomFont = boldCustomFont.deriveFont(Font.BOLD, (int) frameDimension.getHeight() / 25);
        titleFont = resourceManager.getAnonymousProBold();
        titleFont = titleFont.deriveFont(Font.BOLD, (int) frameDimension.getHeight() / 10);

        // Setup main layout
        this.setLayout(new BorderLayout());
        
        // Create layered pane for content and overlay
        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(frameDimension);
        
        // Create content panel with main UI
        contentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.drawImage(bg_image, 0, 0, getWidth(), getHeight(), GamePanel.this);
            }
        };
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setOpaque(false);

        setUpperPanel();
        setLowerPanel();
        setFooterPanel();

        contentPanel.add(upperPanel, BorderLayout.NORTH);
        contentPanel.add(lowerPanel, BorderLayout.CENTER);
        contentPanel.add(footerPanel, BorderLayout.SOUTH);
        
        // Add content panel to layered pane
        contentPanel.setBounds(0, 0, (int)frameDimension.getWidth(), (int)frameDimension.getHeight());
        layeredPane.add(contentPanel, JLayeredPane.DEFAULT_LAYER);
        
        setupClueOverlay();
        
        // Add layered pane to this panel
        add(layeredPane, BorderLayout.CENTER);
    }
    
    @Override
    public void doLayout() {
        super.doLayout();
        if (layeredPane != null && contentPanel != null) {
            int width = layeredPane.getWidth();
            int height = layeredPane.getHeight();
            contentPanel.setBounds(0, 0, width, height);
            if (clueOverlay != null) {
                clueOverlay.setBounds(0, 0, width, height);
            }
        }
    }

    private void setUpperPanel() {
        GridBagConstraints gbc = new GridBagConstraints();
        upperPanel = new JPanel();
        upperPanel.setOpaque(false);
        upperPanel.setLayout(new GridBagLayout());

        title = new JLabel(gameSession.getCurrentRound() > 0 ? "Round " + gameSession.getCurrentRound() : "Type Or Die");
        title.setForeground(java.awt.Color.lightGray);
        title.setFont(boldCustomFont);
        title.addMouseListener(this);

        homePageButton = new JLabel("Back To Home");
        homePageButton.setForeground(java.awt.Color.black);
        homePageButton.setFont(customFont);
        homePageButton.addMouseListener(this);

        clueButton = new JLabel("Clue");
        clueButton.setForeground(java.awt.Color.black);
        clueButton.setFont(customFont);
        clueButton.addMouseListener(this);

        exitButton = resourceManager.getImageIcon("Exit Button");
        Image exitButtonResized = exitButton.getImage().getScaledInstance((int) frameDimension.getWidth() / 25, (int) frameDimension.getWidth() / 25, Image.SCALE_DEFAULT);
        exitButton = new ImageIcon(exitButtonResized);

        minimizeButton = resourceManager.getImageIcon("Minimize Button");
        Image minimizeButtonResized = minimizeButton.getImage().getScaledInstance((int) frameDimension.getWidth() / 25, (int) frameDimension.getWidth() / 25, Image.SCALE_DEFAULT);
        minimizeButton = new ImageIcon(minimizeButtonResized);

        exitButtonClicked = resourceManager.getImageIcon("Exit Button Clicked");
        Image exitButtonClickedResized = exitButtonClicked.getImage().getScaledInstance((int) frameDimension.getWidth() / 25, (int) frameDimension.getWidth() / 25, Image.SCALE_DEFAULT);
        exitButtonClicked = new ImageIcon(exitButtonClickedResized);

        minimizeButtonClicked = resourceManager.getImageIcon("Minimize Button Clicked");
        Image minimizeButtonClickedResized = minimizeButtonClicked.getImage().getScaledInstance((int) frameDimension.getWidth() / 25, (int) frameDimension.getWidth() / 25, Image.SCALE_DEFAULT);
        minimizeButtonClicked = new ImageIcon(minimizeButtonClickedResized);

        exitButtonLabel = new JLabel(exitButton);
        exitButtonLabel.addMouseListener(this);

        minimizeButtonLabel = new JLabel(minimizeButton);
        minimizeButtonLabel.addMouseListener(this);

        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        navPanel.setOpaque(false);
        navPanel.add(homePageButton);
        navPanel.add(clueButton);
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;

        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets((int) frameDimension.getHeight() / 25, (int) (frameDimension.getWidth() / 27.5), 0, 0);
        upperPanel.add(title, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        upperPanel.add(new JLabel(""), gbc);

        gbc.gridx = 2;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets((int) frameDimension.getHeight() / 28, 0, 0, 10);
        upperPanel.add(minimizeButtonLabel, gbc);

        gbc.gridx = 3;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets((int) frameDimension.getHeight() / 28, 0, 0, (int) (frameDimension.getWidth() / 27.5));
        upperPanel.add(exitButtonLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(
                (int) (frameDimension.getHeight() / 30),
                (int) (frameDimension.getWidth() / 80),
                (int) (frameDimension.getHeight() / 40),
                0
        );
        upperPanel.add(navPanel, gbc);
        upperPanel.setBorder(BorderFactory.createEmptyBorder((int) (frameDimension.getHeight() / 45), 0, 0, 0));
    }

    private void setupClueOverlay() {
        // Create overlay panel with semi-transparent background
        clueOverlay = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Draw semi-transparent background
                g2d.setColor(new Color(0, 0, 0, 180));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        clueOverlay.setLayout(new GridBagLayout());
        clueOverlay.setOpaque(false);
        clueOverlay.setVisible(false);
        clueOverlay.setBounds(0, 0, (int)frameDimension.getWidth(), (int)frameDimension.getHeight());
        
        // Create clue text label with styling
        clueOverlayText = new JLabel();
        clueOverlayText.setPreferredSize(new Dimension(800, 400)); 
        clueOverlayText.setFont(boldCustomFont.deriveFont(32f));
        clueOverlayText.setForeground(Color.WHITE);
        clueOverlayText.setHorizontalAlignment(JLabel.CENTER);
        clueOverlayText.setBackground(new Color(0, 87, 204));
        clueOverlayText.setOpaque(true);
        clueOverlayText.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 3),
                BorderFactory.createEmptyBorder(30, 50, 30, 50)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        clueOverlay.add(clueOverlayText, gbc);
        
        // Add click-to-dismiss functionality
        clueOverlay.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                hideClueOverlay();
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        
        // Add overlay to layered pane on top layer
        layeredPane.add(clueOverlay, JLayeredPane.PALETTE_LAYER);
    }
    
    private void showClueOverlay() {
        clueOverlayText.setText("<html><div style='text-align: center;'>" + gameSession.getClue() + "<br/><br/><span style='font-size: 18px;'>(Click anywhere to dismiss)</span></div></html>");
        clueOverlay.setVisible(true);
        clueOverlay.repaint();
    }
    
    private void hideClueOverlay() {
        clueOverlay.setVisible(false);
    }

    private void setLowerPanel() {
        lowerPanel = new JPanel();
        lowerPanel.setLayout(new BorderLayout());
        lowerPanel.setOpaque(false);
        // lowerPanel.setBorder(BorderFactory.createLineBorder(Color.black, 2));

        // Left Panel
        JPanel leftPanel = new JPanel();
        leftPanel.setOpaque(false);
        leftPanel.setLayout(new GridBagLayout());

        clueLabel = new JLabel(gameSession.getDifficulty());
        clueLabel.setFont(boldCustomFont.deriveFont(24f));
        clueLabel.setHorizontalAlignment(JLabel.CENTER);
        clueLabel.setForeground(Color.lightGray);
        clueLabel.setPreferredSize(new Dimension(320, 50));
        clueLabel.setBackground(new Color(0, 87, 204));
        clueLabel.setOpaque(true);
        clueLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.black, 3),
                BorderFactory.createEmptyBorder(10, 30, 10, 30)
        ));

        // Word display label
        wordDisplayLabel = new JLabel(gameSession.getDisplayWord()) {
            private static final float MAX_FONT_SIZE = 32f;
            private static final float MIN_FONT_SIZE = 16f;

            private void resizeFontToFitText() {
                Font baseFont = customFont != null ? customFont : getFont();
                int availableWidth = getWidth() - 12;
                if (availableWidth <= 0) {
                    availableWidth = (int) (frameDimension.getWidth() * 0.42);
                }

                String text = getText();
                if (text == null || text.isEmpty()) {
                    setFont(baseFont.deriveFont(MAX_FONT_SIZE));
                    return;
                }

                float currentSize = MAX_FONT_SIZE;
                Font fittedFont = baseFont.deriveFont(currentSize);
                FontMetrics metrics = getFontMetrics(fittedFont);

                while (currentSize > MIN_FONT_SIZE && metrics.stringWidth(text) > availableWidth) {
                    currentSize -= 1f;
                    fittedFont = baseFont.deriveFont(currentSize);
                    metrics = getFontMetrics(fittedFont);
                }

                if (!fittedFont.equals(getFont())) {
                    setFont(fittedFont);
                }
            }

            @Override
            public void setText(String text) {
                super.setText(text);
                resizeFontToFitText();
            }

            @Override
            public void doLayout() {
                super.doLayout();
                resizeFontToFitText();
            }
        };
        wordDisplayLabel.setFont(customFont.deriveFont(32f));
        wordDisplayLabel.setHorizontalAlignment(JLabel.CENTER);
        wordDisplayLabel.setForeground(Color.BLACK);
        wordDisplayLabel.setOpaque(false);
        wordDisplayLabel.setPreferredSize(new Dimension((int) (frameDimension.getWidth() * 0.42), 56));
        // wordDisplayLabel.setBorder(BorderFactory.createLineBorder(Color.green, 2));

        // Keyboard
        keyboard = new Keyboard();
        keyboard.setGameSession(gameSession);
        keyboard.setWordDisplayLabel(wordDisplayLabel);

        JPanel keyboardWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        keyboardWrapper.setOpaque(false);
        keyboardWrapper.add(keyboard);
        // keyboardWrapper.setBorder(BorderFactory.createLineBorder(Color.blue, 2));

        JPanel fillerPanel = new JPanel();
        fillerPanel.setOpaque(false);
        // Increase the height to push the keyboard lower (e.g., 80px or more as needed)
        fillerPanel.setPreferredSize(new Dimension(30, 100));
        // fillerPanel.setBorder(BorderFactory.createLineBorder(Color.red, 2));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        leftPanel.add(fillerPanel, gbc);
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        leftPanel.add(clueLabel, gbc);
        gbc.gridy = 2;
        gbc.insets = new Insets((int) (frameDimension.getHeight() / 20), 0, 0, 0);
        gbc.fill = GridBagConstraints.NONE;
        leftPanel.add(wordDisplayLabel, gbc);
        gbc.gridy = 3;
        gbc.insets = new Insets((int) (frameDimension.getHeight() / 20), 0, (int) (frameDimension.getHeight() / 15), 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        leftPanel.add(keyboardWrapper, gbc);

        int lowerPanelWidth = (int) (frameDimension.getWidth() - (frameDimension.getWidth() / 16));
        int leftPanelWidth = (int) (lowerPanelWidth * 0.53);

        leftPanel.setMinimumSize(new Dimension(leftPanelWidth, 0));
        leftPanel.setPreferredSize(new Dimension(leftPanelWidth, 592));
        leftPanel.setMaximumSize(new Dimension(leftPanelWidth, Integer.MAX_VALUE));
        // leftPanel.setBorder(BorderFactory.createLineBorder(Color.black, 2));

        // Right Panel — square, holds the sprite GIF
        JPanel rightPanel = new JPanel(new java.awt.BorderLayout()) {
            @Override
            public void doLayout() {
                int size = Math.min(getWidth(), getHeight());
                setBounds(getX(), getY(), size, size);
                super.doLayout();
            }

            @Override
            public Dimension getPreferredSize() {
                int h = getHeight();
                if (h <= 0) h = 592;
                return new Dimension(h, h);
            }
        };
        rightPanel.setLayout(new java.awt.BorderLayout());
        rightPanel.setOpaque(false);
        rightPanel.setMinimumSize(new Dimension(1, 1));
        rightPanel.setPreferredSize(new Dimension(592, 592));
        rightPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        // rightPanel.setBorder(BorderFactory.createLineBorder(Color.blue, 2));

        // Sprite display button — passive, just shows the current GIF scaled to fit
        // All icons are now GIFs (stage or transition), so we always use the scaled GIF path.
        spriteButton = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                ImageIcon icon = (ImageIcon) getIcon();
                if (icon == null) {
                    super.paintComponent(g);
                    return;
                }
                int imgWidth = icon.getIconWidth();
                int imgHeight = icon.getIconHeight();
                int panelWidth = getWidth();
                int panelHeight = getHeight();
                if (panelWidth <= 0 || panelHeight <= 0 || imgWidth <= 0 || imgHeight <= 0) {
                    super.paintComponent(g);
                    return;
                }
                // Scale to fit panel while preserving aspect ratio, with a small margin
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
                    icon.paintIcon(this, g2d, 0, 0);
                } finally {
                    g2d.dispose();
                }
            }
        };
        spriteButton.setOpaque(false);
        spriteButton.setContentAreaFilled(false);
        spriteButton.setBorderPainted(false);
        spriteButton.setFocusPainted(false);
        spriteButton.setFocusable(false);
        spriteButton.setRolloverEnabled(false);
        rightPanel.add(spriteButton, java.awt.BorderLayout.CENTER);

        Runnable updateSprite = () -> {
            ImageIcon icon = spriteTransition.getCurrentImage();
            if (icon != null) {
                spriteButton.setIcon(icon);
                spriteButton.repaint();
            }
            
            // lock or unlock the keyboard based on the transition state
            if (keyboard != null) {
                keyboard.setKeysActive(!spriteTransition.isTransitioning());
            }
        };

        spriteTransition.setOnFrameChange(updateSprite);
        javax.swing.SwingUtilities.invokeLater(updateSprite);

        JPanel leftPaddingPanel = new JPanel();
        leftPaddingPanel.setOpaque(false);
        leftPaddingPanel.setPreferredSize(new Dimension((int) (frameDimension.getWidth() / 100), 0));
        // leftPanel.setBorder(BorderFactory.createLineBorder(Color.black, 2));

        JPanel rightPaddingPanel = new JPanel();
        rightPaddingPanel.setOpaque(false);
        rightPaddingPanel.setPreferredSize(new Dimension(20, 0));
        // rightPaddingPanel.setBorder(BorderFactory.createLineBorder(Color.blue, 2));

        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BorderLayout());
        // centerPanel.setBorder(BorderFactory.createLineBorder(Color.black, 2));
        centerPanel.add(leftPanel, BorderLayout.CENTER);
        centerPanel.add(rightPanel, BorderLayout.EAST);

        lowerPanel.add(leftPaddingPanel, BorderLayout.WEST);
        lowerPanel.add(centerPanel, BorderLayout.CENTER);
        lowerPanel.add(rightPaddingPanel, BorderLayout.EAST);
    }

    private void setFooterPanel() {
        footerPanel = new JPanel();
        footerPanel.setOpaque(false);
        footerPanel.setPreferredSize(new Dimension(0, (int) (frameDimension.getHeight() / 10)));
        // footerPanel.setBorder(BorderFactory.createLineBorder(Color.black, 2));
    }

    public void createNewGame(String difficulty) {
        gameSession.startNewGame(difficulty);
        title.setText("Round " + gameSession.getCurrentRound());
        wordDisplayLabel.setText(gameSession.getDisplayWord());
        clueLabel.setText(gameSession.getDifficulty());
        spriteTransition.reset();
        spriteButton.setIcon(spriteTransition.getCurrentImage());
        keyboard.reset();
        keyboard.setGameSession(gameSession);
        keyboard.setWordDisplayLabel(wordDisplayLabel);
        
        // Register callback for next round transitions
        gameSession.setOnNextRoundReady(() -> {
            title.setText("Round " + gameSession.getCurrentRound());
            wordDisplayLabel.setText(gameSession.getDisplayWord());
            clueLabel.setText(gameSession.getDifficulty());
            spriteButton.setIcon(spriteTransition.getCurrentImage());
            
            // Delay keyboard reset to allow any pending click events to finish processing
            Timer resetTimer = new Timer(150, e -> {
                keyboard.reset();
                lowerPanel.revalidate();
                lowerPanel.repaint();
            });
            resetTimer.setRepeats(false);
            resetTimer.start();
        });
        
        lowerPanel.revalidate();
        lowerPanel.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == minimizeButtonLabel) {
            System.out.println("Minimize Button Pressed");
            java.awt.Window window = javax.swing.SwingUtilities.getWindowAncestor(this);
            if (window instanceof JFrame) {
                ((JFrame) window).setState(JFrame.ICONIFIED);
            }
        } else if (e.getSource() == exitButtonLabel) {
            System.out.println("Exit Button Pressed");
            System.exit(0);
        } else if (e.getSource() == homePageButton) {
            cardLayout.show(cardPanel, "Home Page");
        } else if (e.getSource() == clueButton) {
            showClueOverlay();
        } else if (e.getSource() == title) {
            Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
            if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                try {
                    desktop.browse(new URI("https://github.com/CMSC-125-Lab/RINBOW-XP"));
                } catch (Exception e1) {
                    System.out.println("Desktop browsing Failed.");
                    e1.printStackTrace();
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getSource() == homePageButton) {
            SoundManager.getInstance().playSFX(SoundManager.SFX.KEY_TYPE);
        } else if (e.getSource() == clueButton) {
            SoundManager.getInstance().playSFX(SoundManager.SFX.KEY_TYPE);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (e.getSource() == homePageButton) {
            homePageButton.setFont(boldCustomFont);
        } else if (e.getSource() == clueButton) {
            clueButton.setFont(boldCustomFont);
        } else if (e.getSource() == exitButtonLabel) {
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
        } else if (e.getSource() == clueButton) {
            clueButton.setFont(customFont);
        } else if(e.getSource() == gamedescriptionButton){
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