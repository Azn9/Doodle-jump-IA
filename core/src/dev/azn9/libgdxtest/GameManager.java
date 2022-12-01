package dev.azn9.libgdxtest;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import dev.azn9.libgdxtest.nn.BrainDrawer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GameManager extends ApplicationAdapter {

    private static final int CONCURRENT_GAMES = 100;

    private final List<Game> games = new ArrayList<>();

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    private Thread physicsThread;

    private Vector3 cameraPos;

    private BrainDrawer brainDrawer;
    private Thread brainThread;

    private Game trackedGame;

    @Override
    public void create() {
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, 800, 600);
        this.cameraPos = new Vector3(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, 0);

        this.shapeRenderer = new ShapeRenderer();
        this.shapeRenderer.setAutoShapeType(true);
        this.batch = new SpriteBatch();
        this.font = new BitmapFont();

        this.brainDrawer = new BrainDrawer(null, 400, 0);

        long seed = System.currentTimeMillis();

        for (int i = 0; i < GameManager.CONCURRENT_GAMES; i++) {
            this.games.add(new Game(seed));
        }

        this.games.forEach(Game::initialize);
        this.games.get(0).getPlayer().setCanMove(true);

        this.games.forEach(game -> game.setCameraPos(this.cameraPos));

        Platform platform = new Platform();
        platform.setX(175);
        platform.setY(100);
        this.games.forEach(game -> game.setInitPlatform(platform));

        this.physicsThread = new Thread(this::physics);
        this.physicsThread.start();

        this.brainThread = new Thread(this::think);
        this.brainThread.start();
    }

    private void think() {
        while (true) {
            this.games.forEach(Game::tickThink);
        }
    }

    private void physics() {
        while (true) {
            TimeUtils.setDeltaTime();

            this.games.forEach(Game::tickPhysics);
        }
    }

    @Override
    public void render() {
        if (this.games.stream().allMatch(Game::isDead)) {

        }

        int alive = (int) this.games.stream().filter(game -> !game.isDead()).count();

        this.games.stream().filter(game -> !game.isDead()).max(Comparator.comparingInt(Game::getScore)).ifPresent(game -> {
            this.trackedGame = game;
            this.brainDrawer.setBrain(game.getPlayer().getBrain());
            this.cameraPos = game.getCameraPos();
        });

        ScreenUtils.clear(1, 1, 1, 1);

        this.camera.position.lerp(this.cameraPos, Gdx.graphics.getDeltaTime() * 2);
        this.camera.update();
        this.shapeRenderer.setProjectionMatrix(camera.combined);

        this.shapeRenderer.begin();

        this.games.forEach(game -> game.render(this.shapeRenderer));
        this.trackedGame.renderFull(this.shapeRenderer);

        this.brainDrawer.setBrain(this.trackedGame.getPlayer().getBrain());
        this.brainDrawer.setxOffset(this.camera.position.x);
        this.brainDrawer.setyOffset(this.camera.position.y - 300);
        this.brainDrawer.draw(this.shapeRenderer);
        this.shapeRenderer.end();

        this.batch.begin();
        this.font.draw(this.batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 590);
        this.font.draw(this.batch, "PPS: " + TimeUtils.getPhysicsTime(), 10, 570);
        this.font.draw(this.batch, "Score: " + this.trackedGame.getScore(), 10, 550);
        this.font.draw(this.batch, "Alive: " + alive, 10, 530);
        this.batch.end();
    }

    @Override
    public void dispose() {
        if (this.physicsThread.isAlive()) {
            this.physicsThread.interrupt();
        }

        if (this.brainThread.isAlive()) {
            this.brainThread.interrupt();
        }

        this.batch.dispose();
        this.shapeRenderer.dispose();
        this.font.dispose();

        System.exit(0);
    }
}
