package org.bnez.xiaoyue.lsfy.rsp;

import org.apache.log4j.Logger;
import org.bnez.lsfy.service.BizServiceClient;
import org.bnez.lsfy.service.FayuanBean;
import org.bnez.lsfy.service.ReportPeriod;
import org.bnez.lsfy.service.ZhibiaoBean;
import org.bnez.xiaoyue.lsfy.XiaoyueResponse;
import org.bnez.xiaoyue.lsfy.common.Config;
import org.bnez.xiaoyue.lsfy.db.ReportDB;
import org.bnez.xiaoyue.lsfy.db.ReportData;

public class ZhibiaoQuery
{
	private static Logger _logger = Logger.getLogger(ZhibiaoQuery.class);

	private String _zb;
	private String _fy;
	private ReportPeriod _period;

	public ZhibiaoQuery(String zb, String fy, ReportPeriod period)
	{
		_zb = zb;
		_fy = fy;
		_period = period;
	}

	public XiaoyueResponse query()
	{
		ReportData rd = new ReportDB().query(_zb, _fy, _period);
		XiaoyueContext.setZhibiaoContext(_zb, _period, _fy, rd);

		if (rd == null)
		{
			String rsp = null;
			if (_fy == null)
				rsp = String.format("没有找到%s，%s的数据", _period.toChinese(), _zb);
			else
				rsp = String.format("没有找到%s，%s的%s的数据", _period.toChinese(), _fy, _zb);
			return ResponseBuilder.build(rsp);
		}

		String value = rd.getValue();
		XiaoyueResponse xr = null;
		if (value == null)
		{
			xr = ResponseBuilder.build(_period.toChinese() + "，" + _fy + "的" + _zb + "没有数据");
		} else if (value.trim().equals("") || value.trim().equals("-"))
		{
			xr = ResponseBuilder.build(_period.toChinese() + "，" + _fy + "的" + _zb + "在报表系统中没有数据");
		} else
		{
			if (_zb.endsWith("率"))
				value = "百分之" + value;
			String r = _period.toChinese() + "，" + _fy + "的" + _zb + "是" + value;
			r += "，" + buildTongbiResponse(rd.getTongbi());
			if(rd.getPaimingQuansheng() != null)
				r += "，全省排名" + buildPaiming(rd.getPaimingQuansheng());
			if(rd.getPaimingJichen() != null)
				r += "，基层排名" + buildPaiming(rd.getPaimingJichen());
			if(rd.getPaimingZhongyuan() != null)
				r += "，中院排名" + buildPaiming(rd.getPaimingZhongyuan());
			xr = ResponseBuilder.build(r);
		}

		String url = buildUrl(_zb, _fy, _period);
		if (url != null)
			xr.setUrl(url);

		return xr;
	}

	private String buildPaiming(Integer paimingQuansheng)
	{
		if (paimingQuansheng == null)
			return "在报表系统中无数据";

		return "第" + paimingQuansheng + "位";
	}

	private String buildTongbiResponse(String tongbi)
	{
		if (tongbi == null || tongbi.length() == 0 || tongbi.equals("-"))
			return "在报表系统中无数据";

		String v = tongbi;
		boolean isAdd = true;

		if (tongbi.startsWith("-"))
		{
			isAdd = false;
			v = tongbi.substring(1);
		}

		String zhengjia = null;
		if (isAdd)
			zhengjia = "增加";
		else
			zhengjia = "减少";
		
		v = "比往年同期" + zhengjia + v;
		if (_zb.endsWith("率"))
		{
			v = v + "个百分点";
		}

		return v;
	}

	private String buildUrl(String normalZb, String normalFy, ReportPeriod period)
	{
		ZhibiaoBean zb = BizServiceClient.getInstance().queryZhibiaoByName(normalZb);
		if (zb == null)
		{
			_logger.error("build url FAILED for zb " + normalZb);
			return null;
		}

		FayuanBean fy = BizServiceClient.getInstance().queryFayuanByRptName(normalFy);
		if (fy == null)
		{
			_logger.error("build url FAILED for fy " + normalFy);
			return null;
		}

		String fmt = Config.getInstance().getString("url.zhibiao");
		String url = String.format(fmt, zb.getId(), fy.getId(), period.getFrom(), period.getTo());
		return url;
	}
}
