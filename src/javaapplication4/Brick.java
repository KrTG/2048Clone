package javaapplication4;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.Color;
import javax.swing.JLabel;

/**
 *
 * @author Kristoph
 */
public class Brick extends JLabel
{
    private int value;
    public boolean wasMoved = false; 
    public Brick()
    {
        super();
        this.setBackground(new java.awt.Color(255, 0, 51));
        this.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        this.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        this.setText("2");
        this.setSize(80,80);
        this.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));  
    }
    
    public void setValue(int value)
    {
        this.value = value;
        String text = Integer.toString(value);
        this.setText(text);
        this.changeColour();
    }
    
    public void doubleValue()
    {
        int old = this.value;
        setValue(old*2);
    }
    
    public void changeColour()
    {
        Color color = Color.blue;
        switch(this.value)
        {
            case 2 : color = Color.blue;
                break;
            case 4 : color = Color.red;
                break;
            case 8 : color = Color.GREEN;
                break;
            case 16 : color = Color.ORANGE;
                break;
            case 32 : color = Color.MAGENTA;
                break;
            case 64 : color = Color.cyan;
                break;
            case 128 : color = Color.PINK;
                break;
            case 256 : color = Color.LIGHT_GRAY;
                break;
            case 512 : color = Color.GRAY;
                break;
            case 1024 : color = Color.DARK_GRAY;
                break;
            case 2048 : color = Color.BLACK;
                break;
            default :
                System.out.println("Stuff went wrong");   
        }
        this.setForeground(color);
    }
    
    
    public int getValue()
    {
        return value;
    }
}
