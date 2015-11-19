package panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.io.File;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import backend.xml_parser.TableNode;
import backend.xml_parser.XmlParser;

public class SomePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	protected JLabel panelNameLabel;
	protected JLabel isReadyLabel;
	protected JButton watchResults;
	protected JButton enterData;
	protected Statement s;
	
	protected TableNode tabNode; // XML data in DOM object.

	static Color red = Color.red;
	static Color dark_green = new Color(0, 125, 0);
	protected String whenDoesntReady = "Нет", whenReady = "Есть";
	protected boolean ready;
	
	int monthNumber = 0;
	int yearNumber = 0;

	public SomePanel(JFrame frm, Statement s) {
		super();
		this.s = s; 
		this.setAlignmentX(Component.LEFT_ALIGNMENT);
		frm.getContentPane().add(this);
		this.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		panelNameLabel = new JLabel("Something:");
		this.add(panelNameLabel);

		isReadyLabel = new JLabel();
		this.add(isReadyLabel);

		watchResults = new JButton("Посмотреть");
		watchResults.addActionListener(event -> watchResults());
		this.add(watchResults);

		enterData = new JButton("Ввести");
		enterData.addActionListener(event -> enterData());
		this.add(enterData);

		setReady(false);
	}

	protected void setReady(boolean ready) {
		this.ready = ready;
		if (!ready) {
			isReadyLabel.setText(whenDoesntReady);
			isReadyLabel.setForeground(red);

			watchResults.setEnabled(false);
		} else {
			isReadyLabel.setText(whenReady);
			isReadyLabel.setForeground(dark_green);

			watchResults.setEnabled(true);
		}
	}
	public boolean isReady()
	{
		return ready;
	}

	protected void watchResults() {
		// TODO Auto-generated method stub

	}

	protected boolean enterData() {
		if (this.monthNumber == 0 || this.yearNumber == 0)
		{
			JOptionPane.showMessageDialog(this, "Не выбраны месяц или год.", "Ошибка", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		JFileChooser chooser = new JFileChooser();
		// for other PC it may be path like this: ("/")
		chooser.setCurrentDirectory(new File ("/project/workspace/MAI/ServiceDeskReports/"));
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
		        "XML files", "xml");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showDialog(this, "Выбрать");
		if(returnVal == JFileChooser.CANCEL_OPTION || returnVal == JFileChooser.ABORT)
	    {
			return false;
	    }
	    if(returnVal == JFileChooser.APPROVE_OPTION)
	    {
	    	try
			{
	    		XmlParser parser = new XmlParser(chooser.getSelectedFile());
				tabNode = parser.getTabNode();
			} catch (Exception e)
			{
				JOptionPane.showMessageDialog(this, e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
				return false;
			}
	    }
	    return true;
	}

	public void updateAll(int monthNumber, int yearNumber) throws SQLException {
		this.monthNumber = monthNumber;
		this.yearNumber = yearNumber;
		// TODO Auto-generated method stub
	}

	protected String convertData(String oldData){
		String convertedData="";
		if (oldData==null) return "";
		String []a = oldData.trim().split("\\.");
		for (int i=0; i<a.length-1; i++)
		{
			convertedData+=a[i]+"-";
		}
		convertedData+=a[a.length-1];
		return convertedData;
	}
	
	protected int getMonthFromDate(String oldData) throws Exception{
		if (oldData==null) throw new Exception ("В XML файле ошибка, нет даты.");
		String []a = oldData.trim().split("\\.");
		return Integer.parseInt(a[1]);
	}
	
	protected int getYearFromDate(String oldData) throws Exception{
		if (oldData==null)  throw new Exception ("В XML файле ошибка, нет даты.");
		String []a = oldData.trim().split("\\.");
		return Integer.parseInt(a[2]);
	}

}
