import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.*;

/**
 * Write a description of class MapScreenActor here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MapScreenActor extends Actor
{
    /**
     * Act - do whatever the MapScreenActor wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    GreenfootImage image = null;

    public MapScreenActor()
    {
        image = new GreenfootImage("BlankMap.jpg");
    }

    public void act() 
    {
        setImage(image);
        // Add your action code here.
    }

    public void drawRoom(int i, int j, MazeRoom room,boolean key)
    {
        int x,y;
        x = i * 16;
        y = j * 12;

        drawRoom(i,j,room);

        image.setColor(Color.ORANGE);
        //image.drawRect(x+1,y+1,13,9);
        if(key)
        {
            image.drawLine(x + 3,y+3,x+3,y+7);
            image.drawLine(x + 3,y+5,x+5,y+3);
            image.drawLine(x + 3,y+5,x+5,y+7);
        }
    }

    public void drawRoom(int i, int j, MazeRoom room)
    {
        int x,y;
        x = i * 16;
        y = j * 12;

        image.setColor(Color.GREEN);
        switch(room.level)
        {
            case 1:
            image.setColor(Color.YELLOW);
            break;
            case 2:
            image.setColor(Color.RED);
            break;
            case 3:
            image.setColor(Color.PINK);
            break;
        }

        image.drawRect(x,y,15,11);
        image.drawRect(x+1,y+1,13,9);

        image.setColor(Color.BLACK);

        if(room.North)
        {
            image.drawLine(x+6,y,x+9,y);
            image.drawLine(x+6,y+1,x+9,y+1);
        }
        if(room.South)
        {
            image.drawLine(x+6,y+11,x+9,y+11);
            image.drawLine(x+6,y+10,x+9,y+10);
        }
        if(room.East)
        {
            image.drawLine(x+15,y+4,x+15,y+7);
            image.drawLine(x+14,y+4,x+14,y+7);
        }
        if(room.West)
        {
            image.drawLine(x,y+4,x,y+7);
            image.drawLine(x+1,y+4,x+1,y+7);
        }

    }
}
