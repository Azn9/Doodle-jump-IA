package dev.azn9.libgdxtest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import dev.azn9.libgdxtest.nn.Brain;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private final Rectangle rectangle;
    private float x;
    private float y;
    private float xVelocity;
    private float yVelocity;

    private Brain brain;

    private boolean canMove = false;

    public Player() {
        this.rectangle = new Rectangle();
        this.rectangle.setWidth(20);
        this.rectangle.setHeight(50);

        this.setX(190);
        this.setY(300);
    }

    public void setBrain(Brain brain) {
        this.brain = brain;
    }

    public void move() {
        if (canMove) {
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                this.xVelocity = -200;
            } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                this.xVelocity = 200;
            } else {
                this.xVelocity = 0;
            }
        }

        this.x += this.xVelocity * TimeUtils.getDeltaTimeSeconds();
        this.y += this.yVelocity * TimeUtils.getDeltaTimeSeconds();
    }

    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(1, 0, 0, 1);
        shapeRenderer.rect(this.x, this.y, this.rectangle.getWidth(), this.rectangle.getHeight());
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setxVelocity(float xVelocity) {
        this.xVelocity = xVelocity;
    }

    public void setyVelocity(float yVelocity) {
        this.yVelocity = yVelocity;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getxVelocity() {
        return this.xVelocity;
    }

    public float getyVelocity() {
        return this.yVelocity;
    }

    public float getWidth() {
        return this.rectangle.getWidth();
    }

    public float getHeight() {
        return this.rectangle.getHeight();
    }

    public Hitbox getBottom() {
        return null;
    }

    public void think(Platform currentPlatform, Platform nextPlatform) {
        if (this.brain == null) {
            return;
        }

        List<Float> input = new ArrayList<>();

        float currentXDif = currentPlatform.getX() - this.getX();
        float currentYDif = currentPlatform.getY() - this.getY();
        float nextXDif = nextPlatform.getX() - this.getX();
        float nextYDif = nextPlatform.getY() - this.getY();
        float yVelocity = this.getyVelocity();

        input.add(currentXDif);
        input.add(currentYDif);
        input.add(nextXDif);
        input.add(nextYDif);
        input.add(yVelocity);

        this.brain.reset();
        this.brain.feedInput(input);
        this.brain.engage();

        List<Float> output = this.brain.getOutput();

        float outputValue = output.get(0);

        if (outputValue > 0.75D) {
            this.setxVelocity(200);
        } else if (outputValue < 0.25D) {
            this.setxVelocity(-200);
        } else {
            this.setxVelocity(0);
        }
    }

    public Brain getBrain() {
        return this.brain;
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }
}
