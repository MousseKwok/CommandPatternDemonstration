 import objectdraw.FilledRect;
 
 /**
  * Remove command class to undo the creation of the rectangle
  * @author Xijie Guo
  *
  */
 public class RemoveCommand extends Command {

	//The rectangle
	private FilledRect rect;
	
	/**
	 * Save the state of the rectangle and put it on the command history
	 * to be used later
	 * @param rect the rectangle
	 */
	@Override
	public boolean execute(FilledRect rect) {
		saveState(rect);
		CommandHistory.record(this);
		return true;
	}

	/**
	 * Undo the command
	 */
	@Override
	public void undo() {
		rect.hide();
	}

	/**
	 * Redo the command
	 */
	@Override
	public void redo() {
		rect.show();
	}
	
	/**
	 * Save the state of the rectangle
	 * @param rect the rectangle
	 */
	@Override
	public void saveState(FilledRect rect) {
		this.rect = rect;
	}

}
