import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class TemporaryEndScreen here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class TemporaryEndScreen extends World
{

    long startTime = 0;
    int counter = 0;

    /**
     * Constructor for objects of class TemporaryEndScreen.
     * 
     */
    public TemporaryEndScreen()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(600, 400, 1); 
        counter = 0;
        prepare();
    }

    @Override
    public void act()
    {
        long endTime = System.currentTimeMillis();
        if((endTime - startTime) < 500)
        {
            return;
        }
        startTime = endTime;
        counter++;

        if(counter > 30)
        {
            Greenfoot.setWorld(new SplashScreen());
        }
    }

    /**
     * Prepare the world for the start of the program. That is: create the initial
     * objects and add them to the world.
     */
    private void prepare()
    {
        PlayerHasEscaped playerhasescaped = new PlayerHasEscaped();
        addObject(playerhasescaped, 313, 330);
    }
}
