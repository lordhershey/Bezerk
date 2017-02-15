import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class SavingGameActor here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class SavingGameActor extends Actor
{
    
    long startTime = 0;
    
    public void act() 
    {
        if(startTime < 1)
        {
            startTime = System.currentTimeMillis();
            return;
        }
        
        long endTime = System.currentTimeMillis();
        if((endTime - startTime) < 300)
        {
            return;
        }
        
        getWorld().removeObject(this);
    }    
}
