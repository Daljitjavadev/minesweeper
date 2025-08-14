package minesweeper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Minesweeper {
    public static void main(String[] args) {
        SpringApplication.run(Minesweeper.class, args);
        new Game().start();
    }
}
