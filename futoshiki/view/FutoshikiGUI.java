package view;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class FutoshikiGUI extends JFrame implements ActionListener,
		KeyListener {
	private static final long serialVersionUID = 2923975805665801740L;
	JPanel container;
	JComboBox<String> dim;
	JButton conferma;

	public FutoshikiGUI() {
		super("Futoshiki");
		this.setTitle("Futoshiki");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setSize(300, 100);
		this.setFocusable(true);
		this.requestFocusInWindow();
		this.addKeyListener(this);
		this.setLayout(null);
		container = new JPanel();
		container.setLayout(null);
		container.setBounds(0, 0, 300, 100);
		this.add(container);
		// ComboBox
		String[] sa = new String[9];
		for (int s = 2; s <= sa.length; s++) {
			sa[s - 1] = s + "x" + s;
		}
		dim = new JComboBox<String>(sa);
		dim.setSelectedIndex(3);
		container.add(dim);
		dim.setBounds(50, 50, 100, 20);
		// Button
		conferma = new JButton("Conferma");
		container.add(conferma);
		conferma.setBounds(150, 50, 100, 20);
		// Label
		JLabel text = new JLabel("Scegli la dimensione della griglia");
		container.add(text);
		text.setBounds(50, 10, 250, 20);
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
		this.setLocation(x, y);

		conferma.addActionListener(this);
		conferma.addKeyListener(this);
		dim.addKeyListener(this);

		this.setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton source = (JButton) e.getSource();
		String testo = source.getText();
		if (testo.equals("Conferma")) {
			new FutoshikiFrame(dim.getSelectedIndex() + 1);
			this.setVisible(false);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

	public static void main(String[] args) {
		@SuppressWarnings("unused")
		FutoshikiGUI s = new FutoshikiGUI();
	}
}
