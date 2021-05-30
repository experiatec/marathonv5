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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javafx.stage.Stage;
import net.sourceforge.marathon.runtime.api.Constants;
import net.sourceforge.marathon.runtime.api.ProjectFile;
import static net.sourceforge.marathon.util.I18n.*;

public class EditProjectHandler implements IEditProjectHandler {

    public static final Logger LOGGER = Logger.getLogger(EditProjectHandler.class.getName());

    private Stage parent;

    public EditProjectHandler(Stage parent) {
        this.parent = parent;
    }

    @Override
    public boolean editProject(ProjectInfo selected) {
        List<Boolean> projectEdited = new ArrayList<>();
        String title = getI18nLabel(EDIT_PROJECT_TITLE);
        String dirName = selected.getFolder();
        Properties properties = new Properties();
        try {
            properties = ProjectFile.getProjectProperties(dirName);
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
        properties.setProperty(Constants.PROP_PROJECT_DIR, dirName);
        setFrameWork(properties);
        String name = properties.getProperty(Constants.PROP_PROJECT_NAME);
        if (name != null) {
            title = title.concat(" (").concat(name).concat(")");
        }
        MPFConfigurationInfo mpfConfigurationInfo = new MPFConfigurationInfo(title, selected.getFolder(), properties);
        MPFConfigurationStage mpfConfigurationStage = new MPFConfigurationStage(parent, mpfConfigurationInfo) {
            @Override
            public void onSave() {
                if (validInupt()) {
                    File projectFile = mpfConfigurationInfo.saveProjectFile(layouts);
                    selected.setName(System.getProperty(Constants.PROP_PROJECT_NAME));
                    selected.setFolder(projectFile.getAbsolutePath());
                    projectEdited.add(true);
                    dispose();
                }
            }
        };
        mpfConfigurationStage.getStage().showAndWait();
        if (projectEdited.size() == 0) {
            System.clearProperty(Constants.PROP_PROJECT_LAUNCHER_MODEL);
            return false;
        }
        return true;
    }

    private void setFrameWork(Properties properties) {
        String framework = properties.getProperty(Constants.PROP_PROJECT_FRAMEWORK);
        if (framework != null) {
            System.setProperty(Constants.PROP_PROJECT_FRAMEWORK, framework);
        }
        String launcherModel = properties.getProperty(Constants.PROP_PROJECT_LAUNCHER_MODEL);
        if (launcherModel != null) {
            System.setProperty(Constants.PROP_PROJECT_LAUNCHER_MODEL, launcherModel);
        }
    }

}
