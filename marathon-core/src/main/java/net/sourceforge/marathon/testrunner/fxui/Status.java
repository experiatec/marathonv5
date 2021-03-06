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
package net.sourceforge.marathon.testrunner.fxui;

import java.util.logging.Logger;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class Status extends Label {

    public static final Logger LOGGER = Logger.getLogger(Status.class.getName());

    public Status() {
        super("Ready");
        Font font = getFont();
        double size = font.getSize();
        setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.ITALIC, size - 1));
        setPrefHeight(size + 14);
        setAlignment(Pos.CENTER);
    }

    public void reset() {
        setText("Ready");
    }
}
