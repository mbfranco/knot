package gui;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;

import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JTextArea;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.wb.swt.SWTResourceManager;

import knotCat.patterns.cluster.Browser;
import knotCat.patterns.cluster.BrowserProxy;
import knotCat.patterns.cluster.ClusterSearchResult;
import knotCat.patterns.cluster.Feature;
import knotCat.patterns.cluster.Knot;
import knotCat.patterns.cluster.Search;

import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class Window {

	protected Shell shlKnotSearcher;
	private Text textQueryBox;
	private Text textResultsBox;
	private double spinnerResult;

	LinkedList<Feature> featureNames;
	LinkedList<Knot> knotList;
	LinkedList<String> knotNames;
	
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
		shlKnotSearcher.setSize(515, 334);
		shlKnotSearcher.setText("Knot Searcher");
		
		Button btnSearch = new Button(shlKnotSearcher, SWT.NONE);
		btnSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				String result = "";
				
				BrowserProxy b = new BrowserProxy();
				try {
					result = b.doBrowserProxyStuff(textQueryBox.getText(), spinnerResult);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				textResultsBox.setText(result);
			}
		});
		btnSearch.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				//TODO
//				Search s = new Search(browser);
//				List<ClusterSearchResult> result = s.searchForKnot("tie.bight use.hand-hold use.shoulder-hold haul.gun-to-position", 0.5, "hamming");
//				textQueryBox.getText();
			}
		});
		btnSearch.setBounds(414, 158, 75, 25);
		btnSearch.setText("Search");
		
		Label lblMinProperty = new Label(shlKnotSearcher, SWT.NONE);
		lblMinProperty.setBounds(10, 67, 142, 15);
		lblMinProperty.setText("Minimum Probability [0,1]:");
		
		Label lblKnotImage = new Label(shlKnotSearcher, SWT.NONE);
		lblKnotImage.setImage(SWTResourceManager.getImage("C:\\Users\\miguel\\Desktop\\Tese\\Images\\icon.png"));
		lblKnotImage.setBounds(10, 10, 75, 39);
		
		Label lblSearchFeatures = new Label(shlKnotSearcher, SWT.NONE);
		lblSearchFeatures.setBounds(10, 95, 85, 15);
		lblSearchFeatures.setText("Search Features:");
		
		textQueryBox = new Text(shlKnotSearcher, SWT.BORDER);
//		textQueryBox. addVerifyListener(new VerifyListener() {
//			public void verifyText(VerifyEvent e) {
//			}
//		});
		textQueryBox.setBounds(10, 117, 307, 66);
		
		Composite compositeHelpTextBox = new Composite(shlKnotSearcher, SWT.NONE);
		compositeHelpTextBox.setBounds(323, 10, 166, 142);
		
		Label lblNewLabel_2 = new Label(compositeHelpTextBox, SWT.NONE);
		lblNewLabel_2.setBounds(0, 0, 87, 15);
		lblNewLabel_2.setText("QUERY RULES:");
		
		Label lblWord = new Label(compositeHelpTextBox, SWT.NONE);
		lblWord.setBounds(0, 21, 55, 15);
		lblWord.setText("word");
		
		Label lblsetOfWords = new Label(compositeHelpTextBox, SWT.NONE);
		lblsetOfWords.setBounds(0, 40, 76, 15);
		lblsetOfWords.setText("(set of words)");
		
		Label lbluncertainty = new Label(compositeHelpTextBox, SWT.NONE);
		lbluncertainty.setBounds(0, 60, 76, 15);
		lbluncertainty.setText("?uncertainty");
		
		Label lblExampleTemporaryeasy = new Label(compositeHelpTextBox, SWT.NONE);
		lblExampleTemporaryeasy.setBounds(0, 91, 177, 15);
		lblExampleTemporaryeasy.setText("Examples:");
		
		Label lbldecorativeshorteningRope = new Label(compositeHelpTextBox, SWT.NONE);
		lbldecorativeshorteningRope.setBounds(0, 127, 177, 15);
		lbldecorativeshorteningRope.setText("?decorative ?(shortening rope)");
		
		Label lblTemporaryeasyTo = new Label(compositeHelpTextBox, SWT.NONE);
		lblTemporaryeasyTo.setBounds(0, 112, 119, 15);
		lblTemporaryeasyTo.setText("temporary (easy to tie)");
		
		Label lblIST_logo = new Label(compositeHelpTextBox, SWT.NONE);
		lblIST_logo.setBounds(126, 0, 40, 46);
		lblIST_logo.setImage(SWTResourceManager.getImage("C:\\Users\\miguel\\Desktop\\Tese\\Images\\iconIST.jpg"));
		
		Button btnHelp = new Button(shlKnotSearcher, SWT.NONE);
		btnHelp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
			}
		});
		btnHelp.setBounds(100, 93, 20, 20);
		btnHelp.setText("?");
		
		textResultsBox = new Text(shlKnotSearcher, SWT.BORDER);
		textResultsBox.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
			}
		});
		textResultsBox.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		textResultsBox.setEditable(false);
		textResultsBox.setBounds(9, 199, 480, 87);
		
		final Spinner spinner = new Spinner(shlKnotSearcher, SWT.BORDER);
		spinner.setBounds(158, 65, 50, 22);
		// allow 3 decimal places
	    spinner.setDigits(2);
	    // set the minimum value to 0.001
	    spinner.setMinimum(0);
	    // set the maximum value to 20
	    spinner.setMaximum(100);
	    
	    spinnerResult = spinner.getDigits()/100;
	    
	}
}
