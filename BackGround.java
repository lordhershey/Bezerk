import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.*;
import java.awt.image.*;
import java.util.HashMap;
import java.util.List;
import java.util.*;

/**
 * Write a description of class BackGround here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class BackGround extends World
{

    GreenfootImage bggf = null;
    BufferedImage bi = null;
    Graphics2D g2d = null;

    PlayerActor player = null;

    public static enum State {PLAYING,WAITING,PAUSED}

    public State gameState = State.WAITING;
    public MapScreenActor map = null;
    public MapPlayerIconActor smallPlayer = null;

    KeyPiece requiredKey[] = new KeyPiece[4];

    public boolean isPaused()
    {
        if(State.PAUSED == gameState)
        {
            return (true);
        }
        return (false);
    }

    public State playerState = State.WAITING;
    /**
     * Constructor for objects of class BackGround.
     * 
     */

    private HashMap<MazeRoom,String> paintRoom = null;

    static final int numRoomX = 25;
    static final int numRoomY = 25;
    public MazeRoom room[][] = new MazeRoom[numRoomX][numRoomY];
    boolean sIsDown = false;
    int roomx = 0;
    int roomy = 0;
    /*0 - north, 1 - east, 2 - south, 3 - west*/
    public int enter=3;

    /*61,200 - West
    297,52 - North
    548,199 - East
    296,352 - south*/

    GreenfootSound backgroundMusic = null;
    boolean PKeyDown = false;
    boolean MKeyDown = false;

    static Random generator = null;
    static int seed = 0;

    /*
    static 
    {
    seed = Greenfoot.getRandomNumber(Integer.MAX_VALUE);
    generator = new Random(seed);
    }
     */

    boolean RestoreGame = false;

    public BackGround(boolean RestoreGame)
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(600, 400, 1,false); 

        bggf = getBackground();
        bi = bggf.getAwtImage();
        g2d = (Graphics2D)bi.getGraphics();
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0,0,600,400);

        player = new PlayerActor();

        map = new MapScreenActor();
        smallPlayer = new MapPlayerIconActor();

        this.RestoreGame = RestoreGame;
        prepare();
        //Greenfoot.start();
        backgroundMusic = new GreenfootSound("Life_Cri-RepaidGa-10581_hifi.wav");

        setPaintOrder(MapPlayerIconActor.class,MapScreenActor.class,BulletActor.class,PlayerActor.class,RobotActor.class,PopUpMine.class,DoorCoverActor.class,DoorActor.class);
    }

    public BackGround()
    {
        this(false);
    }

    @Override
    public void act()
    {

        if(gameState == State.WAITING)
        {
            gameState = State.PLAYING;
        }

        if(gameState == State.PLAYING)
        {
            if(player.isDead())
            {
                if(player.hasMoreLives())
                {
                    switchRooms(-1);
                }
                else
                {
                    Greenfoot.setWorld(new SplashScreen());
                }
            }

            //press save key
            if(Greenfoot.isKeyDown("1") && !sIsDown)
            {
                addObject(new SavingGameActor(),320,200);
                GameSaveManager.composeStringBuffer(seed,room,player,roomx,roomy);
                sIsDown = true;
            }
            else if(!Greenfoot.isKeyDown("1"))
            {
                sIsDown = false;
            }
        }

        if(!PKeyDown && Greenfoot.isKeyDown("p"))
        {
            PKeyDown = true;
            if(backgroundMusic.isPlaying())
            {
                backgroundMusic.stop();
            }
            else
            {
                backgroundMusic.setVolume(30);
                backgroundMusic.playLoop();
            }
        }
        else  if(!Greenfoot.isKeyDown("p"))
        {
            PKeyDown = false;

        }

        if(!MKeyDown && Greenfoot.isKeyDown("m"))
        {
            MKeyDown = true;
            if(State.PAUSED == gameState)
            {
                gameState = State.PLAYING;
                removeObject(map);
                removeObject(smallPlayer);
            }
            else if(State.PLAYING == gameState)
            {
                gameState = State.PAUSED;

                addObject(map,320,200);
                addObject(smallPlayer,120 + roomx * 16 + 8, 50 + roomy * 12 + 6);
            }

        }
        else if(!Greenfoot.isKeyDown("m"))
        {
            MKeyDown = false;
        }
    }

    private boolean closesRoom(int x, int y,int closeDir)
    {

        if(x < 0 || y < 0 || x >= numRoomX || y >= numRoomY)
        {
            return false;
        }

        /*
        if(!room[x][y].North)
        {
        closures++;
        }

        if(!room[x][y].South)
        {
        closures++;
        }

        if(!room[x][y].East)
        {
        closures++;
        }

        if(!room[x][y].West)
        {
        closures++;
        }
         */

        switch(closeDir)
        {
            case 3:
            //closing the west entrance
            if(!room[x][y].North && !room[x][y].South && !room[x][y].East)
            {
                return true;
            }
            break;
            case 2:
            //closing the south entrance
            if(!room[x][y].North && !room[x][y].East && !room[x][y].West)
            {
                return true;
            }
            break;
            case 1:
            //closing the east entrance
            if(!room[x][y].North && !room[x][y].South && !room[x][y].West)
            {
                return true;
            }
            break;
            case 0:
            default:
            //closing the north entrance
            if(!room[x][y].South && !room[x][y].East && !room[x][y].West)
            {
                return true;
            }
            break;
        }

        return false;
    }

    public boolean MapOk()
    {
        int i,j;
        for(i= 0 ; i < 25 ; i++)
        {
            for(j = 0 ; j < 25 ; j++)
            {
                paintRoom = new HashMap<MazeRoom,String>(20);
                if(!CanGetToStart(i,j))
                {
                    return (false);
                }
            }
        }

        return (true);
    }

    public boolean CanGetToStart(int x,int y)
    {
        //System.out.println("X " + x + " Y " + y);
        if (0 == x && 0 == y)
        {
            return(true);
        }

        String test = paintRoom.get(room[x][y]);
        if(test != null)
        {
            return (false);
        }

        paintRoom.put(room[x][y], "PAINTED");

        if(room[x][y].North && CanGetToStart(x,y-1))
        {
            return true;
        }

        if(room[x][y].West && CanGetToStart(x-1,y))
        {
            return true;
        }

        if(room[x][y].East && CanGetToStart(x+1,y))
        {
            return true;
        }

        if(room[x][y].South && CanGetToStart(x,y+1))
        {
            return true;
        }

        return (false);
    }

    public void buildRandomWalls()
    {
        int i,j,k;
        int limit = 1;

        for(i = 0; i < numRoomX ; i++)
        {
            for(j = 0 ; j < numRoomY ; j++)
            {
                /*
                if((i+j) %2 == 0)
                {
                Even Rank No Closing
                continue;
                }
                 */

                limit = generator.nextInt(2);
                for(k = 0 ; k < limit; k++)
                {
                    //randomly close
                    //was 4 but will only close north and east
                    switch(generator.nextInt(4))
                    {
                        case 3:
                        //close the west side
                        if(room[i][j].West && !closesRoom(i,j,3) && !closesRoom((i-1),j,1))
                        {
                            try
                            {
                                room[i-1][j].East = false;
                                room[i][j].West = false;
                            }
                            catch(Exception e)
                            {
                                //easily possilbe to overstep array bounds, but who cares?
                            }
                        }
                        break;
                        case 1:
                        //close the east side
                        if(room[i][j].East && !closesRoom(i,j,1) && !closesRoom((i+1),j,3))
                        {
                            try
                            {
                                room[i+1][j].West = false;
                                room[i][j].East = false;
                            }
                            catch(Exception e)
                            {
                                //easily possilbe to overstep array bounds, but who cares?
                            }
                        }
                        break;
                        case 2:
                        //close the south side
                        if(room[i][j].South && !closesRoom(i,j,2) && !closesRoom(i,(j+1),0))
                        {
                            try
                            {
                                room[i][j+1].North = false;
                                room[i][j].South = false;
                            }
                            catch(Exception e)
                            {
                                //easily possilbe to overstep array bounds, but who cares?
                            }
                        }
                        break;
                        case 0:
                        //close the north side
                        if(room[i][j].North && !closesRoom(i,j,0) && !closesRoom(i,(j-1),2))
                        {
                            try
                            {
                                room[i][j-1].South = false;
                                room[i][j].North = false;
                            }
                            catch(Exception e)
                            {
                                //easily possilbe to overstep array bounds, but who cares?
                            }
                        }
                        break;
                        default:
                        break;
                    }
                }
            }
        }
    }

    public void buildRooms()
    {
        roomx = 0;
        roomy = 0;
        /*0 - north, 1 - east, 2 - south, 3 - west*/
        enter=3;

        room = new MazeRoom[numRoomX][numRoomY];

        int i,j;

        for(i = 0; i < numRoomX ; i++)
        {
            for(j = 0 ; j < numRoomY ; j++)
            {
                room[i][j]=new MazeRoom(generator);

                if(i == 0)
                {
                    room[i][j].West = false;
                }
                if(i == (numRoomX - 1))
                {
                    room[i][j].East = false;
                }
                if(j == 0 )
                {
                    room[i][j].North = false;
                }
                if(j == (numRoomY - 1))
                {
                    room[i][j].South = false;
                }
            }
        }

        //we use this to make keys to drop
        int numKeys = 0;
        int arrx[] = new int[15];
        int arry[] = new int[15];
        int reqidx = 0;
        while(numKeys < 15)
        {
            int randx = generator.nextInt(20) + 5;
            int randy = generator.nextInt(20) + 5;

            if(null != room[randx][randy].key)
            {
                //That room has a key
                continue;
            }

            arrx[numKeys] = randx;
            arry[numKeys] = randy;

            KeyPiece key = new KeyPiece(++numKeys);

            room[randx][randy].key = key;
            if((1 == key.keyId) || (4 == key.keyId) || (7 == key.keyId) || (8 == key.keyId))
            {
                /*grab a reference to this key*/
                requiredKey[reqidx++] = key;
                //player.takeKey(key);
            }
        }

        //figure out room rank
        for(i = 0; i < numRoomX ; i++)
        {
            for(j = 0 ; j < numRoomY ; j++)
            {
                for(int k = 0 ; k < 15 ; k++)
                {
                    if(room[i][j].level > 1)
                    {
                        //Max Level is 2 so break
                        break;
                    }
                    int dist = (i-arrx[k])*(i-arrx[k]) + (j-arry[k])*(j-arry[k]);

                    if(dist < 16)
                    {
                        room[i][j].level = 1;
                    }
                    if(dist < 4)
                    {
                        room[i][j].level = 2;
                    }
                }
            }
        }

        /*Boss Tele Port Room*/
        //room[0][0].level = 3;
        //find a green room
        int tries = 0;
        while(true)
        {
            int tx = generator.nextInt(numRoomX);
            int ty = generator.nextInt(numRoomX);
            tries++;
            if(tries > 100)
            {
                room[3][3].level = 3;
                System.out.println("Escape Room is 3,3 ");
                break;
            }
            if(tx < 8 || ty < 8)
            {
                continue;
            }

            if(room[tx][ty].level < 1)
            {
                room[tx][ty].level = 3;
                System.out.println("Escape Room is " + tx + "," + ty);
                break;
            }
        }

    }

    public void renderRoom(MazeRoom room)
    {
        /*
        X = 68
        X = 184
        X = 300
        X = 416
        X = 532
        Y = 73
        Y = 200
        Y = 327
         */

        /*keypiece*/
        if(room.key != null)
        {
            addObject(room.key,64,64);
        }

        int i;
        int x;
        int y;
        for(i = 0,x = 68 ; i < 5 ; i++, x+= 116)
        {
            //System.out.println("X = " + x);
            /*136 x 19*/

            if(2 != i || !room.North)
            {
                MazeActor greenmazehorizontal = null;

                switch(room.level)
                {
                    case 3:
                    greenmazehorizontal = (MazeActor) new BossMazeHorizontal();
                    break;
                    case 2:
                    greenmazehorizontal = (MazeActor) new RedMazeHorizontal();
                    break;
                    case 1:
                    greenmazehorizontal = (MazeActor) new YellowMazeHorizontal();
                    break;
                    case 0:
                    default:
                    greenmazehorizontal = (MazeActor) new GreenMazeHorizontal();
                    break;
                }

                addObject(greenmazehorizontal, 0, 0);
                greenmazehorizontal.setLocation(x, 9);

            }

            if(2 != i || !room.South)
            {

                MazeActor greenmazehorizontal = null;

                switch(room.level)
                {
                    case 3:
                    greenmazehorizontal = (MazeActor) new BossMazeHorizontal();
                    break;
                    case 2:
                    greenmazehorizontal = (MazeActor) new RedMazeHorizontal();
                    break;
                    case 1:
                    greenmazehorizontal = (MazeActor) new YellowMazeHorizontal();
                    break;
                    case 0:
                    default:
                    greenmazehorizontal = (MazeActor) new GreenMazeHorizontal();
                    break;
                }

                addObject(greenmazehorizontal, 0, 0);
                greenmazehorizontal.setLocation(x, 390);
            }
        }

        for(i = 0,y = 73 ; i < 3 ; i++, y += 127)
        {
            //System.out.println("Y = " + y);
            if(1 != i || !room.West)
            {
                /*20 x 146*/
                MazeActor greenmazevertical = null;

                switch(room.level)
                {
                    case 3:
                    greenmazevertical = (MazeActor) new BossMazeVertical();
                    break;
                    case 2:
                    greenmazevertical = (MazeActor) new RedMazeVertical();
                    break;
                    case 1:
                    greenmazevertical = (MazeActor) new YellowMazeVertical();
                    break;
                    case 0:
                    default:
                    greenmazevertical = (MazeActor) new GreenMazeVertical();
                    break;
                }

                addObject(greenmazevertical, 0, 0);
                greenmazevertical.setLocation(10, y);

            }

            if(1 != i || !room.East)
            {
                /*20 x 146*/

                MazeActor greenmazevertical = null;

                switch(room.level)
                {
                    case 3:
                    greenmazevertical = (MazeActor) new BossMazeVertical();
                    break;
                    case 2:
                    greenmazevertical = (MazeActor) new RedMazeVertical();
                    break;
                    case 1:
                    greenmazevertical = (MazeActor) new YellowMazeVertical();
                    break;
                    case 0:
                    default:
                    greenmazevertical = (MazeActor) new GreenMazeVertical();
                    break;
                }

                addObject(greenmazevertical, 0, 0);
                greenmazevertical.setLocation(590, y);
            }
        } 

        /*
        {
        GreenMazeVertical greenmazevertical = new GreenMazeVertical();
        addObject(greenmazevertical,0 ,0 );
        greenmazevertical.setLocation(126 , 73);
        }
         */

        for(i = 0; i < 4 ; i++)
        {

            MazeActor greenmazevertical = null;
            MazeActor greenmazehorizontal = null;

            switch(room.level)
            {
                case 2:
                greenmazevertical = (MazeActor) new RedMazeVertical();
                greenmazehorizontal = (MazeActor) new RedMazeHorizontal();
                break;
                case 1:
                greenmazevertical = (MazeActor) new YellowMazeVertical();
                greenmazehorizontal = (MazeActor) new YellowMazeHorizontal();
                break;
                case 0:
                default:
                greenmazevertical = (MazeActor) new GreenMazeVertical();
                greenmazehorizontal = (MazeActor) new GreenMazeHorizontal();
                break;
            }

            if(room.level != 3)
            {
                switch(room.walls[i])
                {
                    case 1:
                    /*East*/
                    //greenmazehorizontal = new GreenMazeHorizontal();
                    addObject(greenmazehorizontal, 0, 0);
                    greenmazehorizontal.setLocation(68 + (i+1)*116, 136);
                    break;
                    case 2: 
                    /*South*/
                    //greenmazevertical = new GreenMazeVertical();
                    addObject(greenmazevertical,0 ,0 );
                    greenmazevertical.setLocation(126 + i * 116, 200);
                    break;
                    case 3:
                    //greenmazehorizontal = new GreenMazeHorizontal();
                    addObject(greenmazehorizontal, 0, 0);
                    greenmazehorizontal.setLocation(68 + i*116, 136);
                    break;
                    case 0:
                    /*North*/
                    //greenmazevertical = new GreenMazeVertical();
                    addObject(greenmazevertical,0 ,0 );
                    greenmazevertical.setLocation(126 + i * 116, 73);
                    break;
                    default:
                    //We could opt to not have a wall (maybe a spepcial room
                    break;
                }

                greenmazevertical = null;
                greenmazehorizontal = null;

                switch(room.level)
                {
                    case 2:
                    greenmazevertical = (MazeActor) new RedMazeVertical();
                    greenmazehorizontal = (MazeActor) new RedMazeHorizontal();
                    break;
                    case 1:
                    greenmazevertical = (MazeActor) new YellowMazeVertical();
                    greenmazehorizontal = (MazeActor) new YellowMazeHorizontal();
                    break;
                    case 0:
                    default:
                    greenmazevertical = (MazeActor) new GreenMazeVertical();
                    greenmazehorizontal = (MazeActor) new GreenMazeHorizontal();
                    break;
                }

                switch(room.walls[i+4])
                {
                    case 1:
                    /*East*/
                    //greenmazehorizontal = new GreenMazeHorizontal();
                    addObject(greenmazehorizontal, 0, 0);
                    greenmazehorizontal.setLocation(68 + (i+1)*116, 263);
                    break;
                    case 2: 
                    /*South*/
                    //greenmazevertical = new GreenMazeVertical();
                    addObject(greenmazevertical,0 ,0 );
                    greenmazevertical.setLocation(126 + i * 116, 327);
                    break;
                    case 3:
                    //greenmazehorizontal = new GreenMazeHorizontal();
                    addObject(greenmazehorizontal, 0, 0);
                    greenmazehorizontal.setLocation(68 + i*116, 263);
                    break;
                    case 0:
                    /*North*/
                    //greenmazevertical = new GreenMazeVertical();
                    addObject(greenmazevertical,0 ,0 );
                    greenmazevertical.setLocation(126 + i * 116, 200);
                    break;
                    default:
                    break;
                }
            }    
        }

    }

    public void placeRobots(MazeRoom room)
    {
        int i;
        int j;
        int x = 0;
        int y = 0;

        if(3 == room.level)
        {
            /*Place the Floor Switches*/
            FloorSwitchActor fap1 = new FloorSwitchActor();
            fap1.setKey(requiredKey[0]);
            addObject(fap1,300,100);

            FloorSwitchActor fap2 = new FloorSwitchActor();
            fap2.setKey(requiredKey[1]);
            addObject(fap2,400,200);

            FloorSwitchActor fap3 = new FloorSwitchActor();
            fap3.setKey(requiredKey[3]);
            addObject(fap3,200,200);

            FloorSwitchActor fap4 = new FloorSwitchActor();
            fap4.setKey(requiredKey[2]);
            addObject(fap4,300,300);

            DoorActor da = new DoorActor();
            da.addSwitch(fap1);
            da.addSwitch(fap2);
            da.addSwitch(fap3);
            da.addSwitch(fap4);
            addObject(da,300,233);

            DoorCoverActor dca = new DoorCoverActor();
            addObject(dca,300,233);
            return;
        }

        for(i = 0, x = 70 ; i < 5 ; i++, x += 116)
        {
            for(j = 0, y = 74; j < 3 ; j++, y+=127)
            {
                if(enter == 3 && i == 0 && j ==1)
                {
                    /*player spawns here*/
                    continue;
                }
                else if(enter == 2 && i == 2 && j == 2)
                {
                    /*player spawns here*/
                    continue;
                }
                else if(enter == 1 && i == 4 && j ==1)
                {
                    /*player spawns here*/
                    continue;
                }
                else if(enter ==0 && i == 2 && j == 0)
                {
                    /*player spawns here*/
                    continue;
                }

                //check some current room attribute to determine how dangerous a robot should be
                if(generator.nextInt(100) < 50)
                {
                    /*50-50 on having a robot*/
                    continue;
                }
                RobotActor robby = null;
                if(generator.nextInt(40) > 36 - (room.level * 5))
                {
                    robby = new BloodHoundActor();
                }
                else
                {
                    robby = new RobotActor();
                }

                if(generator.nextInt(40) > 37 - (room.level * 5))
                {
                    addObject(new PopUpMine(),x + generator.nextInt(21) - 10,y+ generator.nextInt(21) - 10);
                }

                addObject(robby,x + generator.nextInt(41) - 20,y+ generator.nextInt(41) - 20);

                if(2 == room.level)
                {
                    robby.senseRange = 160;
                    robby.fireCounterLimit = 50;
                    robby.bulletSpeed = 8;
                }

                if(0 == room.level)
                {
                    //robby.senseRange = 120;
                    //robby.fireCounterLimit = 60;
                    robby.bulletSpeed = 4;
                }
            }
        }
    }

    public void placePlayer()
    {
        /*61,200 - West
        297,52 - North
        548,199 - East
        296,352 - south*/
        //put a thing here to set the player state to null orr blank
        switch(enter)
        {
            case 3:
            addObject(player,40,200);
            break;
            case 2:
            addObject(player,296,359);
            break;
            case 1:
            addObject(player,559,200);
            break;
            case 0:
            default:
            addObject(player,296,40);
            break;
        }
    }

    public void switchRooms(int dirfrom)
    {
        player.resetState();

        try
        {
            List l = getObjects(KeyPiece.class);
            if(null == l || l.size() < 1)
            {
                room[roomx][roomy].key = null;
            }
        }
        catch (Exception e)
        {
        }

        try{
            removeObjects(getObjects(null));
        }
        catch(Exception e)
        {
        }

        switch(dirfrom)
        {
            case 1:
            roomx++;
            enter = 3;
            break;
            case 2:
            roomy++;
            enter = 0;
            break;
            case 3:
            roomx--;
            enter = 1;
            break;
            case 0:
            roomy--;
            enter = 2;
            break;
            default:
            break;
        }

        if(roomx < 0)
        {
            roomx = 0;
        }

        if(roomy<0)
        {
            roomy = 0;
        }

        if(roomx >= numRoomX)
        {
            roomx = numRoomX - 1;
        }

        if(roomy >= numRoomY)
        {
            roomy = numRoomY -1;
        }

        if(!room[roomx][roomy].visited)
        {
            map.drawRoom(roomx,roomy,room[roomx][roomy],(null != room[roomx][roomy].key));
        }
        room[roomx][roomy].visited = true;
        renderRoom(room[roomx][roomy]);
        placeRobots(room[roomx][roomy]);
        placePlayer();
    }

    /**
     * Prepare the world for the start of the program. That is: create the initial
     * objects and add them to the world.
     */
    private void prepare()
    {
        /*Seed must be set here*/
        if(RestoreGame)
        {
            seed = GameSaveManager.getSeed();
        }
        else
        {
            seed = Greenfoot.getRandomNumber(Integer.MAX_VALUE);
        }

        generator = new Random(seed);

        buildRooms();
        buildRandomWalls();

        while(!MapOk())
        {
            System.out.println("Oh No! There are closed sections... Regenerating...");
            //seed = Greenfoot.getRandomNumber(Integer.MAX_VALUE);
            //generator = new Random(seed);   
            buildRooms();
            buildRandomWalls();
        }

        System.out.println("Random Seed is " + seed);

        if(RestoreGame)
        {
            GameSaveManager.drawMap(room,map);
            roomx = GameSaveManager.getX();
            roomy = GameSaveManager.getY();
            System.out.println("Restore " + roomx + " " + roomy);
            GameSaveManager.takeKeysPlayerHad(room,player);
        }

        renderRoom(room[roomx][roomy]);
        placePlayer(); 
        placeRobots(room[roomx][roomy]);
        map.drawRoom(roomx,roomy,room[roomx][roomy]);

        room[roomx][roomy].visited = true;
    }
}
