package frames;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Properties;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.SqlDateModel;

import additional.DateLabelFormatter;
import additional.HintTextField;

public class InsertingNewEmployeeDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private HintTextField employeeNameTextField;
	private JComboBox<String> regionComboBox;
	private HintTextField cityTextField;

	private SqlDateModel birdthDateModel;
	private SqlDateModel beginWorkDateModel;
	private SqlDateModel beginContractDateModel;
	private SqlDateModel finishWorkDateModel;

	private JLabel birdthDateLabel;
	private JLabel beginWorkLabel;
	private JLabel beginContractLabel;
	private JLabel finishWorkLabel;

	private JCheckBox isProbationFinishedCheckBox;
	private JCheckBox isAutoExistCheckBox;
	private JCheckBox isKmFixedCheckBox;
	private JCheckBox isKtuFixedCheckBox;
	private JCheckBox isWorkingNowCheckBox;

	private int[] params = new int[] { 8000, 4000, 15000, 2000, 6000, 4000, 3000, 0, 0, 0 };
	private Statement s;

	/**
	 * Create the dialog.
	 */
	public InsertingNewEmployeeDialog(JFrame parent, Statement s) {
		super(parent, true);
		setLocationByPlatform(true);
		this.s = s;

		setTitle("Создание нового сотрудника");
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		{
			JPanel panel = new JPanel();
			panel.setAlignmentY(Component.TOP_ALIGNMENT);
			panel.setAlignmentX(Component.LEFT_ALIGNMENT);
			FlowLayout flowLayout = (FlowLayout) panel.getLayout();
			flowLayout.setAlignment(FlowLayout.LEFT);
			contentPanel.add(panel);
			{
				employeeNameTextField = new HintTextField("Имя сотрудника");
				panel.add(employeeNameTextField);
				employeeNameTextField.setColumns(10);
			}
			{
				regionComboBox = new JComboBox();
				panel.add(regionComboBox);
				regionComboBox.setModel(
						new DefaultComboBoxModel(new String[] { "Регион", "Омский регион", "Новосибирский регион",
								"Кемеровский регион", "Алтайский регион", "Томский регион", "Тюменский регион" }));
			}
			{
				cityTextField = new HintTextField("Город");
				panel.add(cityTextField);
				cityTextField.setColumns(10);
			}
		}
		{
			isProbationFinishedCheckBox = new JCheckBox("Испытательный срок завершён");
			isProbationFinishedCheckBox.setAlignmentY(Component.TOP_ALIGNMENT);
			contentPanel.add(isProbationFinishedCheckBox);
		}
		{
			isAutoExistCheckBox = new JCheckBox("Есть машина");
			contentPanel.add(isAutoExistCheckBox);
			isAutoExistCheckBox.setAlignmentY(0.0f);
		}
		{
			isKmFixedCheckBox = new JCheckBox("Километражёвые фиксированы");
			isKmFixedCheckBox.setAlignmentY(0.0f);
			contentPanel.add(isKmFixedCheckBox);
		}
		{
			isKtuFixedCheckBox = new JCheckBox("КТУ фиксирован");
			isKtuFixedCheckBox.setAlignmentY(0.0f);
			contentPanel.add(isKtuFixedCheckBox);
		}
		{
			isWorkingNowCheckBox = new JCheckBox("Сейчас работает");
			isWorkingNowCheckBox.setAlignmentY(0.0f);
			contentPanel.add(isWorkingNowCheckBox);
		}
		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		{
			JPanel birdthDatePanel = new JPanel();
			birdthDatePanel.setAlignmentY(Component.TOP_ALIGNMENT);
			birdthDatePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
			contentPanel.add(birdthDatePanel);
			birdthDatePanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));

			birdthDateModel = new SqlDateModel();
			JDatePanelImpl birdthDateInsidePanel = new JDatePanelImpl(birdthDateModel, p);
			JDatePickerImpl birdthDateDatePicker = new JDatePickerImpl(birdthDateInsidePanel, new DateLabelFormatter());

			birdthDateLabel = new JLabel("Дата рождения");
			birdthDatePanel.add(birdthDateLabel);

			birdthDatePanel.add(birdthDateDatePicker);
		}
		{
			JPanel beginWorkDatePanel = new JPanel();
			beginWorkDatePanel.setAlignmentY(Component.TOP_ALIGNMENT);
			beginWorkDatePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
			contentPanel.add(beginWorkDatePanel);
			beginWorkDatePanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));

			beginWorkDateModel = new SqlDateModel();
			JDatePanelImpl beginWorkDateInsidePanel = new JDatePanelImpl(beginWorkDateModel, p);
			JDatePickerImpl beginWorkDatePicker = new JDatePickerImpl(beginWorkDateInsidePanel,
					new DateLabelFormatter());

			beginWorkLabel = new JLabel("Начало работы");
			beginWorkDatePanel.add(beginWorkLabel);

			beginWorkDatePanel.add(beginWorkDatePicker);
		}
		{
			JPanel beginContractDatePanel = new JPanel();
			beginContractDatePanel.setAlignmentY(Component.TOP_ALIGNMENT);
			beginContractDatePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
			contentPanel.add(beginContractDatePanel);
			beginContractDatePanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));

			beginContractDateModel = new SqlDateModel();
			JDatePanelImpl beginContractDateInsidePanel = new JDatePanelImpl(beginContractDateModel, p);
			JDatePickerImpl beginContractDatePicker = new JDatePickerImpl(beginContractDateInsidePanel,
					new DateLabelFormatter());

			beginContractLabel = new JLabel("Начало контракта");
			beginContractDatePanel.add(beginContractLabel);

			beginContractDatePanel.add(beginContractDatePicker);
		}
		{
			JPanel finishWorkDatePanel = new JPanel();
			finishWorkDatePanel.setAlignmentY(Component.TOP_ALIGNMENT);
			finishWorkDatePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
			contentPanel.add(finishWorkDatePanel);
			finishWorkDatePanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));

			finishWorkDateModel = new SqlDateModel();
			JDatePanelImpl finishWorkDateInsidePanel = new JDatePanelImpl(finishWorkDateModel, p);
			JDatePickerImpl finishWorkDatePicker = new JDatePickerImpl(finishWorkDateInsidePanel,
					new DateLabelFormatter());

			finishWorkLabel = new JLabel("Дата увольнения");
			finishWorkDatePanel.add(finishWorkLabel);

			finishWorkDatePanel.add(finishWorkDatePicker);
		}

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(event -> ok());
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(event -> cancel());
				{
					JButton more = new JButton("Подробнее");
					more.addActionListener(event -> more());
					buttonPane.add(more);
				}
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		pack();
	}

	private static String[] regEnum = new String[] { "omsk_reg", "nsk_reg", "kem_reg", "altai_reg", "tomsk_reg",
			"tumen_reg" };

	private void ok() {
		String errors = checkData();
		if (errors.length() != 0) {
			JOptionPane.showMessageDialog(this, "Некоторые поля заполнены некорректно:\n" + errors, "Ошибка",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		String name = employeeNameTextField.getText();
		String region = regEnum[regionComboBox.getSelectedIndex() - 1];
		String city = cityTextField.getText();

		String isProbationFinished = isProbationFinishedCheckBox.isSelected() ? "true" : "false";
		String isAutoExist = isAutoExistCheckBox.isSelected() ? "true" : "false";
		String isKmFixed = isKmFixedCheckBox.isSelected() ? "true" : "false";
		String isKtuFixed = isKtuFixedCheckBox.isSelected() ? "true" : "false";
		String isWorkingNow = isWorkingNowCheckBox.isSelected() ? "true" : "false";

		String birdthDate = birdthDateModel.getValue().toString();
		String beginWork = beginWorkDateModel.getValue().toString();

		String finishWorkColumnName;
		String finishWork;
		if (finishWorkDateModel.getValue() == null) {
			finishWork = "";
			finishWorkColumnName = "";
		} else {
			finishWork = ", \'" + finishWorkDateModel.getValue().toString() + "\'";
			finishWorkColumnName = ", finishWork";
		}

		String beginContract = beginContractDateModel.getValue().toString();

		try {
			s.executeUpdate("INSERT INTO " + ZPCounter.db_name + ".worker_general_data "
					+ "(name, region, city, isProbationFinished, isAutoExist, isKmFixed, isKtuFixed, isWorkingNow, birthDate, beginWork"
					+ finishWorkColumnName + ", beginContract) " + "VALUES (\'" + name + "\', \'" + region + "\', \'"
					+ city + "\', " + isProbationFinished + ", " + isAutoExist + ", " + isKmFixed + ", " + isKtuFixed
					+ ", " + isWorkingNow + ", \'" + birdthDate + "\', \'" + beginWork + "\'" + finishWork + ", \'"
					+ beginContract + "\')");

			ResultSet rs = s.executeQuery(
					"SELECT workerID FROM " + ZPCounter.db_name + ".worker_general_data WHERE name = \'" + name + "\'");
			if (!rs.next())
				throw new SQLException();
			int workerID = rs.getInt(1);

			String workerDefaultValues = "" + workerID;
			for (int param : params) {
				workerDefaultValues += ", " + param;
			}

			s.executeUpdate("INSERT INTO " + ZPCounter.db_name + ".worker_default_values VALUES (" + workerDefaultValues
					+ ");");
			
			s.executeUpdate("INSERT INTO " + ZPCounter.db_name + ".worker_actual_values VALUES (" + workerDefaultValues
					+ ", 0);");

			dispose();
		} catch (SQLException e) {
			
			//TODO trash deleting. Sometimes some inserting are successful and other not
			JOptionPane.showMessageDialog(this, "SQL error: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
			// e.printStackTrace();
		}
	}

	private String checkData() {
		String errs = "";

		if (!validString(employeeNameTextField.getText()))
			errs += employeeNameTextField.getHint() + "\n";
		if (regionComboBox.getSelectedIndex() == 0)
			errs += ((String) regionComboBox.getSelectedItem()) + "\n";
		if (!validString(cityTextField.getText()))
			errs += cityTextField.getHint() + "\n";

		if (!validDate(birdthDateModel.getValue()))
			errs += birdthDateLabel.getText() + "\n";
		if (!validDate(beginWorkDateModel.getValue()))
			errs += beginWorkLabel.getText() + "\n";

		if (finishWorkDateModel.getValue() != null)
			if (!validDate(finishWorkDateModel.getValue()))
				errs += finishWorkLabel.getText() + "\n";

		if (!validDate(beginContractDateModel.getValue()))
			errs += beginContractLabel.getText() + "\n";

		return errs;
	}

	private boolean validString(String str) {
		if (str.length() == 0)
			return false;
		char[] cstr = str.toCharArray();
		for (char c : cstr) {
			if (('а' <= c && c <= 'я') || ('А' <= c && c <= 'Я') || c == ' ' || c == '\0' || c == '-' || c == 'ё') {
				continue;
			} else {
				return false;
			}
		}
		return true;
	}

	private boolean validDate(Date date) {
		// TODO
		return date != null;
	}

	private void more() {
		InsertingNewEmployeeDialogMore dialog = new InsertingNewEmployeeDialogMore(this, params);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setVisible(true);
	}

	private void cancel() {
		dispose();
	}

}
