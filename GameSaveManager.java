import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * Write a description of class GameSaveManager here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public enum GameSaveManager  
{
    INSTANCE;

    static StringBuffer saveState = null;
    static char hex[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};

    static
    {
        try
        {
            if(UserInfo.isStorageAvailable())
            {
                UserInfo me = UserInfo.getMyInfo();
                if(null != me)
                {
                    String userLoadedString = me.getString(0) + me.getString(1) + me.getString(2) + me.getString(3);
                    if(userLoadedString.length() > 184)
                    {
                        System.out.println("Save Data Loaded");
                        saveState = new StringBuffer(userLoadedString);
                    }   
                }
                else
                {
                    System.out.println("Cannot Retrieve User Info, Not Signed in.");
                }

            }
        }
        catch(Exception e)
        {
            System.out.println("You are using Java 8 and do not have Socket Permission set.");
            e.printStackTrace();
        }
    }

    public static void composeStringBuffer(int seed,MazeRoom room[][],PlayerActor player,int roomx,int roomy)
    {
        int bitmask = 0x0000000F;
        saveState = new StringBuffer();
        /*
        SSSSSSSSKKKKKKKKKKKKKKKLXXYYRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR
        420447DF000000000000000300008000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000
        01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234
        _         1         2         3         4         5         6         7         8         9         0         1         2         3         4         5         6         7         8
        _                                                                                                   1         1         1         1         1         1         1         1         1   _
         */      
        for(int i = 28; i > -1 ; i-=4)
        {
            saveState.append(hex[(seed >> i) & bitmask]);
        }
        //saveState.append('-');
        System.out.println("Number of Keys : " + player.getKeys().size());
        for(int i = 0; i < 15; i++)
        {

            if(i >= player.getKeys().size())
            {
                saveState.append(hex[0]);
            }
            else
            {
                KeyPiece key = (KeyPiece)player.getKeys().get(i);
                System.out.println("Key ID : " + key.keyId);
                saveState.append(hex[key.keyId & bitmask]);
            }
        }
        //saveState.append('-');
        saveState.append(hex[player.numLives & bitmask]);

        //Room Positions Room x
        for(int i = 4 ; i > -1 ; i-=4)
        {
            saveState.append(hex[(roomx >> i) & bitmask]);
        }
        //Room postions Room y
        for(int i = 4 ; i > -1 ; i-=4)
        {
            saveState.append(hex[(roomy >> i) & bitmask]);
        }

        //saveState.append('-');
        int k = 3;
        int total=0;
        for(int i = 0; i < room.length ; i++)
            for(int j = 0 ; j < room[i].length ; j++)
            {
                if(room[i][j].visited)
                {
                    total += (1 << k);
                }
                k--;
                if(k < 0)
                {
                    saveState.append(hex[total]);
                    k = 3;
                    total = 0;
                }
        }
        if(3 != k)
        {
            saveState.append(hex[total]);
        }

        System.out.println(saveState.toString());
        System.out.println("String Length " + saveState.length());

        try
        {
            if(UserInfo.isStorageAvailable())
            {
                UserInfo me = UserInfo.getMyInfo();
                if(null == me)
                {
                    System.out.println("Not Signed in");
                    return;
                }
                me.setString(0,saveState.substring(0,50));
                me.setString(1,saveState.substring(50,100));
                me.setString(2,saveState.substring(100,150));
                me.setString(3,saveState.substring(150));
                me.store();

                System.out.println(saveState.substring(0,50));
                System.out.println(saveState.substring(50,100));
                System.out.println(saveState.substring(100,150));
                System.out.println(saveState.substring(150));
            }
        }
        catch(Exception e)
        {
            System.out.println("You are using Java 8 and do not have Socket Permission set.");
            e.printStackTrace();
        }

    }

    /* This routine expands the data so we do not need it
    public static String makeUserString()
    {
    if(null == saveState)
    {
    return ("");
    }

    int i;
    int k = 0;
    char ch = (char)0;
    StringBuffer sb = new StringBuffer();

    int ul =  ((saveState.length() + 3)/4) * 4;

    for(i = 0 ; i < ul ;i++)
    {
    char tmpchar = '0';

    if(i < saveState.length())
    {
    tmpchar = saveState.charAt(i);
    }

    tmpchar = (char)(tmpchar << 4);

    switch(tmpchar)
    {
    case 'F':
    case 'E':
    case 'D':
    case 'C':
    case 'B':
    case 'A':
    ch += (char)tmpchar - (char)'A' + (char)10;
    break;
    default:
    ch += (char)tmpchar - (char)'0';
    break;
    }

    k++;
    if(k > 3)
    {

    sb.append(ch);
    k = 0;
    ch = (char)0;
    }
    }
    System.out.println("Len Of Compressed String " + sb.length() + " " + sb.toString());
    return(Base64.encode(sb.toString()));
    }
     */

    public static int getSeed()
    {
        if(null == saveState)
        {
            return (0);
        }

        int seedVal = 0;
        for(int i = 0; i < 8 ; i++)
        {
            seedVal = seedVal << 4;
            char ch = saveState.charAt(i);
            switch(ch)
            {
                case 'F':
                case 'E':
                case 'D':
                case 'C':
                case 'B':
                case 'A':
                seedVal += (int)ch - (int)'A' + 10;
                break;
                default:
                seedVal += (int)ch - (int)'0';
                break;
            }
        }
        return seedVal;
    }

    public static boolean hasSaveGame()
    {
        if(null != saveState)
        {
            return (true);
        }

        return false;
    }

    public static int getX()
    {
        return getShortInt(24);
    }

    public static int getY()
    {
        return getShortInt(26);
    }

    public static int getShortInt(int idx)
    {
        int val = 0;
        char ch=' ';
        for(int i = idx; i < (idx+2); i++)
        {
            val = val << 4;
            ch = saveState.charAt(i);
            switch(ch)
            {
                case 'F':
                case 'E':
                case 'D':
                case 'C':
                case 'B':
                case 'A':
                val += (int)ch - (int)'A' + 10;
                break;
                default:
                val += (int)ch - (int)'0';
                break;
            }
        }

        return (val);

    }

    public static void takeKeysPlayerHad(MazeRoom room[][],PlayerActor player)
    {
        boolean haveit[] = new boolean[16];

        for(int i = 0 ; i < 16; i++)
        {
            haveit[i] = false;
        }

        for(int i=8; i < 23 ;i++)
        {
            int val = 0;
            char ch = saveState.charAt(i);
            switch(ch)
            {
                case 'F':
                case 'E':
                case 'D':
                case 'C':
                case 'B':
                case 'A':
                val = (int)ch - (int)'A' + 10;
                break;
                default:
                val = (int)ch - (int)'0';
                break;
            }
            haveit[val] = true;
            if(val > 0)
            {
                System.out.println("We Have A Key : " + val);
            }
        }

        for(int i = 0 ; i < room.length ; i++)
        {
            for(int j = 0; j < room[i].length; j++)
            {
                if((null != room[i][j].key) && (haveit[room[i][j].key.keyId]))
                {
                    System.out.println("Key Taken : " + room[i][j].key.keyId + " at " + i + " " + j);
                    player.takeKey(room[i][j].key);
                    room[i][j].key = null;
                }
            }
        }
    }

    public static void drawMap(MazeRoom room[][],MapScreenActor map)
    {
        int bitmask = 0x00000001;
        if(null == saveState)
        {
            return;
        }

        int k = -1;
        //one before the start
        int idx = 27;
        char ch = ' ';
        int val = -1;

        for(int i = 0 ; i < room.length ; i++)
        {
            for(int j = 0 ; j < room[i].length;j++)
            {
                if(k < 0)
                {
                    idx++;
                    ch = saveState.charAt(idx);
                    switch(ch)
                    {
                        case 'F':
                        case 'E':
                        case 'D':
                        case 'C':
                        case 'B':
                        case 'A':
                        val = (int)ch - (int)'A' + 10;
                        break;
                        default:
                        val = (int)ch - (int)'0';
                        break;
                    }
                    k = 3;
                }

                int bit = ((val >> k) & bitmask);

                if(1 == bit)
                {
                    //System.out.println("Draw Room " + i + " " + j);
                    room[i][j].visited = true;
                    map.drawRoom(i,j,room[i][j],(null != room[i][j].key));
                }

                k--;
            }
        }
    }
}
