package gui;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

public class Window {

	protected Shell shlKnotSearcher;
	private Text text;
	private Text text_1;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Window window = new Window();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlKnotSearcher.open();
		shlKnotSearcher.layout();
		while (!shlKnotSearcher.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlKnotSearcher = new Shell();
		shlKnotSearcher.setSize(459, 334);
		shlKnotSearcher.setText("Knot Searcher");
		
		Button btnSearch = new Button(shlKnotSearcher, SWT.NONE);
		btnSearch.setBounds(358, 158, 75, 25);
		btnSearch.setText("Search");
		
		Label lblDistanceFunction = new Label(shlKnotSearcher, SWT.NONE);
		lblDistanceFunction.setBounds(10, 67, 98, 15);
		lblDistanceFunction.setText("Distance Function:");
		
		Label lblNewLabel = new Label(shlKnotSearcher, SWT.NONE);
		lblNewLabel.setImage(SWTResourceManager.getImage("C:\\Users\\miguel\\Desktop\\Tese\\Images\\icon.png"));
		lblNewLabel.setBounds(10, 10, 75, 39);
		
		Combo combo = new Combo(shlKnotSearcher, SWT.NONE);
		combo.setToolTipText("Select One");
		combo.setBounds(114, 64, 91, 23);
		combo.add("Euclidean", 0);
		combo.add("Hamming", 1);
		combo.select(0);
		
		Label lblNewLabel_1 = new Label(shlKnotSearcher, SWT.NONE);
		lblNewLabel_1.setImage(SWTResourceManager.getImage("C:\\Users\\miguel\\Desktop\\Tese\\Images\\iconIST.jpg"));
		lblNewLabel_1.setBounds(393, 10, 40, 46);
		
		Label lblSearchFeatures = new Label(shlKnotSearcher, SWT.NONE);
		lblSearchFeatures.setBounds(10, 96, 85, 15);
		lblSearchFeatures.setText("Search Features:");
		
		text = new Text(shlKnotSearcher, SWT.BORDER);
		text.setBounds(10, 117, 195, 66);
		
		Composite composite = new Composite(shlKnotSearcher, SWT.NONE);
		composite.setBounds(211, 10, 177, 142);
		
		Label lblNewLabel_2 = new Label(composite, SWT.NONE);
		lblNewLabel_2.setBounds(0, 0, 87, 15);
		lblNewLabel_2.setText("QUERY RULES:");
		
		Label lblWord = new Label(composite, SWT.NONE);
		lblWord.setBounds(0, 21, 55, 15);
		lblWord.setText("word");
		
		Label lblsetOfWords = new Label(composite, SWT.NONE);
		lblsetOfWords.setBounds(0, 40, 76, 15);
		lblsetOfWords.setText("(set of words)");
		
		Label lbluncertainty = new Label(composite, SWT.NONE);
		lbluncertainty.setBounds(0, 60, 76, 15);
		lbluncertainty.setText("?uncertainty");
		
		Label lblExampleTemporaryeasy = new Label(composite, SWT.NONE);
		lblExampleTemporaryeasy.setBounds(0, 91, 177, 15);
		lblExampleTemporaryeasy.setText("Examples:");
		
		Label lbldecorativeshorteningRope = new Label(composite, SWT.NONE);
		lbldecorativeshorteningRope.setBounds(0, 127, 177, 15);
		lbldecorativeshorteningRope.setText("?decorative ?(shortening rope)");
		
		Label lblTemporaryeasyTo = new Label(composite, SWT.NONE);
		lblTemporaryeasyTo.setBounds(0, 112, 119, 15);
		lblTemporaryeasyTo.setText("temporary (easy to tie)");
		
		Button btnNewButton = new Button(shlKnotSearcher, SWT.NONE);
		btnNewButton.setBounds(95, 95, 20, 20);
		btnNewButton.setText("?");
		
		text_1 = new Text(shlKnotSearcher, SWT.BORDER);
		text_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		text_1.setEditable(false);
		text_1.setBounds(9, 199, 424, 87);

	}
}
