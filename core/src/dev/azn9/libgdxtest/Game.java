package dev.azn9.libgdxtest;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import dev.azn9.libgdxtest.nn.Brain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {

    private final List<Platform> platforms = new ArrayList<>();
    private final Random random;
    private Platform lastCreatedPlatform;
    private Platform lastPlatform;

    private Player player;

    private boolean isDead = false;
    private int score = 0;
    private Vector3 cameraPos;

    public Game(long randomSeed) {
        this.random = new Random(randomSeed);
    }

    public void setCameraPos(Vector3 cameraPos) {
        this.cameraPos = cameraPos;
    }

    public void initialize() {
        this.player = new Player();
        Brain brain = new Brain(5, 1);
        brain.initialize();
        player.setBrain(brain);
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setInitPlatform(Platform platform) {
        this.lastPlatform = platform;
        this.platforms.add(platform);

        Platform newPlatform = new Platform();
        newPlatform.setX(platform.getX() + (this.random.nextBoolean() ? 1 : -1) * (this.random.nextFloat() * 100 + 50));
        newPlatform.setY(platform.getY() + 150);
        newPlatform.setSelected(true);

        newPlatform.setSelected(true);
        this.platforms.add(newPlatform);
        this.lastCreatedPlatform = newPlatform;
    }

    public void tickPhysics() {
        this.player.move();

        if (this.player.getY() < this.cameraPos.y - 400) {
            this.isDead = true;
        }

        boolean isOnPlatform = false;
        for (Platform platform : this.platforms) {
            if (Utils.collide(this.player, platform)) {
                this.player.setY(platform.getY() + platform.getHeight());
                this.player.setyVelocity(0);
                isOnPlatform = true;

                if (platform.isSelected()) {
                    platform.setSelected(false);

                    Platform newPlatform = new Platform();
                    newPlatform.setX(this.lastCreatedPlatform.getX() + (this.random.nextBoolean() ? 1 : -1) * (this.random.nextFloat() * 100 + 50));
                    newPlatform.setY(this.lastCreatedPlatform.getY() + 150);
                    newPlatform.setSelected(true);

                    this.cameraPos = new Vector3(this.cameraPos.x - this.lastPlatform.getX() + platform.getX(), platform.getY() + 200, 0);

                    this.platforms.remove(this.lastPlatform);
                    this.lastPlatform = this.lastCreatedPlatform;
                    this.lastCreatedPlatform = newPlatform;

                    this.platforms.add(newPlatform);

                    this.score++;
                }

                platform.setHasTouched(true);
                break;
            }
        }

        if (!isOnPlatform) {
            // Gravity
            this.player.setyVelocity(this.player.getyVelocity() + (-700f) * TimeUtils.getDeltaTimeSeconds());
        } else {
            this.player.setyVelocity(500f);
        }
    }

    public void tickThink() {
        this.player.think(this.lastPlatform, this.lastCreatedPlatform);
    }

    public void render(ShapeRenderer shapeRenderer) {
        if (this.isDead) {
            return;
        }

        shapeRenderer.set(ShapeRenderer.ShapeType.Line);
        this.player.draw(shapeRenderer);
    }

    public int getScore() {
        return this.score;
    }

    public void renderFull(ShapeRenderer shapeRenderer) {
        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        this.player.draw(shapeRenderer);

        new ArrayList<>(this.platforms).forEach(platform -> platform.draw(shapeRenderer));
    }

    public boolean isDead() {
        return this.isDead;
    }

    public Vector3 getCameraPos() {
        return this.cameraPos;
    }
}
