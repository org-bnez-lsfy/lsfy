package org.bnez.xiaoyue.lsfy.common;

import org.apache.log4j.Logger;

public class ErrorMessage
{
	public static final String ZHIBIAO_UNKNOWN_TIME_TAG = "zhibiao.unknownTimeTag";
	
	private static Logger _logger = Logger.getLogger(ErrorMessage.class);

	public static String getErrorDefine(String key)
	{
		String err = Config.getInstance().getString("error." + key);
		if (err == null)
		{
			_logger.error("undefined key " + key + " for error define");
			return "不清楚您咨询的问题！";
		}
		return err;
	}

	public static String getErrorDefine(String key, String... params)
	{
		String err = Config.getInstance().getString("error." + key);
		if (err == null)
		{
			_logger.error("undefined key " + key + " for error define");
			return "不清楚您咨询的问题！";
		}

		if (params == null)
		{
			_logger.error("params need for " + key + ", but its NULL");
			return "不清楚您咨询的问题！";
		}
		
		try
		{
			Object[] os = new Object[params.length];
			for(int i=0; i<params.length; i++)
				os[i] = params[i];
			return String.format(err, os);
		} catch (Exception e)
		{
			StringBuilder sb = new StringBuilder();
			for (String p : params)
				sb.append(p + " ");
			_logger.error("failed to format error message " + key + " [" + sb.toString() + "]");
			return "不清楚您咨询的问题！";
		}
	}
}
