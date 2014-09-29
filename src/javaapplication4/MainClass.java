/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package javaapplication4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

/**
 *
 * @author Kristoph
 */
public class MainClass extends JFrame implements KeyListener
{

    /**
     * @param args the command line arguments
     */
    private JPanel mainPanel;
    
    int BOARD_SIZE = 4;
    int MOVE_AMOUNT = BOARD_SIZE - 1;
    Brick[][] bricks = new Brick[BOARD_SIZE][BOARD_SIZE];
    
    int points = 0;
    JLabel pointsLabel;
    
    int OFFSET_FROM_CORNER = 20;
    
    int DOWN = 40;
    int UP = 38;
    int RIGHT = 39;
    int LEFT = 37;
    
    private boolean anyBrickHasMoved = false;
    
    public MainClass()
    {
        initComponents();
        mainPanel.setFocusable(true);
        mainPanel.addKeyListener(this);
        mainPanel.requestFocus();
        randomizeBoard();
    }
    private void initComponents()
    {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(450, 550));
        this.setVisible(true);
        
        mainPanel = new JPanel();
        GroupLayout mainPanelLayout = new GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanel.setSize(400, 500);
        mainPanel.setVisible(true);
        this.add(mainPanel);
        
        pointsLabel = new JLabel();
        pointsLabel.setText("Points: 0");
        pointsLabel.setLocation(0,450);
        pointsLabel.setSize(200,40);
        pointsLabel.setFont(new java.awt.Font("Tahoma", 0, 20));
        mainPanel.add(pointsLabel);
         
        pack();
    }
    
    /*
    KezListener Interface
    */
    @Override public void keyPressed(KeyEvent e){}
    @Override public void keyTyped(KeyEvent e){}
    
    @Override public void keyReleased(KeyEvent e)
    {
        handleKey(e); 
    }
    
    public void handleKey(KeyEvent e)
    {
        int key = e.getKeyCode();
        if(key > 36 && key < 41)
        {
            tryMoveBricks(e);
        }
        resetBricks();
        if(anyBrickHasMoved)
        {
            createRandomBrick();
            anyBrickHasMoved = false;
        }
    }
   public void tryMoveBricks(KeyEvent e)
    {
        int key = e.getKeyCode();
        for(int i = 0; i < BOARD_SIZE;i++)
        {
            for(int j = 0;j < BOARD_SIZE;j++)
            {
                Point oldPos = new Point(-1,-1);
                try 
                {
                    oldPos = getNextBrick(i,j,key);
                }
                catch(Exception exc )
                {
                    System.out.println("Direction went wrong"); 
                }
                        
                Brick brick = bricks[oldPos.x][oldPos.y];
                if(brick != null && brick.wasMoved == false)
                {
                    boolean canAdd = false;
                    brick.wasMoved = true;
                    
                    Point direction = getDirection(key);
                    Point newPos = getNewPos(direction, oldPos, brick);
                    
                    Point newLoc = new Point(newPos.x * 100 + OFFSET_FROM_CORNER,newPos.y *100 + OFFSET_FROM_CORNER); 
                    brick.setLocation(newLoc);
                    bricks[oldPos.x][oldPos.y] = null;
                    bricks[newPos.x][newPos.y] = brick;     
                    
                    if((oldPos.x == newPos.x && oldPos.y == newPos.y) == false)
                    {
                        anyBrickHasMoved = true;
                    }
                }
            }
        }
    }
   
    private Point getDirection(int key)
    {
        if(key == DOWN)
        {
            return new Point(0,1);  
        }
        else if(key == RIGHT)
        {
            return new Point(1,0);  
        }
        else if(key == UP)
        {   
            return new Point(0,-1);  
        }
        else if(key == LEFT)
        {
            return new Point(-1,0);  
        }
        else
        {
            return null;
        }
    }
   
    public Point getNextBrick(int i,int j,int direction) throws Exception
    {
        Point nextBrick = new Point(-1,-1);
        if(direction == UP || direction == LEFT)
        {
            return new Point(i,j);
        }
        else if(direction == DOWN)
        {
            return new Point(i,3 - j);
        }
        else if(direction == RIGHT)
        {
            return new Point(3 - i,j);
        }
        else
        {
            throw new Exception("NOT A VALID DIRECTION!");
        }
    }
   
    private Point getNewPos(Point direction,Point oldPos, Brick brick)
    {
        Point newPos = new Point(oldPos.x,oldPos.y);
        
        while(checkIfCanMoveTo(oldPos, new Point(newPos.x + direction.x,newPos.y + direction.y)) == true)
        {
            newPos.x += direction.x;
            newPos.y += direction.y;
        }
        Brick targetBrick = bricks[newPos.x][newPos.y];
        if(targetBrick != null && targetBrick != brick)
        {
            if(canAddBricks(brick, targetBrick))
            {
                addBricks(oldPos, newPos);
            }
            else
            {
                newPos.x -= direction.x;
                newPos.y -= direction.y;
            }
        }
        return newPos;
        
    }
 
    public boolean checkIfCanMoveTo(Point oldPos, Point newPos)
    {
        try
        {
            if(bricks[newPos.x][newPos.y] == null)
            {
                return true;
            }
        }
        catch(ArrayIndexOutOfBoundsException exc)
        {
            return false;
        }
        
        if(bricks[oldPos.x][oldPos.y].getValue() == bricks[newPos.x][newPos.y].getValue())
        {
            return true;
        }
        else if(oldPos.x == newPos.x && oldPos.y == newPos.y)
        {
            return true;
        }
        return false;
    }
    
    public boolean canAddBricks(Brick brick1,Brick brick2)
    {
        return brick1.getValue() == brick2.getValue();
    }
    
    public void addBricks(Point oldPos, Point newPos)
    {
        bricks[oldPos.x][oldPos.y].doubleValue();
        mainPanel.remove(bricks[newPos.x][newPos.y]);
        mainPanel.revalidate(); 
        
        addPoints(bricks[oldPos.x][oldPos.y].getValue());
    }
    
    public void addPoints(int value)
    {
        this.points += value;
        this.pointsLabel.setText("Points: "+Integer.toString(this.points));
    }
    
    public void resetBricks()
    {
        for(Brick[] column : bricks)
        {
            for(Brick brick : column) 
            {
                if(brick != null)
                {
                    brick.wasMoved = false;
                }
            }
        }
    }
    
    private void randomizeBoard()
    {
        createRandomBrick();
        createRandomBrick();
    }
    
    public void createRandomBrick()
    {
        Random generator = new Random();
        
        int x;
        int y; 
        do
        {
            x = generator.nextInt(BOARD_SIZE);
            y = generator.nextInt(BOARD_SIZE);
        }while(bricks[x][y] != null); 
        
        int value = randomizeValue(generator);
        addNewBrick(x,y, value);    
    }
    
    public int randomizeValue(Random generator)
    {
        int valueTest = generator.nextInt(100);
        return valueTest > 90 ? 4 : 2;
    }
    
    public void addNewBrick(int x,int y,int value)
    {
        Brick brick = new Brick();
        brick.setLocation(x * 100 + OFFSET_FROM_CORNER,y*100 + OFFSET_FROM_CORNER);
        brick.setValue(value);
        bricks[x][y] = brick;
        mainPanel.add(brick);
        repaint();
        //System.out.println(new Point(x,y));
                
    }
    
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable()
        {
            public void run() {
                MainClass mainClass = new MainClass(); 
            }
        });
    }
    
}
