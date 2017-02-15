import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
import java.lang.Math;

/**
 * Write a description of class RobotActor here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class RobotActor extends PointUpActor
{
    public static enum State {IDLE,WALKING,DYING,DEAD,NAN}
    
    public State playerState = State.NAN;
    
    public static GreenfootImage idle[] = new GreenfootImage[6];
    public static GreenfootImage frying[] = new GreenfootImage[31];
    public static GreenfootImage left[] = new GreenfootImage[5];
    public static GreenfootImage right[] = new GreenfootImage[5];
    public static GreenfootImage up[] = new GreenfootImage[5];
    public static GreenfootImage down[] = new GreenfootImage[5];
    public GreenfootImage walk[] = new GreenfootImage[5];
    int idlecounter = 0;
    int dyingcounter = 0;
    int firecounter = 80;
    
    int walkcounter = 0;
    int walkcounterlimit = 4;
    
    public World world = null;
    
    public int senseRange = 120;
    public int fireCounterLimit = 60;
    public int bulletSpeed = 7;
    
    long startTime = 0;
    
    public RobotActor()
    {
        int i;
        if(null == idle[0])
        {
            for ( i = 0 ; i < 6 ; i++)
            {
                idle[i] = new GreenfootImage("idlerobot0" + (i+1) + ".png");
            }
            
            for(i = 0 ; i < 31 ; i++)
            {
                if(i < 9)
                {
                    frying[i] = new GreenfootImage("robotexplode0" + (i+1) + ".png");
                }
                else
                {
                    frying[i] = new GreenfootImage("robotexplode" + (i+1) + ".png");
                }
            }
            
            //left and right images
            for(i= 0 ; i < 5 ; i++)
            {
                left[i] = new GreenfootImage("leftrobot0" + (i+1) + ".png");
                right[i] = new GreenfootImage("rightrobot0" + (i+1) + ".png");
            }
            
            //north and south
            for(i= 0 ; i < 4 ; i++)
            {
                up[i] = new GreenfootImage("robotnorth0" + (i+1) + ".png");
                down[i] = new GreenfootImage("robotsouth0" + (i+1) + ".png");
            }
            up[4] = null;
            down[4] = null;
        }
        playerState = State.NAN;
        setImage(idle[0]);
        firecounter = 80;
        senseRange = 120;
        fireCounterLimit = 60;
        bulletSpeed = 7;
    }
    
    public boolean isAlive()
    {
        if(State.IDLE == playerState || State.WALKING == playerState)
        {
            return (true);
        }
        
        return(false);
    }
    
    public void takeABullet()
    {
        if(State.IDLE == playerState || State.WALKING == playerState)
        {
            //if we are alive then we are dead
            dyingcounter = 0;
            playerState = State.DYING;
            startTime = 0;
        }
    }
    
    
    /**
     * Act - do whatever the RobotActor wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        // Add your action code here.
        
        if(((BackGround)world).isPaused())
        {
            return;
        }
        
        if(State.NAN == playerState)
        {
            setImage(idle[0]);
            playerState = State.IDLE;
            return;
        }
        
        if(State.DYING == playerState)
        {
            int index;
            long endTime = 0;
            
            if(startTime < 1)
            {
                startTime = System.currentTimeMillis();
            }
            else
            {
                endTime = System.currentTimeMillis();
                if(Math.abs(endTime - startTime) < 40)
                {
                    return;
                }
                startTime = endTime;
                
            }
            
            index = dyingcounter;
            
            if (index >= 30)
            {
                index = 30;
            }
            setImage(frying[index]);
            
            dyingcounter++;
            
            if(dyingcounter > 31)
            {
                playerState = State.DEAD;
                world.removeObject(this);
            }
            
            return;
        }
        
        if(State.IDLE == playerState)
        {
            idlecounter = (idlecounter + 1) % 6;
            setImage(idle[idlecounter]);
        }
        
        if(firecounter > 0)
        {
            firecounter--;
        }
        
        List<PlayerActor> playerlist = getObjectsInRange(senseRange,PlayerActor.class);
        if((null != playerlist) && (firecounter < 1)  && (State.IDLE == playerState))
        {
            for(PlayerActor player : playerlist)
            {
                
                if((player.getX() >= (getX() - 16)) && (player.getX() <= (getX() + 16)))
                {
                    if (player.getY() > getY())
                    {
                        BulletActor bullet = new BulletActor();
                        bullet.speed = bulletSpeed;
                        bullet.setOwner((Actor)this);
                        world.addObject(bullet,getX(),getY());
            
                        bullet.setRotation(90);
                        bullet.move(5);
                        firecounter = fireCounterLimit;
                        return;
                    }
                    else
                    {
                        BulletActor bullet = new BulletActor();
                        bullet.speed = bulletSpeed;
                        bullet.setOwner((Actor)this);
                        world.addObject(bullet,getX(),getY());
            
                        bullet.setRotation(270);
                        bullet.move(5);
                        firecounter = fireCounterLimit;
                        return;
                    }
                }
                else if((player.getY() >= (getY() - 16)) && (player.getY() <= (getY() + 16)))
                {
                     if (player.getX() > getX())
                    {
                        BulletActor bullet = new BulletActor();
                        bullet.setOwner((Actor)this);
                        bullet.speed = bulletSpeed;
                        world.addObject(bullet,getX(),getY());
            
                        bullet.setRotation(0);
                        bullet.move(5);
                        firecounter = fireCounterLimit;
                        return;
                    }
                    else
                    {
                        BulletActor bullet = new BulletActor();
                        bullet.setOwner((Actor)this);
                        bullet.speed = bulletSpeed;
                        world.addObject(bullet,getX(),getY());
            
                        bullet.setRotation(180);
                        bullet.move(5);
                        firecounter = fireCounterLimit;
                        return;
                    }
                }
              
                //We are going to walk.
                turnTowards(player.getX(),player.getY());
                int rot = (getRotation()+45)/90 * 90;
                
                //System.out.println("Set Rotation " + rot);
                
                //setRotation(rot);
                
                playerState = State.WALKING;
                walkcounter = 0;
               
                switch(rot)
                {
                    case 270:
                    case 90:
                    if(player.getX() > getX())
                    {
                       rot = 0;
                    }
                    else
                    {
                        rot = 180;
                    }
                    break;
                     
                    case 180:
                    case 0:
                    default:
                    if(player.getY() > getY())
                    {
                        rot = 90;
                    }
                    else
                    {
                        rot = 270;
                    }
                    break;
                }
                
                setRotation(rot);
                
                switch(rot)
                {
                    case 90:
                    walkcounterlimit = 4;
                    walk = down;
                    break;
                        
                    case 270:
                    walkcounterlimit = 4;
                    walk = up;
                    break;
                        
                    case 180:
                    walkcounterlimit = 5;
                    walk = left;
                    break;
                        
                    case 0:
                    default:
                    walkcounterlimit = 5;
                    walk = right;
                    break;
                }
                
               
               
                return;
            }
        }
        
        if(State.WALKING == playerState)
        {
            walkcounter = (walkcounter + 1) % walkcounterlimit;
            move(2);
         
            MazeActor wall = (MazeActor)getOneIntersectingObject(MazeActor.class);
            if(null != wall)
            {
                takeABullet();
                return;
            }
            
            setImage(walk[walkcounter]);
            
            List<PlayerActor> playerlist2 = getObjectsInRange(senseRange,PlayerActor.class);
            if(null == playerlist2 || playerlist2.size() < 1)
            {
                playerState = State.IDLE;
                return;
            }
            else
            {
                for(PlayerActor player : playerlist2)
                {
                    if((player.getX() >= (getX() - 16)) && (player.getX() <= (getX() + 16)))
                    {
                        playerState = State.IDLE;
                        return;
                    }
                    if((player.getY() >= (getY() - 16)) && (player.getY() <= (getY() + 16)))
                    {
                        playerState = State.IDLE;
                        return;
                    }
                }
            }
        }
    }    
    
    @Override
    public void addedToWorld(World world) {
        this.world = world;
        
    }
}
