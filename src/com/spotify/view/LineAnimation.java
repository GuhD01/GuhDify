package com.spotify.view;

import java.awt.*;
import java.util.Random;

public class LineAnimation implements AnimationInterface {
    @Override
    public void drawAnimation(Graphics2D g, int width, int height, Random random) {
        int x1 = 0;
        int y1 = height / 2;
        int x2 = width;
        int y2 = height / 2;
        g.setColor(Color.GREEN);
        for (int i = 0; i < 20; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            g.drawLine(x1, y1, x, y);
            g.drawLine(x2, y2, x, y);
        }
    }
}