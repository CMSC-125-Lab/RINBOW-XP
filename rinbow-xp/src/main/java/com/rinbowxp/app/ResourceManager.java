package com.rinbowxp.app;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.util.Iterator;

/**
 * Future refactor for loading resources into the app.
 */

public class ResourceManager {
    protected static final String FILES_PATH = "com/rinbowxp/app/resources/files/";
    private Cursor cursor;
    private HashMap<String, ImageIcon> imageIconHashMap; // strong cache for UI assets & GIFs
    private HashMap<String, java.lang.ref.SoftReference<ImageIcon>> transitionCache; // soft cache for large PNG frames
    private Image logo;
    // Limit decoded PNG transition frame size to avoid huge memory use (OOM)
    private static final int TRANSITION_MAX_DIM = 600; // pixels (fits right panel ~500)

    private Font cousineRegular, cousineBold, anonymousProBold;
    public ResourceManager() {
        System.out.println("Loading resources...");
        loadResources();
        System.out.println("Resources loaded successfully!");
    }

    private void loadResources() {
        loadCursor();
        try {
            loadFonts();
        } catch (IOException | FontFormatException e) {
            System.out.println("ERROR: Fonts failed to load");
            throw new RuntimeException(e);
        }
        imageIconHashMap = new HashMap<>();
        transitionCache = new HashMap<>();
        loadImages();
    }

    private void loadFonts() throws IOException, FontFormatException {
        System.out.println("Loading Fonts...");
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
        System.out.println("Done loading fonts!");
    }

    private void loadImages() {
        System.out.println("Loading image icons...");
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
        imageIconHashMap.put(
            "Keyboard",
            new ImageIcon(getURLFromFiles("keyboard2.png"))
        );
        
        System.out.println("Image Icons loaded successfully!");
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
            System.out.println("ERROR: Custom Cursor failed to load");
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
    
    /**
     * Decode a PNG from classpath with subsampling to cap max dimension.
     * This avoids decoding massive 6k images into memory.
     */
    private ImageIcon loadScaledPngIcon(String filePath, int maxDim) {
        InputStream is = getStreamFromFiles(filePath);
        try (ImageInputStream iis = ImageIO.createImageInputStream(is)) {
            Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("png");
            if (!readers.hasNext()) {
                throw new RuntimeException("No PNG ImageReader available");
            }
            ImageReader reader = readers.next();
            try {
                reader.setInput(iis, true, true);
                int width = reader.getWidth(0);
                int height = reader.getHeight(0);
                int factor = 1;
                if (width > maxDim || height > maxDim) {
                    int fx = (int) Math.ceil(width / (double) maxDim);
                    int fy = (int) Math.ceil(height / (double) maxDim);
                    factor = Math.max(1, Math.max(fx, fy));
                }
                ImageReadParam param = reader.getDefaultReadParam();
                param.setSourceSubsampling(factor, factor, 0, 0);
                BufferedImage scaled = reader.read(0, param);
                return new ImageIcon(scaled);
            } finally {
                reader.dispose();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read scaled PNG: " + filePath, e);
        }
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
        // Check if already cached
        if (imageIconHashMap.containsKey(key)) {
            ImageIcon cached = imageIconHashMap.get(key);
            if (cached == null) {
                throw new RuntimeException("ERROR: Key is not in Image Icon Hashmap!");
            }
            return cached;
        }
        
        // Try to load sprite on-demand (lazy loading)
        if (key.startsWith("stage") && !key.contains("/")) {
            // It's a stage GIF (stage0, stage1, etc.)
            try {
                String stageName = key + ".gif";
                ImageIcon icon = new ImageIcon(getURLFromFiles("sprites/" + stageName));
                imageIconHashMap.put(key, icon);
                return icon;
            } catch (RuntimeException e) {
                System.err.println("Failed to load sprite: " + key);
                throw new RuntimeException("ERROR: Could not load sprite: " + key);
            }
        } else if (key.startsWith("transition-")) {
            // It's a transition frame (transition-0/stage0_f1, etc.)
            try {
                // Check soft cache first
                java.lang.ref.SoftReference<ImageIcon> ref = transitionCache.get(key);
                if (ref != null) {
                    ImageIcon cached = ref.get();
                    if (cached != null) {
                        return cached;
                    }
                }
                String[] parts = key.split("/");
                String transitionDir = parts[0];  // "transition-0"
                String frameName = parts[1];      // "stage0_f1"
                
                // Handle typo in folder name for transition-0
                String actualFolder = transitionDir.equals("transition-0") ? "tranistion-0" : transitionDir;
                
                String filePath = "sprites/" + actualFolder + "/" + frameName + ".png";
                // Decode with subsampling to cap size and avoid heap bloat
                ImageIcon icon = loadScaledPngIcon(filePath, TRANSITION_MAX_DIM);
                transitionCache.put(key, new java.lang.ref.SoftReference<>(icon));
                return icon;
            } catch (RuntimeException e) {
                System.err.println("Failed to load transition frame: " + key);
                throw new RuntimeException("ERROR: Could not load transition: " + key);
            }
        }
        
        throw new RuntimeException("ERROR: Key is not in Image Icon Hashmap!");
    }
}