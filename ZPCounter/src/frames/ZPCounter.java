package frames;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import panels.CalculationPanel;
import panels.ImagePanel;
import panels.MileagePanel;
import panels.EmployeePanel;
import panels.TimesheetPanel;

public class ZPCounter {

	private JFrame frmZpcounter;
	public static String db_name = "zp_data";
	private Connection con;
	private Statement s;

	private JComboBox<Object> month;
	private JComboBox<Object> year;

	private CalculationPanel calculation;
	private MileagePanel mileage;
	private ImagePanel image;
	private TimesheetPanel timesheet;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ZPCounter window = new ZPCounter();
					window.frmZpcounter.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * 
	 * @throws SQLException
	 */
	public ZPCounter() throws SQLException {

		JPasswordField pf = new JPasswordField();
		int okCxl = JOptionPane.showConfirmDialog(null, pf, "Введите пароль", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);

		String password = "";
		if (okCxl == JOptionPane.OK_OPTION) {
			password = new String(pf.getPassword());
		} else {
			System.exit(0);
		}

		con = DriverManager.getConnection(
				"jdbc:mysql://localhost/?" + "user=root&password=" + password + "&characterEncoding=utf8");
		// do we need "collation utf-8"?
		s = con.createStatement();

		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmZpcounter = new JFrame();
		frmZpcounter.addWindowListener(newListner());
		frmZpcounter.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);		
		frmZpcounter.setTitle("ZPCounter");
		frmZpcounter.setLocationByPlatform(true);
		frmZpcounter.getContentPane().setLayout(new BoxLayout(frmZpcounter.getContentPane(), BoxLayout.Y_AXIS));

		JPanel date = new JPanel();
		date.setAlignmentX(Component.LEFT_ALIGNMENT);
		frmZpcounter.getContentPane().add(date);

		month = new JComboBox<Object>();
		month.setAlignmentY(Component.TOP_ALIGNMENT);
		month.setAlignmentX(Component.RIGHT_ALIGNMENT);
		month.setMaximumRowCount(13);
		month.setModel(new DefaultComboBoxModel<Object>(new String[] { "Месяц", "Январь", "Февраль", "Март", "Апрель", "Май",
				"Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь" }));
		month.addItemListener(itemEvent -> updateFrame(itemEvent));

		year = new JComboBox<Object>();
		year.setAlignmentY(Component.TOP_ALIGNMENT);
		year.setAlignmentX(Component.RIGHT_ALIGNMENT);
		year.setModel(new DefaultComboBoxModel<Object>(new String[] { "Год", "2013", "2014", "2015", "2016", "2017", "2018",
				"2019", "2020", "2021" }));
		year.addItemListener(itemEvent -> updateFrame(itemEvent));

		date.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		date.add(month);
		date.add(year);

		calculation = new CalculationPanel(frmZpcounter, s);
		mileage = new MileagePanel(frmZpcounter, s);
		image = new ImagePanel(frmZpcounter, s);
		timesheet = new TimesheetPanel(frmZpcounter, s);
		//SomePanel[] panels = new SomePanel[] { calculation, mileage, image, timesheet };
		new EmployeePanel(frmZpcounter, s);

		frmZpcounter.pack();
	}
	
	private WindowAdapter newListner(){
		WindowAdapter l = new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				int reply = JOptionPane.showConfirmDialog(frmZpcounter, "Завершить работу приложения?", "",
						JOptionPane.YES_NO_OPTION);
				if (reply == JOptionPane.YES_OPTION) {
					frmZpcounter.dispose();
					try {
						con.close();
					} catch (SQLException e) {
						JOptionPane.showMessageDialog(frmZpcounter, e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
						e.printStackTrace();
					}
					System.exit(0);
				}
			}
		};
		return l;
	}

	private void updateFrame(ItemEvent itemEvent) {

		if (itemEvent.getStateChange() != ItemEvent.SELECTED)
			return;

		int monthNumber = month.getSelectedIndex();
		int yearNumber = year.getSelectedIndex();
		if (monthNumber == 0 || yearNumber == 0)
			return;

		yearNumber += 2012;

		try {
			mileage.updateAll(monthNumber, yearNumber);
			image.updateAll(monthNumber, yearNumber);
			timesheet.updateAll(monthNumber, yearNumber);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(frmZpcounter, e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		calculation.updateAll(monthNumber, yearNumber, mileage, image, timesheet, s);
	}
}
