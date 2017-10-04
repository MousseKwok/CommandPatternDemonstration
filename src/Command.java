import objectdraw.FilledRect;
/**
 * Command class which has abstact methods that are implemented in its 
 * subclasses
 * @author Xijie Guo
 *
 */
public abstract class Command implements Cloneable{

	/**
	 * This method is a template method. subclasses need to define
	 * doCommands to carray the command actions
	 * @param rect the rectangle
	 * @return true if a command is executed
	 */
	public abstract boolean execute(FilledRect rect);
	
	/**
	 * Subclasses must define the undo method.
	 */
	public abstract void undo();
	
	/**
	 * Subclasses must define the redo method
	 */
	public abstract void redo();
	
	/**
	 * Creates and returns a copy of this object
	 */
	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			// Should not happen
			throw new AssertionError();
		}
	}
	
	public abstract void saveState(FilledRect rect);
}
