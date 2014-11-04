package org.bnez.xiaoyue.lsfy.rsp;

import org.bnez.lsfy.service.ReportPeriod;
import org.bnez.xiaoyue.lsfy.db.ReportData;

public class ZhibiaoContext
{
	private String zhibiao;
	private String time;
	private ReportPeriod period;
	private String fayuan;
	private ReportData data;

	public String getZhibiao()
	{
		return zhibiao;
	}

	public void setZhibiao(String zhibiao)
	{
		this.zhibiao = zhibiao;
	}

	public String getTime()
	{
		return time;
	}

	public void setTime(String time)
	{
		this.time = time;
	}

	public ReportPeriod getPeriod()
	{
		return period;
	}

	public void setPeriod(ReportPeriod period)
	{
		this.period = period;
	}

	public String getFayuan()
	{
		return fayuan;
	}

	public void setFayuan(String fayuan)
	{
		this.fayuan = fayuan;
	}

	public ReportData getData()
	{
		return data;
	}

	public void setData(ReportData data)
	{
		this.data = data;
	}
}
