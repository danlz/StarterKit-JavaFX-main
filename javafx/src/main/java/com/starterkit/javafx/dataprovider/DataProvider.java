package com.starterkit.javafx.dataprovider;

import java.util.Collection;

import com.starterkit.javafx.dataprovider.data.PersonVO;
import com.starterkit.javafx.dataprovider.data.SexVO;
import com.starterkit.javafx.dataprovider.impl.DataProviderImpl;

/**
 * Provides data.
 *
 * @author Leszek
 */
public interface DataProvider {

	/**
	 * Instance of this interface.
	 */
	DataProvider INSTANCE = new DataProviderImpl();

	/**
	 * Finds persons with their name containing specified string and/or given
	 * sex.
	 *
	 * @param name
	 *            string contained in name
	 * @param sex
	 *            sex
	 * @return collection of persons matching the given criteria
	 */
	Collection<PersonVO> findPersons(String name, SexVO sex);
}
