package myVelibGUI;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Map extends JPanel {

    public static final Color USER = new Color(214,217,223);
    public static final Color STATION = new Color(255,204,102);
    public static final Color BIKE = new Color(153,102,0);
    

    public static final Color[] GROUND = {
       USER,
       STATION, 
       BIKE
    };
    
    public int NUM_ROWS;
    public int NUM_COLS;

    public static final double PREFERRED_GRID_SIZE_PIXELS = 0.1;

    // In reality you will probably want a class here to represent a map tile,
    // which will include things like dimensions, color, properties in the
    // game world.  Keeping simple just to illustrate.
    private final Color[][] GROUNDGrid;

    public Map(int rows, int columns){
    	
    		NUM_ROWS = rows;
    		NUM_COLS = columns;
        this.GROUNDGrid = new Color[NUM_ROWS][NUM_COLS];
        Random r = new Random();
        
        
        double preferredWidth = NUM_COLS * PREFERRED_GRID_SIZE_PIXELS;
        double preferredHeight = NUM_ROWS * PREFERRED_GRID_SIZE_PIXELS;
        setPreferredSize(new Dimension((int) preferredWidth, (int)preferredHeight));
    }
    
    public void update() {
    	
    }
    @Override
    public void paintComponent(Graphics g) {
        // Important to call super class method
        super.paintComponent(g);
        // Clear the board
        g.clearRect(0, 0, getWidth(), getHeight());
        // Draw the grid
        int rectWidth = getWidth() / NUM_COLS;
        int rectHeight = getHeight() / NUM_ROWS;

        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLS; j++) {
                // Upper left corner of this GROUND rect
                int x = i * rectWidth;
                int y = j * rectHeight;
                Color GROUNDColor = GROUNDGrid[i][j];
                g.setColor(GROUNDColor);
                g.fillRect(x, y, rectWidth, rectHeight);
            }
        }
    }

  
}