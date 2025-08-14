package minesweeper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    void testFirstMoveIsAlwaysSafe() {
        Board board = new Board(5, 5);
        board.reveal(2, 2); // First move triggers mine placement
        assertFalse(board.isGameOver(), "First move should never be a mine");
    }

    @Test
    void testRevealMineEndsGame() {
        Board board = new Board(4, 1);

        // Reveal first move safely (so mines are placed)
        board.reveal(0, 0);

        // Try to find a mine by checking all unrevealed cells
        boolean mineRevealed = false;
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                if (!board.reveal(r, c)) {
                    mineRevealed = true;
                    break;
                }
            }
            if (mineRevealed) break;
        }

        assertTrue(board.isGameOver(), "Game should end after revealing a mine");
    }

   
    @Test
    void testRevealingSameCellAgainDoesNotChangeState() {
        Board board = new Board(4, 1);
        board.reveal(0, 0); // First reveal

        boolean result = board.reveal(0, 0); // Reveal again
        assertTrue(result, "Revealing an already revealed cell should be safe");
    }
    

    @Test
    void firstMoveIsSafe() {
        Board board = new Board(5, 5);
        // First reveal, must NOT be a mine (game not over)
        boolean safe = board.reveal(2, 2);
        assertTrue(safe, "First move must be safe");
        assertFalse(board.isGameOver(), "Game should not be over on first safe move");
    }

    @Test
    void gameOverWhenMineRevealed() {
        Board board = new Board(4, 1);
        board.reveal(0, 0); // first safe move triggers mine placement

        // Try revealing all other cells until a mine detonates
        boolean detonated = false;
        outer:
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                if (r == 0 && c == 0) continue; // skip first move cell
                boolean safe = board.reveal(r, c);
                if (!safe) {
                    detonated = true;
                    break outer;
                }
            }
        }
        assertTrue(detonated, "A mine should have detonated");
        assertTrue(board.isGameOver(), "Game over after detonating a mine");
    }

    @Test
    void revealSameCellTwiceDoesNotFail() {
        Board board = new Board(4, 1);
        boolean firstReveal = board.reveal(1, 1);
        boolean secondReveal = board.reveal(1, 1);
        assertTrue(firstReveal, "First reveal should be safe or detonated");
        assertTrue(secondReveal, "Revealing already revealed cell should be safe");
    }
}
