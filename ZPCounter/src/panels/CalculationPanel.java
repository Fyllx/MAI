package panels;


import java.sql.Statement;

import javax.swing.JFrame;

public class CalculationPanel extends SomePanel {

	private static final long serialVersionUID = 1L;

	public CalculationPanel(JFrame frm, Statement s) {
		super(frm, s);
		whenReady = "Да";
		panelNameLabel.setText("Рассчитано:");
		enterData.setText("Рассчитать");
		setCalcEnabled(false);
	}

	private void setCalcEnabled(boolean isEnable) {
		enterData.setEnabled(isEnable);
	}

	public void updateAll(int monthNumber, int yearNumber, MileagePanel mileage, ImagePanel image,
			TimesheetPanel timesheet, Statement s) {
		setCalcEnabled((mileage.isReady() && image.isReady() && timesheet.isReady()));
	}

}
