package minesweeper;

/**
 * Represents a single cell on the Minesweeper board.
 * <p>
 * Each cell can either contain a mine or not, track if it has been revealed,
 * and store the number of adjacent mines.
 */
public class Cell {
    private boolean isMine;
    private boolean isRevealed;
    private int adjacentMines;

    /**
     * Checks if this cell contains a mine.
     *
     * @return {@code true} if the cell is a mine; otherwise {@code false}.
     */
    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean isMine) {
        this.isMine = isMine;
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public void reveal() {
        this.isRevealed = true;
    }

    public int getAdjacentMines() {
        return adjacentMines;
    }

    /**
     * Sets the number of adjacent mines for this cell.
     *
     * @param count Number of adjacent mines.
     */
    public void setAdjacentMines(int count) {
        this.adjacentMines = count;
    }

    /**
     * Returns the string representation of the cell: - "*" if it's a revealed mine
     * - A number if revealed and not a mine - "_" if not revealed
     *
     * @return A string representing the cell's current state.
     */
    @Override
    public String toString() {
        return isRevealed ? (isMine ? "*" : String.valueOf(adjacentMines)) : "_";
    }
}
