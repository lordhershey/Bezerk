import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * Write a description of class PopUpMine here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class PopUpMine extends PointUpActor
{
    /**
     * Act - do whatever the PopUpMine wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    
    private BackGround world = null;
    public static GreenfootImage images[] = new GreenfootImage[6];
    public static GreenfootImage blankImage;
    
    public static enum State {STANDING,
        WALKING,
        DYING,DEAD,NAN,WAITING,RISING,SINKING}
        
    public State playerState = State.NAN;
        
    public int senseRange = 60;
    public int exitRange = 100;
    
    int riseCounter = 0;
    long startTime = 0;
    
    PlayerActor player = null;
    int hitPoints = 5;
    int dyingcounter = 0;
    
    int fireCounterLimit = 30;
    int firecounter = 0;
    int bulletSpeed = 5;
    
    public PopUpMine()
    {
        if(null == images[0])
        {
            images[0] = new GreenfootImage("turret0.png");
            images[1] = new GreenfootImage("turret1.png");
            images[2] = new GreenfootImage("turret2.png");
            images[3] = new GreenfootImage("turret3.png");
            images[4] = new GreenfootImage("turret4.png");
            images[5] = new GreenfootImage("turret5.png");
            blankImage = new GreenfootImage(20,40);
        }
        
        playerState = State.NAN;
        hitPoints = 5;
        
        firecounter = fireCounterLimit;
    }
    
    public boolean isAlive()
    {
        if(State.WALKING == playerState  || 
            State.RISING == playerState || 
            State.SINKING == playerState)
        {
            return (true);
        }
        
        return(false);
    }
    
    public void takeABullet()
    {
        if(State.WALKING == playerState || 
            State.RISING == playerState || 
            State.SINKING == playerState)
        {
            hitPoints--;
            if(hitPoints > 0)
            {
                return;
            }
            //if we are alive then we are dead
            dyingcounter = 0;
            playerState = State.DYING;
            startTime = 0;
        }
    }
    
    public void act() 
    {
        // Add your action code here.
        if(State.NAN == playerState)
        {
            
            setImage(images[0]);
            playerState = State.WAITING;
            return;
        }
        
        if(State.WAITING == playerState)
        {
            if(null == player)
            {
                List<PlayerActor> playerlist = getObjectsInRange(senseRange,PlayerActor.class);
                for(PlayerActor Player : playerlist)
                {
                    player = Player;
                }
            }
            else
            {
                List<PlayerActor> playerlist = getObjectsInRange(exitRange,PlayerActor.class);
                for(PlayerActor Player : playerlist)
                {  
                    return;
                }
                //If no Players are there then we pop up
                playerState = State.RISING;
                riseCounter = 0;
                startTime = System.currentTimeMillis();
            }
            return;
        }
        
        if(State.RISING == playerState)
        {
            long endTime = System.currentTimeMillis();
            
            if(Math.abs(endTime - startTime) < 100)
            {
                return;
            }
            
            startTime=endTime;
            riseCounter++;
            
            if(riseCounter >= 5)
            {
                riseCounter = 5;
                playerState = State.WALKING;
            }
            setImage(images[riseCounter]);
            return;
        }
        
        if(State.WALKING == playerState)
        {
            //Check to see if the player is too close, if they are - hide
            List<PlayerActor> playerlist = getObjectsInRange(senseRange,PlayerActor.class);
            for(PlayerActor Player : playerlist)
            {
                playerState=State.SINKING;
                riseCounter = 5;
                startTime = System.currentTimeMillis();
                return;
            }
            
            turnTowards(player.getX(),player.getY());
            move(1);
            
            if(firecounter > 0)
            {
                firecounter--;
            }
            else
            {
                BulletActor bullet = new BulletActor();
                bullet.speed = bulletSpeed;
                bullet.setOwner((Actor)this);
                world.addObject(bullet,getX(),getY());
            
                bullet.turnTowards(player.getX(),player.getY());
                bullet.move(5);
                firecounter = fireCounterLimit;
            }
        }
        
        if(State.SINKING == playerState)
        {
            long endTime = System.currentTimeMillis();
            
            if(Math.abs(endTime - startTime) < 100)
            {
                return;
            }
            
            startTime=endTime;
            riseCounter--;
            
            if(riseCounter <= 0)
            {
                riseCounter = 0;
                playerState = State.WAITING;
            }
            setImage(images[riseCounter]);
            return;
        }
        
        if(State.DYING == playerState)
        {
            dyingcounter++;
            if(dyingcounter % 3 == 0)
            {
                setImage(images[riseCounter]);
            }
            else
            {
                SparksActor s = new SparksActor();
                s.setRotation(Greenfoot.getRandomNumber(90) + 45+180);
                s.speed = 5;
                world.addObject(s,
                    getX() + Greenfoot.getRandomNumber(21) - 10,
                    getY() + Greenfoot.getRandomNumber(21) - 10);
                setImage(blankImage);
            }
            if(dyingcounter > 60)
            {
                playerState = State.DEAD;
            }
            return;
        }
        
        if(State.DEAD == playerState)
        {
            world.removeObject(this);
            return;
        }
    }
    
    @Override
    public void addedToWorld(World world) {
        this.world = (BackGround) world;
        
    }
}
