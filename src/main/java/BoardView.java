import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;


public class BoardView extends JPanel {
    
    private final int SCALING_FACTOR = 12;

    private int width, height;
    private int xScale, yScale;
    Dimension size;
    private Graphics g;
    private Image boardImage;

    public BoardView(int width, int height) {
        this.width = width;
        this.height = height;
        size = new Dimension(0, 0);

        setPreferredSize(new Dimension(
            width * SCALING_FACTOR, height * SCALING_FACTOR)
        );
    }

    public void resize() {
        Dimension newSize = getSize();

        // if the size has changed
        if(!size.equals(newSize) && newSize.width > 0 && newSize.height > 0) {
            size = newSize;
            boardImage = createImage(size.width, size.height);
            g = boardImage.getGraphics();
            
            xScale = size.width / width;
            if(xScale < 1) 
                xScale = SCALING_FACTOR;
            yScale = size.height / height;
            if(yScale < 1)
                yScale = SCALING_FACTOR;
        }
    }

    public void draw(Board board) {
        resize();
        for (int i = 0; i < board.getDepth(); i++) {
            for (int j = 0; j < board.getWidth(); j++) {
                if (board.getValueAt(i, j)) {
                    paintCell(j, i, Color.black);
                } else {
                    paintCell(j, i, Color.white);
                }
            }
        }

        revalidate();
        repaint();
    }

    public void paintCell(int x, int y, Color c) {
        g.setColor(c);
        g.fillRect(x * xScale, y * yScale, xScale-1, yScale-1);
    }

    public int[] getCell(int x, int y) {
        return new int[]{x / SCALING_FACTOR, y / SCALING_FACTOR};
    }

    @Override
    public void paintComponent(Graphics g) {
        Dimension currentSize = getSize();
        if(size.equals(currentSize)) {
            g.drawImage(boardImage, 0, 0, null);
        }
        else {
            // Rescale the previous image.
            g.drawImage(boardImage, 0, 0, currentSize.width, currentSize.height, null);
        }
    }
}