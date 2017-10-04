import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import objectdraw.FilledRect;
import objectdraw.Location;
import objectdraw.RandomIntGenerator;
import objectdraw.WindowController;

/**
 * A drawing program that draws a rectangle.  The user can change its size and color
 * and drag the rectangle to a new location.
 *
 */
@SuppressWarnings("serial")
public class DrawingProgram extends WindowController {
	// Approximate height of the menu bar added by Java
	private static final int MENU_BAR_HEIGHT = 50;
	
	//Three dimensions of the rectangle
	private static final int MIN_SIZE = 10;
	private static final int MEDIUM_SIZE = 50;
	private final static int MAX_SIZE = 100;

	//List holding all rectangles created
	private ArrayList<FilledRect> shapeList = new ArrayList<FilledRect>();
	
	// The shape on the screen
	private FilledRect shape;

	// Where was the mouse the last time onMousePress/Drag was called?
	// Used to calculate how far to move the rectangle when dragging
	private Location lastMouse;  

	// The shape that the user is dragging.  This is null if
	// the user is not dragging the shape.
	private FilledRect selectedShape;

	// Menu to change color of the shape
	private JComboBox<Command> colorMenu = new JComboBox<>();

	// Menu to change size of shape
	private JComboBox<Command> sizeMenu = new JComboBox<>();
	
	//Button to undo commands
	private JButton undoButton;
	
	//Button to redo commands
	private JButton redoButton;
	
	//Button to create more rectangles
	private JButton newRectButton;
	
	//The drag command
	private DragCommand dragCommand;
	
	//The remove command
	private RemoveCommand removeCommand = new RemoveCommand();

	/** 
	 * Draws the program with the menus and a blank drawing area
	 */
	@Override
	public void begin() {
		resize (600, 500);
		JPanel southPanel = new JPanel();
		southPanel.add (createSizeMenu());
		southPanel.add (createColorMenu());
		undoButton = createUndoButton();
		redoButton = createRedoButton();
		newRectButton = createMoreRectButton();
		southPanel.add(undoButton);
		southPanel.add(redoButton);
		southPanel.add(newRectButton);
		add (southPanel, BorderLayout.SOUTH);

		Location defaultLoc = new Location(canvas.getWidth() / 2 - MAX_SIZE / 2, 
				(canvas.getHeight() - MENU_BAR_HEIGHT) / 2 - MAX_SIZE / 2);
		// Create and display the rectangle
		FilledRect defaultShape = new FilledRect(defaultLoc, MAX_SIZE, MAX_SIZE, canvas);
		shape = defaultShape;
		//Add the default rectangle to the list
		shapeList.add(defaultShape);
		dragCommand = new DragCommand(defaultShape, defaultLoc);
		removeCommand.execute(shape);
	}

	/**
	 * Creates the size menu and attaches an actionlistener to it
	 * When the menu is clicked, implement corresponding commands
	 * @return the size menu 
	 */
	private JComboBox<Command> createSizeMenu() {
		// Put values in the size menu
		sizeMenu.addItem(new SizeCommand(MAX_SIZE, "Big"));
		sizeMenu.addItem(new SizeCommand(MEDIUM_SIZE, "Medium"));
		sizeMenu.addItem(new SizeCommand(MIN_SIZE, "Small"));
		sizeMenu.addActionListener(new ActionListener() {

			/**
			 * Determine which menu entry the user selected and
			 * execute the corresponding command.  Remember the
			 * command so that it can be undone later.
			 */
			@Override
			public void actionPerformed(ActionEvent evt) {
				Command selectedCommand = (Command) sizeMenu.getSelectedItem();
				if(selectedCommand.execute(shape)) {
					undoButton.setEnabled(true);
					redoButton.setEnabled(false);
					CommandHistory.clearRedoCommands();
				}
			}
		});
		return sizeMenu;
	}

	/**
	 * Creates the color menu and attaches an actionListener to it
	 * When the menu is clicked, implement corresponding commands
	 * @return the color menu
	 */
	private JComboBox<Command> createColorMenu() {
		// Create color menu
		colorMenu.addItem(new ColorCommand(Color.BLACK, "Black"));
		colorMenu.addItem(new ColorCommand(Color.BLUE, "Blue"));
		colorMenu.addItem(new ColorCommand(Color.RED, "Red"));
		colorMenu.addItem(new ColorCommand(Color.GREEN, "Green"));
		colorMenu.addItem(new ColorCommand(Color.YELLOW, "Yellow"));
		colorMenu.addActionListener(new ActionListener() {

			/**
			 * Determine which menu entry the user selected and
			 * execute the corresponding command.  Remember the
			 * command so that it can be undone later.
			 */
			@Override
			public void actionPerformed(ActionEvent evt) {
				Command selectedCommand = (Command) colorMenu.getSelectedItem();
				if(selectedCommand.execute(shape)) {
					undoButton.setEnabled(true);
					redoButton.setEnabled(false);
					CommandHistory.clearRedoCommands();
				}
			}
		});
		return colorMenu;
	}  


