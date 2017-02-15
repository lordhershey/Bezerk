import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class KeyPiece here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class KeyPiece extends Actor
{
    World world = null;

    //public int keynumber = 0;
    public MazeRoom backref = null;

    public int keyId = -1;

    public KeyPiece()
    {
        //keynumber = Greenfoot.getRandomNumber(10);
    }

    public KeyPiece(int keyId)
    {
        this.keyId = keyId;
    }

    /**
     * Act - do whatever the KeyPiece wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        // Add your action code here.
        PlayerActor player = (PlayerActor)getOneIntersectingObject(PlayerActor.class);

        if(null != player)
        {
            if(player.takeKey(this))
            {
                world.removeObject(this);
            }

        }
    }    

    @Override
    public void addedToWorld(World world) {
        this.world = (BackGround) world;

    }

}
