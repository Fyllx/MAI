package panels;


import java.awt.Component;
import java.awt.FlowLayout;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import frames.EmployeeDeletingDialog;
import frames.InsertingNewEmployeeDialog;

public class StaffPanel extends JPanel {

	private JButton insertNewEmployee;
	private JButton deleteEmployee;
	private JButton editEmployee;

	private JFrame parent;
	private Statement s;
	
	public StaffPanel(JFrame frm, Statement s) {
		super();
		this.s = s;
		parent = frm;
		this.setAlignmentX(Component.LEFT_ALIGNMENT);
		frm.getContentPane().add(this);
		this.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		insertNewEmployee = new JButton("Ввести нового сотрудника");
		insertNewEmployee.addActionListener(event -> insertNewEmployee());
		this.add(insertNewEmployee);

		deleteEmployee = new JButton("Удалить сотрудника");
		deleteEmployee.addActionListener(event -> deleteEmployee());
		this.add(deleteEmployee);

		editEmployee = new JButton("Редактировать сотрудника");
		editEmployee.addActionListener(event -> editEmployee());
		this.add(editEmployee);
	}

	private void insertNewEmployee() {
		// TODO Auto-generated method stub
		InsertingNewEmployeeDialog dialog = new InsertingNewEmployeeDialog(parent, s);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setVisible(true);
	}

	private void deleteEmployee() {
		// TODO Auto-generated method stub
		EmployeeDeletingDialog dialog = new EmployeeDeletingDialog(parent, s);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setVisible(true);
	}

	private void editEmployee() {
		// TODO Auto-generated method stub
	}

}
