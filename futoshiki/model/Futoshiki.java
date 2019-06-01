package model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import backtrack.Problema;

public class Futoshiki extends Problema<Cella, Valore> {

	// Valori iniziali
	private int length;
	private int[][] valori;
	private char[][] orizzRules;
	private char[][] vertRules;
	// Backtrack
	private LinkedList<Cella> puntiDiScelta;
	private Map<Cella, Valore> scelteCellaValore;
	private Cella first;
	private Cella last;
	private LinkedList<int[][]> soluzioni;
	private Cella[][] celle;

	public Futoshiki(int numSoluzioni, int[][] valori, char[][] orizzRules,
			char[][] vertRules) {
		super(numSoluzioni);
		this.length = valori.length;
		this.valori = valori;
		this.orizzRules = orizzRules;
		this.vertRules = vertRules;
		Cella.setLength(length);
		puntiDiScelta = new LinkedList<Cella>();
		scelteCellaValore = new HashMap<Cella, Valore>();
		soluzioni = new LinkedList<int[][]>();
		// inizializzo
		celle = new Cella[length][length];
		for (int i = 0; i < length; i++)
			for (int j = 0; j < length; j++) {
				celle[i][j] = new Cella(i, j);
			}
		// setto osservatori
		for (int i = 0; i < length; i++)
			for (int j = 0; j < length; j++) {
				celle[i][j].addObserver(celle);
			}
		// Setto valori iniziali celle
		for (int i = 0; i < length; i++)
			for (int j = 0; j < length; j++) {
				celle[i][j].setValue(valori[i][j]);
			}
		// Setto cella iniziale e finale
		int lastCol = length - 1, lastRow = length - 1;
		for (int i = 0; i < valori.length; i++)
			for (int j = 0; j < valori.length; j++) {
				if (valori[i][j] == 0) {
					lastCol = j;
					lastRow = i;
					if (this.first == null)
						this.first = new Cella(i, j);

				}
			}
		this.last = new Cella(lastRow, lastCol);
	}

	@Override
	protected Cella primoPuntoDiScelta() {
		return first;
	}

	@Override
	protected Cella prossimoPuntoDiScelta(Cella ps, Valore s) {
		for (int i = ps.getRow(); i < length; i++) {
			for (int j = 0; j < length; j++) {
				Cella corrente = new Cella(i, j);
				if (!scelteCellaValore.containsKey(corrente)
						&& valori[i][j] == 0) {
					return celle[i][j];
				}
			}
		}
		return last;
	}

	@Override
	protected Cella ultimoPuntoDiScelta() {
		return last;
	}

	@Override
	protected Valore primaScelta(Cella ps) {
		return ps.primaScelta();
	}

	@Override
	protected Valore prossimaScelta(Cella c, Valore v) {
		return c.prossimaScelta(v);
	}

	@Override
	protected Valore ultimaScelta(Cella ps) {
		return ps.ultimaScelta();
	}

