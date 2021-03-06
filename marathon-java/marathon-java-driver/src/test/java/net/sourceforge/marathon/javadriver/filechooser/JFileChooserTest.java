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
package net.sourceforge.marathon.javadriver.filechooser;

import java.awt.BorderLayout;
import java.io.File;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import components.FileChooserDemo;
import net.sourceforge.marathon.javadriver.JavaDriver;

@Test
public class JFileChooserTest {
    private WebDriver driver;
    protected JFrame frame;
	protected JFileChooser fc;

    @BeforeMethod
    public void showDialog() throws Throwable {
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                frame = new JFrame(JFileChooserTest.class.getSimpleName());
                frame.setName("frame-" + JFileChooserTest.class.getSimpleName());
                frame.getContentPane().add(new FileChooserDemo(), BorderLayout.CENTER);
                frame.pack();
                frame.setVisible(true);
                frame.setAlwaysOnTop(true);
                File file = new File(System.getProperty("java.home"));
                System.setProperty("marathon.project.dir", file.getPath());
            }
        });

        driver = new JavaDriver();
    }

    @AfterMethod
    public void disposeDriver() throws InterruptedException, InvocationTargetException {
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                frame.setVisible(false);
                frame.dispose();
            }
        });
        if (driver != null) {
            driver.quit();
        }
    }

    public void jfileChooserTest() throws Throwable {
        WebElement button = driver.findElement(By.tagName("button"));
        button.click();

        driver.switchTo().window("Open");
        WebElement fc = driver.findElement(By.tagName("file-chooser"));
        File file = new File(System.getProperty("user.dir"));
        marathon_select(fc, file.getAbsolutePath());
        String attribute = fc.getAttribute("selectedFile");
        AssertJUnit.assertEquals(file.getAbsoluteFile().toString(), attribute);
    }
    
    protected void marathon_select(WebElement e, String state) {
        String encodedState = state.replaceAll("\\\\", "\\\\\\\\").replaceAll("'", "\\\\'");
        e.findElement(By.cssSelector(".::call-select('" + encodedState + "')"));
    }
}
