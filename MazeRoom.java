import greenfoot.*;
import java.util.*;

/**
 * Write a description of class MazeRoom here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MazeRoom  
{
    // instance variables - replace the example below with your own
    public int walls[] = new int[8];

    public boolean North = true;
    public boolean South = true;
    public boolean East = true;
    public boolean West = true;
    
    public boolean visited = false;
    
    /*add a key to this rooms maybe*/
    public KeyPiece key = null;
    
    public int level = 0;
    /**
     * Constructor for objects of class MazeRoom
     */
    public MazeRoom()
    {
        generateWalls();
        level = 0;
        visited = false;
    }

    public MazeRoom(Random generator)
    {
        generateWalls(generator);
        level = 0;
        visited = false;
    }
    
    public void generateWalls()
    {
        int i;
        for(i = 0 ; i < 7 ; i++)
        {
            walls[i] = Greenfoot.getRandomNumber(5);
        }
    }
    
    public void generateWalls(Random generator)
    {
        int i;
        for(i = 0 ; i < 7 ; i++)
        {
            walls[i] = generator.nextInt(5);
        }
    }
}
