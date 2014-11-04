package org.bnez.xiaoyue.lsfy.common;

import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;

public class Config
{
	private static Config _instance = null;
	private static Logger _logger = Logger.getLogger(Config.class);

	private XMLConfiguration _config;

	public synchronized static Config getInstance()
	{
		if (_instance == null)
			_instance = new Config();
		return _instance;
	}

	private Config()
	{
		load();
	}

	public void reload()
	{
		_logger.debug("reload config");
		load();
	}

	private void load()
	{
		_config = new ConfigLoader("config.xml").getConfig();
	}

	public String getString(String key)
	{
		return _config.getString(key);
	}

	public String getString(String key, String defaultValue)
	{
		return _config.getString(key, defaultValue);
	}
	
	public int getInt(String key)
	{
		return _config.getInt(key);
	}

	public int getInt(String key, int defaultValue)
	{
		return _config.getInt(key, defaultValue);
	}

	public boolean getBool(String key)
	{
		return getBool(key, false);
	}

	public boolean getBool(String key, boolean defaultValue)
	{
		String s = getString(key);
		if(s == null)
			return defaultValue;
		return s.equals("1") || s.equals("on") || s.equals("true");
	}
}
