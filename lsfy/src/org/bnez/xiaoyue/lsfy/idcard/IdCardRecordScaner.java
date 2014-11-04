package org.bnez.xiaoyue.lsfy.idcard;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.bnez.xiaoyue.lsfy.common.Config;

public class IdCardRecordScaner
{
	private static final Logger _logger = Logger.getLogger(IdCardRecordScaner.class);
	private static IdCardRecordScaner _instance = null;
	
	private Map<String, List<String>> _todayLines;
	private Date _current;
	private String _todayFileKey;
	
	public static IdCardRecordScaner getInstance()
	{
		if(_instance == null)
			_instance = new IdCardRecordScaner();
		return _instance;
	}
	
	private IdCardRecordScaner()
	{
		_todayLines = new HashMap<String, List<String>>();
		scan(true);
	}
	
	private File buildRecordPath()
	{
		String path = Config.getInstance().getString("idcard.path");
		String month = new SimpleDateFormat("yyyy年MM月").format(_current);
		String dayFile = new SimpleDateFormat("MMd").format(_current);
		File f = new File(path + "/" + month + "/ReaD" + dayFile + ".txt");
		return f;
	}

	public void scan()
	{
		scan(false);
	}
	
	private void scan(boolean isInit)
	{
		_current = new Date();
		_todayFileKey = new SimpleDateFormat("yyyyMMdd").format(_current);
		
		if(!_todayLines.containsKey(_todayFileKey))
		{
			_todayLines.clear();
			_todayLines.put(_todayFileKey, new ArrayList<String>());
		}
		
		File f = buildRecordPath();
		if(!f.exists())
			return;
		
		scanFile(f, isInit);
	}

	private void scanFile(File f, boolean isInit)
	{
		try
		{
			@SuppressWarnings("unchecked")
			List<String> lines = FileUtils.readLines(f);
			List<String> existsLines = _todayLines.get(_todayFileKey);
			
			for(String line : lines)
			{
				if(existsLines.contains(line))
					continue;
				
				existsLines.add(line);
				IdCardRecord r = IdCardRecord.buildFromFileLine(line);
				if(r != null && !r.isOutOfTime())
				{
					IdCardRepository rep = IdCardRepository.getInstance();
					if(rep.contains(r))
						rep.remove(r);
					rep.add(r);
					
					if(!isInit)
						fireUserLogin(r.getCardId(), r.getDeviceId());
				}
			}
		} catch (IOException e)
		{
			_logger.error(f.getAbsolutePath(), e);
			e.printStackTrace();
		}
		//IdCardRepository.getInstance().print();
	}

	private void fireUserLogin(final String cardId, final String deviceId)
	{
		_logger.debug(cardId + " login from " + deviceId);
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				new UserLoginEvent(cardId, deviceId).fire();
			}
		}).start();
	}

	public static void main(String[] args)
	{
		IdCardRecordScaner s = new IdCardRecordScaner();
		s.scan();
		IdCardRepository.getInstance().print();
		System.out.println("done");
	}
}
