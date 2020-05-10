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
import javafx.scene.control.CheckBox;
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

public class WebStartLauncherLayout implements ISubPropertiesLayout, IFileSelectedAction, IPropertiesLayout {

    public static final Logger LOGGER = Logger.getLogger(WebStartLauncherLayout.class.getName());

    private ModalDialog<?> parent;
    private TextField urlField = new TextField();
    private TextField windowTitle = new TextField();
    private TextArea vmArgumentsField = new TextArea();
    private TextArea webstartOptionsField = new TextArea();
    private CheckBox nosplashCheck = new CheckBox();
    private CheckBox offlineCheck = new CheckBox();
    private TextField javaHomeField = new TextField() {
        @Override
        public void requestFocus() {
        };
    };
    private Button urlDirBrowse = FXUIUtils.createButton("browse", "Browse JNLP file", true, "Browse");
    private Button javaHomeBrowse = FXUIUtils.createButton("browse", "Browse java home", true, "Browse");

    public WebStartLauncherLayout(ModalDialog<?> parent) {
        this.parent = parent;
        iniComponents();
    }

    @Override
    public Node getContent() {
        FormPane form = new FormPane("main-layout", 3);
        // @formatter:off
            form.addFormField("URL/File: ", urlField, urlDirBrowse)
	            .addFormField("Window Title: ", windowTitle)
	            .addFormField("VM Arguments: ", vmArgumentsField)
	            .addFormField("Webstart Options: ", webstartOptionsField)
	            .addFormField("No Splash: ", nosplashCheck)
	            .addFormField("Offline: ", offlineCheck)
                .addFormField("Java Home: ", javaHomeField, javaHomeBrowse);
        // @formatter:on
        return form;

    }

    private void iniComponents() {
    	urlField.setEditable(true);
        FileSelectionHandler urlDirHandler = new FileSelectionHandler(this, null, parent, urlField,
                "Select a JNLP File");
        urlDirHandler.setMode(FileSelectionHandler.FILE_CHOOSER);
        urlDirBrowse.setOnAction(urlDirHandler);

        javaHomeField.setEditable(false);
        FileSelectionHandler javaHomeHandler = new FileSelectionHandler(this, null, parent, javaHomeField,
                "Select Java Home Folder");
        javaHomeHandler.setMode(FileSelectionHandler.DIRECTORY_CHOOSER);
        javaHomeBrowse.setOnAction(javaHomeHandler);

    }

    @Override
    public String getName() {
        return "Main";
    }

    @Override
    public Node getIcon() {
        return FXUIUtils.getIcon("main_obj");
    }

    @Override
    public void getProperties(Properties props) {
        props.setProperty(Constants.PROP_APPLICATION_WEBSTART_JNLP_LOCATION, urlField.getText());
        props.setProperty(Constants.PROP_APPLICATION_WINDOW_TITLE, windowTitle.getText());
        props.setProperty(Constants.PROP_APPLICATION_VM_ARGUMENTS, vmArgumentsField.getText().trim());
        props.setProperty(Constants.PROP_APPLICATION_WEBSTART_OPTIONS, webstartOptionsField.getText().trim());
        props.setProperty(Constants.PROP_APPLICATION_JAVA_HOME, javaHomeField.getText());
        props.setProperty(Constants.PROP_APPLICATION_WEBSTART_NOSPLASH, nosplashCheck.isSelected() ? "true" : "false");
        props.setProperty(Constants.PROP_APPLICATION_WEBSTART_OFFLINE, offlineCheck.isSelected() ? "true" : "false");
    }

    @Override
    public void setProperties(Properties props) {
        urlField.setText(props.getProperty(Constants.PROP_APPLICATION_WEBSTART_JNLP_LOCATION, ""));
        windowTitle.setText(props.getProperty(Constants.PROP_APPLICATION_WINDOW_TITLE, ""));
        vmArgumentsField.setText(props.getProperty(Constants.PROP_APPLICATION_VM_ARGUMENTS, ""));
        webstartOptionsField.setText(props.getProperty(Constants.PROP_APPLICATION_WEBSTART_OPTIONS, ""));
        javaHomeField.setText(props.getProperty(Constants.PROP_APPLICATION_JAVA_HOME, ""));
        nosplashCheck.setSelected(Boolean.valueOf(props.getProperty(Constants.PROP_APPLICATION_WEBSTART_NOSPLASH, "")));
        offlineCheck.setSelected(Boolean.valueOf(props.getProperty(Constants.PROP_APPLICATION_WEBSTART_OFFLINE, "")));
    }

    @Override
    public boolean isValidInput(boolean showAlert) {
        if (urlField.getText() == null || urlField.getText().equals("")) {
            if (showAlert) {
                FXUIUtils.showMessageDialog(parent.getStage(), "JNLP file can't be empty", "Jar File", AlertType.ERROR);
            }
            Platform.runLater(() -> urlField.requestFocus());
            return false;
        }
//        if (!ValidationUtil.isValidClassName(jarfileField.getText())) {
//            if (showAlert) {
//                FXUIUtils.showMessageDialog(parent.getStage(), "Invalid class name given for main class", "Main Class",
//                        AlertType.ERROR);
//            }
//            Platform.runLater(() -> jarfileField.requestFocus());
//            return false;
//        }
//        if (classNameField.getText().indexOf('.') == -1) {
//            if (showAlert) {
//                Optional<ButtonType> r = FXUIUtils.showConfirmDialog(parent.getStage(),
//                        "There is no package given for the main class. You need to give fully qualified class name. Do you want to continue?",
//                        "Main Class", AlertType.CONFIRMATION, ButtonType.YES, ButtonType.NO);
//                if (r.get() != ButtonType.YES) {
//                    Platform.runLater(() -> classNameField.requestFocus());
//                    return false;
//                }
//            }
//            if (!showAlert) {
//                Platform.runLater(() -> classNameField.requestFocus());
//                return false;
//            }
//        }
        if (webstartOptionsField.getText().indexOf('\n') != -1 || webstartOptionsField.getText().indexOf('\r') != -1) {
            if (showAlert) {
                FXUIUtils.showMessageDialog(parent.getStage(), "Can not have new lines in Program Arguments", "Webstart Options",
                        AlertType.ERROR);
            }
            Platform.runLater(() -> webstartOptionsField.requestFocus());
            return false;
        }
        if (vmArgumentsField.getText().indexOf('\n') != -1 || vmArgumentsField.getText().indexOf('\r') != -1) {
            if (showAlert) {
                FXUIUtils.showMessageDialog(parent.getStage(), "Can not have new lines in VM Arguments", "VM Arguments",
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
