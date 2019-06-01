package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class Cella extends Observable implements Observer {
	private static int length;
	private List<Integer> scelte = new ArrayList<Integer>();
	private int row;
	private int col;

	public Cella(int row, int col) {
		this.row = row;
		this.col = col;
		for (int n = 1; n <= length; n++) {
			scelte.add(new Integer(n));
			scelte.add(new Integer(n));
		}
		Collections.sort(scelte);
	}

	public synchronized void addObserver(Cella[][] cells) {
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				boolean isSame = (i == row) && (j == col);
				boolean isSameLine = (i == row) || (j == col);
				if (!isSame && (isSameLine)) {
					super.addObserver(cells[i][j]);
				}
			}
		}
	}

	public void setValue(int value) {
		scelte.remove(new Integer(value));
		super.setChanged();
		super.notifyObservers(new Notifica(true, value));
	}

	public void clearValue(int value) {
		scelte.add(new Integer(value));
		Collections.sort(scelte);
		super.setChanged();
		super.notifyObservers(new Notifica(false, value));
	}

	public void update(Observable o, Object arg) {
		if (((Notifica) arg).isSet()) {
			this.scelte.remove(new Integer(((Notifica) arg).getVal()));
		} else {
			scelte.add(new Integer(((Notifica) arg).getVal()));
			Collections.sort(scelte);
		}
	}

	public boolean assegnabile(int n) {
		if ((Collections.frequency(scelte, new Integer(n))) == 2)
			return true;
		return false;
	}

	private class Notifica {
		private boolean set;
		private int value;

		public Notifica(boolean set, int value) {
			this.set = set;
			this.value = value;
		}

		public boolean isSet() {
			return set;
		}

		public int getVal() {
			return value;
		}

		public String toString() {
			StringBuilder sb = new StringBuilder();
			if (set)
				sb.append("Inserimento di ");
			else
				sb.append("Cancellazione di ");
			sb.append(value);
			return sb.toString();
		}
	}

	public int getCol() {
		return col;
	}

	public int getRow() {
		return row;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + col;
		result = prime * result + row;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cella other = (Cella) obj;
		if (col != other.getCol())
			return false;
		if (row != other.getRow())
			return false;
		return true;
	}

	public String toString() {
		return "[" + row + "," + col + "]";
	}

	public Valore primaScelta() {
		int primo;
		for (Integer i : scelte) {
			primo = i.intValue();
			if (scelte.indexOf(i) + 1 < scelte.size())
				if (primo == scelte.get(scelte.indexOf(i) + 1).intValue())
					return new Valore(primo);
		}
		return new Valore(1);
	}

	public Valore ultimaScelta() {
		int primo;
		int ris = length;
		for (Integer i : scelte) {
			primo = i.intValue();
			if (scelte.indexOf(i) + 1 < scelte.size())
				if (primo == scelte.get(scelte.indexOf(i) + 1).intValue())
					ris = primo;
		}
		return new Valore(ris);
	}

	public Valore prossimaScelta(Valore v) {
		int val = v.getValue();
		int primo;
		for (Integer i : scelte) {
			primo = i.intValue();
			if (scelte.indexOf(i) + 1 < scelte.size())
				if (primo == scelte.get(scelte.indexOf(i) + 1).intValue()) {
					if (primo > val)
						return new Valore(primo);
				}
		}
		return new Valore(length);
	}

	public List<Integer> getScelte() {
		return scelte;
	}

	public static void setLength(int l) {
		length = l;
	}
}