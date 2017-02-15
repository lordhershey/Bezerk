import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class PlayerHasEscaped here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class PlayerHasEscaped extends Actor
{
    public static GreenfootImage[] image = null;
    long startTime = 0;
    int counter = 0;
    static
    {
        image = new GreenfootImage[2];
        image[0] = new GreenfootImage("PlayerHasEscaped1.png");
        image[1] = new GreenfootImage("PlayerHasEscaped2.png");
    }
    
    public PlayerHasEscaped()
    {
        startTime = 0;
        counter = 0;
    }
    
    /**
     * Act - do whatever the ClickToStartActor wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        // Add your action code here.
        long endTime = System.currentTimeMillis();
        if((endTime - startTime) < 200)
        {
            return;
        }
        startTime = endTime;
        counter = Greenfoot.getRandomNumber(2);
        
        setImage(image[counter]);
    }    
}
