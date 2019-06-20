import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class Game2048 extends JPanel {

    /**
     * width and height of the game board.
     */
    private final int WIDTH = 4, HEIGHT = 4;

    /**
     * amount of start tiles.
     */
    private final int START_TILES = 4;

    /**
     * game board:
     * 0 is empty.
     * n (n > 0) is pow(2, n - 1).
     * e.g. 1 -> 1
     * 2 -> 2
     * 3 -> 4
     */
    private int[][] board = new int[HEIGHT][WIDTH];

    /**
     * amount of pixels for a tile.
     */
    private final int PIXELS = 40;

    /**
     * initializes the gameboard to zero.
     */
    private void reset() {
        for (int i = 0; i < HEIGHT; i++) {
            for (int k = 0; k < WIDTH; k++) {
                board[i][k] = 0;
            }
        }
    }

    /**
     * resets the game.
     */
    Game2048() {
        reset();

        for (int i = 0; i < START_TILES; i++) {
            generateNewTile();
        }
    }

    /**
     * draws the tiles to the ui.
     *
     * @param g
     */
    public void paint(Graphics g) {
        int a;
        for (int h = 0; h < HEIGHT; h++) {
            for (int w = 0; w < WIDTH; w++) {
                a = board[h][w];

                if (a == 0) {
                    g.setColor(Color.WHITE);
                } else {
                    g.setColor(
                            new Color(
                                    (45 * a) % 256,
                                    (64 * a) % 256,
                                    (33 * a) % 256
                            )
                    );
                }

                g.fillRect(w * PIXELS, h * PIXELS, PIXELS, PIXELS);
                g.setColor(Color.BLUE);
                if (a != 0) {
                    g.drawString("" + (int) Math.pow(2, a - 1),
                            w * PIXELS + PIXELS / 3,
                            h * PIXELS + PIXELS / 2);
                }
            }
        }
    }

    /**
     * updates the window.
     *
     * @param jframe the frame to repaint.
     */
    private void updateWindow(JFrame jframe) {
        jframe.getContentPane().validate();
        jframe.repaint();
    }

    /**
     * slide tiles as far left as possible.
     */
    private void slideLeft() {
        boolean moved = false;
        while (!moved) {
            moved = true;
            for (int h = 0; h < HEIGHT; h++) {
                for (int w = 1; w < WIDTH; w++) {
                    if (board[h][w] != 0 && board[h][w - 1] == 0) {
                        moved = false;
                        board[h][w - 1] = board[h][w];
                        board[h][w] = 0;
                    }
                }
            }
        }
    }

    /**
     * slide tiles as far right as possible.
     */
    private void slideRight() {
        boolean moved = false;
        while (!moved) {
            moved = true;
            for (int h = 0; h < HEIGHT; h++) {
                for (int w = WIDTH - 2; w >= 0; w--) {
                    if (board[h][w] != 0 && board[h][w + 1] == 0) {
                        moved = false;
                        board[h][w + 1] = board[h][w];
                        board[h][w] = 0;
                    }
                }
            }
        }
    }

    /**
     * slide tiles as far up as possible.
     */
    private void slideUp() {
        boolean moved = false;
        while (!moved) {
            moved = true;
            for (int h = 1; h < HEIGHT; h++) {
                for (int w = 0; w < WIDTH; w++) {
                    if (board[h][w] != 0 && board[h - 1][w] == 0) {
                        moved = false;
                        board[h - 1][w] = board[h][w];
                        board[h][w] = 0;
                    }
                }
            }
        }
    }

    /**
     * slide tiles as far down as possible.
     */
    private void slideDown() {
        boolean moved = false;
        while (!moved) {
            moved = true;
            for (int h = HEIGHT - 2; h >= 0; h--) {
                for (int w = 0; w < WIDTH; w++) {
                    if (board[h][w] != 0 && board[h + 1][w] == 0) {
                        moved = false;
                        board[h + 1][w] = board[h][w];
                        board[h][w] = 0;
                    }
                }
            }
        }
    }

    /**
     * action if user pressed left.
     */
    private void moveLeft() {
        slideLeft();

        // combine colliding tiles.
        for (int h = 0; h < HEIGHT; h++) {
            for (int w = 1; w < WIDTH; w++) {
                if (board[h][w] == board[h][w - 1] && board[h][w] != 0) {
                    board[h][w - 1]++;
                    board[h][w] = 0;
                }
            }
        }

        slideLeft();
    }

    /**
     * action if user pressed right.
     */
    private void moveRight() {
        slideRight();

        // combine colliding tiles.
        for (int h = 0; h < HEIGHT; h++) {
            for (int w = WIDTH - 2; w >= 0; w--) {
                if (board[h][w] == board[h][w + 1] && board[h][w] != 0) {
                    board[h][w + 1]++;
                    board[h][w] = 0;
                }
            }
        }

        slideRight();
    }

    /**
     * action if user pressed up.
     */
    private void moveUp() {
        slideUp();

        // combine colliding tiles.
        for (int h = 1; h < HEIGHT; h++) {
            for (int w = 0; w < WIDTH; w++) {
                if (board[h][w] == board[h - 1][w] && board[h][w] != 0) {
                    board[h - 1][w]++;
                    board[h][w] = 0;
                }
            }
        }

        slideUp();
    }

    /**
     * action if user pressed down.
     */
    private void moveDown() {
        slideDown();

        // combine colliding tiles.
        for (int h = HEIGHT - 2; h >= 0; h--) {
            for (int w = 0; w < WIDTH; w++) {
                if (board[h][w] == board[h + 1][w] && board[h][w] != 0) {
                    board[h + 1][w]++;
                    board[h][w] = 0;
                }
            }
        }

        slideDown();
    }

    private void generateNewTile() {
        Random randomNumber = new Random();
        int x, y;
        do {
            x = randomNumber.nextInt(WIDTH);
            y = randomNumber.nextInt(HEIGHT);
        } while (board[y][x] != 0);
        board[y][x] = 1;
    }

    /**
     * check if there's still a tile with 0.
     *
     * @return
     */
    private boolean checkFullBoard() {
        boolean zero = false;
        for (int h = 0; h < HEIGHT; h++) {
            for (int w = 0; w < WIDTH; w++) {
                if (board[h][w] == 0) {
                    zero = true;
                }
            }
        }

        return !zero;
    }

    /**
     * gameloop.
     *
     * @param frame
     */
    public void gameloop(JFrame frame) {
        updateWindow(frame);

        //add key listener
        frame.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent ke) {
                char key = ke.getKeyChar();
                System.out.println(key);
                switch (key) {
                    case 'a':
                        System.out.println("moved left");
                        moveLeft();
                        break;
                    case 'd':
                        System.out.println("moved right");
                        moveRight();
                        break;
                    case 'w':
                        System.out.println("moved up");
                        moveUp();
                        break;
                    case 's':
                        System.out.println("moved down");
                        moveDown();
                        break;
                    default:
                        break;
                }

                if (!checkFullBoard()) {
                    generateNewTile();
                }

            }

            public void keyPressed(KeyEvent ke) {
            }

            public void keyReleased(KeyEvent ke) {

            }
        });

        while (true) {
            updateWindow(frame);
        }


    }

    /**
     * main function
     *
     * @param args
     */
    public static void main(String[] args) {
        Game2048 newGame = new Game2048();
        JFrame frame = new JFrame("2048");
        frame.setSize((newGame.WIDTH + 1) * newGame.PIXELS,
                (newGame.HEIGHT + 1) * newGame.PIXELS);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(newGame);
        frame.setVisible(true);
        newGame.gameloop(frame);
    }
}
