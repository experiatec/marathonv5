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
package net.sourceforge.marathon.runtime;

import java.io.File;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import net.sourceforge.marathon.fx.api.FXUIUtils;
import net.sourceforge.marathon.fx.api.ModalDialog;
import net.sourceforge.marathon.fx.projectselection.FormPane;
import net.sourceforge.marathon.runtime.api.Constants;
import net.sourceforge.marathon.runtime.fx.api.FileSelectionHandler;
import net.sourceforge.marathon.runtime.fx.api.IFileSelectedAction;
import net.sourceforge.marathon.runtime.fx.api.IPropertiesLayout;
import net.sourceforge.marathon.runtime.fx.api.ISubPropertiesLayout;
import static net.sourceforge.marathon.util.I18n.*;

public class CommandLineLauncherLayout implements ISubPropertiesLayout, IFileSelectedAction, IPropertiesLayout {

    public static final Logger LOGGER = Logger.getLogger(CommandLineLauncherLayout.class.getName());

    private ModalDialog<?> parent;
    private TextField commandBatchScriptField = new TextField();
    private TextField windowTitle = new TextField();
    private TextArea programArgumentsField = new TextArea();
    private TextArea vmArgumentsField = new TextArea();
    private TextField workingDirField = new TextField() {
        @Override
        public void requestFocus() {
        };
    };
    private TextField javaHomeField = new TextField() {
        @Override
        public void requestFocus() {
        };
    };
    private Button commandBatchScriptDirBrowse = FXUIUtils.createButton("browse", getI18nLabel(LAUNCHER_CMDLINE_BUTTON_BROWSE_SCRIPT_TOOLTIP), true, getI18nLabel(LAUNCHER_CMDLINE_BUTTON_BROWSE_SCRIPT));
    private Button workindDirBrowse = FXUIUtils.createButton("browse", getI18nLabel(LAUNCHER_CMDLINE_BUTTON_BROWSE_WORKING_DIR_TOOLTIP), true, getI18nLabel(LAUNCHER_CMDLINE_BUTTON_BROWSE_WORKING_DIR));
    private Button javaHomeBrowse = FXUIUtils.createButton("browse", getI18nLabel(LAUNCHER_CMDLINE_BUTTON_BROWSE_JAVA_HOME_TOOLTIP), true, getI18nLabel(LAUNCHER_CMDLINE_BUTTON_BROWSE_SCRIPT));

    public CommandLineLauncherLayout(ModalDialog<?> parent) {
        this.parent = parent;
        iniComponents();
    }

    @Override
    public Node getContent() {
        FormPane form = new FormPane("main-layout", 3);
        // @formatter:off
            form.addFormField(getI18nLabel(LAUNCHER_CMDLINE_SCRIPT), commandBatchScriptField, commandBatchScriptDirBrowse)
	            .addFormField(getI18nLabel(LAUNCHER_CMDLINE_WORKING_DIR), workingDirField, workindDirBrowse)
	            .addFormField(getI18nLabel(LAUNCHER_CMDLINE_WINDOW_TITLE), windowTitle)
                .addFormField(getI18nLabel(LAUNCHER_CMDLINE_PROGRAM_ARGS), programArgumentsField)
                .addFormField(getI18nLabel(LAUNCHER_CMDLINE_VM_ARGS), vmArgumentsField)
                .addFormField(getI18nLabel(LAUNCHER_CMDLINE_JAVA_HOME), javaHomeField, javaHomeBrowse);
        // @formatter:on
        return form;

    }

    private void iniComponents() {
    	commandBatchScriptField.setEditable(false);
        FileSelectionHandler jarfileDirHandler = new FileSelectionHandler(this, null, parent, commandBatchScriptField,
                getI18nLabel(LAUNCHER_CMDLINE_JARFILE_DIR_HANDLER_TITLE));
        jarfileDirHandler.setMode(FileSelectionHandler.FILE_CHOOSER);
        commandBatchScriptDirBrowse.setOnAction(jarfileDirHandler);
        
        workingDirField.setEditable(false);
        FileSelectionHandler workingDirHandler = new FileSelectionHandler(this, null, parent, workingDirField,
        		getI18nLabel(LAUNCHER_CMDLINE_WOWRKING_DIR_HANDLER_TITLE));
        workingDirHandler.setMode(FileSelectionHandler.DIRECTORY_CHOOSER);
        workindDirBrowse.setOnAction(workingDirHandler);

        javaHomeField.setEditable(false);
        FileSelectionHandler javaHomeHandler = new FileSelectionHandler(this, null, parent, javaHomeField,
        		getI18nLabel(LAUNCHER_CMDLINE_JAVA_HOME_DIR_HANDLER_TITLE));
        javaHomeHandler.setMode(FileSelectionHandler.DIRECTORY_CHOOSER);
        javaHomeBrowse.setOnAction(javaHomeHandler);

    }

