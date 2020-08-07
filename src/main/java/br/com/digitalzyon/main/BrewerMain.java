package br.com.digitalzyon.main;

import java.time.LocalDate;
import java.time.Month;

public class BrewerMain {

	public static void main(String[] args) {
		LocalDate data = LocalDate.of(2020, Month.JUNE, 28);
		System.out.println(data.toEpochDay() * 24 * 60 * 60);

	}

}
