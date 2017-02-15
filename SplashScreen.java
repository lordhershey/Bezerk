import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * Write a description of class SplashScreen here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class SplashScreen extends World
{

    /**
     * Constructor for objects of class SplashScreen.
     * 
     */
    public SplashScreen()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(600, 400, 1); 
        prepare();
        Greenfoot.start();
    }

    @Override
    public void act()
    {
        /*
        try
        {
        MouseInfo mi = Greenfoot.getMouseInfo();
        if(mi.getClickCount() > 0)
        {
        Greenfoot.setWorld(new Background());
        }
        }
        catch (Exception e)
        {
        }
         */
        if (Greenfoot.mouseClicked(null))  
        {  
            MouseInfo mouse = Greenfoot.getMouseInfo();
            List l = getObjectsAt(mouse.getX(),mouse.getY(),ClickToResumeActor.class);
            boolean restore = false;
            if(null != l)
            {
                for(Object o : l)
                {
                    System.out.println("Restore The Game");
                    restore = true;
                }
            }
            BackGround bg = new BackGround(restore);
           
            Greenfoot.setWorld(bg);
        }
    }

    /**
     * Prepare the world for the start of the program. That is: create the initial
     * objects and add them to the world.
     */
    private void prepare()
    {
        ClickToStartActor clicktostartactor = new ClickToStartActor();
        addObject(clicktostartactor, 295, 265);

        if(GameSaveManager.hasSaveGame())
        {
            ClickToResumeActor resume = new ClickToResumeActor();
            addObject(resume, 495, 329);
        }
    }
}
