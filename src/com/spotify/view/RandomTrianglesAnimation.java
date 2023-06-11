package com.spotify.view;

import java.awt.*;
import java.util.Random;

public class RandomTrianglesAnimation implements AnimationInterface {
    @Override
    public void drawAnimation(Graphics2D g, int width, int height, Random random) {
        int numTriangles = 20;
        int size = 50;
        for (int i = 0; i < numTriangles; i++) {
            int x = random.nextInt(width - size);
            int y = random.nextInt(height - size);
            int[] xPoints1 = {x, x + size / 2, x + size};
            int[] yPoints1 = {y + size, y, y + size};
            Color color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
            g.setColor(color);
            g.fillPolygon(xPoints1, yPoints1, 3);
        }
    }
}