package com.starterkit.javafx.model;

import java.util.ArrayList;
import java.util.List;

import com.starterkit.javafx.dataprovider.data.PersonVO;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

/**
 * Data displayed on the person search screen.
 *
 * @author Leszek
 */
public class PersonSearch {

	private final StringProperty name = new SimpleStringProperty();
	private final ObjectProperty<Sex> sex = new SimpleObjectProperty<>();
	private final ListProperty<PersonVO> result = new SimpleListProperty<>(
			FXCollections.observableList(new ArrayList<>()));

	public final String getName() {
		return name.get();
	}

	public final void setName(String value) {
		name.set(value);
	}

	public StringProperty nameProperty() {
		return name;
	}

	public final Sex getSex() {
		return sex.get();
	}

	public final void setSex(Sex value) {
		sex.set(value);
	}

	public ObjectProperty<Sex> sexProperty() {
		return sex;
	}

	public final List<PersonVO> getResult() {
		return result.get();
	}

	public final void setResult(List<PersonVO> value) {
		result.setAll(value);
	}

	public ListProperty<PersonVO> resultProperty() {
		return result;
	}

	@Override
	public String toString() {
		return "PersonSearch [name=" + name + ", sex=" + sex + ", result=" + result + "]";
	}

}
