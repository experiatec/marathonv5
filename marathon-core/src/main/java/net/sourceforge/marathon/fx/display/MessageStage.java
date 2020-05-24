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
package net.sourceforge.marathon.fx.display;

import java.util.logging.Logger;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.sourceforge.marathon.fx.api.ButtonBarX;
import net.sourceforge.marathon.fx.api.FXUIUtils;
import net.sourceforge.marathon.fx.api.ModalDialog;

public class MessageStage extends ModalDialog<MessageInfo> {

    public static final Logger LOGGER = Logger.getLogger(MessageStage.class.getName());

    private MessageInfo resultInfo;
    private ButtonBarX buttonBar = new ButtonBarX();
    private TextInputControl textArea;
    private Button closeButton = FXUIUtils.createButton("cancel", "Close", true, "Close");

    public MessageStage(MessageInfo resultInfo) {
        super(resultInfo.getTitle(), null, null);
        this.resultInfo = resultInfo;
        initComponents();
    }

    @Override
    protected void initialize(Stage stage) {
        super.initialize(stage);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
    }

    private void initComponents() {
        textArea = resultInfo.getNode();
        textArea.setText(resultInfo.getMessage());
        textArea.setEditable(false);

        closeButton.setOnAction((e) -> dispose());
        buttonBar.setButtonMinWidth(Region.USE_PREF_SIZE);
        buttonBar.getButtons().add(closeButton);
    }

    @Override
    public Parent getContentPane() {
        BorderPane root = new BorderPane();
        root.setId("message-stage");
        root.setCenter(new ScrollPane(textArea));
        root.setBottom(buttonBar);
        return root;
    }

    @Override
    protected void setDefaultButton() {
        closeButton.setDefaultButton(true);
    }
}
