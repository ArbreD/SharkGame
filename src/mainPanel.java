
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */

/**
 *
 * @author "ArbreD"
 */
class mainPanel extends javax.swing.JPanel {

    /**
     * Creates new form mainPanel
     */
    Fish shark;
    ArrayList<Fish> list; // list of fishes
    final int numberOfFish = 10; // number of fishes on the screen
    Thread t; // control the game
    int grade = 0;
    int time = 60;
    Thread t2; // control time countdown
    boolean isPlay; // true: playing; false: win or game over
    int numberOfSmall; // number of small fish in the list
    final String bgFile = "images/bg.jpg";
    final String sharkFile = "images/shark.png";
    final String smallFile = "images/small.png";
    final String bigFile = "images/jellyfish.png";
    int intWidth, intHeight;

    class CountDownThread extends Thread {

        @Override
        public void run() {
            while (isPlay) {
                if (time < 1) {
                    isPlay = false;
                    continue;
                }
                repaint(); // call back of paintComponent function
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
                time--;
            }
        }
    } // end of class CountDownThread

    class GameControlThread extends Thread {

        @Override
        public void run() {
            while (isPlay) {
                // move all the fish in list
                for (Fish fish : list) {
                    Random r = new Random();
                    int direction = r.nextInt(4);
                    int speed = r.nextInt(20); // number of steps a fish will move
                    switch (direction) {
                        case 0: // go up
                            for (int i = 0; i < speed; i++) {
                                if (fish.y > 10) {
                                    fish.y -= 4;
                                }
                                getOption();
                            }
                            break;
                        case 1: // go right
                            for (int i = 0; i < speed; i++) {
                                if (fish.x < 1000) {
                                    fish.x += 4;
                                }
                                getOption();
                            }
                            break;
                        case 2: // go down
                            for (int i = 0; i < speed; i++) {
                                if (fish.y < 600) {
                                    fish.y += 4;
                                }
                                getOption();
                            }
                            break;
                        case 3: // go left
                            for (int i = 0; i < speed; i++) {
                                if (fish.x > 10) {
                                    fish.x -= 4;
                                }
                                getOption();
                            }
                            break;
                    } // end switch
                } // end for
                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
            } // end while
        }
    } // end of class GameControlThread

    mainPanel() {
        initComponents();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Double width = screenSize.getWidth();
        Double height = screenSize.getHeight();
        intWidth = width.intValue();
        intHeight = height.intValue();

        initGame();
        startThread();
    }

    private void getOption() {
        getGrade();
        if (isWin() || gameOver()) {
            isPlay = false;
            // ask user if they want to play again
            int answer = JOptionPane.showConfirmDialog(null, "Do you want to restart the game?", "Restart?", JOptionPane.YES_NO_OPTION);
            if (answer == JOptionPane.YES_OPTION) {
                initGame();
                t2 = new CountDownThread();
                t2.start();
            } else if (answer == JOptionPane.NO_OPTION) {
                JOptionPane.showMessageDialog(null, "See you again! Have a good day.", "Goodbye!", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
        }
        repaint();
        try {
            Thread.sleep(2);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }

    private void getGrade() {
        for (Fish fish : list) {
            if (shark.intersects(fish) && !fish.isBigger && fish.isAlive) {
                grade++;
                fish.isAlive = false;
            }
        }
    }

    private boolean isWin() {
        if (time > 0 && grade == numberOfSmall) {
            return true;
        }
        return false;
    }

    private boolean gameOver() {
        for (Fish fish : list) {
            if (shark.intersects(fish) && fish.isBigger) {
                return true;
            } else if (time <= 0 && grade < numberOfSmall) {
                return true;
            }
        }
        return false;
    }

    private void initGame() {
        shark = new Fish(sharkFile, false, true, 0, 0, 80, 80);
        list = new ArrayList<>();
        numberOfSmall = 3;
        for (int i = 0; i < numberOfFish - numberOfSmall; i++) {
            Random r = new Random();
            int dx = r.nextInt(intWidth);
            int dy = r.nextInt(intHeight);
            int w = 54;
            int h = 54;

            list.add(new Fish(bigFile, true, true, dx, dy, w, h));
        } // end for
        for (int i = 0; i < numberOfSmall; i++) {
            Random r = new Random();
            int dx = r.nextInt(intWidth);
            int dy = r.nextInt(intHeight);
            int w = 54;
            int h = 54;

            list.add(new Fish(smallFile, false, true, dx, dy, w, h));
        } // end for
        grade = 0;
        time = 60;
        isPlay = true;
    }

    private void startThread() {
        t = new GameControlThread();
        t.start();
        t2 = new CountDownThread();
        t2.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // To change body of generated methods, choose Tools | Templates


        // draw background
        ImageIcon icon = new ImageIcon(bgFile);
        Image img = icon.getImage();
        g.drawImage(img, 0, 0, intWidth, intHeight, null);

        // draw fishes
        shark.draw(g);
        for (Fish fish : list) {
            fish.draw(g);
        }

        // draw grade and time onto panel
        g.setColor(Color.orange);
        g.setFont(new Font("Arial", Font.BOLD, 35));
        g.drawString("Coin: " + grade + "/3", 10, 50);
        g.drawString("Time: " + time, 300, 50);

        // draw game result
        if (!isPlay && isWin()) {
            g.setFont(new Font("Arial", Font.BOLD, 80));
            g.setColor(Color.yellow);
            g.drawString("YOU WIN!", (intWidth / 3), 200);
        } else if (!isPlay && !isWin()) {
            g.setFont(new Font("Arial", Font.BOLD, 80));
            g.setColor(Color.darkGray);
            g.drawString("GAME OVER", (intWidth / 3), 200);
        }
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
