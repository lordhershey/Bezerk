import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
/**
 * Write a description of class DoorActor here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class DoorActor extends Actor
{
    public static GreenfootImage[] image = null; 
    public static enum State {NAN,DOWN,RISING,UP,SINKING}
    State playerState = State.NAN;
    
    int basex = 0;
    int basey = 0;
    int counter = 0;
    int risecounter = 0;
    
    Vector<FloorSwitchActor> doors = new Vector<FloorSwitchActor>();
    
    static
    {
        image = new GreenfootImage[4];
        image[0] = new GreenfootImage("door1.png");
        image[1] = new GreenfootImage("door2.png");
        image[2] = new GreenfootImage("door3.png");
        image[3] = new GreenfootImage("door4.png");
    }
    
    public void addSwitch(FloorSwitchActor doorswitch)
    {
        doors.add(doorswitch);
    }
    
    public boolean allDoorsActive()
    {
        
        for(FloorSwitchActor d : doors)
        {
            if(!d.activated)
            {
                return false;
            }
        }
        return true;
    }
    
    public boolean allDoorsInactive()
    {
        
        for(FloorSwitchActor d : doors)
        {
            if(d.activated)
            {
                return false;
            }
        }
        return true;
    }
    
    public DoorActor()
    {
        playerState = State.NAN;
    }
    
    /**
     * Act - do whatever the DoorActor wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        counter = (counter + 1) % 4;
        setImage(image[counter]);
        
        if(State.NAN == playerState)
        {
            basex = getX();
            basey = getY();
            playerState = State.DOWN;
            return;
        }
        
        if(State.DOWN == playerState)
        {
            if(allDoorsActive())
            {
                playerState = State.RISING;
                risecounter = 0;
                return;
            }
        }
        
        if(State.RISING == playerState)
        {
            risecounter++;
            if(risecounter > 52)
            {
                playerState = State.UP;
                return;
            }
            setLocation(basex,basey-risecounter);
        }
        
        if(State.UP == playerState)
        {
            if(allDoorsInactive())
            {
                playerState = State.SINKING;
                return;
            }
            else
            {
                List<PlayerActor> players = getObjectsInRange(10,PlayerActor.class);
                if(null != players && players.size() > 0)
                {
                    //Go to the end screen.
                    Greenfoot.setWorld(new TemporaryEndScreen());
                }
            }
        }
        
        if(State.SINKING == playerState)
        {
            risecounter--;
            if(risecounter < 0)
            {
                playerState = State.DOWN;
                return;
            }
            setLocation(basex,basey-risecounter);
        }
    }    
}
