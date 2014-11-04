package org.bnez.xiaoyue.lsfy.rsp;

import java.util.HashMap;
import java.util.Map;

import org.bnez.lsfy.service.ReportPeriod;
import org.bnez.xiaoyue.lsfy.XiaoyueResponse;
import org.bnez.xiaoyue.lsfy.db.ReportData;
import org.bnez.xiaoyue.lsfy.db.User;

public class XiaoyueContext
{
	private static ThreadLocal<User> _currentThreadUser = new ThreadLocal<User>();
	private static Map<User, ZhibiaoContext> _zhibiaoContext = new HashMap<User, ZhibiaoContext>();
	private static Map<User, XiaoyueResponse> _lastContext = new HashMap<User, XiaoyueResponse>();

	public static void setCurrentThreadUser(User user)
	{
		_currentThreadUser.set(user);
	}

	public static User getCurrentThreadUser()
	{
		return _currentThreadUser.get();
	}

	public static void setZhibiaoContext(String normalZb, ReportPeriod period, String normalFy,
			ReportData rd)
	{
		User u = getCurrentThreadUser();
		if(u == null)
			return;
		
		ZhibiaoContext c = new ZhibiaoContext();
		c.setZhibiao(normalZb);
		c.setPeriod(period);
		c.setFayuan(normalFy);
		c.setData(rd);
		_zhibiaoContext.put(u, c);
	}

	public static ZhibiaoContext getZhibiaoContext()
	{
		User u = getCurrentThreadUser();
		if(u == null)
			return null;
		return _zhibiaoContext.get(u);
	}

	public static void setLastResponse(XiaoyueResponse xr)
	{
		User u = getCurrentThreadUser();
		if(u == null)
			return;
		
		_lastContext.put(u, xr);
	}
	
	public static XiaoyueResponse getLastResponse()
	{
		User u = getCurrentThreadUser();
		if(u == null)
			return null;
		return _lastContext.get(u);
	}
}
