package org.bnez.xiaoyue.lsfy.report;

import org.bnez.xiaoyue.lsfy.common.Config;
import org.bnez.xiaoyue.lsfy.util.HibernateUtil;

public class NeedQuery
{

	private String _from;
	private String _to;
	private String _zhibiao;

	public NeedQuery(String from, String to, String zhibiao)
	{
		_from = from;
		_to = to;
		_zhibiao = zhibiao;
	}

	public boolean isNeed()
	{
		boolean checkExists = Config.getInstance().getBool("report.checkExists", true);
		if (!checkExists)
			return true;

		int count = 0;
		HibernateUtil.currentSession();
		try
		{
			count = new ReportDB().queryCount(_from, _to, _zhibiao);
		} catch (Exception e)
		{
		} finally
		{
			HibernateUtil.closeSession();
		}
		
		if (count > 0)
			return false;
		return true;
	}

}
