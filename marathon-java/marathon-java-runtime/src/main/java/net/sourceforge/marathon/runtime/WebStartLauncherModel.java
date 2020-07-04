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

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
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

public class WebStartLauncherModel extends AbstractJavaDriverRuntimeLauncherModel
        implements IRuntimeLauncherModel, IJavaDriverRuntimeLauncherModel {

    public static final Logger LOGGER = Logger.getLogger(WebStartLauncherModel.class.getName());

    @Override
    public List<String> getPropertyKeys() {
        return Arrays.asList(
        		Constants.PROP_APPLICATION_WEBSTART_JNLP_LOCATION,
        		Constants.PROP_APPLICATION_WINDOW_TITLE,
        		Constants.PROP_APPLICATION_VM_ARGUMENTS, 
        		Constants.PROP_APPLICATION_WEBSTART_OPTIONS,
                Constants.PROP_APPLICATION_JAVA_HOME,
                Constants.PROP_APPLICATION_WEBSTART_NOSPLASH,
                Constants.PROP_APPLICATION_WEBSTART_OFFLINE
        		);
    }

    @Override
    public IRuntimeFactory getRuntimeFactory() {
        return new WebDriverRuntimeFactory(this);
    }

    @Override
    public JavaProfile createProfile(Map<String, Object> props, MarathonMode mode) {
        JavaProfile profile = new JavaProfile(LaunchMode.JAVA_WEBSTART);

        String jnlpLocation = (String) props.get(Constants.PROP_APPLICATION_WEBSTART_JNLP_LOCATION);
        if (jnlpLocation != null && !"".equals(jnlpLocation)) {
            profile.setJNLPPath(jnlpLocation);
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

        String args = (String) props.get(Constants.PROP_APPLICATION_WEBSTART_OPTIONS);
        if (!args.equals("")) {
            ArgumentProcessor p = new ArgumentProcessor(args);
            List<String> appArgs = p.parseArguments();
            profile.addWSArgument(appArgs.toArray(new String[appArgs.size()]));
        }
        
        String javaHome = (String) props.get(Constants.PROP_APPLICATION_JAVA_HOME);
        if (javaHome != null && !javaHome.equals("")) {
            profile.setJavaHome(javaHome);
        }
        
//      ADD NOSPLASH AS WEBSTART OPTION
//      -Xnosplash
//      Do not display the initial splash screen.
        Boolean nosplash = Boolean.valueOf((String)props.get(Constants.PROP_APPLICATION_WEBSTART_NOSPLASH));
        if (Boolean.TRUE.equals(nosplash)) {
        	profile.addWSArgument("-Xnosplash");
        }
        
//      ADD OFFLINE AS WEBSTART OPTION
//      -offline
//      Run Java Web Start in offline mode.
        boolean offline = Boolean.valueOf((String)props.get(Constants.PROP_APPLICATION_WEBSTART_OFFLINE));
        if (Boolean.TRUE.equals(offline)) {
        	profile.addWSArgument("-offline");
        }
        
        // Add security configuration to allow Marathon agents to run
        // It is also possible to pass this variable through Marathon Project Config or set it 
        // by editing java.policy system wide file (javahome>/lib/security) but it is not advisable to do it.
        String javaPolicyFilePath = STBConfigReader.getJavaSecurityPolicyFilePath();
        if (StringUtils.isNotEmpty(javaPolicyFilePath)) {
        	profile.addWSArgument(String.format("-J-Djava.security.policy=%s", getJavaPolicyFilePath()));
		}

        String deploymentSecurityLevel = STBConfigReader.getDeploymentSecurityLevel();
        if (StringUtils.isNotEmpty(deploymentSecurityLevel)) {
        	profile.addWSArgument("-J-Ddeployment.security.level=" + deploymentSecurityLevel);
		}
        return profile;
    }

    @Override
    public ISubPropertiesLayout[] getSublayouts(ModalDialog<?> parent) {
    	// Tab panels
        return new ISubPropertiesLayout[] { new WebStartLauncherLayout(parent) };
    }

    @Override
    public String getLaunchErrorMessage() {
        // @formatter:off
        return
            "Check the class path, main class name";
        // @formatter:on
    }

    /**
     * 
     * @return String javaws.policy file path
     */
	protected static String getJavaPolicyFilePath() {
		URL url = WebStartLauncherModel.class.getClassLoader().getResource("javaws.policy");
		if (url == null) {
			LOGGER.warning("Sorry, unable to find javaws.policy!");
			throw new RuntimeException("Sorry, unable to find javaws.policy");
		}
		LOGGER.info(String.format("Picking provided policy file: '%s' ", url.getPath()));
		String filePath = "";
		try {
			filePath = new File(URLDecoder.decode(url.getFile(), "UTF-8")).getAbsolutePath();
		} catch (UnsupportedEncodingException e) {
			LOGGER.severe("UTF-8 encoding error while parsing file path!");
		}
		return filePath;
	}
}
