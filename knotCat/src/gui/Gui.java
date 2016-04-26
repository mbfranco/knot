package gui;
import org.eclipse.swt.widgets.*;

public class Gui {

	public static void main (String [] args) {
		Display display = new Display ();
		Shell shell = new Shell(display);
		shell.open ();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	}
	
}
