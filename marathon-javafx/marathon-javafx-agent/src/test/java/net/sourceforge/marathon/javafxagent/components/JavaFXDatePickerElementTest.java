package net.sourceforge.marathon.javafxagent.components;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.testng.AssertJUnit;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.Pane;
import net.sourceforge.marathon.javafx.tests.DatePickerSample;
import net.sourceforge.marathon.javafxagent.IJavaFXElement;
import net.sourceforge.marathon.javafxagent.JavaFXAgent;
import net.sourceforge.marathon.javafxagent.Wait;

public class JavaFXDatePickerElementTest extends JavaFXElementTest {

    private JavaFXAgent driver;
    private List<IJavaFXElement> datePickers;
    private String dateString;

    @BeforeMethod public void initializeDriver() {
        LocalDate d = LocalDate.of(2016, 3, 16);
        dateString = new DatePicker(d).getConverter().toString(d);
        driver = new JavaFXAgent();
        datePickers = driver.findElementsByTagName("date-picker");
    }

    @Test public void selectDate() {
        DatePicker datePickerNode = (DatePicker) getPrimaryStage().getScene().getRoot().lookup(".date-picker");
        IJavaFXElement datePicker = datePickers.get(0);
        Platform.runLater(() -> {
            datePicker.marathon_select(dateString);
        });
        List<Object> dates = new ArrayList<>();
        Platform.runLater(() -> {
            LocalDate value = datePickerNode.getValue();
            dates.add(datePickerNode.getConverter().toString(value));
        });
        new Wait("Waiting for date to be set.") {
            @Override public boolean until() {
                return dates.size() > 0;
            }
        };
        AssertJUnit.assertEquals(dateString, dates.get(0));
    }

    @Test public void getText() {
        IJavaFXElement datePicker = datePickers.get(0);
        List<String> text = new ArrayList<>();
        Platform.runLater(() -> {
            datePicker.marathon_select(dateString);
            text.add(datePicker.getAttribute("text"));
        });
        new Wait("Waiting for date picker text.") {
            @Override public boolean until() {
                return text.size() > 0;
            }
        };
        AssertJUnit.assertEquals(dateString, text.get(0));
    }

    @Test public void selectEdiotrDate() {
        Set<Node> datePickerNodes = getPrimaryStage().getScene().getRoot().lookupAll(".date-picker");
        List<Node> pickers = new ArrayList<>(datePickerNodes);
        DatePicker datePickerNode = (DatePicker) pickers.get(1);
        IJavaFXElement datePicker = datePickers.get(1);
        Platform.runLater(() -> {
            datePicker.marathon_select(dateString);
        });
        List<Object> dates = new ArrayList<>();
        Platform.runLater(() -> {
            LocalDate value = datePickerNode.getValue();
            dates.add(datePickerNode.getConverter().toString(value));
        });
        new Wait("Waiting for date to be set.") {
            @Override public boolean until() {
                return dates.size() > 0;
            }
        };
        AssertJUnit.assertEquals(dateString, dates.get(0));
    }

    @Test(expectedExceptions = DateTimeException.class) public void datePickerWithInvalidDateFormat() {
        datePickers.get(1).marathon_select("16/7");
    }

    @Override protected Pane getMainPane() {
        return new DatePickerSample();
    }
}
