package com.capgemini.starterkit.javafx.controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.capgemini.starterkit.javafx.controls.LocalDateTableCell;
import com.capgemini.starterkit.javafx.dataprovider.DataProvider;
import com.capgemini.starterkit.javafx.dataprovider.data.PersonVO;
import com.capgemini.starterkit.javafx.dataprovider.data.SexVO;
import com.capgemini.starterkit.javafx.model.PersonSearchModel;
import com.capgemini.starterkit.javafx.model.Sex;
import com.capgemini.starterkit.javafx.texttospeech.Speaker;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 * Controller for the person search screen.
 * <p>
 * The JavaFX runtime will inject corresponding objects in the @FXML annotated
 * fields. The @FXML annotated methods will be called by JavaFX runtime at
 * specific points in time.
 * </p>
 *
 * @author Leszek
 */
public class PersonSearchController {

	private static final Logger LOG = Logger.getLogger(PersonSearchController.class);

	/**
	 * <p>
	 * NOTE: The variable name must be {@code resources}.
	 * </p>
	 */
	@FXML
	private ResourceBundle resources;

	/**
	 * <p>
	 * NOTE: The variable name must be {@code location}.
	 * </p>
	 */
	@FXML
	private URL location;

	@FXML
	private TextField nameField;

	@FXML
	private ComboBox<Sex> sexField;

	@FXML
	private Button searchButton;

	@FXML
	private TableView<PersonVO> resultTable;

	@FXML
	private TableColumn<PersonVO, String> nameColumn;

	@FXML
	private TableColumn<PersonVO, String> sexColumn;

	@FXML
	private TableColumn<PersonVO, LocalDate> birthDateColumn;

	private final DataProvider dataProvider = DataProvider.INSTANCE;

	private final Speaker speaker = Speaker.INSTANCE;

	private final PersonSearchModel model = new PersonSearchModel();

	/**
	 * The JavaFX runtime instantiates this controller.
	 * <p>
	 * The @FXML annotated fields are not yet initialized at this point.
	 * </p>
	 */
	public PersonSearchController() {
		LOG.debug("Constructor: nameField = " + nameField);
	}

	/**
	 * The JavaFX runtime calls this method after the FXML file loaded.
	 * <p>
	 * The @FXML annotated fields are initialized at this point.
	 * </p>
	 * <p>
	 * NOTE: The method name must be {@code initialize}.
	 * </p>
	 */
	@FXML
	private void initialize() {
		LOG.debug("initialize(): nameField = " + nameField);

		initializeSexField();

		initializeResultTable();

		/*
		 * Bind controls properties to model properties.
		 */
		nameField.textProperty().bindBidirectional(model.nameProperty());
		sexField.valueProperty().bindBidirectional(model.sexProperty());
		resultTable.itemsProperty().bind(model.resultProperty());

		/*
		 * Preselect the default value for sex.
		 */
		model.setSex(Sex.ANY);

		/*
		 * This works also, because we are using bidirectional binding.
		 */
		// sexField.setValue(SexModel.ANY);

		/*
		 * Make the Search button inactive when the Name field is empty.
		 */
		searchButton.disableProperty().bind(nameField.textProperty().isEmpty());
	}

	private void initializeSexField() {
		/*
		 * Add items to the list in combobox.
		 */
		sexField.getItems().add(Sex.ANY);
		for (SexVO sex : SexVO.values()) {
			sexField.getItems().add(Sex.fromSexVO(sex));
		}

		/*
		 * Set cell factory to render internationalized texts for list items.
		 */
		sexField.setCellFactory(new Callback<ListView<Sex>, ListCell<Sex>>() {

			@Override
			public ListCell<Sex> call(ListView<Sex> param) {
				return new ListCell<Sex>() {

					@Override
					protected void updateItem(Sex item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							return;
						}
						setText(resources.getString("sex." + item.name()));
					}
				};
			}
		});

