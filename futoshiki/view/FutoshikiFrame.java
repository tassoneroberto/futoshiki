package view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import model.Futoshiki;
import model.Valore;

public class FutoshikiFrame extends JFrame implements ActionListener {

	private static final long serialVersionUID = 5553669461798820644L;
	private int length;
	private JTextField[][] mat;
	private String[][] values;
	int[][] valori;
	char[][] orizzRules;
	char[][] vertRules;
	private int matLength;
	private static final int DIM_CELL_VALUE = 50;
	private static final int DIM_CELL_RULE = 30;
	private static final int OFFSET = 40;
	private static final int OFFSET_RULES = 10;
	private static final int DIM_CONTROLS = 200;
	private int dimGriglia;
	JPanel grid;
	JPanel controls;
	JButton changeDim;
	JButton risolvi;
	JButton next;
	JButton previous;
	JButton azzera;
	JLabel numSoluzioniText;
	JTextArea solutions;
	JTextArea error;
	JTextField numSoluzioni;
	LinkedList<int[][]> soluzioni;
	private int indexSoluzione;
	private int minAltezzaControls = 230;
	private Futoshiki f;

	public FutoshikiFrame(int length) {
		super("Futoshiki");
		this.setTitle("Futoshiki");
		Valore.setLength(length);
		this.length = length;
		this.matLength = length * 2 - 1;
		this.dimGriglia = length * DIM_CELL_VALUE + (length - 1)
				* DIM_CELL_RULE + 10;
		// JFrame
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setSize(dimGriglia + DIM_CONTROLS, dimGriglia + 20);
		this.setMinimumSize(new Dimension(dimGriglia + DIM_CONTROLS,
				minAltezzaControls));
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
		this.setLocation(x, y);
		this.setLayout(null);
		// Pannello valori e vincoli
		grid = new JPanel();
		grid.setLayout(null);
		mat = new JTextField[matLength][matLength];
		values = new String[matLength][matLength];
		Font numeri = new Font("Times New Roman", Font.BOLD, 24);
		Font vincoli = new Font("", Font.PLAIN, 18);
		for (int i = 0; i < matLength; i++) {
			for (int j = 0; j < matLength; j++) {
				mat[i][j] = new JTextField(1);
				mat[i][j].setHorizontalAlignment(JTextField.CENTER);
				values[i][j] = "";
				grid.add(mat[i][j]);
				if (i % 2 == 0 && j % 2 == 0) {
					mat[i][j].setBounds(j * OFFSET, i * OFFSET, DIM_CELL_VALUE,
							DIM_CELL_VALUE);
					mat[i][j].setFont(numeri);
				} else {
					mat[i][j].setBounds(j * OFFSET + OFFSET_RULES, i * OFFSET
							+ OFFSET_RULES, DIM_CELL_RULE, DIM_CELL_RULE);
					mat[i][j].setFont(vincoli);
				}
				if (i % 2 != 0 && j % 2 != 0)
					mat[i][j].setVisible(false);
			}
		}
		this.add(grid);
		grid.setBounds(5, 5, dimGriglia, dimGriglia);
		// Comandi
		controls = new JPanel();
		controls.setLayout(null);
		this.add(controls);
		controls.setLocation(dimGriglia, 0);
		controls.setSize(new Dimension(DIM_CONTROLS, minAltezzaControls));

		changeDim = new JButton("Cambia dimensione");
		risolvi = new JButton("Risolvi");
		next = new JButton("Next");
		next.setEnabled(false);
		previous = new JButton("Previous");
		previous.setEnabled(false);
		solutions = new JTextArea("");
		error = new JTextArea("");
		solutions.setEditable(false);
		error.setEditable(false);
		solutions.setBackground(null);
		error.setBackground(null);
		numSoluzioniText = new JLabel("Soluzioni massime desiderate:");
		numSoluzioni = new JTextField("MAX");
		azzera = new JButton("Azzera");

		controls.add(changeDim);
		controls.add(risolvi);
		controls.add(next);
		controls.add(previous);
		controls.add(solutions);
		controls.add(error);
		controls.add(numSoluzioniText);
		controls.add(numSoluzioni);
		controls.add(azzera);

		changeDim.addActionListener(this);
		risolvi.addActionListener(this);
		next.addActionListener(this);
		previous.addActionListener(this);
		azzera.addActionListener(this);

		changeDim.setBounds(5, 5, 180, 20);
		numSoluzioniText.setBounds(5, 30, DIM_CONTROLS, 20);
		numSoluzioni.setBounds(5, 50, 180, 20);
		risolvi.setBounds(5, 75, 180, 20);
		azzera.setBounds(5, 100, 180, 20);
		next.setBounds(97, 125, 88, 20);
		previous.setBounds(5, 125, 88, 20);
		solutions.setBounds(5, 150, DIM_CONTROLS, 20);
		error.setBounds(5, 170, DIM_CONTROLS, 40);

		this.setVisible(true);
	}

