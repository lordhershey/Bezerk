import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class SparksActor here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class SparksActor extends Actor
{
    private BackGround world = null;
  
    public static GreenfootImage images[] = new GreenfootImage[16];
    int counter = 0;
    public int speed = 0;
    
    public SparksActor()
    {
        if(null == images[0])
        {
            for(int i = 0 ; i < 16 ; i++)
            {
                int j = i + 1;
                images [i] = new GreenfootImage("fire-" + j + ".png");
            }
        }
        setImage(images[0]);
    }
    /**
     * Act - do whatever the SparksActor wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        // Add your action code here.
        int index = counter;
        
        if(index > 15)
        {
            index = 15;
        }
        
        setImage(images[index]);
        
        setRotation(getRotation() + Greenfoot.getRandomNumber(11)-5);
        
        move(speed);
        
        counter++;
        
        if(counter > 16)
        {
            world.removeObject(this);
        }
    }    
    
    @Override
    public void addedToWorld(World world) {
        this.world = (BackGround) world;
        
    }
}
