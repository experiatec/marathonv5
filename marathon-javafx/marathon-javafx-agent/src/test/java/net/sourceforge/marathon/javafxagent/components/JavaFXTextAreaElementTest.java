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
package net.sourceforge.marathon.javafxagent.components;

import org.testng.AssertJUnit;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import net.sourceforge.marathon.javafx.tests.TextAreaSample;
import net.sourceforge.marathon.javafxagent.IJavaFXElement;
import net.sourceforge.marathon.javafxagent.JavaFXAgent;
import net.sourceforge.marathon.javafxagent.Wait;

public class JavaFXTextAreaElementTest extends JavaFXElementTest {

    private JavaFXAgent driver;
    private IJavaFXElement textarea;

    @BeforeMethod public void initializeDriver() {
        driver = new JavaFXAgent();
        textarea = driver.findElementByTagName("text-area");
    }

    @Test public void marathon_select() {
        TextArea textAreaNode = (TextArea) getPrimaryStage().getScene().getRoot().lookup(".text-area");
        textarea.marathon_select("Hello World");
        new Wait("Waiting for the text area value to be set") {
            @Override public boolean until() {
                return "Hello World".equals(textAreaNode.getText());
            }
        };
    }

    @Test public void clear() {
        TextArea textAreaNode = (TextArea) getPrimaryStage().getScene().getRoot().lookup(".text-area");
        textarea.marathon_select("Hello World");
        new Wait("Waiting for the text area value to be set") {
            @Override public boolean until() {
                return "Hello World".equals(textAreaNode.getText());
            }
        };
        textarea.clear();
        new Wait("Waiting for the text area value to be cleared") {
            @Override public boolean until() {
                return "".equals(textAreaNode.getText());
            }
        };
    }

    @Test public void getText() {
        TextArea textAreaNode = (TextArea) getPrimaryStage().getScene().getRoot().lookup(".text-area");
        AssertJUnit.assertEquals("", textarea.getText());
        textarea.marathon_select("Hello World");
        new Wait("Waiting for the text area value to be set") {
            @Override public boolean until() {
                return "Hello World".equals(textAreaNode.getText());
            }
        };
        AssertJUnit.assertEquals("Hello World", textarea.getText());
    }

    @Test public void getAttributeText() {
        Platform.runLater(() -> {
            textarea.marathon_select("Hello World");
        });
        new Wait("Waiting for the text area text") {
            @Override public boolean until() {
                return textarea.getAttribute("text").equals("Hello World");
            }
        };
        AssertJUnit.assertEquals("Hello World", textarea.getAttribute("text"));
    }

    @Override protected Pane getMainPane() {
        return new TextAreaSample();
    }
}
