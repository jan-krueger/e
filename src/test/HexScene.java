package test;

import de.SweetCode.e.E;
import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.rendering.GameScene;

import java.awt.*;

public class HexScene extends GameScene {

    private int WIDTH = E.getE().getSettings().getWidth();
    private int HEIGHT = E.getE().getSettings().getHeight();
    private int RADIUS = 600;

    private Font font = new Font("Arial", Font.BOLD, 18);
    private FontMetrics metrics;

    public HexScene() {}

    @Override
    public void render(Graphics2D g) {
        Point origin = new Point(WIDTH / 2, HEIGHT / 2);

        g.setStroke(new BasicStroke(4.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));
        g.setFont(font);
        metrics = g.getFontMetrics();

        // draw the circle
        Stroke tmpS = g.getStroke();
        Color tmpC = g.getColor();

        g.setColor(Color.BLUE);
        g.setStroke(new BasicStroke(0, BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND));

        int diameter = RADIUS * 2;
        int x2 = origin.x - RADIUS;
        int y2 = origin.y - RADIUS;


        g.fillOval(x2, y2, diameter, diameter);


        // Set values to previous when done.
        g.setColor(tmpC);
        g.setStroke(tmpS);

        drawHexGridLoop(g, origin, 11, 50, 8);
    }

    @Override
    public void update(InputEntry input, long delta) {

    }

    @Override
    public boolean isActive() {
        return true;
    }

    private void drawHexGridLoop(Graphics g, Point origin, int size, int radius, int padding) {
        double ang30 = Math.toRadians(30);
        double xOff = Math.cos(ang30) * (radius + padding);
        double yOff = Math.sin(ang30) * (radius + padding);
        int half = size / 2;

        for (int row = 0; row < size; row++) {
            int cols = size - java.lang.Math.abs(row - half);

            for (int col = 0; col < cols; col++) {
                int xLbl = row < half ? col - row : col - half;
                int yLbl = row - half;
                int x = (int) (origin.x + xOff * (col * 2 + 1 - cols));
                int y = (int) (origin.y + yOff * (row - half) * 3);

                drawHex(g, xLbl, yLbl, x, y, radius);
            }
        }
    }

    private void drawHex(Graphics g, int posX, int posY, int x, int y, int r) {
        Graphics2D g2d = (Graphics2D) g;

        Hexagon hex = new Hexagon(x, y, r);
        String text = String.format("%s : %s", coord(posX), coord(posY));
        int w = metrics.stringWidth(text);
        int h = metrics.getHeight();

        hex.draw(g2d, x, y, 0, 0x008844, true);
        hex.draw(g2d, x, y, 4, 0xFFDD88, false);

        g.setColor(new Color(0xFFFFFF));
        g.drawString(text, x - w/2, y + h/2);
    }

    private String coord(int value) {
        return (value > 0 ? "+" : "") + Integer.toString(value);
    }


}
