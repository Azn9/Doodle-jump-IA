package dev.azn9.libgdxtest;

public class Utils {

    public static boolean collide(Player player, Platform platform) {
        return player.getX() < platform.getX() + platform.getWidth() &&
                player.getX() + player.getWidth() > platform.getX() &&
                player.getY() < platform.getY() + platform.getHeight() &&
                player.getY() + player.getHeight() > platform.getY();
    }

}
