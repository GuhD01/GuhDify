package com.spotify.view;

import java.awt.*;
import java.util.Random;

public class RandomBarsAnimation implements AnimationInterface {
    @Override
    public void drawAnimation(Graphics2D g, int width, int height, Random random) {
        int numBars = 10;
        int barWidth = (width - 20) / numBars;
        int barHeight = 20;
        int gap = (width - numBars * barWidth) / (numBars + 1);
        Color color3 = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        for (int i = 0; i < numBars; i++) {
            int barX = gap + i * (barWidth + gap);
            int barY = (int) (height * Math.random() - barHeight);
            g.setColor(color3);
            g.fillRect(barX, barY, barWidth, barHeight);
        }
    }
}
