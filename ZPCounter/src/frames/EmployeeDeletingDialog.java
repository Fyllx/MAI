package frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.text.BadLocationException;

public class EmployeeDeletingDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private Statement s;
	private TableModel tableModel;
	private JTextField textField;
	private JPanel employeeList;
	private JScrollPane scrollPane;

	private int width;

	/**
	 * Create the dialog.
	 */
	public EmployeeDeletingDialog(JFrame parent, Statement s) {
		super(parent, true);
		setLocationByPlatform(true);
		this.s = s;

		setTitle("Удаление сотрудника");
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		{
			JPanel textPanel = new JPanel();
			getContentPane().add(textPanel, BorderLayout.NORTH);
			{
				textField = new JTextField();
				textPanel.add(textField);
				textPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, textPanel.getHeight()));
				textField.setColumns(10);

				textField.getDocument().addDocumentListener(new DocumentListener() {

					@Override
					public void removeUpdate(DocumentEvent arg0) {
						event(arg0);
					}

					@Override
					public void insertUpdate(DocumentEvent arg0) {
						event(arg0);
					}

					@Override
					public void changedUpdate(DocumentEvent arg0) {
						event(arg0);
					}

					private void event(DocumentEvent e) {
						String str = "";
						try {
							str = e.getDocument().getText(0, e.getDocument().getLength());
						} catch (BadLocationException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
						rebuildEmployeeListWithNameEntered(str);
					}
				});
			}
			{
				scrollPane = new JScrollPane();
				contentPanel.add(scrollPane);
				employeeList = new JPanel();
				scrollPane.setViewportView(employeeList);

				employeeList.setBackground(Color.WHITE);
				employeeList.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
				employeeList.setLayout(new BoxLayout(employeeList, BoxLayout.Y_AXIS));

				rebuildEmployeeListWithNameEntered("");
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
//			{
//				JButton okButton = new JButton("OK");
//				okButton.addActionListener(event -> ok());
//				okButton.setActionCommand("OK");
//				buttonPane.add(okButton);
//				getRootPane().setDefaultButton(okButton);
//			}
			{
				JButton cancelButton = new JButton("Назад");
				cancelButton.addActionListener(event -> cancel());
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		pack();
		width = getWidth();
	}

	private void rebuildEmployeeListWithNameEntered(String name) {

		ResultSet res = null;
		try {
			res = s.executeQuery("SELECT workerID, name FROM " + ZPCounter.db_name + ".worker_general_data "
					+ "WHERE name LIKE \'%" + name + "%\';");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		employeeList.removeAll();

		try {
			while (res.next()) {
				JPanel employee = new JPanel();
				employee.setLayout(new BoxLayout(employee, BoxLayout.X_AXIS));
				employee.setBackground(Color.WHITE);

				int id = res.getInt(1);
				JButton deleteEmployee = new JButton("");
				deleteEmployee.setIcon(new ImageIcon(EmployeeDeletingDialog.class
						.getResource("/javax/swing/plaf/metal/icons/ocean/paletteClose-pressed.gif")));

				deleteEmployee.addActionListener(event -> delete(id));
				employee.add(deleteEmployee);

				JLabel employeeName = null;
				try {
					employeeName = new JLabel(res.getString(2));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				employee.add(employeeName);

				employee.setMaximumSize(new Dimension(Integer.MAX_VALUE, employeeName.getHeight()));

				employeeList.add(employee);
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
//		for (int q = 0; q < 50; q++) {
//			JPanel employee = new JPanel();
//			employee.setLayout(new BoxLayout(employee, BoxLayout.X_AXIS));
//			employee.setBackground(Color.WHITE);
//
//			JButton deleteEmployee = new JButton("");
//			deleteEmployee.setIcon(new ImageIcon(EmployeeDeletingDialog.class
//					.getResource("/javax/swing/plaf/metal/icons/ocean/paletteClose-pressed.gif")));
//			employee.add(deleteEmployee);
//
//			JLabel employeeName = new JLabel("Безымянный сотрудник номер" + (q + 1));
//			employee.add(employeeName);
//
//			employee.setMaximumSize(new Dimension(Integer.MAX_VALUE, employeeName.getHeight()));
//
//			employeeList.add(employee);
//		}
		try {
			res.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		employeeList.add(Box.createVerticalGlue());

		// TODO
//		scrollPane.setMinimumSize(new Dimension(employeeList.getWidth(), 500));
//		scrollPane.setMaximumSize(new Dimension(99999, 256));
		// TODO Why does maximum size not work?

		employeeList.revalidate();
		employeeList.repaint();
	}

	private void delete(int id) {
		ResultSet res = null;
		try {
			res = s.executeQuery("SELECT name FROM "+ZPCounter.db_name+".worker_general_data WHERE workerID = "+id+";");
		} catch (SQLException e) {
			//TODO
			JOptionPane.showMessageDialog(this, "SQL error: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
		}
		try {
			if(!res.next()) 
			{
				JOptionPane.showMessageDialog(this, "Неизвестная ошибка", "Ошибка", JOptionPane.ERROR_MESSAGE);
				return;
			}
		} catch (HeadlessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		JLabel label = null;
		try {
			label = new JLabel("Удалить сотрудника " + res.getString(1) + "?");
		} catch (SQLException e) {
			// TODO
			JOptionPane.showMessageDialog(this, "SQL error: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
		}
		int reply = JOptionPane.showConfirmDialog(null, label, "Подтверждение",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		
		if(reply == JOptionPane.OK_OPTION)
		{
			try {
				s.execute("DELETE FROM "+ZPCounter.db_name+".worker_general_data WHERE workerID = "+id+";");
			} catch (SQLException e) {
				// TODO
				JOptionPane.showMessageDialog(this, "SQL error: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
			}
			rebuildEmployeeListWithNameEntered(textField.getText());
		}
		else
		{
			return;
		}
	}

	private void ok() {
	}

	private void cancel() {
		dispose();
	}

}
