/*******************************************************************************
 * Copyright 2016 Jalian Systems Pvt. Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package net.sourceforge.marathon.fx.projectselection;

import java.util.logging.Logger;

import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import net.sourceforge.marathon.api.ThemeHelper.StyleClassHelper;

public class FormPane extends GridPane {

    public static interface ISetConstraints {
        public void setFormConstraints(FormPane form);
    }

    public static final Logger LOGGER = Logger.getLogger(FormPane.class.getName());

    private int columns;

    private int currentRow = 0;

    public FormPane(String styleClass, int columns) {
        this.columns = columns;
        getStyleClass().addAll("form-pane", styleClass);
        ColumnConstraints cc = new ColumnConstraints();
        cc.setMinWidth(Region.USE_PREF_SIZE);
        getColumnConstraints().add(cc);
        getStyleClass().add(StyleClassHelper.BACKGROUND);
    }

    public FormPane addFormField(String text, Node... fields) {
        Label label = new Label(text);
        String labelId = idText(text);
        label.setId(labelId);
        GridPane.setValignment(label, VPos.TOP);
        int column = 0;
        add(label, column++, currentRow, 1, 1);
        int colspan = columns - fields.length;
        int fieldIndex = 1;
        for (Node field : fields) {
            field.setId(labelId + "-field-" + fieldIndex);
            setFormConstraints(field);
            GridPane.setValignment(field, VPos.TOP);
            add(field, column++, currentRow, colspan, 1);
            fieldIndex++;
        }
        currentRow++;
        column = 0;
        return this;
    }

    public FormPane addFormField(String text, Node field, int colSpan, int rowSpan) {
        Label label = new Label(text);
        String labelId = idText(text);
        label.setId(labelId);
        GridPane.setValignment(label, VPos.TOP);
        int column = 0;
        add(label, column++, currentRow, 1, 1);
        field.setId(labelId + "-field-1");
        setFormConstraints(field);
        GridPane.setValignment(field, VPos.TOP);
        add(field, column++, currentRow, colSpan, rowSpan);
        currentRow += rowSpan;
        column = 0;
        return this;
    }

    private String idText(String text) {
        String id = text.trim().replace(' ', '_').replaceAll("[^_a-zA-Z]", "").toLowerCase();
        return id;
    }

    private void setFormConstraints(Node field) {
        if (field instanceof ISetConstraints) {
            ((ISetConstraints) field).setFormConstraints(this);
        } else if (field instanceof Button) {
            _setFormConstraints((Button) field);
        } else if (field instanceof TextField) {
            _setFormConstraints((TextField) field);
        } else if (field instanceof TextArea) {
            _setFormConstraints((TextArea) field);
        } else if (field instanceof ComboBox<?>) {
            _setFormConstraints((ComboBox<?>) field);
        } else if (field instanceof ChoiceBox<?>) {
            _setFormConstraints((ChoiceBox<?>) field);
        } else if (field instanceof CheckBox) {
            _setFormConstraints((CheckBox) field);
        } else if (field instanceof Spinner<?>) {
            _setFormConstraints((Spinner<?>) field);
        } else if (field instanceof VBox) {
            _setFormConstraints((VBox) field);
        } else if (field instanceof Label) {
            _setFormConstraints((Label) field);
        } else {
            LOGGER.info("FormPane.setFormConstraints(): unknown field type: " + field.getClass().getName());
        }
    }

    private void _setFormConstraints(ChoiceBox<?> field) {
        field.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(field, Priority.ALWAYS);
    }

    private void _setFormConstraints(Label field) {
    }

    private void _setFormConstraints(VBox field) {
        GridPane.setHgrow(field, Priority.ALWAYS);
    }

    private void _setFormConstraints(Spinner<?> field) {
        GridPane.setHgrow(field, Priority.ALWAYS);
    }

    private void _setFormConstraints(CheckBox field) {
        GridPane.setHgrow(field, Priority.ALWAYS);
    }

    private void _setFormConstraints(TextArea field) {
        GridPane.setHgrow(field, Priority.ALWAYS);
        GridPane.setVgrow(field, Priority.ALWAYS);
    }

    private void _setFormConstraints(TextField field) {
        GridPane.setHgrow(field, Priority.ALWAYS);
    }

    private void _setFormConstraints(Button field) {
    }

    private void _setFormConstraints(ComboBox<?> field) {
        field.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(field, Priority.ALWAYS);
    }

}
