package dev.azn9.libgdxtest;

public class TimeUtils {

    private static long lastUpdateTime;
    private static long deltaTime;

    private static final Object lock = new Object();
    private static Long physicsTime = 0L;

    public static void setDeltaTime() {
        if (lastUpdateTime == 0) {
            lastUpdateTime = System.currentTimeMillis();
            return;
        }

        long currentTime = System.currentTimeMillis();
        deltaTime = currentTime - lastUpdateTime;
        lastUpdateTime = currentTime;

        synchronized (TimeUtils.lock) {
            TimeUtils.physicsTime++;
        }
    }

    public static Long getPhysicsTime() {
        synchronized (TimeUtils.lock) {
            long time = TimeUtils.physicsTime;
            TimeUtils.physicsTime = 0L;
            return time;
        }
    }

    public static long getDeltaTime() {
        return TimeUtils.deltaTime;
    }

    public static float getDeltaTimeSeconds() {
        return TimeUtils.deltaTime / 1000f;
    }
}
