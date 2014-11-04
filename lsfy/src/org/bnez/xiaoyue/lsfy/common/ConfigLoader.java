package org.bnez.xiaoyue.lsfy.common;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;

public class ConfigLoader
{
	private static final Logger _logger = Logger.getLogger(ConfigLoader.class);
	private XMLConfiguration _config;
	
	public ConfigLoader(String xmlConfigFile)
	{
		_logger.debug("loading config " + xmlConfigFile);
		_config = loadFile(xmlConfigFile);
	}
	
	private XMLConfiguration loadFile(String fileName) 
	{
		XMLConfiguration config = new XMLConfiguration();
		config.setFileName(fileName);
		try
		{
			config.load();
		} catch (ConfigurationException e)
		{
			_logger.error("failed when load " + fileName, e);
		}
		return config;
	}

//	public String getString(String key, String defaultValue)
//	{
//		String v = getString(key);
//		if(v == null)
//			return defaultValue;
//		return v;
//	}
//
//	public String getString(String key)
//	{
//		return _config.getString(key);
//	}
//
//	public int getInt(String key, int defaultValue)
//	{
//		return _config.getInteger(key, defaultValue);
//	}
//
//	public int getInt(String key)
//	{
//		return _config.getInt(key);
//	}

	public XMLConfiguration getConfig()
	{
		return _config;
	}
}
