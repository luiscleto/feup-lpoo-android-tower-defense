package pt.up.fe.lpoo.towerdefense;

import pt.up.fe.lpoo.framework.Graphics;
import android.graphics.Paint;

/**
 * Class used to parse strings of text and separate them by lines
 * @author Joao Marinheiro
 * @author Luis Cleto
 */
public class TextParser {
	private String text;
	private int textSize;
	
	/**
	 * Constructor for the TextParser. Calculates the number of lines in the text.
	 * @param text text to be parsed
	 */
	public TextParser(String text)
	{
		this.text = text;
		textSize = 0;
		
		for(int i = 0; i < text.length(); i++)
		{
			if(text.charAt(i) == '\n')
				textSize++;
		}
		if(!text.endsWith("\n"))
			textSize++;
	}
	/**
	 * @param lineNum number of the line to search for
	 * @return returns a string with the specified line
	 */
	private String getLine(int lineNum)
	{
		if(lineNum < 0 || lineNum > textSize)
			return null;
		
		int lineStart = 0;
		int currChar = 0;
		
		for(int currLine = 0; currLine != lineNum; currLine++)
		{	
			while(text.charAt(currChar) != '\n')
			{	
				currChar++;
				
				if(currChar >= text.length())
					return null;
			}
			
			currChar++;
			
			if(currChar >= text.length())
				return null;
		}
		
		lineStart = currChar;
		
		while(text.charAt(currChar) != '\n' || currChar >= text.length())
		{
			currChar++;
		}
		
		return text.substring(lineStart, currChar);
	}
	
	/**
	 * Draws the text in the parser line by line on the screen in the specified position
	 * @param font Paint object to be used for the drawing
	 * @param x drawing starting x position
	 * @param y drawing starting y position
	 * @param spacing number of pixels between each line
	 * @param g Graphics object containing the string drawing functions to use for the drawing
	 */
	public void drawText(Paint font, int x, int y, int spacing, Graphics g)
	{	
		for(int i = 0; i < textSize; i++)
		{
			int currY = y + spacing*i;
			
			g.drawString(getLine(i), x, currY, font);
		}
	}
}
