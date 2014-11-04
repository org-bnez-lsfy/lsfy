package org.bnez.xiaoyue.lsfy.rsp;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.bnez.lsfy.service.BizServiceClient;
import org.bnez.lsfy.service.FayuanBean;
import org.bnez.lsfy.service.ReportPeriod;
import org.bnez.lsfy.service.ZhibiaoBean;
import org.bnez.xiaoyue.lsfy.XiaoyueResponse;
import org.bnez.xiaoyue.lsfy.db.ReportDB;
import org.bnez.xiaoyue.lsfy.db.ReportData;
import org.bnez.xiaoyue.lsfy.db.User;

public class HeliqujianOutHandler
{
	private static final Logger _logger = Logger.getLogger(HeliqujianOutHandler.class);

	private String _combString;
	private ZhibiaoBean _zb;
	private String _rsp;

	public HeliqujianOutHandler(String combString)
	{
		_combString = combString;
	}

	public XiaoyueResponse handle()
	{
		pickZhibiao();
		if (_zb == null)
			return ResponseBuilder.build("您要询问哪个指标超出合理区间？");

		String hlqjdesc = BizServiceClient.getInstance().queryHeliqujianDesc(_zb.getId());
		if(hlqjdesc == null || hlqjdesc.length() == 0)
			return ResponseBuilder.build(_zb.getName() + "没有定义合理区间值");

		findOut(hlqjdesc);

		return ResponseBuilder.build(_rsp);
	}

	private void findOut(String hlqjdesc)
	{
		List<String> notfound = new ArrayList<String>();
		List<String> isout = new ArrayList<String>();

		ReportPeriod rp = BizServiceClient.getInstance().getCurrentYearReportPeriod();

		User u = XiaoyueContext.getCurrentThreadUser();
		FayuanBean f = BizServiceClient.getInstance().queryFayuan(u.getFayuanId());
		if(f.isJichen())
			f = BizServiceClient.getInstance().queryFayuan(f.getParentId());
		List<FayuanBean> allFy = BizServiceClient.getInstance().queryAllFayuanWithTop(f.getId());
		
		List<ReportData> datas = new ReportDB().queryExistsFy(_zb.getName(), rp);

		for(FayuanBean fy : allFy)
		{
			ReportData data = pick(datas, fy);
			if (data == null || data.getValue() == null || data.getValue().equals("-"))
				notfound.add(fy.getRptName());
			if (isOutHeliQujian(data, fy))
				isout.add(fy.getRptName() + "（" + data.getValue() + "）");
		}

		_rsp = hlqjdesc;
		if (isout.size() == 0)
		{
			_rsp += "没有法院超出合理区间。";
		} else
		{
			_rsp += "其中";
			for (String out : isout)
				_rsp += out + "、";
			_rsp = _rsp.substring(0, _rsp.length() - 1);
			_rsp += "超出了合理区间值。";
		}

		if (notfound.size() != 0)
		{
			for (String nf : notfound)
				_rsp += nf + "、";
			_rsp = _rsp.substring(0, _rsp.length() - 1);
			_rsp += "没有找到数据。";
		}
	}

	private ReportData pick(List<ReportData> datas, FayuanBean fy)
	{
		for (ReportData d : datas)
			if (d.getKoujing4() != null && d.getKoujing4().equals(fy.getRptName()))
				return d;
		return null;
	}

	private void pickZhibiao()
	{
		String s = BizServiceClient.getInstance().parseReportQueryZhibiaoName(_combString);
		_zb = BizServiceClient.getInstance().queryZhibiaoByName(s);
	}

	private boolean isOutHeliQujian(ReportData data, FayuanBean fy)
	{
		if(data == null)
			return false;
		
		double v = -1997;
		try
		{
			v = Double.parseDouble(data.getValue());
		} catch (Exception e)
		{
			_logger.error("parse report data FAILED " + data.getValue());
			return false;
		}
		return BizServiceClient.getInstance().isOutHeliqujian(_zb.getId(), fy.getId(), v);
	}
}
