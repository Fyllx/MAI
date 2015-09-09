package panels;


import java.sql.Statement;

import javax.swing.JFrame;

public class CalculationPanel extends SomePanel {

	public CalculationPanel(JFrame frm, Statement s) {
		super(frm, s);
		whenReady = "Да";
		isReadyQuestionLabel.setText("Рассчитано:");
		enterData.setText("Рассчитать");
		setCalcEnabled(false);
	}

	private void setCalcEnabled(boolean enable) {
		enterData.setEnabled(enable);
	}

	public void updateAll(int monthNumber, int yearNumber, MileagePanel mileage, ImagePanel image,
			TimesheetPanel timesheet, Statement s) {
		// TODO Auto-generated method stub
		setCalcEnabled((mileage.isReady() && image.isReady() && timesheet.isReady()));
	}

}
