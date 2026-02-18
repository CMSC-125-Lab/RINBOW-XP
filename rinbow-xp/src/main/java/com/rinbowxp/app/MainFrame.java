package com.rinbowxp.app;

import java.awt.*;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class MainFrame extends JFrame{
    final int frameWidth = 1200, frameHeight = 800;
    private final ResourceManager resourceManager;

    public MainFrame() {
        setup();
        resourceManager = new ResourceManager();
        CardLayout cardLayout = new CardLayout();
        JPanel cardPanel = getCardPanel(cardLayout);

        add(cardPanel);

        cardLayout.show(cardPanel, "Home Page");
        setVisible(true);
    }

    private JPanel getCardPanel(CardLayout cardLayout) {
        JPanel cardPanel = new JPanel(cardLayout);
        MainPagePanel homePagePanel = new MainPagePanel(cardLayout, cardPanel, resourceManager, new Dimension(frameWidth, frameHeight));  
        ContactPagePanel contactPagePanel = new ContactPagePanel(cardLayout, cardPanel, resourceManager, new Dimension(frameWidth, frameHeight));
        RulesPage rulesPage = new RulesPage(cardLayout, cardPanel, resourceManager, new Dimension(frameWidth, frameHeight));
        GameResultPage gameResultPage = new GameResultPage(cardLayout, cardPanel, resourceManager, new Dimension(frameWidth, frameHeight));
        GamePanel gamePanel = new GamePanel(cardLayout, cardPanel, gameResultPage, resourceManager, new Dimension(frameWidth, frameHeight));
        ChooseDifficultyPage chooseDifficultyPage = new ChooseDifficultyPage(cardLayout, cardPanel, gamePanel, resourceManager, new Dimension(frameWidth, frameHeight));   
        SettingsPage settingsPage = new SettingsPage(cardLayout, cardPanel, resourceManager, new Dimension(frameWidth, frameHeight));
        
        cardPanel.add(homePagePanel, "Home Page");
        cardPanel.add(contactPagePanel, "Contact Page");
        cardPanel.add(rulesPage, "Rules Page");
        cardPanel.add(gamePanel, "Game Panel");
        cardPanel.add(gameResultPage, "Game Result Page");
        cardPanel.add(chooseDifficultyPage, "Choose Difficulty Page");
        cardPanel.add(settingsPage, "Settings Page");
        return cardPanel;
    }

    private void setup() {
        // Set Up Jframe
        setTitle("Type Or Die");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(false);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        setBounds((int) ((screenSize.getWidth() - frameWidth) / 2), (int) ((screenSize.getHeight() - frameHeight) / 2), frameWidth, frameHeight);
        setResizable(false);
    }

}
