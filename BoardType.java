import java.awt.Graphics;
import java.awt.Shape;
import java.util.ArrayList;

/**
 * Sets the amount of stones per pit and draws the graphics
 */
public interface BoardType 
{
    public void setInitial(int i);
    public void draw(Graphics g);
	public ArrayList<Shape> getList();
        
}
