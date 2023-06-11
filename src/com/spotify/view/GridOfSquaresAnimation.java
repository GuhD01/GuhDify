package com.spotify.view;

import java.awt.*;
import java.util.Random;

public class GridOfSquaresAnimation implements AnimationInterface {
    @Override
    public void drawAnimation(Graphics2D g, int width, int height, Random random) {
        int gap = 10;
        int size1 = (width - 6 * gap) / 5;
        int size2 = (height - 6 * gap) / 5;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                int x = gap + i * (size1 + gap);
                int y = gap + j * (size2 + gap);
                Color color1 = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
                Color color2 = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
                GradientPaint gradient = new GradientPaint(x, y, color1, x + size1, y + size2, color2);
                g.setPaint(gradient);
                g.fillRect(x, y, size1, size2);
            }
        }
    }
}