package frames;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class InsertingNewEmployeeDialogMore extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField[] texts;
	private JLabel[] labels;
	private int[] params;

	/**
	 * Create the dialog.
	 */
	public InsertingNewEmployeeDialogMore(InsertingNewEmployeeDialog insertingNewEmployeeDialog, int[] params) {
		super(insertingNewEmployeeDialog, true);
		setLocationByPlatform(true);
		
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

		this.params = params;
		String[] names = new String[] { "Оклад", "Аванс", "Зарплата", "Аренда авто", "Стартовые КТУ",
				"Стартовые километражёвые", "Обеды", "ЗОЖ", "Фиксированная премия", "Компенсации транзакций" };

		texts = new JTextField[names.length];
		labels = new JLabel[names.length];
		for (int q = 0; q < names.length; q++) {
			JPanel panel = new JPanel();
			FlowLayout flowLayout = (FlowLayout) panel.getLayout();
			flowLayout.setAlignment(FlowLayout.RIGHT);
			contentPanel.add(panel);

			JLabel label = new JLabel(names[q]);
			panel.add(label);
			labels[q] = label;

			JTextField text = new JTextField();
			text.setText("" + params[q]);
			panel.add(text);
			text.setColumns(6);
			texts[q] = text;
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
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		pack();
	}

	private void ok() {
		int[] newparams = new int[params.length];
		boolean[] valid = new boolean[params.length];
		boolean allvalid = true;
		for (int q = 0; q < params.length; q++) {
			try {
				newparams[q] = Integer.valueOf(texts[q].getText());
				valid[q] = true;
			} catch (NumberFormatException ex) {
				valid[q] = false;
				allvalid = false;
			}
		}
		if (allvalid) {
			for (int q = 0; q < params.length; q++) {
				params[q] = newparams[q];
			}
			dispose();
		} else {
			String err = "Некоторые поля заполнены некорректно:\n";
			for (int q = 0; q < valid.length; q++) {
				if (!valid[q])
					err += labels[q].getText() + "\n";
			}
			JOptionPane.showMessageDialog(this, err, "Ошибка", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void cancel() {
		dispose();
	}

}
