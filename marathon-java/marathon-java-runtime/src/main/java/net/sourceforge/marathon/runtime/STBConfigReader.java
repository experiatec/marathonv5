package net.sourceforge.marathon.runtime;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

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

}
