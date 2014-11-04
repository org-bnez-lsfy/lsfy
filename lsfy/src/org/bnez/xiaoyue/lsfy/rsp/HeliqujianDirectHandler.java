package org.bnez.xiaoyue.lsfy.rsp;

import org.apache.log4j.Logger;
import org.bnez.lsfy.service.BizServiceClient;
import org.bnez.lsfy.service.ZhibiaoBean;
import org.bnez.xiaoyue.lsfy.XiaoyueResponse;

public class HeliqujianDirectHandler
{
	@SuppressWarnings("unused")
	private static final Logger _logger = Logger.getLogger(HeliqujianDirectHandler.class);

	private String _combString;
	private ZhibiaoBean _zb;
	private String _rsp;

	public HeliqujianDirectHandler(String combString)
	{
		_combString = combString;
	}

	public XiaoyueResponse handle()
	{
		pickZhibiao();
		if (_zb == null)
			return ResponseBuilder.build("您要询问哪个指标超出合理区间？");

		_rsp = BizServiceClient.getInstance().queryHeliqujianDesc(_zb.getId());
		if(_rsp == null || _rsp.length() == 0)
			return ResponseBuilder.build(_zb.getName() + "没有定义合理区间值");

		return ResponseBuilder.build(_rsp);
	}

	private void pickZhibiao()
	{
		String s = BizServiceClient.getInstance().parseReportQueryZhibiaoName(_combString);
		_zb = BizServiceClient.getInstance().queryZhibiaoByName(s);
	}

	public static void main(String[] args)
	{
		XiaoyueResponse xr = new HeliqujianDirectHandler("在我市法院中一审普通程序陪审率数是哪些法院超了合理区间的").handle();
		System.out.println(xr.getSpeak());

		xr = new HeliqujianDirectHandler("在我市法院中结案率是哪些法院超了合理区间的").handle();
		System.out.println(xr.getSpeak());

		xr = new HeliqujianDirectHandler("在我市法院中上诉案件平均移送天数是哪些法院超了合理区间的").handle();
		System.out.println(xr.getSpeak());
	}
}
