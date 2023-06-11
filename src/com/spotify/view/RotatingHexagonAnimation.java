package com.spotify.view;

import java.awt.*;
import java.util.Random;

public class RotatingHexagonAnimation implements AnimationInterface {
    @Override
    public void drawAnimation(Graphics2D g, int width, int height, Random random) {
        int centerX = width / 2;
        int centerY = height / 2;
        int radius = 100;
        double time = System.currentTimeMillis() / 2000.0; // current time in seconds, slower rotation
        Color[] colors = {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.MAGENTA};
        for (int i = 0; i < colors.length; i++) {
            int x = (int) (centerX + radius * Math.cos(Math.toRadians(i * 60 + time * 60)));
            int y = (int) (centerY + radius * Math.sin(Math.toRadians(i * 60 + time * 60)));
            g.setColor(colors[i]);
            g.fillOval(x - 25, y - 25, 50, 50);
        }
    }
}