	@Override
	protected boolean assegnabile(Valore value, Cella cell) {
		if (!cell.assegnabile(value.getValue())) {
			return false;
		}

		// Ricostruisco temporaneamente la tabella dei valori per la verifica
		int[][] temp = new int[length][length];
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				temp[i][j] = valori[i][j];
				if (scelteCellaValore.keySet().contains(new Cella(i, j)))
					temp[i][j] = scelteCellaValore.get(new Cella(i, j))
							.getValue();
			}
		}

		int val = value.getValue();
		int row = cell.getRow();
		int col = cell.getCol();

		// Verifico regole
		// Orizzantali
		if (col != 0) {
			if (orizzRules[row][col - 1] != ' ')
				if (temp[row][col - 1] != 0) {
					switch (orizzRules[row][col - 1]) {
					case '>':
						if (temp[row][col - 1] < val)
							return false;
						break;
					case '<':
						if (temp[row][col - 1] > val)
							return false;
						break;

					}

				}
		}
		if (col != length - 1) {
			if (orizzRules[row][col] != ' ')
				if (temp[row][col + 1] != 0) {
					switch (orizzRules[row][col]) {
					case '>':
						if (val < temp[row][col + 1])
							return false;
						break;
					case '<':
						if (val > temp[row][col + 1])
							return false;
						break;

					}
				}
		}
		// Verticali
		if (row != 0) {
			if (vertRules[row - 1][col] != ' ')
				if (temp[row - 1][col] != 0) {
					switch (vertRules[row - 1][col]) {
					case '^':
						if (temp[row - 1][col] > val)
							return false;
						break;
					case 'v':
						if (temp[row - 1][col] < val)
							return false;
						break;

					}
				}
		}
		if (row != length - 1) {
			if (vertRules[row][col] != ' ')
				if (temp[row + 1][col] != 0) {
					switch (vertRules[row][col]) {
					case '^':
						if (val > temp[row + 1][col])
							return false;
						break;
					case 'v':
						if (val < temp[row + 1][col])
							return false;
						break;
					}
				}
		}
		return true;
	}

	@Override
	protected void assegna(Valore scelta, Cella puntoDiScelta) {
		celle[puntoDiScelta.getRow()][puntoDiScelta.getCol()].setValue(scelta
				.getValue());
		puntiDiScelta.add(puntoDiScelta);
		scelteCellaValore.put(puntoDiScelta, scelta);
	}

	@Override
	protected void deassegna(Valore scelta, Cella puntoDiScelta) {
		celle[puntoDiScelta.getRow()][puntoDiScelta.getCol()].clearValue(scelta
				.getValue());
		if (!puntoDiScelta.equals(puntiDiScelta.getLast()))
			throw new IllegalArgumentException();
		puntiDiScelta.removeLast();
		scelteCellaValore.remove(puntoDiScelta);
	}

	@Override
	protected Cella precedentePuntoDiScelta(Cella puntoDiScelta) {
		return puntiDiScelta.getLast();
	}

	@Override
	protected Valore ultimaSceltaAssegnata(Cella puntoDiScelta) {
		return scelteCellaValore.get(puntoDiScelta);
	}

	@Override
	protected void scriviSoluzione(int nrsol) {
		int[][] soluzione = new int[length][length];
		// Copio Valori
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				if (scelteCellaValore.containsKey(new Cella(i, j))) {
					soluzione[i][j] = scelteCellaValore.get(new Cella(i, j))
							.getValue();
				}
			}
		}
		soluzioni.add(soluzione);
	}

	@Override
	public String toString() {
		int printLength = length * 2 - 1;
		String[][] mat = new String[printLength][printLength];
		for (int i = 0; i < printLength; i++)
			for (int j = 0; j < printLength; j++) {
				mat[i][j] = " ";
			}
		// Stampo Valori
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				if (scelteCellaValore.containsKey(new Cella(i, j))) {
					mat[i * 2][j * 2] = Integer.toString(scelteCellaValore.get(
							new Cella(i, j)).getValue());
				} else {
					mat[i * 2][j * 2] = Integer.toString(valori[i][j]);
				}
			}
		}
		// Stampo Regole Orizzontali
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length - 1; j++) {
				mat[i * 2][j * 2 + 1] = Character.toString(orizzRules[i][j]);
			}
		}
		// Stampo Regole Verticali
		for (int i = 0; i < length - 1; i++) {
			for (int j = 0; j < length; j++) {
				mat[i * 2 + 1][j * 2] = Character.toString(vertRules[i][j]);
			}
		}

		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < printLength; i++) {
			for (int j = 0; j < printLength; j++) {
				builder.append(mat[i][j]);
			}
			builder.append('\n');
		}
		return builder.toString();
	}

	public int getLength() {
		return length;
	}

	public int get(int column, int row) {
		return valori[row][column];
	}

	public LinkedList<int[][]> getSoluzioni() {
		return soluzioni;
	}
}