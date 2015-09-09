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
import panels.SomePanel;
import panels.StaffPanel;
import panels.TimesheetPanel;

public class ZPCounter {

	private JFrame frmZpcounter;

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

	public static String db_name = "zp_data";
	private Connection con;
	private Statement s;

	private JComboBox month;
	private JComboBox year;

	private CalculationPanel calculation;
	private MileagePanel mileage;
	private ImagePanel image;
	private TimesheetPanel timesheet;
	private StaffPanel staff;
	private SomePanel[] panels;

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmZpcounter = new JFrame();
		frmZpcounter.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				int reply = JOptionPane.showConfirmDialog(frmZpcounter, "Завершить работу приложения?", "",
						JOptionPane.YES_NO_OPTION);
				if (reply == JOptionPane.YES_OPTION) {
					frmZpcounter.dispose();
					try {
						con.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.exit(0);
				}
			}
		});
		frmZpcounter.setTitle("ZPCounter");
		frmZpcounter.setLocationByPlatform(true);
		frmZpcounter.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmZpcounter.getContentPane().setLayout(new BoxLayout(frmZpcounter.getContentPane(), BoxLayout.Y_AXIS));

		JPanel date = new JPanel();
		date.setAlignmentX(Component.LEFT_ALIGNMENT);
		frmZpcounter.getContentPane().add(date);

		month = new JComboBox();
		month.setAlignmentY(Component.TOP_ALIGNMENT);
		month.setAlignmentX(Component.RIGHT_ALIGNMENT);
		month.setMaximumRowCount(13);
		month.setModel(new DefaultComboBoxModel(new String[] { "Месяц", "Январь", "Февраль", "Март", "Апрель", "Май",
				"Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь" }));
		month.addItemListener(itemEvent -> updateFrame(itemEvent));

		year = new JComboBox();
		year.setAlignmentY(Component.TOP_ALIGNMENT);
		year.setAlignmentX(Component.RIGHT_ALIGNMENT);
		year.setModel(new DefaultComboBoxModel(
				new String[] { "Год", "2013", "2014", "2015", "2016", "2017", "2018", "2019", "2020", "2021" }));
		year.addItemListener(itemEvent -> updateFrame(itemEvent));

		date.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		date.add(month);
		date.add(year);

		calculation = new CalculationPanel(frmZpcounter, s);
		mileage = new MileagePanel(frmZpcounter, s);
		image = new ImagePanel(frmZpcounter, s);
		timesheet = new TimesheetPanel(frmZpcounter, s);
		panels = new SomePanel[] { calculation, mileage, image, timesheet };
		staff = new StaffPanel(frmZpcounter, s);

		frmZpcounter.pack();
	}

	private void updateFrame(ItemEvent itemEvent) {

		if (itemEvent.getStateChange() != ItemEvent.SELECTED)
			return;
		// TODO
		int monthNumber = month.getSelectedIndex();
		int yearNumber = year.getSelectedIndex();
		if (monthNumber == 0 || yearNumber == 0) {
			return;
		}
		yearNumber += 2012;

		try {
			mileage.updateAll(monthNumber, yearNumber);
			image.updateAll(monthNumber, yearNumber);
			timesheet.updateAll(monthNumber, yearNumber);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		calculation.updateAll(monthNumber, yearNumber, mileage, image, timesheet, s);
	}
}
