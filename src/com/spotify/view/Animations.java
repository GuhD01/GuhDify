//// Animations.java
//package com.spotify.view;
//
//import java.awt.*;
//import java.util.Random;
//
//public class Animations {
//
//    public static class CircleAnimation implements AnimationInterface {
//        @Override
//        public void drawAnimation(Graphics2D g, int width, int height, Random random) {
//            for (int i = 0; i < 20; i++) {
//                int x = random.nextInt(width);
//                int y = random.nextInt(height);
//                int size = random.nextInt(50);
//                Color color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
//                g.setColor(color);
//                g.fillOval(x, y, size, size);
//            }
//        }
//    }
//
//    public static class LineAnimation implements AnimationInterface {
//        @Override
//        public void drawAnimation(Graphics2D g, int width, int height, Random random) {
//            int x1 = 0;
//            int y1 = height / 2;
//            int x2 = width;
//            int y2 = height / 2;
//            g.setColor(Color.GREEN);
//            for (int i = 0; i < 20; i++) {
//                int x = random.nextInt(width);
//                int y = random.nextInt(height);
//                g.drawLine(x1, y1, x, y);
//                g.drawLine(x2, y2, x, y);
//            }
//        }
//    }
//    public static class RotatingHexagonAnimation implements AnimationInterface {
//        @Override
//        public void drawAnimation(Graphics2D g, int width, int height, Random random) {
//            int centerX = width / 2;
//            int centerY = height / 2;
//            int radius = 100;
//            double time = System.currentTimeMillis() / 2000.0; // current time in seconds, slower rotation
//            Color[] colors = {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.MAGENTA};
//            for (int i = 0; i < colors.length; i++) {
//                int x = (int) (centerX + radius * Math.cos(Math.toRadians(i * 60 + time * 60)));
//                int y = (int) (centerY + radius * Math.sin(Math.toRadians(i * 60 + time * 60)));
//                g.setColor(colors[i]);
//                g.fillOval(x - 25, y - 25, 50, 50);
//            }
//        }
//    }
//    public static class GridOfSquaresAnimation implements AnimationInterface {
//        @Override
//        public void drawAnimation(Graphics2D g, int width, int height, Random random) {
//            int gap = 10;
//            int size1 = (width - 6 * gap) / 5;
//            int size2 = (height - 6 * gap) / 5;
//            for (int i = 0; i < 5; i++) {
//                for (int j = 0; j < 5; j++) {
//                    int x = gap + i * (size1 + gap);
//                    int y = gap + j * (size2 + gap);
//                    Color color1 = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
//                    Color color2 = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
//                    GradientPaint gradient = new GradientPaint(x, y, color1, x + size1, y + size2, color2);
//                    g.setPaint(gradient);
//                    g.fillRect(x, y, size1, size2);
//                }
//            }
//        }
//    }
//    public static class RandomBarsAnimation implements AnimationInterface {
//        @Override
//        public void drawAnimation(Graphics2D g, int width, int height, Random random) {
//            int numBars = 10;
//            int barWidth = (width - 20) / numBars;
//            int barHeight = 20;
//            int gap = (width - numBars * barWidth) / (numBars + 1);
//            Color color3 = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
//            for (int i = 0; i < numBars; i++) {
//                int barX = gap + i * (barWidth + gap);
//                int barY = (int) (height * Math.random() - barHeight);
//                g.setColor(color3);
//                g.fillRect(barX, barY, barWidth, barHeight);
//            }
//        }
//    }
//
//    public static class StarAnimation implements AnimationInterface {
//        @Override
//        public void drawAnimation(Graphics2D g, int width, int height, Random random) {
//            int numStars = 50;
//            for (int i = 0; i < numStars; i++) {
//                int x = random.nextInt(width);
//                int y = random.nextInt(height);
//                int size = random.nextInt(5) + 1;
//                g.setColor(Color.RED);
//                g.fillRect(x, y, size, size);
//            }
//        }
//    }
//    public static class CirclesAnimation implements AnimationInterface {
//        @Override
//        public void drawAnimation(Graphics2D g, int width, int height, Random random) {
//            int numCircles = 20;
//            int radius = 50;
//            int centerX = width / 2;
//            int centerY = height / 2;
//            for (int i = 0; i < numCircles; i++) {
//                double angle = i * (2 * Math.PI / numCircles);
//                int x = (int) (centerX + radius * Math.cos(angle));
//                int y = (int) (centerY + radius * Math.sin(angle));
//                Color color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
//                g.setColor(color);
//                g.fillOval(x - 25, y - 25, 50, 50);
//            }
//        }
//    }
//
//    public static class RandomSquaresAnimation implements AnimationInterface {
//        @Override
//        public void drawAnimation(Graphics2D g, int width, int height, Random random) {
//            int numSquares = 20;
//            int sideLength = 50;
//            for (int i = 0; i < numSquares; i++) {
//                int x = random.nextInt(width - sideLength);
//                int y = random.nextInt(height - sideLength);
//                Color color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
//                g.setColor(color);
//                g.fillRect(x, y, sideLength, sideLength);
//            }
//        }
//    }
//
//    public static class RandomTrianglesAnimation implements AnimationInterface {
//        @Override
//        public void drawAnimation(Graphics2D g, int width, int height, Random random) {
//            int numTriangles = 20;
//            int size = 50;
//            for (int i = 0; i < numTriangles; i++) {
//                int x = random.nextInt(width - size);
//                int y = random.nextInt(height - size);
//                int[] xPoints1 = {x, x + size / 2, x + size};
//                int[] yPoints1 = {y + size, y, y + size};
//                Color color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
//                g.setColor(color);
//                g.fillPolygon(xPoints1, yPoints1, 3);
//            }
//        }
//    }
//
//}
//
//
