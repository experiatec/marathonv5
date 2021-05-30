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

public class AppletLauncherLayout implements ISubPropertiesLayout, IFileSelectedAction, IPropertiesLayout {

    public static final Logger LOGGER = Logger.getLogger(AppletLauncherLayout.class.getName());

    private ModalDialog<?> parent;
    private TextField urlField = new TextField();
    private TextField windowTitle = new TextField();
    private TextArea vmArgumentsField = new TextArea();
    private TextField javaHomeField = new TextField() {
        @Override
        public void requestFocus() {
        };
    };
    private Button urlDirBrowse = FXUIUtils.createButton("browse", getI18nLabel(LAUNCHER_APPLET_BUTTON_BROWSE_URL_TOOLTIP), true, getI18nLabel(LAUNCHER_APPLET_BUTTON_BROWSE_URL));
    private Button javaHomeBrowse = FXUIUtils.createButton("browse", getI18nLabel(LAUNCHER_APPLET_BUTTON_BROWSE_JAVA_HOME_TOOLTIP), true, getI18nLabel(LAUNCHER_APPLET_BUTTON_BROWSE_JAVA_HOME));

    public AppletLauncherLayout(ModalDialog<?> parent) {
        this.parent = parent;
        iniComponents();
    }

    @Override
    public Node getContent() {
        FormPane form = new FormPane("main-layout", 3);
        // @formatter:off
            form.addFormField(getI18nLabel(LAUNCHER_APPLET_URL), urlField, urlDirBrowse)
	            .addFormField(getI18nLabel(LAUNCHER_APPLET_WINDOW_TITLE), windowTitle)
	            .addFormField(getI18nLabel(LAUNCHER_APPLET_VM_ARGS), vmArgumentsField)
                .addFormField(getI18nLabel(LAUNCHER_APPLET_JAVA_HOME), javaHomeField, javaHomeBrowse);
        // @formatter:on
        return form;
    }

    private void iniComponents() {
    	urlField.setEditable(true);
        FileSelectionHandler urlDirHandler = new FileSelectionHandler(this, null, parent, urlField,
                getI18nLabel(LAUNCHER_APPLET_URL_DIR_HANDLER_TITLE));
        urlDirHandler.setMode(FileSelectionHandler.FILE_CHOOSER);
        urlDirBrowse.setOnAction(urlDirHandler);

        javaHomeField.setEditable(false);
        FileSelectionHandler javaHomeHandler = new FileSelectionHandler(this, null, parent, javaHomeField,
        		getI18nLabel(LAUNCHER_APPLET_JAVA_HOME_DIR_HANDLER_TITLE));
        javaHomeHandler.setMode(FileSelectionHandler.DIRECTORY_CHOOSER);
        javaHomeBrowse.setOnAction(javaHomeHandler);

    }

    @Override
    public String getName() {
        return getI18nLabel(LAUNCHER_APPLET_TAB_MAIN_LABEL);
    }

    @Override
    public Node getIcon() {
        return FXUIUtils.getIcon("main_obj");
    }

    @Override
    public void getProperties(Properties props) {
        props.setProperty(Constants.PROP_APPLICATION_APPLET_URL_FILE, urlField.getText());
        props.setProperty(Constants.PROP_APPLICATION_WINDOW_TITLE, windowTitle.getText());
        props.setProperty(Constants.PROP_APPLICATION_VM_ARGUMENTS, vmArgumentsField.getText().trim());
        props.setProperty(Constants.PROP_APPLICATION_JAVA_HOME, javaHomeField.getText());
    }

    @Override
    public void setProperties(Properties props) {
        urlField.setText(props.getProperty(Constants.PROP_APPLICATION_APPLET_URL_FILE, ""));
        windowTitle.setText(props.getProperty(Constants.PROP_APPLICATION_WINDOW_TITLE, ""));
        vmArgumentsField.setText(props.getProperty(Constants.PROP_APPLICATION_VM_ARGUMENTS, ""));
        javaHomeField.setText(props.getProperty(Constants.PROP_APPLICATION_JAVA_HOME, ""));
    }

    @Override
    public boolean isValidInput(boolean showAlert) {
        if (urlField.getText() == null || urlField.getText().equals("")) {
            if (showAlert) {
                FXUIUtils.showMessageDialog(parent.getStage(), getI18nLabel(LAUNCHER_APPLET_URL_ERROR_MESSAGE), getI18nLabel(LAUNCHER_APPLET_URL_ERROR_MESSAGE_FIELD), AlertType.ERROR);
            }
            Platform.runLater(() -> urlField.requestFocus());
            return false;
        }

        if (vmArgumentsField.getText().indexOf('\n') != -1 || vmArgumentsField.getText().indexOf('\r') != -1) {
            if (showAlert) {
                FXUIUtils.showMessageDialog(parent.getStage(), getI18nLabel(LAUNCHER_APPLET_VM_ARGS_ERROR_MESSAGE), getI18nLabel(LAUNCHER_APPLET_VM_ARGS_ERROR_MESSAGE_FIELD),
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
