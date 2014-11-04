package org.bnez.xiaoyue.lsfy.idcard;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.bnez.xiaoyue.lsfy.common.Config;

public class IdCardRecord
{
	private static final Logger _logger = Logger.getLogger(IdCardRecord.class);
	
	private String _line;
	private String _deviceId;
	private String _ip;
	private String _cardId;
	private Date _recordTime;

	/**
	 * 
	 * @param line
	 *            SN14207623,192.168.1.10,0007288320,2014-07-21 17:41:50
	 * @return
	 */
	public static IdCardRecord buildFromFileLine(String line)
	{
		if(line == null || !line.contains(","))
			return null;
		
		String[] ss = line.split(",");
		if(ss.length != 4)
		{
			_logger.error("unknown data " + line);
			return null;
		}
		
		IdCardRecord r = new IdCardRecord();
		r._line = line;
		r._deviceId = ss[0];
		r._ip = ss[1];
		r._cardId = ss[2];
		try
		{
			String df = Config.getInstance().getString("idcard.df", "yyyy-MM-dd HH:mm:ss");
			r._recordTime = new SimpleDateFormat(df).parse(ss[3]);
		} catch (Exception e)
		{
			_logger.error(line, e);
			r = null;
		}
		
		return r;
	}

	public boolean isOutOfTime()
	{
		int threshold = Config.getInstance().getInt("idcard.timeout", 30) * 60 * 1000;
		return _recordTime.getTime() + threshold < new Date().getTime();
	}
	
	public String toString()
	{
		return _line;
	}

	public String getDeviceId()
	{
		return _deviceId;
	}

	public String getIp()
	{
		return _ip;
	}

	public String getCardId()
	{
		return _cardId;
	}

	public Date getRecordTime()
	{
		return _recordTime;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_cardId == null) ? 0 : _cardId.hashCode());
		result = prime * result + ((_deviceId == null) ? 0 : _deviceId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IdCardRecord other = (IdCardRecord) obj;
		if (_cardId == null)
		{
			if (other._cardId != null)
				return false;
		} else if (!_cardId.equals(other._cardId))
			return false;
		if (_deviceId == null)
		{
			if (other._deviceId != null)
				return false;
		} else if (!_deviceId.equals(other._deviceId))
			return false;
		return true;
	}

}
