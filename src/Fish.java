
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author "ArbreD"
 */
class Fish extends Rectangle {

    String filename;
    boolean isBigger;
    boolean isAlive;

    Fish(String filename, boolean isBigger, boolean isAlive, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.filename = filename;
        this.isBigger = isBigger;
        this.isAlive = isAlive;
    }

    void draw(Graphics g) {
        if (isAlive) {
            ImageIcon icon = new ImageIcon(filename);
            Image img = icon.getImage();
            img = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            icon = new ImageIcon(img);
            img = icon.getImage();
            g.setColor(Color.blue);
            g.drawRect(x, y, width, height);
            g.drawImage(img, x, y, width, height, null);
        }
    }
}
