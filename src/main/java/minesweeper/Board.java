package minesweeper;

import java.util.*;

/**
 * Represents the Minesweeper game board.
 * <p>
 * Handles initialization, mine placement, revealing cells, checking win/lose
 * conditions, and displaying the board state. Ensures the first move is always
 * safe by excluding the selected cell and its neighbors from initial mine
 * placement.
 */
public class Board {
    private final int size;
    private final int totalMines;
    private final Cell[][] grid;
    private boolean isGameOver = false;
    private boolean firstMove = true;

    /**
     * Constructs a new game board with the given size and number of mines.
     * Initializes all cells in a hidden, non-mined state.
     *
     * @param size       The width/height of the square grid.
     * @param totalMines Total number of mines to be placed on the board.
     */
    public Board(int size, int totalMines) {
        this.size = size;
        this.totalMines = totalMines;
        this.grid = new Cell[size][size];
        initBoard();
    }

    private void initBoard() {
        for (int r = 0; r < size; r++)
            for (int c = 0; c < size; c++)
                grid[r][c] = new Cell();
    }

    /**
     * Randomly places mines on the board, ensuring the first selected cell and its
     * neighbors remain safe.
     * <p>
     * Mines are placed in random positions, skipping any cell in the safe zone
     * around the first move. After placing all mines, adjacent mine counts are
     * calculated for each cell.
     *
     * @param firstRow The row index of the first revealed cell.
     * @param firstCol The column index of the first revealed cell.
     */
    private void placeMines(int firstRow, int firstCol) {
        Random rand = new Random();
        Set<String> banned = getSafeZone(firstRow, firstCol);
        int placed = 0;

        while (placed < totalMines) {
            int r = rand.nextInt(size);
            int c = rand.nextInt(size);
            String key = r + "," + c;

            if (!banned.contains(key) && !grid[r][c].isMine()) {
                grid[r][c].setMine(true);
                placed++;
            }
        }

        calculateAdjacents();
    }

    /**
     * Computes and returns a set of cell coordinates (as "row,col" strings)
     * representing the 3x3 safe zone around the given cell. This zone includes the
     * cell itself and all its valid neighbors.
     *
     * @param row The row index of the center cell.
     * @param col The column index of the center cell.
     * @return A set of coordinate strings marking the safe zone.
     */
    private Set<String> getSafeZone(int row, int col) {
        Set<String> safe = new HashSet<>();
        for (int dr = -1; dr <= 1; dr++)
            for (int dc = -1; dc <= 1; dc++) {
                int nr = row + dr, nc = col + dc;
                if (inBounds(nr, nc)) {
                    safe.add(nr + "," + nc);
                }
            }
        return safe;
    }

    private void calculateAdjacents() {
        for (int r = 0; r < size; r++)
            for (int c = 0; c < size; c++)
                if (!grid[r][c].isMine())
                    grid[r][c].setAdjacentMines(countAdjacentMines(r, c));
    }

    private int countAdjacentMines(int row, int col) {
        int count = 0;
        for (int dr = -1; dr <= 1; dr++)
            for (int dc = -1; dc <= 1; dc++) {
                int nr = row + dr, nc = col + dc;
                if (inBounds(nr, nc) && grid[nr][nc].isMine())
                    count++;
            }
        return count;
    }

    /**
     * Reveals the cell at the specified position.
     * <p>
     * If it's the first move, places mines avoiding the selected cell and its
     * neighbors. If the cell is a mine, marks the game as over. If the cell has no
     * adjacent mines, recursively reveals surrounding cells.
     *
     * @param row The row index (0-based).
     * @param col The column index (0-based).
     * @return {@code true} if the revealed cell is safe; {@code false} if it's a
     *         mine.
     */
    public boolean reveal(int row, int col) {
        if (!inBounds(row, col) || grid[row][col].isRevealed())
            return true;

        if (firstMove) {
            placeMines(row, col); // Safe first move
            firstMove = false;
        }

        grid[row][col].reveal();

        if (grid[row][col].isMine()) {
            isGameOver = true;
            return false;
        }

        if (grid[row][col].getAdjacentMines() == 0) {
            for (int dr = -1; dr <= 1; dr++)
                for (int dc = -1; dc <= 1; dc++)
                    if (!(dr == 0 && dc == 0))
                        reveal(row + dr, col + dc);
        }

        return true;
    }

    /**
     * Checks if the game is won.
     *
     * @return {@code true} if all non-mine cells are revealed; {@code false}
     *         otherwise.
     */
    public boolean isGameWon() {
        for (int r = 0; r < size; r++)
            for (int c = 0; c < size; c++)
                if (!grid[r][c].isMine() && !grid[r][c].isRevealed())
                    return false;
        return true;
    }

    /**
     * Checks if the game is over due to revealing a mine.
     *
     * @return {@code true} if a mine has been revealed; {@code false} otherwise.
     */
    public boolean isGameOver() {
        return isGameOver;
    }

    /**
     * Prints the current state of the board to the console.
     *
     * @param revealAll If {@code true}, all cells (including mines) are revealed.
     *                  If {@code false}, only revealed cells and hidden state are
     *                  shown.
     */
    public void printBoard(boolean revealAll) {
        System.out.print("  ");
        for (int i = 1; i <= size; i++)
            System.out.print(i + " ");
        System.out.println();

        for (int r = 0; r < size; r++) {
            System.out.print((char) ('A' + r) + " ");
            for (int c = 0; c < size; c++) {
                if (revealAll) {
                    System.out.print(grid[r][c].isMine() ? "* " : grid[r][c].getAdjacentMines() + " ");
                } else {
                    System.out.print(grid[r][c] + " ");
                }
            }
            System.out.println();
        }
    }

    private boolean inBounds(int row, int col) {
        return row >= 0 && row < size && col >= 0 && col < size;
    }

}
