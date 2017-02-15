import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * Write a description of class BulletActor here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class BulletActor extends Actor
{
    
    private BackGround world = null;
    
    public Actor owner = null;
    /*
    public BulletActor(int rotation)
    {
        setRotation(rotation);
    }
    */
   
    public int speed = 7;
    
    public BulletActor()
    {
        super();
        speed = 7;
    }
    
    public void setOwner(Actor owner)
    {
        this.owner = owner;
    }
    
    public void explodeFireball()
    {
        int limit = Greenfoot.getRandomNumber(15) + 11;
        for(int i = 0 ; i < 25 ; i++)
        {
                
            BulletFireActor bfa = new BulletFireActor();
            bfa.counter = 0;
            world.addObject(bfa,getX() + Greenfoot.getRandomNumber(7) - 3,
            getY()+ Greenfoot.getRandomNumber(7) - 3);
            
            bfa.setRotation(getRotation() + (Greenfoot.getRandomNumber(31) - 15) * 5);
            bfa.speed = Greenfoot.getRandomNumber(5) - 6;
            bfa.move(3);
       
        }
        world.removeObject(this);
        return;
    }
    
    /**
     * Act - do whatever the BulletActor wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        int i;
        int x = getX();
        int y = getY();
        BulletFireActor bfa;
        
        if(world.isPaused())
        {
            return;
        }
        
        MazeActor wall = (MazeActor)getOneIntersectingObject(MazeActor.class);
        if(null != wall)
        {
            explodeFireball();
            return;
        }
        
        BulletActor bullet = (BulletActor)getOneIntersectingObject(BulletActor.class);
        if(null != bullet)
        {
            explodeFireball();
            bullet.explodeFireball();
            return;
        }
        
        RobotActor robby = (RobotActor)getOneIntersectingObject(RobotActor.class);
        if((null != robby) && (owner != robby) && (robby.isAlive()))
        {
            robby.takeABullet();
            explodeFireball();
            return;
        }
        
        PlayerActor player = (PlayerActor)getOneIntersectingObject(PlayerActor.class);
        if((null != player) && (owner != player))
        {
            player.takeABullet();
            explodeFireball();
            return;
        }
        
        PopUpMine mine = (PopUpMine)getOneIntersectingObject(PopUpMine.class);
        if((null != mine) && (owner != mine) && (mine.isAlive()))
        {
            mine.takeABullet();
            explodeFireball();
            return;
        }
        /*
        List<MazeActor> wallchunks = world.getObjects(MazeActor.class);
        
        if(null != wallchunks)
        {
            for(MazeActor wall : wallchunks)
            {
                
            }
        }
        */
        
        bfa = new BulletFireActor();
        world.addObject(bfa,getX() + Greenfoot.getRandomNumber(7) - 3,
            getY()+ Greenfoot.getRandomNumber(7) - 3);
            
        bfa.setRotation(getRotation() + (Greenfoot.getRandomNumber(31) - 15) * 2);
        bfa.move(-1);
       
        bfa = new BulletFireActor();
        world.addObject(bfa,getX() + Greenfoot.getRandomNumber(5) - 2,
            getY()+ Greenfoot.getRandomNumber(5) - 2);
            
        bfa.setRotation(getRotation() + (Greenfoot.getRandomNumber(31) - 15) * 2);
        bfa.move(-1);
        
        move(speed);
        
        // Add your action code here.
        if(getX() < 0)
        {
            world.removeObject(this);
            return;
        }
                
        if(getX() >= world.getWidth())
        {
            world.removeObject(this);
            return;
        }
                
        if(getY() < 0)
        {
            world.removeObject(this);
            return;
        }
               
        if(getY() >= world.getHeight())
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
