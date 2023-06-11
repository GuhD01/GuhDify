package com.spotify.view;


import java.awt.*;
import java.util.Random;

public interface AnimationInterface {
    void drawAnimation(Graphics2D g, int width, int height, Random random);
}