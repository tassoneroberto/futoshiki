package model;

import junit.framework.TestCase;

public class TestFutoshiki extends TestCase {

	Futoshiki f;

	public void setUp() {
		int numSoluzioni = 5;
		int[][] valori = { { 3, 1, 0 }, { 0, 0, 3 }, { 2, 0, 0 } };
		char[][] orizzRules = { { '>', ' ' }, { ' ', '<' }, { ' ', ' ' } };
		char[][] vertRules = { { ' ', ' ', ' ' }, { '^', ' ', ' ' } };
		f = new Futoshiki(numSoluzioni, valori, orizzRules, vertRules);
	}

	public TestFutoshiki(String nome) {
		super(nome);
	}

	public void testSolutionsFound() {
		f.risolvi();
		int soluzioni = f.getSoluzioni().size();
		assertTrue(soluzioni > 0);
	}
}