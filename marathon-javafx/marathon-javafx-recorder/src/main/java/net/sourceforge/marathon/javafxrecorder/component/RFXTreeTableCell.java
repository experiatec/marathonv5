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

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TreeTableCell;
import net.sourceforge.marathon.javafxrecorder.IJSONRecorder;
import net.sourceforge.marathon.javafxrecorder.JSONOMapConfig;

public class RFXTreeTableCell extends RFXComponent {

    public RFXTreeTableCell(Node source, JSONOMapConfig omapConfig, Point2D point, IJSONRecorder recorder) {
        super(source, omapConfig, point, recorder);
    }

    @Override public String _getValue() {
        TreeTableCell<?, ?> cell = (TreeTableCell<?, ?>) node;
        Node graphic = cell.getGraphic();
        RFXComponent component = getFinder().findRawRComponent(graphic, null, recorder);
        if (graphic != null && component != null) {
            if (graphic instanceof CheckBox) {
                String cellText = cell.getText() == null ? "" : cell.getText();
                return cellText + ":" + component._getValue();
            }
            return component._getValue();
        }
        return super._getValue();
    }
}
