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
                this.images.add(new Image(new FileInputStream("src/main/resources/png/" + images[i] + ".png")));
        } catch (IOException ignored) {
        }
    }

    public void setFPR(int fpr) {
        this.fpr = fpr;
    }

    public Image getImage() {
        Image image = images.get(currentImageId / fpr);
        currentImageId = (currentImageId + 1) % (images.size() * fpr);
        return image;
    }
}
