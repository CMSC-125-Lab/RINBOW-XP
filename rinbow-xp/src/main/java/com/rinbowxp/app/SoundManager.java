package com.rinbowxp.app;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import java.net.URL;
import java.util.EnumMap;
import java.util.Map;

/**
 * Singleton SoundManager – central audio hub for RINBOWS XP OS.
 *
 * <p>Responsibilities:
 * <ul>
 *   <li>Background music (BGM) – loops continuously, supports pause/resume.</li>
 *   <li>Button / keyboard interaction SFX ({@link SFX#KEY_TYPE},
 *       {@link SFX#CORRECT_GUESS}).</li>
 *   <li>Explosion / system-unit-break SFX ({@link SFX#EXPLOSION}).</li>
 *   <li>Game-over SFX ({@link SFX#GAME_OVER}).</li>
 *   <li>Per-category mute toggles and independent volume sliders.</li>
 * </ul>
 *
 * <p>MP3 decoding is handled transparently by the <em>mp3spi</em> library which
 * registers itself as a {@code javax.sound.sampled} service provider.  All audio
 * is loaded through the standard {@link AudioSystem} API so WAV and MP3 clips
 * are treated identically once decoded.
 *
 * <p>All load/playback failures are caught and logged; the game continues
 * silently rather than crashing.
 */
public class SoundManager {

    // -------------------------------------------------------------------------
    // Singleton
    // -------------------------------------------------------------------------

    private static SoundManager instance;

    /** Returns the shared {@link SoundManager} instance, creating it if needed. */
    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    // -------------------------------------------------------------------------
    // SFX catalogue
    // -------------------------------------------------------------------------

    /**
     * Enumeration of all sound-effect categories.  Each entry maps to a
     * resource file under {@code com/rinbowxp/app/resources/sounds/}.
     */
    public enum SFX {
        /** Plays on every key-button press (keyboard interaction). */
        KEY_TYPE("key_type.mp3"),
        /** Plays when the player guesses a correct letter. */
        CORRECT_GUESS("correct_sfx.wav"),
        /** Plays when the player guesses a wrong letter (unit takes damage). */
        EXPLOSION("explosion.wav"),
        /** Plays when the game ends in defeat. */
        GAME_OVER("game_over.wav");

        final String fileName;

        SFX(String fileName) {
            this.fileName = fileName;
        }
    }

    // -------------------------------------------------------------------------
    // Constants
    // -------------------------------------------------------------------------

    /** Classpath prefix shared by all sound resources. */
    private static final String SOUNDS_PATH = "com/rinbowxp/app/resources/sounds/";

    /** BGM file name (MP3 decoded via mp3spi). */
    private static final String BGM_FILE = "background_music.mp3";

    // -------------------------------------------------------------------------
    // State
    // -------------------------------------------------------------------------

    private boolean bgmEnabled  = true;
    private boolean sfxEnabled  = true;
    private float   bgmVolume   = 0.8f;   // 0.0 – 1.0  linear
    private float   sfxVolume   = 0.8f;   // 0.0 – 1.0  linear

    // -------------------------------------------------------------------------
    // Audio objects
    // -------------------------------------------------------------------------

    /** Pre-loaded, loopable BGM clip. */
    private Clip bgmClip;

    /** Pre-loaded SFX clips keyed by category. */
    private final Map<SFX, Clip> sfxClips = new EnumMap<>(SFX.class);

    // -------------------------------------------------------------------------
    // Constructor (private – use getInstance())
    // -------------------------------------------------------------------------

    private SoundManager() {
        System.out.println("SoundManager: initialising audio...");
        loadBGM();
        loadAllSFX();
        System.out.println("SoundManager: audio ready.");
    }

    // -------------------------------------------------------------------------
    // Internal loading helpers
    // -------------------------------------------------------------------------

