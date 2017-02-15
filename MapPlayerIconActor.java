import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class MapPlayerIconActor here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MapPlayerIconActor extends Actor
{
    public static GreenfootImage images[] = new GreenfootImage[2];
    
    int counter = 0;
    
    public MapPlayerIconActor()
    {
        if(null == images[0])
        {
            images[0] = new GreenfootImage("bullet-04.png");
            images[1] = new GreenfootImage("bullet-03.png");
        }
    }
    /**
     * Act - do whatever the MapPlayerIconActor wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        // Add your action code here.
        counter = (counter + 1) % 2;
        setImage(images[counter]);
        
    }    
}