	/**
	 * Create undo button and attaches the actionlistener
	 * @return the undo button
	 */
	private JButton createUndoButton() {
		JButton button = new JButton("Undo");
		button.setEnabled(false);
		button.addActionListener(new ActionListener() {

			//Undo the last command executed
			@Override
			public void actionPerformed(ActionEvent e) {
				
				CommandHistory.undo();
				if(CommandHistory.canUndo()) {
					undoButton.setEnabled(false);
				}
				redoButton.setEnabled(true);
			}
			
		});
		return button;
	}

	/**
	 * Creates the redo button and attaches the actionlistener
	 * @return the redo button
	 */
	private JButton createRedoButton() {
		JButton button = new JButton("Redo");
		button.setEnabled(false);
		button.addActionListener(new ActionListener() {

			//redo the last command executed
			@Override
			public void actionPerformed(ActionEvent e) {
				CommandHistory.redo();
				if(CommandHistory.canRedo()) {
					redoButton.setEnabled(false);
				}
				undoButton.setEnabled(true);
			}
			
		});
		return button;
	}
	
	/**
	 * Creates the button to add more rectangles and attaches 
	 * the actionlistener
	 * @return the button
	 */
	private JButton createMoreRectButton() {
		JButton button = new JButton("New Rectangle");
		//Construct a random int generator
		RandomIntGenerator positionGen = new RandomIntGenerator(0, 400);
		
		button.addActionListener(new ActionListener() {
			
			//Create a new rectangle each time the user clicks the button
			@Override
			public void actionPerformed(ActionEvent e) {
				//Generate a random number
				int position = positionGen.nextValue();
				//Random location
				Location loc  = new Location(position, position);
				//Create a rectangle in the random location
				FilledRect newShape = new FilledRect(loc, MAX_SIZE, MAX_SIZE, canvas);
				selectedShape = newShape;
				shapeList.add(newShape);
			}
			
		});
		return button;
	}
	
	/** 
	 * Select the object if it is pressed on and start a drag of that object
	 * @param loc where the user pressed the mouse button down
	 */
	@Override
	public void onMousePress(Location loc) {
		if (shape.contains(loc)) {
			selectedShape = shape;
			lastMouse = loc;
			//Save state here to remember the initial location of the rectangle
			dragCommand.saveState(shape);
			
		}
	}

	/** 
	 * Drag the shape with the mouse. 
	 * @param loc where the mouse is 
	 */
	@Override
	public void onMouseDrag(Location loc) {
		if (selectedShape != null) {
			// Move the shape and the selection border around the shape
			selectedShape.move(loc.getX() - lastMouse.getX(), loc.getY() - lastMouse.getY());
			lastMouse = loc;
		}
	}

	/** 
	 * Stop a drag.  
	 * @param point where the mouse is.
	 */
	@Override
	public void onMouseRelease(Location point) {
		//At the first beginning, if we simply click on the blank screen rather than the rectangle
		//nothing should implement, this is to avoid nullpointer exception
		if(selectedShape == shape) {
			if(dragCommand.execute(selectedShape)) {
				selectedShape = null;
				undoButton.setEnabled(true);
			}
		}
	}
	
	/**
	 * Select the rectangle. If the rectangle is not the default rectangle, 
	 * the user need to click on the ractangle to select it
	 */
	@Override
	public void onMouseClick(Location point) {
		//Make sure undo and redo commands only operate on the selected rectangle 
		CommandHistory.clearRedoCommands();
		CommandHistory.clearUndoCommands();
		//Make sure if the user clicks on the blank region, nothing happens
		//Avoid nullpointer exception
		if(getSelectedRect(point) != null) {
			shape = getSelectedRect(point);
			removeCommand.execute(shape);
		}
	}
	
	/**
	 * Get the selected rectangle
	 * @param point where the mouse is
	 * @return the rectangle that is selected
	 */
	private FilledRect getSelectedRect(Location point) {
		//Loop through the array list to check which rectangle contains point(selected)
		for(FilledRect rect : shapeList) {
			if(rect.contains(point)) {
				return rect;
			}
		}
		return null;
	}
	
}
