package pt.up.fe.lpoo.towerdefense;

import pt.up.fe.lpoo.framework.Graphics;
import pt.up.fe.lpoo.framework.Image;

/**
 * Class that contains description objects for the instructions screen. Associates a sprite with a text description.
 * @author Joao Marinheiro
 * @author Luis Cleto
 */
public class DescriptionObject {
	private Image icon;
	private int x, y;
	private TextParser description;
	
	/**
	 * Constructor for the DescriptionObject
	 * @param icon sprite of the object
	 * @param description description of the object
	 * @param x x position where to draw the object
	 * @param y y position where to draw the object
	 */
	public DescriptionObject(Image icon, TextParser description, int x, int y)
	{
		this.icon = icon;
		this.description = description;
		this.x = x;
		this.y = y;
	}
	/**
	 * Draws the icon of the object on the screen at the icon's position
	 * @param g
	 */
	public void draw(Graphics g)
	{
		g.drawImage(icon,x,y);
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public TextParser getText()
	{
		return description;
	}
	
	public int getWidth()
	{
		return icon.getWidth();
	}
	
	public int getHeight()
	{
		return icon.getHeight();
	}
}
