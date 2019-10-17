package net.preibisch.ijannot.view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;

import net.preibisch.ijannot.controllers.managers.CanvasAnnotationManagerV2;
import net.preibisch.ijannot.util.Tools;

public class AnnotationCategoriesView extends JFrame implements ActionListener {
	private static AnnotationCategoriesView instance;
	private static String TITLE = "Annotate View";

	private List<JButton> fields;

	private <T> AnnotationCategoriesView(List<T> categories) {
		super(TITLE);
		fields = new ArrayList<JButton>();
		for (T category : categories) {
			JButton b = new JButton();
			b.setBackground(Tools.randomColor());
			// b.setContentAreaFilled(true);
			b.setOpaque(true);
			// b.setForeground(Tools.randomColor());
			b.setText(String.valueOf(category));
			b.addActionListener(this);
			fields.add(b);
		}
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

		contentPanel1.setBorder(padding);
		contentPanel2.setBorder(padding);
		this.setLayout(new GridLayout(2, 1));
		this.add(contentPanel1);
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
			CanvasAnnotationManagerV2.add(x);
			CanvasAnnotationManagerV2.next();
			return;
		}
		switch (s) {
		case "Exit":
			CanvasAnnotationManagerV2.exit();
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
