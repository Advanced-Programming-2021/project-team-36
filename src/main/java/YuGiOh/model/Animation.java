package YuGiOh.model;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Animation {
    private int currentImageId;
    private int fpr = 1;
    private ArrayList<Image> images = new ArrayList<>();

    public Animation(String ...images) {
        try {
            for (int i = 0; i < images.length; i ++)
                this.images.add(new Image(new FileInputStream(images[i])));
        } catch (IOException ignored) {
        }
    }

    public void setFPR(int fpr) {
        this.fpr = fpr;
    }

    public Image getImage() {
        return images.get(((currentImageId++) % (images.size() * fpr)) / fpr);
    }

    public int getCurrentImageId() {
        return currentImageId;
    }
}
