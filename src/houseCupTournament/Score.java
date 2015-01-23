/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package houseCupTournament;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import static javafx.scene.text.Font.font;
import static javafx.scene.text.Font.font;

/**
 *
 * @author Shayla
 */
public class Score {
    

    public void draw(Graphics graphics) {
     graphics.setColor(Color.WHITE);
     graphics.setFont(font);
        graphics.drawString("Score:" + value, position.x, position.y);
    }

    //<editor-fold defaultstate="collapsed" desc="GettersAndSetters">
    /**
     * @return the value
     */
    private Point position;
    private int value = 0;
    private Font font = new Font("Calibri", Font.PLAIN, 48);
   
    public int getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(int value) {
        this.value = value;
    }

    //the amount to add to the currentvalue
    public void addToValue(int value) {
        this.value += value;
    }


    /**
     * @return the position
     */
    public Point getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(Point position) {
        this.position = position;
    }

    /**
     * @return the position
     */
   //</editor-fold>
}