	public int getLength() {
		return length;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton source = (JButton) e.getSource();
		String testo = source.getText();
		if (testo.equals("Cambia dimensione")) {
			new FutoshikiGUI();
			this.f = null;
			this.setVisible(false);
		}
		if (testo.equals("Risolvi")) {
			for (int i = 0; i < matLength; i++) {
				for (int j = 0; j < matLength; j++) {
					values[i][j] = mat[i][j].getText();
				}
			}
			if (valoriCorretti(values)) {
				error.setText("");
				int numSolDesiderate;
				if (numSoluzioni.getText().equals("MAX"))
					numSolDesiderate = Integer.MAX_VALUE;
				else
					numSolDesiderate = Integer.parseInt(numSoluzioni.getText());
				f = new Futoshiki(numSolDesiderate, valori, orizzRules,
						vertRules);
				f.risolvi();
				indexSoluzione = 1;
				soluzioni = f.getSoluzioni();
				if (soluzioni.isEmpty()) {
					solutions.setText("Nessuna soluzione.");
				} else {
					for (int i = 0; i < matLength; i++) {
						for (int j = 0; j < matLength; j++) {
							mat[i][j].setEditable(false);
						}
					}
					numSoluzioni.setEditable(false);
					solutions.setText("Soluzione " + 1 + " di "
							+ soluzioni.size() + ".");
					int[][] soluzione1 = soluzioni.get(0);
					stampaSoluzione(soluzione1);
					if (soluzioni.size() > 1) {
						next.setEnabled(true);
					}
					risolvi.setEnabled(false);
					changeDim.setEnabled(false);

				}
			} else {
				solutions.setText("Configurazione non valida:");
			}
		}
		if (testo.equals("Azzera")) {
			indexSoluzione = 0;
			error.setText("");
			numSoluzioni.setEditable(true);
			risolvi.setEnabled(true);
			changeDim.setEnabled(true);
			next.setEnabled(false);
			previous.setEnabled(false);
			numSoluzioni.setText("MAX");
			solutions.setText("");
			for (int i = 0; i < matLength; i++) {
				for (int j = 0; j < matLength; j++) {
					mat[i][j].setText("");
					mat[i][j].setEditable(true);
				}
			}
		}
		if (testo.equals("Next")) {
			previous.setEnabled(true);
			if (indexSoluzione + 1 == soluzioni.size()) {
				next.setEnabled(false);
			}
			indexSoluzione++;
			stampaSoluzione(soluzioni.get(indexSoluzione - 1));
			solutions.setText("Soluzione " + indexSoluzione + " di "
					+ soluzioni.size() + ".");

		}
		if (testo.equals("Previous")) {
			next.setEnabled(true);
			if (indexSoluzione == 2) {
				previous.setEnabled(false);
			}
			indexSoluzione--;
			stampaSoluzione(soluzioni.get(indexSoluzione - 1));
			solutions.setText("Soluzione " + indexSoluzione + " di "
					+ soluzioni.size() + ".");

		}
	}

