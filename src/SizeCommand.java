import objectdraw.FilledRect;

/**
 * Size command class that can change the size of the rectangle
 * @author Xijie Guo
 *
 */
public class SizeCommand extends Command{
	
	//The rectangle
	private FilledRect rect;
	//Previous size
	private double prevSize;
	
	//Current size
	private double size;
	
	private String string;
	
	/**
	 * Constructor for sizeCommand
	 * @param size the size of rectangle
	 * @param string the string indicating which color 
	 */
	public SizeCommand(double size, String string) {
		this.size = size;
		this.string = string;
	}
	
	/**
	 * Save the state of the rectangle before the size is changed so that it can be restored on undo.
	 * Then execute the command. If the command has en effect, save it in the command history stack 
	 * so that it can be undone later
	 * @param rect the rectangle
	 * @return true if the command is executed
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
	 * Change the size of the rectangle
	 * @param rect the rectangle
	 * @return true after setting the new size of the rectangle
	 * return false if the new size is the same as the current size
	 */
	private boolean doCommand(FilledRect rect) {
		if(rect.getWidth() == size) {
			return false;
		}
		rect.setSize(size, size);
		return true;
	}
	
	/**
	 * Save the state of the rectangle
	 * @param rect the rectangle
	 */
	@Override
	public void saveState(FilledRect rect) {
		this.rect = rect;
		prevSize = rect.getWidth();
	}

	/**
	 * Undo the command
	 */
	@Override
	public void undo() {
		rect.setSize(prevSize, prevSize);
	}
	
	/**
	 * Redo the command
	 */
	public void redo() {
		doCommand(rect);
	}
	
	/**
	 * Get the string of the size
	 */
	@Override
	public String toString() {
		return string;
	}

}

