package net.preibisch.ijannot.view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import net.preibisch.ijannot.controllers.managers.CanvasAnnotationManagerV2;
import net.preibisch.ijannot.util.Tools;

public class AnnotationCategoriesView extends JFrame implements ActionListener {
	private static AnnotationCategoriesView instance;
	private static String TITLE = "Annotate View";

	private List<Integer> TOTALS;
	private Integer total;

	private List<JButton> fields;
	private JLabel label;

	private <T> AnnotationCategoriesView(List<T> categories) {
		super(TITLE);
		this.label = new JLabel("");
		fields = new ArrayList<JButton>();
		TOTALS = new ArrayList<>(Collections.nCopies(categories.size(), 0));
		total = 0;
		updateLabel();
		for (T category : categories) {
			JButton b = new JButton();
			b.setBackground(Tools.randomColor());
			b.setOpaque(true);
			b.setText(String.valueOf(category));
			b.addActionListener(this);
			fields.add(b);
		}
	}
	
	public static void updateTotals(List<Integer> totals2, Integer total) {
		instance.setTotals(totals2, total);
	}
	
	
	public void setTotals(List<Integer> totals, Integer total) {
		this.TOTALS = totals;
		this.total = total;
		updateLabel();
	}

	public void add(int x) {
		total = total + 1;
		TOTALS.set(x, TOTALS.get(x) + 1);
		updateLabel();
	}

	private void updateLabel() {
		String s = "Total: " + total;
		for (int i = 0; i < TOTALS.size(); i++)
			s = s + " " + i + ":" + TOTALS.get(i);
		this.label.setText(s);
	}

	public static <T> AnnotationCategoriesView get(List<T> categories) {
		if (instance != null)
			return instance;
		return new AnnotationCategoriesView(categories);
	}

	public static <T> void init(List<T> categories) {
		instance = get(categories);
		instance.initView();
		instance.setVisible(true);

	}

	private void initView() {
		JPanel contentPanel1 = new JPanel();
		JPanel contentPanel2 = new JPanel();
		setSize(340, 200);
		Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		label.setBorder(padding);
		contentPanel1.setBorder(padding);
		contentPanel2.setBorder(padding);
		this.setLayout(new GridLayout(3, 1));
		this.add(contentPanel1);
		this.add(label);
		this.add(contentPanel2);

		contentPanel1.setLayout(new GridLayout(1, fields.size(), 10, 10));
		for (JButton b : fields)
			contentPanel1.add(b);

		JButton b1 = new JButton("Exit");
		b1.addActionListener(this);
		JButton b2 = new JButton("Undo");
		b2.addActionListener(this);
		contentPanel2.add(b1);
		contentPanel2.add(b2);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String s = ((JButton) e.getSource()).getText();
		if (Character.isDigit(s.charAt(0))) {
			Integer x = Integer.parseInt(s);
			add(x);
			CanvasAnnotationManagerV2.add(x);
			CanvasAnnotationManagerV2.next();
			return;
		}
		switch (s) {
		case "Exit":
			CanvasAnnotationManagerV2.exit();
			this.setVisible(false);
			break;
		case "Undo":
			CanvasAnnotationManagerV2.undo();
			break;

		}
	}

	public static void main(String[] args) {
		AnnotationCategoriesView.init(new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4)));
	}

}
