package snake;

import javax.sound.sampled.*;

public class SoundPlayer {
    private Clip clip;

    public void playLoop(String resourcePath) {
        try {
            AudioInputStream stream = AudioSystem.getAudioInputStream(
                getClass().getResource(resourcePath)
            );

            clip = AudioSystem.getClip();
            clip.open(stream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
    }
}