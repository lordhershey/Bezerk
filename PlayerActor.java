import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * Write a description of class PlayerActor here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class PlayerActor extends PointUpActor
{
    /**
     * Act - do whatever the PlayerActor wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */

    public static enum State {STANDING,
        WALKING,
        DYING,DEAD,NAN}

    public State playerState = State.NAN;

    public static GreenfootImage northwest[] = new GreenfootImage[4];
    public static GreenfootImage north[] = new GreenfootImage[4];
    public static GreenfootImage northeast[] = new GreenfootImage[4];
    public static GreenfootImage east[] = new GreenfootImage[4];
    public static GreenfootImage southeast[] = new GreenfootImage[4];
    public static GreenfootImage south[] = new GreenfootImage[4];
    public static GreenfootImage southwest[] = new GreenfootImage[4];
    public static GreenfootImage west[] = new GreenfootImage[4];
    public static GreenfootImage standing[] = new GreenfootImage[2];
    public static GreenfootImage walking[] = new GreenfootImage[4];
    public static GreenfootImage frying[] = new GreenfootImage[27];

    public static GreenfootImage blankImage = new GreenfootImage(1,1);

    static
    {

        //Initialize Images 1 time only
        for(int i = 0 ; i < 4 ;i++)
        {
            northwest[i] = new GreenfootImage("walknorthwest0" + (i+1) + ".png");
            north[i]     = new GreenfootImage("walknorth0" + (i+1) + ".png");
            northeast[i] = new GreenfootImage("walknortheast0" + (i+1) + ".png");
            east[i]      = new GreenfootImage("walkeast0" + (i+1) + ".png");
            southeast[i] = new GreenfootImage("walksoutheast0" + (i+1) + ".png");
            south[i]     = new GreenfootImage("walksouth0" + (i+1) + ".png");
            southwest[i] = new GreenfootImage("walksouthwest0" + (i+1) + ".png");
            west[i]      = new GreenfootImage("walkwest0" + (i+1) + ".png");
        }

        for(int i = 0 ; i < 27 ; i++)
        {
            if(i< 9)
            {
                frying[i] = new GreenfootImage("fryguy0" + (i+1) + ".png");
            }
            else
            {
                frying[i] = new GreenfootImage("fryguy" + (i+1) + ".png");
            }
        }

        standing[0] = new GreenfootImage("idle01.png");
        standing[1] = new GreenfootImage("idle02.png");

    }
    private World world = null;
    int walkcounter = 0;
    int standcounter = 0;
    int fireCounter = 0;
    int dyingcounter = 0;
    public int numLives = 3;

    boolean fireKeyUp = true;

    Vector<KeyPiece> keys = null;
    long startTime = 0;

    public boolean hasMoreLives()
    {
        if(numLives > 0)
        {
            return true;
        }

        return false;
    }

    public boolean isDead()
    {
        if(playerState == State.DEAD)
        {
            return(true);
        }

        return (false);
    }

    public void resetState()
    {
        playerState = State.NAN;
    }

    public PlayerActor()
    {
        int i;
        numLives = 3;
       
        keys = new Vector();

        playerState = State.NAN;
    }

    public boolean takeKey(KeyPiece key)
    {
        System.out.println("Pick up a key with ID " + key.keyId);
        //could check to see if we are alive
        keys.add(key);
        return (true);
    }

    public Vector<KeyPiece> getKeys()
    {
        return keys;
    }

    public void takeABullet()
    {
        if(State.STANDING == playerState || State.WALKING == playerState)
        {
            playerState = State.DYING;
            dyingcounter = 0;
            startTime = 0;
        }
    }

    public void act() 
    {
        if(State.DEAD == playerState)
        {
            return;
        }

        // Add your action code here.
        if(State.NAN == playerState)
        {
            setImage(standing[0]);
            playerState = State.STANDING;
            walking=south;
            return;
        }

        if(playerState == State.DYING)
        {
            int index = dyingcounter;

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

            if(index > 26)
            {
                //counter extends past current images
                index = 26;
            }

            setImage(frying[index]);

            dyingcounter++;

            if(dyingcounter > 70)
            {
                playerState = State.DEAD;
                numLives--;
                setImage(blankImage);
                //playerState = State.NAN;
                //world.removeObject(this);
                //world.placePlayer();
                //setImage(standing[0]);
                //if(world instanceof BackGround)
                //{
                //    ((BackGround)world).switchRooms(-1);
                //}
            }
            return;
        }

        if(State.WALKING == playerState || State.STANDING == playerState)
        {

            RobotActor robby = (RobotActor)getOneIntersectingObject(RobotActor.class);
            //check to see if we hit a wall
            MazeActor wall = (MazeActor)getOneIntersectingObject(MazeActor.class);
            if((null != wall) || (null != robby && robby.isAlive()))
            {
                dyingcounter = 0;
                playerState = State.DYING;
                startTime = 0;
                return;
            }

            walkcounter = (walkcounter + 1) % 4;
            standcounter = (standcounter + 1) % 2;
            processWalkingActions();
            setImage(walking[walkcounter]);
            if(State.STANDING == playerState)
            {
                setImage(standing[standcounter]);
            }

            if(State.WALKING == playerState)
            {
                move(3);

                //SLAM into Edge
                if(world instanceof BackGround)
                {
                    if(getX() < 0)
                    {
                        //setLocation(0,getY());
                        ((BackGround)world).switchRooms(3);
                    }

                    if(getX() >= world.getWidth())
                    {
                        //setLocation(world.getWidth() - 1,getY());
                        ((BackGround)world).switchRooms(1);
                    }

                    if(getY() < 0)
                    {
                        //setLocation(getX(),0);
                        ((BackGround)world).switchRooms(0);
                    }

                    if(getY() >= world.getHeight())
                    {
                        //setLocation(getX(),world.getHeight()-1);
                        ((BackGround)world).switchRooms(2);
                    }
                }
            }
        }

    }

    public void processWalkingActions()
    {

        boolean UpKey = false;
        boolean DownKey = false;
        boolean LeftKey = false;
        boolean RightKey = false;
        boolean FireKey = false;

        FireKey = Greenfoot.isKeyDown("shift") || Greenfoot.isKeyDown("control")|| Greenfoot.isKeyDown("space");
        UpKey = Greenfoot.isKeyDown("w") || Greenfoot.isKeyDown("up");
        DownKey = Greenfoot.isKeyDown("s") || Greenfoot.isKeyDown("down");
        LeftKey = Greenfoot.isKeyDown("a") || Greenfoot.isKeyDown("left");
        RightKey = Greenfoot.isKeyDown("d") || Greenfoot.isKeyDown("right");
        if(Greenfoot.isKeyDown("q"))
        {
            UpKey = true;
            LeftKey = true;
        }
        if(Greenfoot.isKeyDown("e"))
        {
            UpKey = true;
            RightKey = true;
        }
        if(Greenfoot.isKeyDown("c"))
        {
            DownKey = true;
            RightKey = true;
        }
        if(Greenfoot.isKeyDown("z"))
        {
            DownKey = true;
            LeftKey = true;
        }

        if(UpKey)
        {
            //System.out.println("Go North!");
            setRotation(270);
            playerState = State.WALKING;
            walking = north;
            /*Check for North East or North West*/

            if(RightKey)
            {
                turn(45);
                walking = northeast;
            }
            else if(LeftKey)
            {
                turn(-45);
                walking = northwest;
            }

        }
        else if(DownKey)
        {
            setRotation(90);
            playerState = State.WALKING;
            walking = south;
            if(LeftKey)
            {
                turn(45);
                walking = southwest;
            }
            else if(RightKey)
            {
                turn(-45);
                walking = southeast;
            }

        }
        else if(LeftKey)
        {
            setRotation(180);
            playerState = State.WALKING;
            walking = west;
        }
        else if(RightKey)
        {
            setRotation(0);
            playerState = State.WALKING;
            walking = east;
        }
        else
        {
            playerState=State.STANDING;

        }

        if(fireCounter < 1)
        {
            if(FireKey && fireKeyUp)
            {
                fireKeyUp = false;
                BulletActor bullet = new BulletActor();
                bullet.setOwner((Actor)this);
                world.addObject(bullet,getX(),getY());

                bullet.setRotation(getRotation());
                bullet.move(5);
                fireCounter = 4;
            }
            else if(!FireKey)
            {
                //test to make sure htey are not holding it in else case there
                //is no rapid fire
                fireKeyUp = true;
            }
        }
        else
        {
            fireCounter--;
        }
    }

    @Override
    public void addedToWorld(World world) {
        this.world = (BackGround) world;

    }
}