		/*
		 * Set converter to display internationalized text for selected value.
		 */
		sexField.setConverter(new StringConverter<Sex>() {

			@Override
			public String toString(Sex object) {
				return resources.getString("sex." + object.name());
			}

			@Override
			public Sex fromString(String string) {
				/*
				 * Not used, because ComboBox is not editable.
				 */
				return null;
			}
		});
	}

	private void initializeResultTable() {
		/*
		 * Define what properties of PersonVO will be displayed in different
		 * columns.
		 */
		nameColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getName()));
		// nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		sexColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<PersonVO, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<PersonVO, String> param) {
						SexVO sex = param.getValue().getSex();
						/*
						 * Get localized string for given SexVO.
						 */
						String str = resources.getString("sex." + sex.name());
						return new ReadOnlyStringWrapper(str);
					}
				});
		birthDateColumn
				.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getBirthDate()));

		/*
		 * Define how the data (formatting, alignment, etc.) is displayed in
		 * columns.
		 */
		birthDateColumn
				.setCellFactory(param -> new LocalDateTableCell<PersonVO>(DateTimeFormatter.ofPattern("dd MMMM yyyy")));

		/*
		 * Define how the values in columns are sorted.
		 */
		birthDateColumn.setComparator(new Comparator<LocalDate>() {

			@Override
			public int compare(LocalDate o1, LocalDate o2) {
				return o1.getDayOfMonth() - o2.getDayOfMonth();
			}
		});

		/*
		 * Show specific text for empty table.
		 */
		resultTable.setPlaceholder(new Label(resources.getString("table.emptyText")));

		/*
		 * When table row gets select say given person name.
		 */
		resultTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<PersonVO>() {

			@Override
			public void changed(ObservableValue<? extends PersonVO> observable, PersonVO oldValue, PersonVO newValue) {
				LOG.debug(newValue + " selected");

				Task<Void> backgroundTask = new Task<Void>() {

					@Override
					protected Void call() throws Exception {
						speaker.say(newValue.getName());
						return null;
					}
				};
				new Thread(backgroundTask).start();
			}
		});
	}

	/**
	 * The JavaFX runtime calls this method when the <b>Search</b> button is
	 * clicked.
	 *
	 * @param event
	 *            {@link ActionEvent} holding information about this event
	 */
	@FXML
	private void searchButtonAction(ActionEvent event) {
		LOG.debug("'Search' button clicked");

		// searchButtonActionVersion1();
		searchButtonActionVersion2();
		// searchButtonActionVersion3();
	}

	/**
	 * <b>This implementation is INCORRECT!<b>
	 * <p>
	 * The {@link DataProvider#findPersons(String, SexVO)} call is executed in
	 * the JavaFX Application Thread.
	 * </p>
	 */
	private void searchButtonActionVersion1() {
		LOG.debug("INCORRECT implementation!");

		/*
		 * Get the data.
		 */
		Collection<PersonVO> result = dataProvider.findPersons( //
				model.getName(), //
				model.getSex().toSexVO());

		/*
		 * Copy the result to model.
		 */
		model.setResult(new ArrayList<PersonVO>(result));

		/*
		 * Reset sorting in table.
		 */
		resultTable.getSortOrder().clear();
	}

	/**
	 * This implementation is correct.
	 * <p>
	 * The {@link DataProvider#findPersons(String, SexVO)} call is executed in a
	 * background thread.
	 * </p>
	 */
	private void searchButtonActionVersion2() {
		/*
		 * Use task to execute the potentially long running call in background
		 * (separate thread), so that the JavaFX Application Thread is not
		 * blocked.
		 */
		Task<Collection<PersonVO>> backgroundTask = new Task<Collection<PersonVO>>() {

			/**
			 * This method will be executed in a background thread.
			 */
			@Override
			protected Collection<PersonVO> call() throws Exception {
				LOG.debug("call() called");

				/*
				 * Get the data.
				 */
				return dataProvider.findPersons( //
						model.getName(), //
						model.getSex().toSexVO());
			}
		};

		/*
		 * Monitor the "state" property to get informed when the background task
		 * finishes.
		 */
		backgroundTask.stateProperty().addListener(new ChangeListener<Worker.State>() {

			/**
			 * This method will be executed in the JavaFX Application Thread.
			 */
			@Override
			public void changed(ObservableValue<? extends State> observable, State oldValue, State newValue) {
				if (newValue == State.SUCCEEDED) {
					LOG.debug("changed() called");

					/*
					 * Copy the result to model.
					 */
					model.setResult(new ArrayList<PersonVO>(backgroundTask.getValue()));

					/*
					 * Reset sorting in table.
					 */
					resultTable.getSortOrder().clear();
				}
			}
		});

		/*
		 * Start the background task. In real life projects some framework
		 * manages background tasks.
		 */
		new Thread(backgroundTask).start();
	}

	/**
	 * This implementation is correct.
	 * <p>
	 * The {@link DataProvider#findPersons(String, SexVO)} call is executed in a
	 * background thread.
	 * </p>
	 */
	private void searchButtonActionVersion3() {
		/*
		 * Use runnable to execute the potentially long running call in
		 * background (separate thread), so that the JavaFX Application Thread
		 * is not blocked.
		 */
		Runnable backgroundTask = new Runnable() {

			/**
			 * This method will be executed in a background thread.
			 */
			@Override
			public void run() {
				LOG.debug("backgroundTask.run() called");

				/*
				 * Get the data.
				 */
				Collection<PersonVO> result = dataProvider.findPersons( //
						model.getName(), //
						model.getSex().toSexVO());

				/*
				 * Add an event(runnable) to the event queue.
				 */
				Platform.runLater(new Runnable() {

					/**
					 * This method will be executed in the JavaFX Application
					 * Thread.
					 */
					@Override
					public void run() {
						LOG.debug("Platform.runLater(Runnable.run()) called");

						/*
						 * Copy the result to model.
						 */
						model.setResult(new ArrayList<PersonVO>(result));

						/*
						 * Reset sorting in table.
						 */
						resultTable.getSortOrder().clear();
					}
				});
			}
		};

		/*
		 * Start the background task. In real life projects some framework
		 * manages threads.
		 */
		new Thread(backgroundTask).start();
	}

}
