import java.util.Stack;

/**
 * Keeps track of the commands that can be undone and redone
 * @author Barbara Lerner & Xijie Guo
 *
 */
public class CommandHistory {

	// The commands that have been executed and not yet undone.
	private static Stack<Command> undoCommands = new Stack<>();

	// The commands that have been undone and not yet redone.
	private static Stack<Command> redoCommands = new Stack<>();
	

	/**
	 * Adds a command to the history.
	 * @param cmd the command to add
	 */
	public static void record (Command cmd) {
		// Since there is just one instance of each command instantiated
		// when we create the GUI, we would have only one undo state 
		// available without the clone.  We need to clone the command
		// to support multi-level undo.
		undoCommands.push((Command) cmd.clone());
	}
	
	/**
	 * Undo the last command and move it to the redo stack.
	 */
	public static void undo () {
		Command cmd = undoCommands.pop();
		cmd.undo();
		redoCommands.push(cmd);
	}

	/**
	 * Redo the last command that was undone and move it to the undo stack.
	 */
	public static void redo () {
		Command cmd = redoCommands.pop();
		cmd.redo();
		undoCommands.push(cmd);
	}

	/**
	 * Checks if there still are commands that can be undone
	 * @return true if there are no commands left to undo
	 */
	public static boolean canUndo() {
		return undoCommands.isEmpty();
	}

	/**
	 * Checks if there still are commands that can be redone
	 * @return true if there are no commands left to redo
	 */
	public static boolean canRedo() {
		return redoCommands.isEmpty();
	}

	/**
	 * Remove all of the commands from the redo stack.
	 */
	public static void clearRedoCommands() {
		redoCommands.clear();
	}
	
	/**
	 * Removes all of the commands from the undo stack
	 */
	public static void clearUndoCommands() {
		undoCommands.clear();
	}
}
