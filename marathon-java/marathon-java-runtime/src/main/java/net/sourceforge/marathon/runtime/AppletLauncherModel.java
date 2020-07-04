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

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.parboiled.common.StringUtils;

import net.sourceforge.marathon.fx.api.ModalDialog;
import net.sourceforge.marathon.javadriver.JavaProfile;
import net.sourceforge.marathon.javadriver.JavaProfile.LaunchMode;
import net.sourceforge.marathon.runtime.api.Constants;
import net.sourceforge.marathon.runtime.api.Constants.MarathonMode;
import net.sourceforge.marathon.runtime.api.IRuntimeFactory;
import net.sourceforge.marathon.runtime.api.IRuntimeLauncherModel;
import net.sourceforge.marathon.runtime.fx.api.ISubPropertiesLayout;
import net.sourceforge.marathon.util.STBConfigReader;

public class AppletLauncherModel extends AbstractJavaDriverRuntimeLauncherModel
        implements IRuntimeLauncherModel, IJavaDriverRuntimeLauncherModel {

    public static final Logger LOGGER = Logger.getLogger(AppletLauncherModel.class.getName());

    @Override
    public List<String> getPropertyKeys() {
        return Arrays.asList(
        		Constants.PROP_APPLICATION_APPLET_URL_FILE,
        		Constants.PROP_APPLICATION_WINDOW_TITLE,
        		Constants.PROP_APPLICATION_VM_ARGUMENTS, 
                Constants.PROP_APPLICATION_JAVA_HOME
        		);
    }

    @Override
    public IRuntimeFactory getRuntimeFactory() {
        return new WebDriverRuntimeFactory(this);
    }

    @Override
    public JavaProfile createProfile(Map<String, Object> props, MarathonMode mode) {
        JavaProfile profile = new JavaProfile(LaunchMode.JAVA_APPLET);

        String appletURLFile = (String) props.get(Constants.PROP_APPLICATION_APPLET_URL_FILE);
        if (appletURLFile != null && !"".equals(appletURLFile)) {
            profile.setAppletURL(appletURLFile);
        } 

        String windowTitle = (String) props.get(Constants.PROP_APPLICATION_WINDOW_TITLE);
        if (windowTitle != null && !"".equals(windowTitle)) {
            profile.setStartWindowTitle(windowTitle);
        }
        
        System.setProperty("marathon.mode", mode == MarathonMode.RECORDING ? "record" : "other");
        String vmArgs = (String) props.get(Constants.PROP_APPLICATION_VM_ARGUMENTS);
        if (vmArgs != null && !vmArgs.equals("")) {
            ArgumentProcessor p = new ArgumentProcessor(vmArgs);
            List<String> args = p.parseArguments();
            profile.addVMArgument(args.toArray(new String[args.size()]));
        }
        
        Set<String> keySet = props.keySet();
        for (String key : keySet) {
            if (key.startsWith(Constants.PROP_PROPPREFIX)) {
                int prefixLength = Constants.PROP_PROPPREFIX.length();
                profile.addVMArgument("-D" + key.substring(prefixLength) + "=" + props.get(key).toString());
            }
        }
        
        // Add security configuration to allow Marathon agents to run
        // It is also possible to pass this variable through Marathon Project Config or set it 
        // by editing java.policy system wide file (javahome>/lib/security) but it is not advisable to do it.
        String javaPolicyFilePath = STBConfigReader.getJavaSecurityPolicyFilePath();
        if (StringUtils.isNotEmpty(javaPolicyFilePath)) {
        	profile.addVMArgument("-Djava.security.policy=" + WebStartLauncherModel.getJavaPolicyFilePath());
		}
        
        String javaHome = (String) props.get(Constants.PROP_APPLICATION_JAVA_HOME);
        if (javaHome != null && !javaHome.equals("")) {
            profile.setJavaHome(javaHome);
        }
        
        return profile;
    }

    @Override
    public ISubPropertiesLayout[] getSublayouts(ModalDialog<?> parent) {
    	// Tab panels
        return new ISubPropertiesLayout[] { new AppletLauncherLayout(parent) };
    }

    @Override
    public String getLaunchErrorMessage() {
        // @formatter:off
        return
            "Check the class path, main class name";
        // @formatter:on
    }

}
