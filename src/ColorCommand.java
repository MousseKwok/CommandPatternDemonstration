import java.awt.Color;
import objectdraw.FilledRect;

/**
 * Color command that can change the color of the rectangle
 * @author Xijie Guo
 *
 */
public class ColorCommand extends Command{
	
	//The rectangle
	private FilledRect rect;
	
	//Previous color of the rectangle
	private Color prevColor;
	
	//Current color
	private Color color;
	private String string;
	
	/**
	 * Constructor
	 * @param color the color of rectangle
	 * @param string the string indicating which color
	 */
	public ColorCommand(Color color, String string) {
		this.color = color;
		this.string = string;
	}
	
	/**
	 * Save the state of the rectangle before the color is changed so that it can be restored on undo.
	 * Then execute the command. If the command has en effect, save it in the command history stack 
	 * so that it can be undone later
	 * @param rect the rectangle
	 * @return true if the command is executed and false if the command is not executing
	 */
	@Override
	public boolean execute(FilledRect rect) {
		saveState(rect);
		if(doCommand(rect)) {
			CommandHistory.record(this);
			return true;
		}
		return false;
	}
	
	/**
	 * Change the color of the rectangle
	 * @param rect the rectangle
	 * @return true after setting the new color and false if the new color has the same color 
	 * as the current color
	 */
	private boolean doCommand(FilledRect rect) {
		if(rect.getColor() == color) {
			return false;
		}
		rect.setColor(color);
		return true;
	}
	
	/**
	 * Save the state of the rectangle
	 * @param rect the rectangle
	 */
	@Override
	public void saveState(FilledRect rect) {
		this.rect = rect;
		prevColor = rect.getColor();
	}
	
	/**
	 * Restores the previous color
	 */
	public void undo() {
		rect.setColor(prevColor);
	}
	
	/**
	 * Re-executes the command
	 */
	public void redo() {
		doCommand(rect);
	}
	
	/**
	 * Get the stirng of the color
	 */
	@Override
	public String toString() {
		return string;
	}
}

