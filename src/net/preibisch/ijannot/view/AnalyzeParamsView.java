package net.preibisch.ijannot.view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import net.preibisch.ijannot.controllers.managers.AnalyzeManager;

public class AnalyzeParamsView extends JFrame implements ActionListener {
	private static AnalyzeParamsView instance;
	private static String TITLE = "Analyze Params";

	private static final double MIN_ANALYZE = 1.0;
	private static final double MAX_ANALYZE = 1000.0;
	private static final double GAUSS = 3.0;
	private static final int CHANNEL = 1;
	private static final int THRESHOLD = 15;

	private double min;
	private double max;
	private double gauss;
	private int channel;
	private int threshold;
	private JTextField minField;
	private JTextField maxField;
	private JTextField gaussField;
	private JTextField channelField;
	private JTextField thresholdField;
	private JButton nextButton;
	private JButton testButton;

	private AnalyzeParamsView(double min, double max, double gauss, int channel, int threshold) {
		super(TITLE);
		this.min = min;
		this.max = max;
		this.gauss = gauss;
		this.channel = channel;
		this.threshold = threshold;
	}

	public static AnalyzeParamsView get() {
		if (instance != null)
			return instance;
		return new AnalyzeParamsView(MIN_ANALYZE, MAX_ANALYZE, GAUSS, CHANNEL, THRESHOLD);
	}

	public static void init() {
		instance = get();
		instance.initView();
		instance.setVisible(true);

	}

	private void initView() {
		JPanel contentPanel = new JPanel();

		setSize(240, 340);
		Border padding = BorderFactory.createEmptyBorder(20, 20, 20, 20);

		contentPanel.setBorder(padding);

		setContentPane(contentPanel);
		contentPanel.setSize(200, 300);

		contentPanel.setLayout(new GridLayout(6, 2, 10, 10));

		channelField = new JTextField(String.valueOf(channel));
		contentPanel.add(new JLabel("Channels:"));
		contentPanel.add(channelField);

		gaussField = new JTextField(String.valueOf(gauss));
		contentPanel.add(new JLabel("Gauss:"));
		contentPanel.add(gaussField);

		thresholdField = new JTextField(String.valueOf(threshold));
		contentPanel.add(new JLabel("Threshold:"));
		contentPanel.add(thresholdField);

		minField = new JTextField(String.valueOf(min));
		contentPanel.add(new JLabel("Analyze Min:"));
		contentPanel.add(minField);

		maxField = new JTextField(String.valueOf(max));
		contentPanel.add(new JLabel("Analyze Max:"));
		contentPanel.add(maxField);

		nextButton = new JButton("Next");
		testButton = new JButton("Test");
		// contentPanel.add(new JLabel());

		contentPanel.add(testButton);
		testButton.addActionListener(this);
		contentPanel.add(nextButton);
		nextButton.addActionListener(this);

	}

	public double getMin() {
		return min;
	}

	public double getMax() {
		return max;
	}

	public double getGauss() {
		return gauss;
	}

	public int getChannel() {
		return channel;
	}

	public int getThreshold() {
		return threshold;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (setValues()) {
			if (e.getSource() == nextButton)
				nextAction();
			else if (e.getSource() == testButton)
				testAction();
		}
	}

	private Boolean setValues() {
		Boolean valid = true;
		try {
			channel = Integer.parseInt(channelField.getText());
			gauss = Double.parseDouble(gaussField.getText());
			threshold = Integer.parseInt(thresholdField.getText());
			min = Double.parseDouble(minField.getText());
			max = Double.parseDouble(maxField.getText());
		} catch (Exception ex) {
			valid = false;
			JOptionPane.showMessageDialog(null, ex.toString(), "Error !", JOptionPane.ERROR_MESSAGE);
		}
		return valid;
	}

	private void testAction() {
		new Thread(new Runnable() {
			@Override
			public void run() {
					AnalyzeManager.test();		
			}
		}).run();
	}

	private void nextAction() {

		this.setVisible(false);
		this.dispose();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					AnalyzeManager.start();
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, e.toString(), "Error !", JOptionPane.ERROR_MESSAGE);
				}
			}
		}).run();

	}

	public static void main(String[] args) {
		AnalyzeParamsView.init();
	}

}
