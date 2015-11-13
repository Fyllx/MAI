package panels;


import java.awt.Component;
import java.awt.FlowLayout;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import frames.DeleteEmployeeDialog;
import frames.InsertingNewEmployeeDialog;

public class EmployeePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JButton insertNewEmployee;
	private JButton deleteEmployee;
	private JButton editEmployee;

	private JFrame parent;
	private Statement s;
	
	public EmployeePanel(JFrame frm, Statement s) {
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
		InsertingNewEmployeeDialog dialog = new InsertingNewEmployeeDialog(parent, s);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setVisible(true);
	}

	private void deleteEmployee() {
		DeleteEmployeeDialog dialog = new DeleteEmployeeDialog(parent, s);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setVisible(true);
	}

	private void editEmployee() {
//		InsertingNewEmployeeDialogMore dialog = new InsertingNewEmployeeDialogMore(parent, s);
//		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//		dialog.setVisible(true);
	}

}
