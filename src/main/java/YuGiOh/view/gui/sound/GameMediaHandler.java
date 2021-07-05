package YuGiOh.view.gui.sound;

import YuGiOh.model.card.event.*;
import YuGiOh.view.gui.GuiReporter;
import YuGiOh.view.gui.Utils;
import YuGiOh.view.gui.event.RoundOverEvent;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class GameMediaHandler {
    private final MediaPlayer background = getMediaPlayer("background");
    private final AudioClip summon = getAudioClip("summon");
    private final AudioClip attack = getAudioClip("attack");
    private final AudioClip activateMagic = getAudioClip("activate-magic");
    private final AudioClip flip = getAudioClip("flip");
    private final AudioClip setMagic = getAudioClip("set-magic");
    private final AudioClip setMonster = getAudioClip("set-monster");

    private SimpleBooleanProperty backgroundMute = new SimpleBooleanProperty(false);

    public GameMediaHandler(GuiReporter reporter) {
        addEventListeners(reporter);
        background.setAutoPlay(true);
        background.setCycleCount(MediaPlayer.INDEFINITE);
        background.muteProperty().bind(backgroundMute);
        background.play();
        GuiReporter.getInstance().addEventHandler(RoundOverEvent.MY_TYPE, e->{
            background.stop();
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
        reporter.addGameEventHandler((GuiReporter.GameEventHandler<SummonEvent>) event->{
//            if(!(event instanceof FlipSummonEvent))
//                summon.play();
        });
        reporter.addGameEventHandler((GuiReporter.GameEventHandler<AttackEvent>) event->{
//            attack.play();
        });
        reporter.addGameEventHandler((GuiReporter.GameEventHandler<MagicActivation>) event->{
//            activateMagic.play();
        });
        reporter.addGameEventHandler((GuiReporter.GameEventHandler<FlipSummonEvent>) event->{
//            flip.play();
        });
        reporter.addGameEventHandler((GuiReporter.GameEventHandler<SetMagic>) event->{
//            setMagic.play();
        });
        reporter.addGameEventHandler((GuiReporter.GameEventHandler<SetMonster>) event->{
//            setMonster.play();
        });
    }
}