    @Override
    public String getName() {
        return getI18nLabel(LAUNCHER_CMDLINE_TAB_MAIN_LABEL);
    }

    @Override
    public Node getIcon() {
        return FXUIUtils.getIcon("main_obj");
    }

    @Override
    public void getProperties(Properties props) {
        props.setProperty(Constants.PROP_APPLICATION_COMMAND_BATCH_SCRIPT, commandBatchScriptField.getText());
        props.setProperty(Constants.PROP_APPLICATION_WINDOW_TITLE, windowTitle.getText());
        props.setProperty(Constants.PROP_APPLICATION_ARGUMENTS, programArgumentsField.getText().trim());
        props.setProperty(Constants.PROP_APPLICATION_VM_ARGUMENTS, vmArgumentsField.getText().trim());
        props.setProperty(Constants.PROP_APPLICATION_JAVA_HOME, javaHomeField.getText());
        props.setProperty(Constants.PROP_APPLICATION_WORKING_DIR, workingDirField.getText());
    }

    @Override
    public void setProperties(Properties props) {
        commandBatchScriptField.setText(props.getProperty(Constants.PROP_APPLICATION_COMMAND_BATCH_SCRIPT, ""));
        windowTitle.setText(props.getProperty(Constants.PROP_APPLICATION_WINDOW_TITLE, ""));
        programArgumentsField.setText(props.getProperty(Constants.PROP_APPLICATION_ARGUMENTS, ""));
        vmArgumentsField.setText(props.getProperty(Constants.PROP_APPLICATION_VM_ARGUMENTS, ""));
        javaHomeField.setText(props.getProperty(Constants.PROP_APPLICATION_JAVA_HOME, ""));
        workingDirField.setText(props.getProperty(Constants.PROP_APPLICATION_WORKING_DIR, ""));
    }

    @Override
    public boolean isValidInput(boolean showAlert) {
        if (commandBatchScriptField.getText() == null || commandBatchScriptField.getText().equals("")) {
            if (showAlert) {
                FXUIUtils.showMessageDialog(parent.getStage(), getI18nLabel(LAUNCHER_CMDLINE_SCRIPT_ERROR_MESSAGE), getI18nLabel(LAUNCHER_CMDLINE_SCRIPT_ERROR_MESSAGE_FIELD), AlertType.ERROR);
            }
            Platform.runLater(() -> commandBatchScriptField.requestFocus());
            return false;
        }

        if (programArgumentsField.getText().indexOf('\n') != -1 || programArgumentsField.getText().indexOf('\r') != -1) {
            if (showAlert) {
                FXUIUtils.showMessageDialog(parent.getStage(), getI18nLabel(LAUNCHER_CMDLINE_PROGRAM_ARGS_ERROR_MESSAGE), getI18nLabel(LAUNCHER_CMDLINE_PROGRAM_ARGS_ERROR_MESSAGE_FIELD),
                        AlertType.ERROR);
            }
            Platform.runLater(() -> programArgumentsField.requestFocus());
            return false;
        }
        if (vmArgumentsField.getText().indexOf('\n') != -1 || vmArgumentsField.getText().indexOf('\r') != -1) {
            if (showAlert) {
                FXUIUtils.showMessageDialog(parent.getStage(), getI18nLabel(LAUNCHER_CMDLINE_VM_ARGS_ERROR_MESSAGE), getI18nLabel(LAUNCHER_CMDLINE_VM_ARGS_ERROR_MESSAGE_FIELD),
                        AlertType.ERROR);
            }
            Platform.runLater(() -> vmArgumentsField.requestFocus());
            return false;
        }
        return true;
    }

    @Override
    public void filesSelected(List<File> selectedFiles, Object cookie) {
        if (selectedFiles != null && selectedFiles.size() != 0) {
            ((TextField) cookie).setText(selectedFiles.get(0).getAbsolutePath());
        }
    }
}
