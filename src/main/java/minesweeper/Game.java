package minesweeper;

import java.util.Scanner;

/**
 * The {@code Game} class manages the overall Minesweeper game flow.
 * <p>
 * It handles user input for board setup (size and number of mines), starts the
 * game loop, and processes user moves until the player either wins, loses by
 * hitting a mine, or exits the game.
 */
public class Game {
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Starts the Minesweeper game loop.
     * <p>
     * Prompts the user to configure the grid size and number of mines, validates
     * inputs, initializes the board, and manages the main game loop. Users can
     * reveal cells by entering coordinates (e.g., A1). The game continues until the
     * player either wins, hits a mine, or chooses to exit by entering 'e'.
     */
    public void start() {
        System.out.println("Welcome to Minesweeper!");
        String input = "";
        while (!input.equalsIgnoreCase("e")) {
            int size = 0;
            do {
                size = askInt("Enter the size of the grid (e.g. 4 for a 4x4 grid, minimum 4): ");
            } while (size < 4);
            int totalCells = size * size;
            /*
             * Reserve a safe zone of 9 cells around the first move (the selected cell plus
             * its up to 8 neighbors) This ensures the first revealed cell and its
             * surrounding cells are always free of mines, allowing for a guaranteed safe
             * start.
             */
            int minSafeZoneSize = 9;

            int maxMinesAllowed = (int) (totalCells * 0.35);
            maxMinesAllowed = Math.min(maxMinesAllowed, totalCells - minSafeZoneSize);
            int mines;

            while (true) {
                mines = askInt("Enter the number of mines (max " + maxMinesAllowed + "): ");
                if (mines <= maxMinesAllowed)
                    break;
                System.out.println("Too many mines!");
            }

            Board board = new Board(size, mines);
            while (!board.isGameOver()) {
                board.printBoard(false);
                String move = askMove("Select a square to reveal (e.g. A1): ", size);
                int row = move.charAt(0) - 'A';
                int col = Integer.parseInt(move.substring(1)) - 1;

                boolean safe = board.reveal(row, col);
                if (!safe) {
                    board.printBoard(true);
                    System.out.println("Oh no, you detonated a mine! Game over.");
                    break;
                }

                if (board.isGameWon()) {
                    board.printBoard(true);
                    System.out.println("Congratulations, you have won the game!");
                    break;
                }
            }
            System.out.println("Press e for exit or Press any key to play again...");
            input = scanner.nextLine();
            if (input.equalsIgnoreCase("e")) {
                System.out.println("Exiting...");
            }
        }
    }

    private int askInt(String prompt) {
        System.out.print(prompt);
        return Integer.parseInt(scanner.nextLine());
    }

    /**
     * Prompts the user for a valid grid coordinate (e.g., "A1") based on the given
     * board size. Continues to prompt until input is within valid row ('A' to max
     * row) and column (1 to size) range.
     *
     * @param prompt The message shown to the user.
     * @param size   The size of the square grid (e.g., 4 for 4x4).
     * @return A valid uppercase grid coordinate string.
     */
    private String askMove(String prompt, int size) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().toUpperCase().trim();
            if (input.length() >= 2) {
                char row = input.charAt(0);
                String colStr = input.substring(1);
                if (row >= 'A' && row < 'A' + size) {
                    try {
                        int col = Integer.parseInt(colStr);
                        if (col >= 1 && col <= size)
                            return input;
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
            System.out.println("Invalid input. Try again.");
        }
    }
}
