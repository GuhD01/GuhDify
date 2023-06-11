package com.spotify.view;

import java.awt.*;
import java.util.Random;

public class CirclesAnimation implements AnimationInterface {
    @Override
    public void drawAnimation(Graphics2D g, int width, int height, Random random) {
        int numCircles = 20;
        int radius = 50;
        int centerX = width / 2;
        int centerY = height / 2;
        for (int i = 0; i < numCircles; i++) {
            double angle = i * (2 * Math.PI / numCircles);
            int x = (int) (centerX + radius * Math.cos(angle));
            int y = (int) (centerY + radius * Math.sin(angle));
            Color color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
            g.setColor(color);
            g.fillOval(x - 25, y - 25, 50, 50);
        }
    }
}