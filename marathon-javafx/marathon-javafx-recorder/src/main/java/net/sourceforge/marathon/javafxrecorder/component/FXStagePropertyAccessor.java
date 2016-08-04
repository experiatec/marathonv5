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
package net.sourceforge.marathon.javafxrecorder.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.stage.Stage;
import net.sourceforge.marathon.javafxagent.JavaPropertyAccessor;
import net.sourceforge.marathon.javafxagent.WindowTitle;

public class FXStagePropertyAccessor extends JavaPropertyAccessor {

    private Stage stage;

    public FXStagePropertyAccessor(Stage stage) {
        super(stage);
        this.stage = stage;
    }

    public static final List<String> LAST_RESORT_RECOGNITION_PROPERTIES = new ArrayList<String>();

    static {
        LAST_RESORT_RECOGNITION_PROPERTIES.add("type");
        LAST_RESORT_RECOGNITION_PROPERTIES.add("title");
    }

    public final Map<String, String> findURP(List<List<String>> rp) {
        for (List<String> list : rp) {
            Map<String, String> rpValues = findValues(list);
            if (rpValues == null) {
                continue;
            }
        }
        return findValues(LAST_RESORT_RECOGNITION_PROPERTIES);
    }

    private Map<String, String> findValues(List<String> list) {
        Map<String, String> rpValues = new HashMap<String, String>();
        for (String attribute : list) {
            String value = getAttribute(attribute);
            if (value == null || "".equals(value)) {
                rpValues = null;
                break;
            }
            rpValues.put(attribute, value);
        }
        return rpValues;
    }

    public String getTitle() {
        return new WindowTitle(stage).getTitle();
    }

    public String getType() {
        return stage.getClass().getName();
    }
}
