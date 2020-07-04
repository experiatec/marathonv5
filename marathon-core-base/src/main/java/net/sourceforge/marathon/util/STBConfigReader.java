package net.sourceforge.marathon.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

import net.sourceforge.marathon.util.STBConfigReader;

/**
 * External STB configuration reader.
 * @author glopez
 *
 */
public class STBConfigReader {

	public static final Logger LOGGER = Logger.getLogger(STBConfigReader.class.getName());

	public static STBConfigReader stbConfigReader =null;

	private Properties stbConfigProperties = null;

	/**
	 * 
	 */
	private STBConfigReader() {
		try (InputStream input = STBConfigReader.class.getClassLoader()
				.getResourceAsStream("stb-config.properties")) {
			this.stbConfigProperties = new Properties();

			if (input == null) {
				LOGGER.warning("Sorry, unable to find stb-config.properties");
				throw new RuntimeException("Sorry, unable to find stb-config.properties");
			}

			// load a properties file from class path, inside static method
			this.stbConfigProperties.load(input);
		} catch (IOException ex) {
			LOGGER.warning("An errror has occurred while reading stb-config.properties");
			throw new RuntimeException(ex);
		}
	}
	
	/**
	 * The STBConfigReader instance.
	 * @return STBConfig
	 */
	public static STBConfigReader getInstance() {
		if (stbConfigReader == null) {
			stbConfigReader = new STBConfigReader();
		}
		return stbConfigReader;
	}
	
	/**
	 * Returns The loaded properties from configuration file.
	 * @return Properties
	 */
	public Properties getSTBConfigProperties() {
		return this.stbConfigProperties;
	}

	/**
	 * The console window height.
	 * @return
	 */
	public static int getConsoleHeight() {
		return Integer.valueOf(getInstance().getSTBConfigProperties().getProperty("console.height"));
	}
	
	/**
	 * The console output width.
	 * @return
	 */
	public static int getConsoleWidth() {
		return Integer.valueOf(getInstance().getSTBConfigProperties().getProperty("console.width"));
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getJavaSecurityPolicyFilePath() {
		return getInstance().getSTBConfigProperties().getProperty("java.security.policy");
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getDeploymentSecurityLevel() {
		return getInstance().getSTBConfigProperties().getProperty("deployment.security.level");
	}
	
	/**
	 * Gets the string value of property name.
	 * @param propertyName
	 * @return
	 */
	public static String getStringValue(String propertyName, String defaultPropertyValue) {
		return getInstance().getSTBConfigProperties().getProperty(propertyName, defaultPropertyValue);
	}
}