    /**
     * Loads the background music file.
     *
     * <p>For MP3 files the mp3spi pipeline converts the compressed stream to
     * PCM before handing it to a {@link Clip}.  This means the full decoded
     * audio sits in heap memory – acceptable for a short looping track but
     * worth noting for very long files.
     */
    private void loadBGM() {
        try {
            URL url = getClass().getClassLoader().getResource(SOUNDS_PATH + BGM_FILE);
            if (url == null) {
                System.err.println("SoundManager: BGM file not found on classpath: " + BGM_FILE);
                return;
            }

            AudioInputStream rawIn = AudioSystem.getAudioInputStream(url);
            AudioFormat srcFormat  = rawIn.getFormat();

            // Convert to linear PCM so a Clip can hold and loop it.
            AudioFormat pcmFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    srcFormat.getSampleRate(),
                    16,
                    srcFormat.getChannels(),
                    srcFormat.getChannels() * 2,
                    srcFormat.getSampleRate(),
                    false
            );

            AudioInputStream pcmIn = AudioSystem.getAudioInputStream(pcmFormat, rawIn);
            DataLine.Info info = new DataLine.Info(Clip.class, pcmFormat);
            bgmClip = (Clip) AudioSystem.getLine(info);
            bgmClip.open(pcmIn);
            applyVolume(bgmClip, bgmVolume);

            System.out.println("SoundManager: BGM loaded (" + BGM_FILE + ").");
        } catch (Exception e) {
            System.err.println("SoundManager: failed to load BGM – " + e.getMessage());
            bgmClip = null;
        }
    }

    /**
     * Pre-loads every {@link SFX} entry into its own {@link Clip}.
     * WAV and MP3 files are both handled via {@link AudioSystem}.
     */
    private void loadAllSFX() {
        for (SFX sfx : SFX.values()) {
            loadSFXClip(sfx);
        }
    }

    /** Loads a single SFX entry into a Clip and caches it. */
    private void loadSFXClip(SFX sfx) {
        try {
            URL url = getClass().getClassLoader().getResource(SOUNDS_PATH + sfx.fileName);
            if (url == null) {
                System.err.println("SoundManager: SFX file not found: " + sfx.fileName);
                return;
            }

            AudioInputStream rawIn   = AudioSystem.getAudioInputStream(url);
            AudioFormat      srcFmt  = rawIn.getFormat();

            // If the format is not directly supported by Clip (e.g. MP3 float encoding)
            // convert to PCM first.
            AudioInputStream playIn;
            if (!srcFmt.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED)
                    && !srcFmt.getEncoding().equals(AudioFormat.Encoding.PCM_UNSIGNED)) {
                AudioFormat pcmFmt = new AudioFormat(
                        AudioFormat.Encoding.PCM_SIGNED,
                        srcFmt.getSampleRate(),
                        16,
                        srcFmt.getChannels(),
                        srcFmt.getChannels() * 2,
                        srcFmt.getSampleRate(),
                        false
                );
                playIn = AudioSystem.getAudioInputStream(pcmFmt, rawIn);
            } else {
                playIn = rawIn;
            }

            DataLine.Info info = new DataLine.Info(Clip.class, playIn.getFormat());
            Clip clip = (Clip) AudioSystem.getLine(info);
            clip.open(playIn);
            applyVolume(clip, sfxVolume);
            sfxClips.put(sfx, clip);

            System.out.println("SoundManager: SFX loaded (" + sfx.name() + " → " + sfx.fileName + ").");
        } catch (Exception e) {
            System.err.println("SoundManager: failed to load SFX [" + sfx.name() + "] – " + e.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // BGM controls
    // -------------------------------------------------------------------------

    /**
     * Starts the background music from the beginning, looping indefinitely.
     * Does nothing if BGM is muted or the clip is unavailable.
     */
    public void playBGM() {
        if (!bgmEnabled || bgmClip == null) return;
        bgmClip.setFramePosition(0);
        bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    /** Stops BGM playback immediately. */
    public void stopBGM() {
        if (bgmClip != null && bgmClip.isRunning()) {
            bgmClip.stop();
        }
    }

    /** Pauses BGM (alias for {@link #stopBGM()}; position is preserved by the Clip). */
    public void pauseBGM() {
        stopBGM();
    }

    /**
     * Resumes BGM from the current position.
     * Does nothing if BGM is muted or the clip is unavailable.
     */
    public void resumeBGM() {
        if (!bgmEnabled || bgmClip == null) return;
        if (!bgmClip.isRunning()) {
            bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    /**
     * Enables or disables background music.
     * Stopping/starting playback is handled automatically.
     *
     * @param enabled {@code true} to unmute BGM, {@code false} to mute it
     */
    public void setBGMEnabled(boolean enabled) {
        this.bgmEnabled = enabled;
        if (enabled) {
            resumeBGM();
        } else {
            stopBGM();
        }
    }

    /** Whether background music is currently enabled. */
    public boolean isBGMEnabled() {
        return bgmEnabled;
    }

    // -------------------------------------------------------------------------
    // SFX controls
    // -------------------------------------------------------------------------

    /**
     * Plays the requested sound effect.
     * If the clip is already playing it is rewound and restarted so
     * rapid repeated presses each produce a sound.
     *
     * @param sfx the effect to play
     */
    public void playSFX(SFX sfx) {
        if (!sfxEnabled) return;
        Clip clip = sfxClips.get(sfx);
        if (clip == null) return;
        if (clip.isRunning()) clip.stop();
        clip.setFramePosition(0);
        clip.start();
    }

    /**
     * Enables or disables all sound effects.
     *
     * @param enabled {@code true} to unmute SFX, {@code false} to mute them
     */
    public void setSFXEnabled(boolean enabled) {
        this.sfxEnabled = enabled;
    }

    /** Whether sound effects are currently enabled. */
    public boolean isSFXEnabled() {
        return sfxEnabled;
    }

    // -------------------------------------------------------------------------
    // Volume controls
    // -------------------------------------------------------------------------

    /**
     * Sets the BGM volume.
     *
     * @param volume linear gain in the range {@code 0.0} (silence) to {@code 1.0} (full)
     */
    public void setBGMVolume(float volume) {
        this.bgmVolume = clamp(volume);
        if (bgmClip != null) applyVolume(bgmClip, this.bgmVolume);
    }

    /**
     * Sets the SFX volume (applies retroactively to all loaded SFX clips).
     *
     * @param volume linear gain in the range {@code 0.0} to {@code 1.0}
     */
    public void setSFXVolume(float volume) {
        this.sfxVolume = clamp(volume);
        for (Clip clip : sfxClips.values()) {
            applyVolume(clip, this.sfxVolume);
        }
    }

    /**
     * Convenience method that sets the same volume for both BGM and all SFX.
     *
     * @param volume linear gain in the range {@code 0.0} to {@code 1.0}
     */
    public void setMasterVolume(float volume) {
        setBGMVolume(volume);
        setSFXVolume(volume);
    }

    /** Returns the current BGM volume (linear, 0.0 – 1.0). */
    public float getBGMVolume() {
        return bgmVolume;
    }

    /** Returns the current SFX volume (linear, 0.0 – 1.0). */
    public float getSFXVolume() {
        return sfxVolume;
    }

    // -------------------------------------------------------------------------
    // Lifecycle
    // -------------------------------------------------------------------------

    /**
     * Stops and closes all audio lines.  Call this when the application exits
     * to release native audio resources cleanly.
     */
    public void shutdown() {
        stopBGM();
        if (bgmClip != null) {
            bgmClip.close();
            bgmClip = null;
        }
        for (Clip clip : sfxClips.values()) {
            if (clip.isRunning()) clip.stop();
            clip.close();
        }
        sfxClips.clear();
        System.out.println("SoundManager: shutdown complete.");
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    /**
     * Applies a linear volume gain to the given {@link Clip} through its
     * {@code MASTER_GAIN} float control (decibel scale).
     *
     * @param clip   target audio clip
     * @param volume linear gain 0.0 – 1.0
     */
    private void applyVolume(Clip clip, float volume) {
        try {
            FloatControl gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            // Convert linear 0-1 to dB: dB = 20 * log10(volume)
            float dB = (volume <= 0f)
                    ? gain.getMinimum()
                    : (float) (20.0 * Math.log10(volume));
            // Clamp to the control's supported range before setting.
            gain.setValue(Math.max(gain.getMinimum(), Math.min(gain.getMaximum(), dB)));
        } catch (IllegalArgumentException | IllegalStateException e) {
            // MASTER_GAIN not supported on this line – skip silently.
        }
    }

    /** Clamps {@code v} to the range [0.0, 1.0]. */
    private float clamp(float v) {
        return Math.max(0f, Math.min(1f, v));
    }
}
