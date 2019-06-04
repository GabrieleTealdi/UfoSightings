package it.polito.tdp.ufo.model;

import java.time.Year;

public class TestModel {
	
	public void run() {
		Model model = new Model();
		model.creaGrafo(Year.of(2010));
		
	}

	public static void main(String[] args) {
		TestModel main = new TestModel();
		main.run();

	}

}
