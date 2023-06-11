package com.spotify.view;

import java.awt.*;
import java.util.Random;

public class RandomSquaresAnimation implements AnimationInterface {
    @Override
    public void drawAnimation(Graphics2D g, int width, int height, Random random) {
        int numSquares = 20;
        int sideLength = 50;
        for (int i = 0; i < numSquares; i++) {
            int x = random.nextInt(width - sideLength);
            int y = random.nextInt(height - sideLength);
            Color color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
            g.setColor(color);
            g.fillRect(x, y, sideLength, sideLength);
        }
    }
}