	private void stampaSoluzione(int[][] soluzione1) {
		// stampo valori iniziali
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				if (valori[i][j] != 0)
					mat[i * 2][j * 2].setText(Integer.toString(valori[i][j]));
				else
					mat[i * 2][j * 2].setText(Integer
							.toString(soluzione1[i][j]));
			}
		}
		// stampo vincoli
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length - 1; j++) {
				mat[i * 2][j * 2 + 1].setText(Character
						.toString(orizzRules[i][j]));
			}
		}
		for (int i = 0; i < length - 1; i++) {
			for (int j = 0; j < length; j++) {
				mat[i * 2 + 1][j * 2].setText(Character
						.toString(vertRules[i][j]));
			}
		}
	}

	public boolean valoriCorretti(String[][] values) {
		// Verifico il numero delle soluzioni desiderate
		String numSol = numSoluzioni.getText();
		if (numSol.equals("") || numSol.charAt(0) == '-') {
			error.setText("Numero soluzioni non valido\n(" + numSol + ").");
			return false;
		}
		try {
			Integer.parseInt(numSol);
		} catch (NumberFormatException e) {
			if (!numSol.equals("MAX")) {
				error.setText("Numero soluzioni non valido\n(" + numSol + ").");
				return false;
			}
		}
		// Inizializzo matrici per la verifica
		valori = new int[length][length];
		orizzRules = new char[length][length - 1];
		vertRules = new char[length - 1][length];
		// Verifico dati inseriti (valori e vincoli)
		for (int i = 0; i < values.length; i++) {
			for (int j = 0; j < values.length; j++) {
				if (!values[i][j].equals("")) {
					if (i % 2 == 0 && j % 2 == 0) {
						try {
							int num = Integer.parseInt(values[i][j]);
							if (num < 1 || num > length) {
								error.setText("Numero fuori intervallo (" + num
										+ ").");
								return false;
							}
							valori[i / 2][j / 2] = num;
						} catch (NumberFormatException e) {
							error.setText("Numero non valido (" + values[i][j]
									+ ").");
							return false;
						}
					} else if (i % 2 == 0 && j % 2 != 0) {
						if (!(values[i][j].charAt(0) == '<' || values[i][j]
								.charAt(0) == '>')) {
							error.setText("Vincolo non valido (" + values[i][j]
									+ ").");
							return false;
						}
						orizzRules[i / 2][(j - 1) / 2] = values[i][j].charAt(0);
					} else if (i % 2 != 0 && j % 2 == 0) {
						if (!(values[i][j].charAt(0) == '^' || values[i][j]
								.charAt(0) == 'v')) {
							error.setText("Vincolo non valido (" + values[i][j]
									+ ").");
							return false;
						}
						vertRules[(i - 1) / 2][j / 2] = values[i][j].charAt(0);
					}
				}
			}
		}
		// Verifico configurazione iniziale
		for (int row = 0; row < length; row++)
			for (int col = 0; col < length; col++) {
				int val = valori[row][col];
				if (val != 0) {
					// Cerco duplicati nella stessa colonna
					for (int i = 0; i < length; i++) {
						if (i != row)
							if (valori[i][col] == val) {
								error.setText("Valori duplicati (Colonna "
										+ (col + 1) + ").");
								return false;
							}
					}
					// Cerco duplicati nella stessa riga
					for (int j = 0; j < length; j++) {
						if (j != col)
							if (valori[row][j] == val) {
								error.setText("Valori duplicati (Riga "
										+ (row + 1) + ").");
								return false;
							}
					}

					// Verifico vincoli
					// Orizzantali
					if (col != 0) {
						if (orizzRules[row][col - 1] != ' ')
							if (valori[row][col - 1] != 0) {
								switch (orizzRules[row][col - 1]) {
								case '>':
									if (valori[row][col - 1] < val) {
										error.setText("Vincolo non rispettato ("
												+ valori[row][col - 1]
												+ ">"
												+ val + ").");
										return false;
									}
									break;
								case '<':
									if (valori[row][col - 1] > val) {
										error.setText("Vincolo non rispettato ("
												+ val
												+ "<"
												+ valori[row][col - 1] + ").");
										return false;
									}
									break;

								}

							}
					}
					if (col != length - 1) {
						if (orizzRules[row][col] != ' ')
							if (valori[row][col + 1] != 0) {
								switch (orizzRules[row][col]) {
								case '>':
									if (val < valori[row][col + 1]) {
										error.setText("Vincolo non rispettato ("
												+ val
												+ ">"
												+ valori[row][col + 1] + ").");
										return false;
									}
									break;
								case '<':
									if (val > valori[row][col + 1]) {
										error.setText("Vincolo non rispettato ("
												+ val
												+ "<"
												+ valori[row][col + 1] + ").");
										return false;
									}
									break;

								}
							}

					}
					// Verticali
					if (row != 0) {
						if (vertRules[row - 1][col] != ' ')
							if (valori[row - 1][col] != 0) {
								switch (vertRules[row - 1][col]) {
								case '^':
									if (valori[row - 1][col] > val) {
										error.setText("Vincolo non rispettato ("
												+ valori[row - 1][col]
												+ "<"
												+ val + ").");
										return false;
									}
									break;
								case 'v':
									if (valori[row - 1][col] < val) {
										error.setText("Vincolo non rispettato ("
												+ valori[row - 1][col]
												+ ">"
												+ val + ").");
										return false;
									}
									break;

								}
							}
					}
					if (row != length - 1) {
						if (vertRules[row][col] != ' ')
							if (valori[row + 1][col] != 0) {
								switch (vertRules[row][col]) {
								case '^':
									if (val > valori[row + 1][col]) {
										error.setText("Vincolo non rispettato ("
												+ val
												+ "<"
												+ valori[row + 1][col] + ").");
										return false;
									}
									break;
								case 'v':
									if (val < valori[row + 1][col]) {
										error.setText("Vincolo non rispettato ("
												+ val
												+ ">"
												+ valori[row + 1][col] + ").");
										return false;
									}
									break;
								}
							}
					}
				}

			}
		return true;
	}
}
