/**
 * ConsoleGui
 * @author AN - DISI - Unibo
===============================================================
The user hoits a button and a message with the same name is
sent to the WEnv by using WEnvConnSupportNoChannel.sendMessage
===============================================================
 */
package it.unibo.resumableBoundaryWalker.consolegui;

import it.unibo.resumableBoundaryWalker.interaction.IssObserver;
import it.unibo.resumableBoundaryWalker.wenv.RobotInputController;
import org.json.JSONObject;

import java.util.Observable;
import java.util.Observer;

public class ConsoleGui implements  Observer{	//Observer deprecated in 11 WHY?
private String[] buttonLabels  = new String[]  { "STOP", "RESUME" };
private IssObserver controller ;

	public ConsoleGui(IssObserver controller) {
		GuiUtils.showSystemInfo();
		ButtonAsGui concreteButton = ButtonAsGui.createButtons( "", buttonLabels );
		concreteButton.addObserver( this );
		this.controller = controller;
 	}

	public void update( Observable o , Object arg ) {	//Observable deprecated WHY?
		String move = arg.toString();
		//System.out.println("GUI input move=" + move);
		JSONObject json = new JSONObject();
		json.put("robotcmd", move.equals("STOP") ? "STOP" : "RESUME");
		//System.out.println("GUI input robotCmd=" + robotCmd );
		controller.handleInfo( json );
	}
	
	public static void main( String[] args) {

		RobotInputController controller = new RobotInputController(null, true,true);
		ConsoleGui consoleGui = new ConsoleGui( controller );
		String map = controller.doBoundary();

		System.out.println("");
		System.out.println("--------------------------");
		System.out.println("MAIN: BOUNDARY END");
		System.out.println("");
		System.out.println(map);
		System.out.println("");
		System.out.println("--------------------------");
	}
}

