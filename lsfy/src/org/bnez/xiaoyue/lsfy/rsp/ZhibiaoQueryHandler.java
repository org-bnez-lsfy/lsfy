package org.bnez.xiaoyue.lsfy.rsp;

import org.apache.log4j.Logger;
import org.bnez.lsfy.service.BizServiceClient;
import org.bnez.lsfy.service.ReportPeriod;
import org.bnez.xiaoyue.lsfy.XiaoyueResponse;
import org.bnez.xiaoyue.lsfy.common.ErrorMessage;

public class ZhibiaoQueryHandler extends Handler
{
	private static Logger _logger = Logger.getLogger(ZhibiaoQueryHandler.class);

	public ZhibiaoQueryHandler(String combName, String combString)
	{
		_combName = combName;
		_combString = combString;
	}

	public XiaoyueResponse response()
	{
		String normalZb = parseZhibiao();
		if (normalZb == null)
			return ResponseBuilder.build(ErrorMessage.getErrorDefine("zb.unknownZhibiao"));
		if(normalZb.equals("审判未结案数"))
			return ResponseBuilder.build("审判未结案数等于未结案数减去执行未结案数");
		if(normalZb.equals("审判结案数"))
			return ResponseBuilder.build("审判结案数等于结案数减去执行结案数");
		if(normalZb.equals("审判收案数"))
			return ResponseBuilder.build("审判收案数等于收案数减去执行收案数");

		ReportPeriod period = parsePeriod();
		if (period == null)
			return ResponseBuilder.build(ErrorMessage.getErrorDefine("zb.emptyTime", normalZb));

		FayuanParser fp = new FayuanParser();
		String normalFy = fp.parse(_combString);
		
		if(fp.hasError())
			return fp.getErrorResponse();
		if(normalFy.equals("全市"))
		{
			normalFy = null;
			normalZb = "全市" + normalZb;
		}
		
		_logger.debug("query zhibiao " + normalZb + " at " + period + " for " + normalFy);
		return new ZhibiaoQuery(normalZb, normalFy, period).query();
	}

	private ReportPeriod parsePeriod()
	{
		_logger.debug(_combString);
		String tmstr = BizServiceClient.getInstance().parseReportQueryTimeString(_combString);
		ReportPeriod rp = BizServiceClient.getInstance().transReportQueryTimeStringToPeriod(tmstr);
		return rp;
	}

	private String parseZhibiao()
	{
		return BizServiceClient.getInstance().parseReportQueryZhibiaoName(_combString);
	}

	public static void main(String[] args)
	{
		ZhibiaoQueryHandler zq = new ZhibiaoQueryHandler("ls_zb_jal", "本年本院的结案率是多少");
		XiaoyueResponse xr = zq.response();
		System.out.println(xr.getSpeak());
	}
}
