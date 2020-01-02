public class Board {

    int depth, width;
    private boolean[][] board;
    private static final int[][] NEIGHBOURS = {
        {-1, +1}, {0, +1}, {+1, +1},
        { -1, 0}, { +1, 0},
        {-1, -1}, {0, -1}, {+1, -1}
    };

    public Board(int depth, int width) {
        this.depth = depth;
        this.width = width;
        board = new boolean[depth][width];
    }

    public int getDepth() {
        return depth;
    }

    public int getWidth() {
        return width;
    }

    public void clear() {
        board = new boolean[depth][width];
    }

    public boolean getValueAt(int x, int y) {
        return x >= 0 && y >= 0 && x < depth && y < width && board[x][y];
    }

    public void setValueAt(int x, int y) {
        if (x >= 0 && y >= 0 && x < depth && y < width)
            board[x][y] = !board[x][y];
    }

    public int countNeighbors(int x, int y) {
        int c = 0;
        for (int[] offset : NEIGHBOURS) {
            int newX = x + offset[0];
            int newY = y + offset[1];
            if (getValueAt(newX, newY)) {
                c++;
            }
        }

        return c;
    }

    public void nextTurn() {
        boolean[][] temp = new boolean[depth][width];

        for (int i = 0; i < depth; i++) {
            for (int j = 0; j < width; j++) {
                int neighbors = countNeighbors(i, j);
                if (board[i][j] && (neighbors < 2 || neighbors > 3)) {
                    temp[i][j] = false;
                } else if (!board[i][j] && neighbors == 3) {
                    temp[i][j] = true;
                } else {
                    temp[i][j] = board[i][j];
                }
            }
        }

        board = temp;
    }
}