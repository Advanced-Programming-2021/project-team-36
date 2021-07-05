package YuGiOh.view.gui.sound;

import YuGiOh.model.card.event.*;
import YuGiOh.view.gui.GuiReporter;
import YuGiOh.view.gui.Utils;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class GameMediaHandler {
    private SimpleBooleanProperty backgroundMute = new SimpleBooleanProperty(false);

    public GameMediaHandler(GuiReporter reporter) {
        System.out.println("STARTED HERE ");
        addEventListeners(reporter);
        MediaPlayer background = getMediaPlayer("background");
        background.setAutoPlay(true);
        background.setCycleCount(MediaPlayer.INDEFINITE);
        background.play();
        background.muteProperty().bind(backgroundMute);
        background.setOnEndOfMedia(()->{
            System.out.println("MUSIC ENDED");
        });
    }

    public void toggleMuteBackground() {
        backgroundMute.set(!backgroundMute.get());
    }
    public BooleanProperty backgroundMuteProperty() {
        return backgroundMute;
    }
    private MediaPlayer getMediaPlayer(String eventName) {
        return new MediaPlayer(new Media(Utils.getAsset("Sound/" + eventName + ".wav").toURI().toString()));
    }
    private AudioClip getAudioClip(String eventName) {
        return new AudioClip(Utils.getAsset("Sound/" + eventName + ".wav").toURI().toString());
    }

    private void addEventListeners(GuiReporter reporter) {
        AudioClip summon = getAudioClip("summon");
        AudioClip attack = getAudioClip("attack");
        AudioClip activateMagic = getAudioClip("activate-magic");
        AudioClip flip = getAudioClip("flip");
        AudioClip setMagic = getAudioClip("set-magic");
        AudioClip setMonster = getAudioClip("set-monster");

        reporter.addGameEventHandler((GuiReporter.GameEventHandler<SummonEvent>) event->{
            summon.play();
        });
        reporter.addGameEventHandler((GuiReporter.GameEventHandler<AttackEvent>) event->{
            attack.play();
        });
        reporter.addGameEventHandler((GuiReporter.GameEventHandler<MagicActivation>) event->{
            activateMagic.play();
        });
        reporter.addGameEventHandler((GuiReporter.GameEventHandler<FlipSummonEvent>) event->{
            flip.play();
        });
        reporter.addGameEventHandler((GuiReporter.GameEventHandler<SetMagic>) event->{
            setMagic.play();
        });
        reporter.addGameEventHandler((GuiReporter.GameEventHandler<SetMonster>) event->{
            setMonster.play();
        });
    }
}
