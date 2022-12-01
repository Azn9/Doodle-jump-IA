package dev.azn9.libgdxtest;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class Platform {

    private final Rectangle rectangle;
    private float x;
    private float y;
    private boolean hasTouched = false;
    private boolean selected = false;

    public Platform() {
        this.rectangle = new Rectangle();
        this.rectangle.setWidth(50);
        this.rectangle.setHeight(10);
    }

    public void setHasTouched(boolean hasTouched) {
        this.hasTouched = hasTouched;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void draw(ShapeRenderer shapeRenderer) {
        if (this.selected) {
            shapeRenderer.setColor(0, 1, 0, 1);
        } else if (this.hasTouched) {
            shapeRenderer.setColor(0, 0, 0, 1);
        } else {
            shapeRenderer.setColor(0, 0, 1, 1);
        }

        shapeRenderer.rect(this.x, this.y, this.rectangle.getWidth(), this.rectangle.getHeight());
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getWidth() {
        return this.rectangle.getWidth();
    }

    public float getHeight() {
        return this.rectangle.getHeight();
    }

    public boolean isSelected() {
        return selected;
    }

    public boolean hasTouched() {
        return hasTouched;
    }
}
