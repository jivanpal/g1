package Views;

import ClientNetworking.GameHost.MapContainer;
import GameLogic.Asteroid;
import GameLogic.Map;
import GameLogic.Ship;
import Physics.Body;
import Utils.Tuple;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * Created by James on 14/02/17.
 * This view shows a top down view of the entire battlefield, showing locations of enemy
 * ships and asteroids.
 */
public class RadarView extends JPanel{
    private final Color ASTEROID_COLOR = Color.red;
    private final Color PLAYER_SHIP_COLOR = Color.green;
    private final Color ENEMY_SHIP_COLOR = Color.blue;
    private final Color BACKGROUND_COLOR = Color.black;

    private final int CIRCLE_DRAW_DIAMETER = 5;

    private Map map;
    private String playerName;

    /**
     * Creates a new RadarView
     */
    public RadarView(String playerName, Map map) {
        super();

        this.playerName = playerName;
        this.map = map;

        this.setBackground(BACKGROUND_COLOR);
        this.setBorder(BorderFactory.createLineBorder(Color.red));
    }

    /**
     * Redraws the entire RadarView map with the new positions of asteroids and
     * enemy ships.
     */
    public void updateMap(Map map) {
        this.map = map;

        // calls paintComponent
        repaint();
    }

    /**
     * Draws an equilateral triangle centered on the given coordinates
     * @param x The x coordinate of the triangle
     * @param y The y coordinate of the triangle
     */
    private void drawTriangle(int x, int y) {
    }

    /**
     * Draws a circle centered on the given coordinates
     * @param x The x coordinate
     * @param y The y coordinate
     */
    private void drawCircle(Graphics2D g, double x, double y) {

    }

    /**
     * Given the center position of for a body in the map, return the position at which we should
     * draw a circle in the RadarView for the resulting circle to be centered on the same spot.
     * @param x The x coordinate of the center of the circle in the map
     * @param y The y coordinate of the center of the circle in the map
     * @return The position at which we should draw this circle
     */
    private Tuple<Integer, Integer> circleDrawPositionFromCenter(double x, double y) {
        return new Tuple<Integer, Integer>((int) Math.round(Utils.Utils.scaleValueToRange(x, 0, MapContainer.MAP_SIZE, 0, this.getWidth())),
                (int) Math.round(Utils.Utils.scaleValueToRange(y, 0, MapContainer.MAP_SIZE, 0, this.getHeight())));
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        this.setBackground(BACKGROUND_COLOR);
        graphics.clearRect(0, 0, getWidth(), getHeight());

        Graphics2D g = (Graphics2D) graphics;

        for(Body b : map) {

            // Select the correct color to paint
            if(b instanceof Asteroid) {
                g.setPaint(ASTEROID_COLOR);

            } else if (b instanceof Ship) {
                Ship s = (Ship) b;

                // Check if this ship is ours so we can draw it a different color
                if(s.getPilotName().equals(playerName) || s.getEngineerName().equals(playerName)) {
                    g.setPaint(PLAYER_SHIP_COLOR);
                } else {
                    g.setPaint(ENEMY_SHIP_COLOR);
                }

            } else {
                // What has happened here? This is really bad.
                System.err.println("Views:RadarView:paintComponent() - Something has gone quite wrong here");
            }

            // Draw the circle on the map
            Tuple<Integer, Integer> position = circleDrawPositionFromCenter(b.getPosition().getX(), b.getPosition().getY());
            Ellipse2D.Double circle = new Ellipse2D.Double(position.getX(), position.getY(), CIRCLE_DRAW_DIAMETER, CIRCLE_DRAW_DIAMETER);
            g.fill(circle);
        }
    }
}
