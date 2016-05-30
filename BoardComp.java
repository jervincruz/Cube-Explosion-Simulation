import java.awt.Graphics;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JComponent;

/**
 * Defines the board style
 */

public class BoardComp extends JComponent
{	
	private BoardType style;
	
	public BoardComp(BoardType b, final GameModel m)
	{
		style = b;
		
		this.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){
					ArrayList<Shape> list = style.getList();
					for(int i=0; i<list.size(); i++){
						if(list.get(i).contains(e.getX(), e.getY()) && i!=6 && i!=13) // User's stores should not be clickable
						{
							m.makeMove(i); // if clicked on user turn's pit, make the move
							repaint();
						}
					}
				}
			});
	}
	
        @Override
	public void paintComponent(Graphics g)
	{	
		style.draw(g);
	}
}
