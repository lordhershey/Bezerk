import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class ClickToResumeActor here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ClickToResumeActor extends Actor
{
    long startTime = 0;
    int counter = 0;
    /**
     * Act - do whatever the ClickToResumeActor wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        
        long endTime = System.currentTimeMillis();
        if((endTime - startTime) < 410)
        {
            return;
        }
        startTime = endTime;
        counter = (counter + 1)%2;
        setRotation(counter * -1);
    }    
}
