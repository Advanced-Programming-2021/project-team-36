package YuGiOh.graphicController;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import lombok.Getter;

import java.io.File;

public class MediaPlayerController {
    private final MediaPlayer themeSongMediaPlayer = new MediaPlayer(new Media(new File("assets/Sound/cool-background.mp3").toURI().toString()));

    @Getter
    private static MediaPlayerController instance;

    public MediaPlayerController() {
        instance = this;
    }

    {
        themeSongMediaPlayer.play();
        themeSongMediaPlayer.setCycleCount(1000000);
    }

    public void setVolume(double value) {
        themeSongMediaPlayer.setVolume(value);
    }

    public void stopThemeMedia() {
        themeSongMediaPlayer.stop();
    }

    public void playThemeMedia() {
        themeSongMediaPlayer.play();
    }
}
