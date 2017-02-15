import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * Write a description of class FloorSwitchActor here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class FloorSwitchActor extends Actor
{
    public static GreenfootImage image[] = new GreenfootImage[3];
    private KeyPiece key = null;
    int counter = 0;
    long startTime = 0;
    public boolean activated = false; 

    static
    {
        image[0] = new GreenfootImage("FootButton1.png");
        image[1] = new GreenfootImage("FootButton2.png");
        image[2] = new GreenfootImage("FootButton3.png");
    }

    public FloorSwitchActor()
    {
        key = null;
        setImage(image[0]);
        counter = 0;
        activated = false;
    }

    public void setKey(KeyPiece key)
    {
        if(null == key)
        {
            System.out.println("I Got a Null Setting.");
        }
        this.key = key;
    }

    /**
     * Act - do whatever the FloorSwitchActor wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        long endTime = System.currentTimeMillis();

        if(activated && ((endTime - startTime) < 300))
        {
            if(counter > 24 && counter < 29)
            {
                setImage(image[Greenfoot.getRandomNumber(2) + 1]);
            }
            return;
        }

        startTime = endTime;

        if(counter > 2)
        {

            counter++;

            if(counter > 30)
            {
                counter = 0;
                setImage(image[0]);
                activated = false;
            }
            else if(counter > 28)
            {
                setImage(image[1]);
            }
            return;
        }

        if(0 == counter && !activated)
        {
            // Add your action code here.
            PlayerActor player = (PlayerActor)getOneIntersectingObject(PlayerActor.class);

            if(null != player)
            {
                //System.out.println("See Player");
                Vector<KeyPiece> keys = player.getKeys();
                //System.out.println("Player has " + keys.size() + " keys");
                Enumeration keysEnum = keys.elements();
                while(keysEnum.hasMoreElements())
                {
                    KeyPiece kp = (KeyPiece)keysEnum.nextElement();
                    //System.out.println(kp.hashCode());
                    if(this.key == kp)
                    {
                        //System.out.println("You Have a Key I want " + key.hashCode());
                        activated = true;
                        startTime = System.currentTimeMillis();
                    }
                }
            }
            return;
        }

        setImage(image[counter]);
        counter++;
    }    
}
