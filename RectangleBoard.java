import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class RectangleBoard implements BoardType, ChangeListener 
{
	private ArrayList<Shape> list = new ArrayList<Shape>();
	private Path2D.Double    path = new Path2D.Double();
	private int width  = 80;
	private int height = 80;
	private int[] stones = new int[14];
	private GameModel m;
    private int initialStones =3;
	
	public RectangleBoard(GameModel m)
	{
		this.m = m;
		
		//defineMancala();
		definePit();
		
		for(int i=0; i<13; i++)
		{
			stones[i] = m.getStone(i);
		}
	}
	
        	
public void setInitial(int i)
{
   initialStones=i;
}
   
	public void defineMancala() 
	{
		Rectangle2D.Double mancala1 = new Rectangle2D.Double(0,0,width,2.5*width);
		Rectangle2D.Double mancala2 = new Rectangle2D.Double(7*width + 7*width/4, 0, width, 2.5*height);
	}

	public void definePit() 
	{
        Rectangle2D.Double mancala1 = new Rectangle2D.Double(0,0,width,2.5*width);
		Rectangle2D.Double mancala2 = new Rectangle2D.Double(7*width + 7*width/4, 0, width, 2.5*height);
                
                
		for(int i=6; i>0; i--)
		{
			Rectangle2D.Double pit  = new Rectangle2D.Double(i*5*width/4, 0, width, height);
		
			list.add(pit);			
			path.append(pit, false);
		}
                 list.add(mancala1);
                 path.append(mancala1, false);    
           
		for(int i=1; i<7; i++)
		{
			Rectangle2D.Double pit2 = new Rectangle2D.Double(i*5*width/4, 3*height/2, width, height);
			
			list.add(pit2);
			path.append(pit2, false);
		}
                list.add(mancala2);
                    path.append(mancala2, false);
               
	}

	public ArrayList<Shape> getList()
	{
		return list;
	}
	
	public void draw(Graphics g)
	{
		Path2D.Double path2 = new Path2D.Double();
		Graphics2D g2 = (Graphics2D) g;
		
		g2.draw(path);
		
		for(int i=0; i<14; i++)
		{
			Rectangle2D rect = list.get(i).getBounds2D();
			int num = stones[i];
			
			if(num <=9)
			{
				for(int j=0; j<num; j++)
				{
					int x = (int) Math.floor(j/3);
				
					path2.append(new Rectangle2D.Double(rect.getX()+width/6 +  (j-3*x)*rect.getWidth()/4 ,rect.getY()+height/6 + (rect.getHeight()/4)*x, rect.getWidth()/5, rect.getWidth()/5 ), false);
				}
			}
			else
				{
					path2.append(new Rectangle2D.Double(rect.getCenterX() - rect.getWidth()/8, rect.getCenterY(), rect.getWidth()/5, rect.getWidth()/5 ), false);
					g2.drawString("(" + num + ")" , (int) (rect.getCenterX()- 10), (int) (rect.getCenterY()-10));
				}
		}
		
		g2.fill(path2);
	}
	
	public void setColor(Color color) 
	{
		color = color.BLUE;
	}

	public void stateChanged(ChangeEvent arg0) 
	{
		int[] list = m.getData();
		for(int i=0; i<14; i++)
		{
                    stones[i] = list[i];				
		}
	}
}
