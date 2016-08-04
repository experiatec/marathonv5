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

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Map;

import net.sourceforge.marathon.runtime.api.ITestLauncher;

public class TestLauncher implements ITestLauncher {

    private IWebDriverRuntimeLauncherModel launcherModel;
    private Map<String, Object> ps;
    private PrintStream writerOutputStream;
    private PrintStream messagePS;
    private IWebdriverProxy proxy;

    public TestLauncher(IWebDriverRuntimeLauncherModel launcherModel, Map<String, Object> ps) {
        this.launcherModel = launcherModel;
        this.ps = ps;
    }

    @Override public void destroy() {
        if (proxy != null) {
            proxy.quit();
        }
    }

    @Override public void copyOutputTo(OutputStream writerOutputStream) {
        this.writerOutputStream = new PrintStream(writerOutputStream);
    }

    @Override public void setMessageArea(OutputStream writerOutputStream) {
        messagePS = new PrintStream(writerOutputStream);
    }

    @Override public int start() {
        int selection = OK_OPTION;
        if (!launcherModel.confirmConfiguration()) {
            return CANCEL_OPTION;
        }
        try {
            proxy = launcherModel.createDriver(ps, -1, writerOutputStream);
        } catch (Throwable t) {
            t.printStackTrace(writerOutputStream);
            writerOutputStream.flush();
            messagePS
                    .println("If you are using webstart/applet launchers this can happen if the policy files are not set properly");
            messagePS.println(
                    "Add the following lines to your policy file in <javahome>/lib/security/javaws.policy (see the above output for the java.home property)");
            messagePS.println(
                    "Add the following lines to your policy file in <javahome>/lib/security/java.policy (see the above output for the java.home property)");
            messagePS.println("grant codeBase \"${marathon.agent}\" {");
            messagePS.println("    permission java.security.AllPermission;");
            messagePS.println("};");
            messagePS.println("grant codeBase \"${marathon.recorder}\" {");
            messagePS.println("    permission java.security.AllPermission;");
            messagePS.println("};");
            messagePS.flush();
        }
        return selection;
    }

    @Override public String toString() {
        if (proxy != null) {
            return proxy.toString();
        }
        return super.toString();
    }
}
