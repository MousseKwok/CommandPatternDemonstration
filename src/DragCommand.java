import objectdraw.FilledRect;
import objectdraw.Location;

/**
 * Implements drag command to carry out undo and redo options
 * @author Xijie Guo
 *
 */
public class DragCommand extends Command{
	
	//The rectangle
	private FilledRect rect;
	
	//The previous location
	private Location prevLocation;
	
	//Current location
	private Location currentLocation;

	/**
	 * Constructor
	 * @param rect the rectangle
	 * @param loc the location
	 */
	public DragCommand(FilledRect rect, Location loc) {
		this.rect = rect;
		this.currentLocation = loc;
	}
	
	/**
	 * If the rectangle has been dragged, add the command to the command history
	 * @param rect the rectangle
	 */
	@Override
	public boolean execute(FilledRect rect) {
		if(!currentLocation.equals(rect.getLocation())) {
			CommandHistory.record(this);
			return true;
		}
		return false;
	}

	/**
	 * Undo the command
	 */
	@Override
	public void undo() {
		currentLocation = rect.getLocation();
		rect.moveTo(prevLocation);
	}

	/**
	 * Redo the command
	 */
	@Override
	public void redo() {
		rect.moveTo(currentLocation);
	}
	
	/**
	 * Save the state of the rectangle. For drag, the state is 
	 * actually saved when the mouse has been pressed to remember 
	 * the initial location of the rectangle
	 * @param rect the rectangle
	 */
	@Override
	public void saveState(FilledRect rect) {
		this.rect = rect;
		prevLocation = rect.getLocation();
	}

}

