package dev.azn9.libgdxtest;

public class Hitbox {

    private final float x;
    private final float y;
    private final float width;
    private final float height;

    public Hitbox(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getWidth() {
        return this.width;
    }

    public float getHeight() {
        return this.height;
    }
}
