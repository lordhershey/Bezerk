import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * Write a description of class BloodHoundActor here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class BloodHoundActor extends RobotActor
{
    /**
     * Act - do whatever the BloodHoundActor wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    
    PlayerActor player = null;
    
    /*Use this counter to lock our movement for a little bit*/
    int dirCounter = 0;
    int rot = 0;
    
    public BloodHoundActor()
    {
        super();
        PlayerActor Player = null;
    }
    
    @Override
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
            dirCounter = 0;
            return;
        }
        
        if(State.DYING == playerState)
        {
            int index;
            
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
        
        /*
        BulletFireActor bfa = new BulletFireActor();
        bfa.counter = 0;
        world.addObject(bfa,getX() + Greenfoot.getRandomNumber(7) - 3,
        getY()+ Greenfoot.getRandomNumber(7) - 3);
            
        bfa.setRotation(90 + (Greenfoot.getRandomNumber(31) - 15) * 5);
        bfa.speed = Greenfoot.getRandomNumber(5) - 6;
        bfa.move(3);
        */
       
        if(State.IDLE == playerState)
        {
            idlecounter = (idlecounter + 1) % 6;
            setImage(idle[idlecounter]);
        }
        
        if(firecounter > 0)
        {
            firecounter--;
        }
        
        if(null == player)
        {
            List<PlayerActor> playerlist = getObjectsInRange(senseRange,PlayerActor.class);
            for(PlayerActor Player : playerlist)
            {
                player = Player;
            }
        }
        
        if((null != player) && (firecounter < 1)  && (State.IDLE == playerState))
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
            
                //Set Player Direction
                if(dirCounter < 1)
                {
                    //We are going to walk.
                    turnTowards(player.getX(),player.getY());
                    rot = (getRotation()+45)/90 * 90;
                    dirCounter = 40;
                    
                    playerState = State.WALKING;
                    walkcounter = 0;
                }
                else
                {
                    dirCounter--;
                }
                //System.out.println("Set Rotation " + rot);
                
                //setRotation(rot);
                
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
        
        if(State.WALKING == playerState)
        {
            walkcounter = (walkcounter + 1) % walkcounterlimit;
            move(2);
         
            try
            {
            MazeActor wall = (MazeActor)getOneIntersectingObject(MazeActor.class);
            if(null != wall)
            {
                //takeABullet();
                //Back the Robot up a bit and change its direction
                move(-4);
                dirCounter = 40;
                if(rot == 0 || rot == 180)
                {
                    if(player.getY() >= getY())
                    {
                        rot = 90;
                        walk = down;
                        walkcounterlimit = 4;
                    }
                    else
                    {
                        rot = 270;
                        walk = up;
                        walkcounterlimit = 4;
                    }
                    
                }
                else
                {
                    if(player.getX() >= getX())
                    {
                        rot = 0;
                        walk = right;
                        walkcounterlimit = 5;
                    }
                    else
                    {
                        rot = 180;
                        walk = left;
                        walkcounterlimit = 5;
                    }
                    
                }
                setRotation(rot);
                return;
                
            }
            }
            catch (Exception e)
            {
                System.out.println("Wwhooooaaaaa!!! I broke something.");
                e.printStackTrace();
            }
            
            setImage(walk[walkcounter]);
            
           
            if((player.getX() >= (getX() - 16)) && (player.getX() <= (getX() + 16)))
            {
                playerState = State.IDLE;
                
                dirCounter = 0;
                return;
            }
            if((player.getY() >= (getY() - 16)) && (player.getY() <= (getY() + 16)))
            {
                playerState = State.IDLE;
                
                dirCounter = 0;
                return;
            }
                
            
            
        }
    }    
}
