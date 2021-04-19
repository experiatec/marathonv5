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
package net.sourceforge.marathon;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import javafx.application.Application;
import javafx.stage.Stage;
import net.sourceforge.marathon.util.GlobalKeyListener;

public class GUIMain extends Application {

    public static final Logger LOGGER = Logger.getLogger(GUIMain.class.getName());

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
    	initGlobalKeyListener();
        List<String> args = getParameters().getRaw();
        RealMain.realmain(args.toArray(new String[args.size()]));
    }

    private void initGlobalKeyListener() {
		try {
			GlobalScreen.registerNativeHook();
		} catch (NativeHookException ex) {
			LOGGER.log(Level.SEVERE, ex, () -> { return "An unexpected error has occurred while registering NativeHook!";});
			System.exit(1);
		}

		GlobalScreen.addNativeKeyListener(GlobalKeyListener.getInstance());
    }
}
