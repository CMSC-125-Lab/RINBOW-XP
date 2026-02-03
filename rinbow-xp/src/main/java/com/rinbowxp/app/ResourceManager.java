package com.rinbowxp.app;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

/**
 * Future refactor for loading resources into the app.
 */

public class ResourceManager {
    protected static final String FILES_PATH = "com/rinbowxp/app/resources/files/";
    private Cursor cursor;
    private HashMap<String, ImageIcon> imageIconHashMap;
    private Image logo;

    private Font cousineRegular, cousineBold, anonymousProBold;
    public ResourceManager() {
        IO.println("Loading resources...");
        loadResources();
        IO.println("Resources loaded successfully!");
    }

    private void loadResources() {
        loadCursor();
        try {
            loadFonts();
        } catch (IOException | FontFormatException e) {
            IO.println("ERROR: Fonts failed to load");
            throw new RuntimeException(e);
        }
        imageIconHashMap = new HashMap<>();
        loadImages();
    }

    private void loadFonts() throws IOException, FontFormatException {
        IO.println("Loading Fonts...");
        cousineRegular = Font.createFont(
            Font.TRUETYPE_FONT,
            getStreamFromFiles("Cousine-Regular.ttf")
        );
        cousineRegular = cousineRegular.deriveFont(Font.PLAIN, 21);
        cousineBold = Font.createFont(
            Font.TRUETYPE_FONT,
            getStreamFromFiles("Cousine-Bold.ttf")
        );
        cousineBold = cousineBold.deriveFont(Font.BOLD, 21);
        anonymousProBold = Font.createFont(
            Font.TRUETYPE_FONT,
            getStreamFromFiles("AnonymousPro-Bold.ttf")
        );
        anonymousProBold = anonymousProBold.deriveFont(Font.BOLD, 50);
        IO.println("Done loading fonts!");
    }

    private void loadImages() {
        IO.println("Loading image icons...");

        imageIconHashMap.put(
            "Contact Panel BG",
            new ImageIcon(getURLFromFiles("contact-bg.png"))
        );

        imageIconHashMap.put(
            "Main Panel BG",
            new ImageIcon(getURLFromFiles("bg_img.png"))
        );

        imageIconHashMap.put(
            "Exit Button",
            new ImageIcon(getURLFromFiles("exitButton.jpg"))
        );
        imageIconHashMap.put(
            "Exit Button Clicked",
            new ImageIcon(getURLFromFiles("exitButton_clicked.jpg"))
        );
        imageIconHashMap.put(
            "Minimize Button",
            new ImageIcon(getURLFromFiles("minimizeButton.jpg"))
        );
        imageIconHashMap.put(
            "Minimize Button Clicked",
            new ImageIcon(getURLFromFiles("minimizeButton_clicked.jpg"))
        );
        imageIconHashMap.put(
            "Start Button",
            new ImageIcon(getURLFromFiles("startButton.jpg"))
        );
        imageIconHashMap.put(
            "Start Button Clicked",
            new ImageIcon(getURLFromFiles("startButton_clicked.jpg"))
        );
        imageIconHashMap.put(
            "End Button Clicked",
            new ImageIcon(getURLFromFiles("endButton_clicked.jpg"))
        );
        imageIconHashMap.put(
            "End Button",
            new ImageIcon(getURLFromFiles("endButton.jpg"))
        );
        imageIconHashMap.put(
            "Back Button",
            new ImageIcon(getURLFromFiles("backButton.jpg"))
        );
        imageIconHashMap.put(
            "Back Button Clicked",
            new ImageIcon(getURLFromFiles("backButton_Clicked.jpg"))
        );
        IO.println("Image Icons loaded successfully!");
    }

    private void loadCursor() {
        BufferedImage cursorImage;
        URL cursorURL = getURLFromFiles("cursor.png");
        try {
            cursorImage = ImageIO.read(cursorURL);
            cursor = Toolkit.getDefaultToolkit().createCustomCursor(
                cursorImage,
                new Point(0,0), "Custom Cursor"
            );
        }
        catch (IOException e) {
            IO.println("ERROR: Custom Cursor failed to load");
            throw new RuntimeException(e);
        }
    }

    public InputStream getStreamFromFiles(String file) {
        System.err.println(getClass());
        InputStream is = getClass().getClassLoader().getResourceAsStream(FILES_PATH + file);
        if (is == null) {
            throw new RuntimeException("Resource not found on classpath: " + FILES_PATH + file);
        }
        return is;
    }

    public URL getURLFromFiles(String file) {
        URL url = getClass().getClassLoader().getResource(FILES_PATH + file);
        if (url == null) {
            throw new RuntimeException("Resource not found on classpath: " + FILES_PATH + file);
        }
        return url;
    }
 

    public Cursor getCursor() {
        return cursor;
    }

    public Font getAnonymousProBold() {
        return anonymousProBold;
    }

    public Font getCousineBold() {
        return cousineBold;
    }

    public Font getCousineRegular() {
        return cousineRegular;
    }

    public Image getLogo() {
        return logo;
    }

    public ImageIcon getImageIcon(String key) {
        if(imageIconHashMap.get(key) == null){
            throw new RuntimeException("ERROR: Key is not in Image Icon Hashmap!");
        }
        return imageIconHashMap.get(key);
    }
}
