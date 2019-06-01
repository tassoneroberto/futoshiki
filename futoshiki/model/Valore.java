package model;

public class Valore {
	private static int length;
	private final int value;
	private static final int MINVALUE = 1;
	private static final int MAXVALUE = length;

	public Valore(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	protected Valore next() {
		int next = value + 1;
		if (next == MAXVALUE + 1)
			next = MINVALUE;
		return new Valore(next);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + value;
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
		Valore other = (Valore) obj;
		if (value != other.getValue())
			return false;
		return true;
	}

	public String toString() {
		return "" + value;
	}

	public static void setLength(int l) {
		length = l;
	}
}
