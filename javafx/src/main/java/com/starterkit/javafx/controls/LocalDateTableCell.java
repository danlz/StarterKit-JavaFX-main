package com.starterkit.javafx.controls;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.scene.control.TableCell;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

/**
 * Table cell, which displays {@link LocalDate} in specified format.
 *
 * @author Leszek
 *
 * @param <T>
 *            The type of the TableView generic type
 */
public class LocalDateTableCell<T> extends TableCell<T, LocalDate> {

	private DateTimeFormatter dateFormat;

	/**
	 * Creates a new instance.
	 *
	 * @param dateFormat
	 *            format of date displayed in cell
	 */
	public LocalDateTableCell(DateTimeFormatter dateFormat) {
		this.dateFormat = dateFormat;
	}

	@Override
	protected void updateItem(LocalDate item, boolean empty) {
		super.updateItem(item, empty);
		if (empty) {
			setText(null);
			return;
		}

		/*
		 * Format the date according to given format.
		 */
		String str = item.format(dateFormat);

		/*
		 * Set the value in this table cell.
		 */
		setText(str);

		/*
		 * Dates before 1990-01-01 will be printed with bold and italic font.
		 * You have to always set the font, because TableCell instances are
		 * reused.
		 */
		Font font = getFont();
		if (item.isBefore(LocalDate.of(1990, 1, 1))) {
			Font changedFont = Font.font(font.getFamily(), FontWeight.BOLD, FontPosture.ITALIC, font.getSize());
			setFont(changedFont);
		} else {
			Font standardFont = Font.font(font.getFamily(), FontWeight.NORMAL, FontPosture.REGULAR, font.getSize());
			setFont(standardFont);
		}

		/*
		 * Define alignment for text in this cell. Better way to do it is CSS.
		 */
		// setAlignment(Pos.CENTER_RIGHT);
	}

}
