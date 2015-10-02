package com.starterkit.javafx.dataprovider.data;

import java.time.LocalDate;

/**
 * Person data.
 *
 * @author Leszek
 */
public class PersonVO {

	private String name;
	private SexVO sex;
	private LocalDate birthDate;

	public PersonVO(String name, SexVO sex, LocalDate birthDate) {
		this.name = name;
		this.sex = sex;
		this.birthDate = birthDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SexVO getSex() {
		return sex;
	}

	public void setSex(SexVO sex) {
		this.sex = sex;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	@Override
	public String toString() {
		return "Person [name=" + name + ", sex=" + sex + ", birthDate=" + birthDate + "]";
	}

}
