package labs.zubovich;

import labs.zubovich.calculator.Calculator;
import labs.zubovich.calculator.RowParam;
import labs.zubovich.calculator.TypicalNormCalculator;
import labs.zubovich.dbutil.GlobalCache;
import labs.zubovich.dbutil.TableParser;
import labs.zubovich.ui.MultylineCellRenderer;
import labs.zubovich.ui.TypicalNormTM;
import labs.zubovich.ui.ValueTableCellView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;


public class Main extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	private TypicalNormTM model = new TypicalNormTM();
	private Calculator calculator = new TypicalNormCalculator();

	private JPanel jContentPane = null;
	private JPanel jResultPane = null;
	private JScrollPane jTablePane = null;
	private JTable jTable = null;

	private JTextField resultTextField = null;
	private JButton calcButton =  null;

	public static void main(String[] args) {
		GlobalCache.init(
				GlobalCache.Key.KLOC_Map,
				TableParser.readTable(Main.class.getResource("db/KLOC.tres"))
		);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Main frame = new Main();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		});
	}

	public Main() {
		super();
		initialize();
	}

	private void initialize() {
		this.setMinimumSize(new Dimension(500, 400));
		this.setContentPane(getJContentPane());
		this.setTitle("Подсчет стоимости проекта по типовам нормам");
		this.pack();
	}

	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJTablePane(), BorderLayout.CENTER);
			jContentPane.add(getjResultPane(), BorderLayout.SOUTH);
		}
		return jContentPane;
	}

	private JScrollPane getJTablePane() {
		if (jTablePane == null) {
			jTablePane = new JScrollPane(getJTable()) {
				{
					this.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
					this.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
					this.setOpaque(false);
					this.setBorder(null);
					this.setViewportBorder(null);
					this.setHorizontalScrollBar(null);
				}

				@Override
				public Dimension getPreferredSize() {
					Dimension tablePreferredSize = getJTable().getPreferredSize();
					Dimension tableHeaderPreferredSize = getJTable().getTableHeader().getPreferredSize();
					int width = (int)tablePreferredSize.getWidth();
					int height = 3 + (int)tableHeaderPreferredSize.getHeight() + (int)tablePreferredSize.getHeight();
					return new Dimension(width, height);
				}
			};
		}
		return jTablePane;
	}

	private JTable getJTable() {
		if (jTable == null) {
			jTable = new JTable(model);

			jTable.setShowGrid(true);
			jTable.setRowHeight(34);
			jTable.setBackground(Color.WHITE);
			jTable.setCellSelectionEnabled(false);

			jTable.getTableHeader().getColumnModel().getColumn(0).setCellRenderer(new MultylineCellRenderer(false));
			jTable.getTableHeader().getColumnModel().getColumn(0).setPreferredWidth(150);

			ValueTableCellView valueTableCellView = new ValueTableCellView();
			jTable.getTableHeader().getColumnModel().getColumn(1).setPreferredWidth(525);
			jTable.getTableHeader().getColumnModel().getColumn(1).setCellEditor(valueTableCellView);
			jTable.getTableHeader().getColumnModel().getColumn(1).setCellRenderer(valueTableCellView);

			jTable.setRowHeight(Arrays.asList(RowParam.values()).indexOf(RowParam.DIFFICULTY_UPPER_COEF), 170);
			jTable.setRowHeight(Arrays.asList(RowParam.values()).indexOf(RowParam.STANDART_USAGES_K), 75);
		}
		return jTable;
	}

	private JButton getCalcButton() {
		if(calcButton == null) {
			calcButton = new JButton("Посчитать!");
			calcButton.addActionListener(this);
		}
		return calcButton;
	}

	public JTextField getResultTextField() {
		if(resultTextField == null) {
			resultTextField = new JTextField();
			resultTextField.setColumns(15);
			resultTextField.setText("Не считалась");
			resultTextField.setEditable(false);
			resultTextField.setBackground(Color.WHITE);
		}
		return resultTextField;
	}

	public JPanel getjResultPane() {
		if(jResultPane == null) {
			jResultPane = new JPanel(new FlowLayout());
			jResultPane.add(new JLabel("Итоговая стоимость: "));
			jResultPane.add(getResultTextField());
			jResultPane.add(getCalcButton());
		}
		return jResultPane;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		jTable.editingStopped(null);
		resultTextField.setText(calculator.calculate(model.fetch()).toString());
	}

}