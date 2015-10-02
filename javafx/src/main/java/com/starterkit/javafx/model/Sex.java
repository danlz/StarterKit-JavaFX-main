package com.starterkit.javafx.model;

import com.starterkit.javafx.dataprovider.data.SexVO;

/**
 * Sex values used in GUI.
 *
 * @author Leszek
 */
public enum Sex {

	ANY, FEMALE, MALE;

	/**
	 * Converts {@link SexVO} to corresponding {@link Sex}.
	 *
	 * @param sex
	 *            {@link SexVO} value
	 * @return {@link Sex} value
	 */
	public static Sex fromSexVO(SexVO sex) {
		return Sex.valueOf(sex.name());
	}

	/**
	 * Converts this {@link Sex} to corresponding {@link SexVO}. For values that
	 * do not have corresponding value {@code null} is returned.
	 *
	 * @return {@link SexVO} value or {@code null}
	 */
	public SexVO toSexVO() {
		if (this == ANY) {
			return null;
		}
		return SexVO.valueOf(this.name());
	}
}
