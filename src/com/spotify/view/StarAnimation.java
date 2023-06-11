package com.spotify.view;

import java.awt.*;
import java.util.Random;

public class StarAnimation implements AnimationInterface {
    @Override
    public void drawAnimation(Graphics2D g, int width, int height, Random random) {
        int numStars = 50;
        for (int i = 0; i < numStars; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int size = random.nextInt(5) + 1;
            g.setColor(Color.RED);
            g.fillRect(x, y, size, size);
        }
    }
}