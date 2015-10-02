package com.starterkit.javafx.dataprovider.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.starterkit.javafx.dataprovider.DataProvider;
import com.starterkit.javafx.dataprovider.data.PersonVO;
import com.starterkit.javafx.dataprovider.data.SexVO;

/**
 * Provides data. Data is stored locally in this object. Additionally a call
 * delay is simulated.
 *
 * @author Leszek
 */
public class DataProviderImpl implements DataProvider {

	private static final Logger LOG = Logger.getLogger(DataProviderImpl.class);

	/**
	 * Delay (in ms) for method calls.
	 */
	private static final long CALL_DELAY = 3000;

	private Collection<PersonVO> persons = new ArrayList<>();

	public DataProviderImpl() {
		persons.add(new PersonVO("Johnny", SexVO.MALE, LocalDate.of(1985, 5, 10)));
		persons.add(new PersonVO("Tommy", SexVO.MALE, LocalDate.of(1992, 2, 11)));
		persons.add(new PersonVO("Jack", SexVO.MALE, LocalDate.of(1991, 9, 11)));
		persons.add(new PersonVO("Jenny", SexVO.FEMALE, LocalDate.of(1995, 8, 25)));
		persons.add(new PersonVO("Mary", SexVO.FEMALE, LocalDate.of(1994, 4, 6)));
		persons.add(new PersonVO("Kate", SexVO.FEMALE, LocalDate.of(1998, 12, 29)));
		persons.add(new PersonVO("Emma", SexVO.FEMALE, LocalDate.of(1997, 6, 2)));
		persons.add(new PersonVO("Frankie", SexVO.MALE, LocalDate.of(1988, 10, 1)));
		persons.add(new PersonVO("Matt", SexVO.MALE, LocalDate.of(1981, 3, 30)));
		persons.add(new PersonVO("Max", SexVO.MALE, LocalDate.of(1983, 8, 22)));
		persons.add(new PersonVO("Go\u015Bcis\u0142aw", SexVO.MALE, LocalDate.of(1970, 4, 4)));
		persons.add(new PersonVO("El\u017Cbieta", SexVO.FEMALE, LocalDate.of(1974, 7, 19)));
	}

	@Override
	public Collection<PersonVO> findPersons(String name, SexVO sex) {
		LOG.debug("Entering findPersons()");

		/*
		 * Simulate a call delay.
		 */
		try {
			Thread.sleep(CALL_DELAY);
		} catch (InterruptedException e) {
			throw new RuntimeException("Thread interrupted", e);
		}

		Collection<PersonVO> result = persons.stream().filter(p -> //
		((name == null || name.isEmpty()) || (name != null && !name.isEmpty() && p.getName().contains(name))) //
				&& //
				((sex == null) || (sex != null && p.getSex() == sex)) //
		).collect(Collectors.toList());

		LOG.debug("Leaving findPersons()");
		return result;
	}
}
