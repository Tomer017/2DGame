import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    // SCREEN SETTINGS
    final int originalTileSize = 16; // 16x16 tile
    final int scale = 3;

    final int tileSize = originalTileSize * scale; //48x48 tile
    final int maxScreenCol = 16; // 4:3 Ratio
    final int maxScreenRow = 12;
    final int screenWidth = tileSize * maxScreenCol; //768 pixels
    final int screenHeight = tileSize * maxScreenRow; //576 pixels
    // FPS
    int fps = 60;

    KeyHandler keyH = new KeyHandler();
    Thread gameThread;

    // Set player's default position
    double playerX = 100.0;
    double playerY = 100.0;

    int playerSpeed = 4;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run(){
        double drawInterval = (double) 1000000000 / fps; // 0.0166666 Seconds
        double nextDrawTime = System.nanoTime() + drawInterval;

        while(gameThread !=null) {
            // Update information such as character position
            // draw: draw the screen with the updated information
            long currentTime = System.nanoTime(); // Use this variable to implement pets missing you

            update();

            repaint();

            try {

                double remainingTime = nextDrawTime - currentTime;
                remainingTime = remainingTime / 1000000;

                if (remainingTime < 0) {
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime);

                nextDrawTime += drawInterval;

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void update() {
        double diagonalSpeed = playerSpeed / Math.sqrt(2); // Normalize speed for diagonal movement

        if (keyH.upPressed && keyH.leftPressed) {
            playerY -= diagonalSpeed;
            playerX -= diagonalSpeed;
        } else if (keyH.upPressed && keyH.rightPressed) {
            playerY -= diagonalSpeed;
            playerX += diagonalSpeed;
        } else if (keyH.downPressed && keyH.leftPressed) {
            playerY += diagonalSpeed;
            playerX -= diagonalSpeed;
        } else if (keyH.downPressed && keyH.rightPressed) {
            playerY += diagonalSpeed;
            playerX += diagonalSpeed;
        } else if (keyH.upPressed) {
            playerY -= playerSpeed;
        } else if (keyH.downPressed) {
            playerY += playerSpeed;
        } else if (keyH.leftPressed) {
            playerX -= playerSpeed;
        } else if (keyH.rightPressed) {
            playerX += playerSpeed;
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.WHITE);
        g2.fillRect((int) playerX, (int) playerY, tileSize, tileSize);

        g2.dispose();

    }
}
