package YuGiOh.view.game;

import lombok.Getter;

public enum Direction {
    UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT(1, 0);

    @Getter
    private final int x, y;
    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
