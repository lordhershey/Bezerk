import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.lang.Math;

/**
 * Write a description of class PointUp here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public abstract class PointUpActor extends Actor
{
    
    private int rotation = 0;
    /**
     * Act - do whatever the PointUp wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public abstract void act();    
    
    @Override 
    public void setRotation(int rotation)
    {
        this.rotation = rotation;
    }
    
    @Override
    public int getRotation()
    {
        return this.rotation;
    }
    
    @Override
    public void move(int distance)
    {
        super.setRotation(this.rotation);
        super.move(distance);
        super.setRotation(0);
    }
    
    @Override
    public void turnTowards(int x,int y)
    {
        //This routine does not act as desired super turn towards does not set rotation
        //right away you will have to do some arc tan stuff or something
        //System.out.println("Turn Towards " + x + " " + y);
        //super.turnTowards(x,y);
        //this.rotation = super.getRotation();
        double deltaX = x - getX();
        double deltaY = y - getY();
        double degrees = Math.atan2(deltaY,deltaX) * 180.0 / Math.PI; 
        this.rotation = (int)degrees;
        
        while(this.rotation < 0)
        {
            this.rotation+=360;
        }
        this.rotation = this.rotation % 360;
        //System.out.println("Degrees " + this.rotation);
        super.setRotation(0);
    }
    
    @Override
    public void turn(int amount)
    {
        rotation += amount;
        
        while(rotation < 0)
        {
            rotation += 360;
        }
        
        if (rotation >= 360)
        {
            rotation = rotation % 360;
        }
    }
}
