import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class BulletFireActor here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class BulletFireActor extends Actor
{
    public static GreenfootImage guy[] = new GreenfootImage[4];
    
    public int counter = 0;
    
    private BackGround world = null;
    public int speed = 0;
    
    public BulletFireActor()
    {
        int i;
        /*
        for(i = 2 ; i < 10 ;i++)
        {
            guy[(i-2)] = new GreenfootImage("fire-0" + i + ".png");
        }
        
        for(i = 10 ; i < 17 ;i++)
        {
            guy[(i-2)] = new GreenfootImage("fire-" + i + ".png");
        }
        */
        if(null == guy[0])
        {
            guy[0] = new GreenfootImage("bullet-01.png");
            guy[1] = new GreenfootImage("bullet-02.png");
            guy[2] = new GreenfootImage("bullet-03.png");
            guy[3] = new GreenfootImage("bullet-04.png");
        }
        counter = Greenfoot.getRandomNumber(3);
        setImage(guy[counter]);
        
        /*start effect with a smaller fireball*/
        counter = 1;
        speed = 0;
    }
    /**
     * Act - do whatever the BulletFireActor wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        if(world.isPaused())
        {
            return;
        }
        // Add your action code here.
        if(speed != 0)
        {
            move(speed);
        }
        
        //move(-1 * (Greenfoot.getRandomNumber(5)+2));
        if(counter >= 4)
        {
            world.removeObject(this);
            return;
        }
        setImage(guy[counter]);
        counter++;
    }    
    
    @Override
    public void addedToWorld(World world) {
        this.world = (BackGround) world;
        
    }
}
