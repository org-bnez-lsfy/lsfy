package org.bnez.xiaoyue.lsfy.rsp;

import org.bnez.lsfy.service.BizServiceClient;
import org.bnez.lsfy.service.FayuanBean;
import org.bnez.lsfy.service.ZhibiaoBean;
import org.bnez.xiaoyue.lsfy.XiaoyueResponse;
import org.bnez.xiaoyue.lsfy.common.ErrorMessage;
import org.bnez.xiaoyue.lsfy.db.ReportData;

public class ZhibiaoMoreHandler extends Handler
{
	//private static final Logger _logger = Logger.getLogger(ZhibiaoMoreHandler.class);

	public ZhibiaoMoreHandler(String combName, String combString)
	{
		_combName = combName;
		_combString = combString;
	}

	public XiaoyueResponse response()
	{
		// 无需上下文支持
		if (isAsk("ls_zbmore_huanbi"))
			return ResponseBuilder.build("目前尚不支持对指标环比数据的查询");

		if (isAsk("ls_zbmore_heliqujian_out"))
			return handleHeliqujianOut();

		if (isAsk("ls_zbmore_heliqujian_direct"))
			return handleHeliqujianDirect();

		// 必须有上下文，且问过指标
		ZhibiaoContext c = XiaoyueContext.getZhibiaoContext();
		if (c == null || c.getZhibiao() == null)
			return ResponseBuilder.build(ErrorMessage.getErrorDefine("zb.unknownZhibiao"));

		if (isAsk("ls_zbmore_heliqujian"))
			return handleHeliqujianMore(c);

		if (isAsk("ls_zbmore_anjian") || isAsk("ls_zbmore_chengbanren"))
			return ResponseBuilder.build("尚不支持指标关联案件或者承办人的查询");

		if (isAsk("ls_zbmore_tongbi") || isAsk("ls_zbmore_paiming_sheng") || isAsk("ls_zbmore_paiming_zhongyuan"))
			return handleTongbiNPaiming(c);

		if (isAsk("ls_zbmore_zhuiwen_fy"))
			return handleZhuiwenFy(c);

		if (isAsk("ls_zbmore_jichen_pingjun"))
			return handleJichenPingjun(c);

		return ResponseBuilder.build("您的问题是：" + _combString + "，但是目前我还不知道答案");
	}

	private XiaoyueResponse handleHeliqujianDirect()
	{
		return new HeliqujianDirectHandler(_combString).handle();
	}

	private XiaoyueResponse handleHeliqujianOut()
	{
		return new HeliqujianOutHandler(_combString).handle();
	}

	private XiaoyueResponse handleJichenPingjun(ZhibiaoContext c)
	{
		ZhibiaoQuery zq = new ZhibiaoQuery(c.getZhibiao(), "基层", c.getPeriod());
		return zq.query();
	}

	private XiaoyueResponse handleZhuiwenFy(ZhibiaoContext c)
	{
		String fy = new FayuanParser().parse(_combString);
		String normalFy = BizServiceClient.getInstance().parseReportQueryFayuanName(fy);
		if (normalFy.equals(c.getFayuan()))
			return ResponseBuilder.build("您刚才问的就是" + normalFy);

		return new ZhibiaoQuery(c.getZhibiao(), normalFy, c.getPeriod()).query();
	}

	private XiaoyueResponse handleTongbiNPaiming(ZhibiaoContext c)
	{
		ReportData d = c.getData();
		if (d == null)
			return ResponseBuilder.build(ErrorMessage.getErrorDefine("zb.notFound", c.getPeriod().getFrom(), c
					.getPeriod().getTo(), c.getFayuan(), c.getZhibiao()));
		if (isAsk("ls_zbmore_tongbi"))
		{
			String tb = d.getTongbi();
			if (tb == null)
				return ResponseBuilder.build(ErrorMessage.getErrorDefine("zb.tongbiNotFound", c.getZhibiao()));
			String rsp = String.format("%s%s的同比是%s", c.getFayuan(), c.getZhibiao(), tb);
			return ResponseBuilder.build(rsp);
		}

		if (isAsk("ls_zbmore_paiming_sheng"))
		{
			Integer pm = d.getPaimingQuansheng();
			if (pm == null)
			{
				String rsp = String.format("系统中没有%s的全省排名数据", c.getZhibiao());
				return ResponseBuilder.build(rsp);
			} else
			{
				String rsp = String.format("%s%s的全省排名是第%d位", c.getFayuan(), c.getZhibiao(), pm);
				return ResponseBuilder.build(rsp);
			}
		}

		if (isAsk("ls_zbmore_paiming_zhongyuan"))
		{
			Integer pm = d.getPaimingZhongyuan();
			if (pm == null)
			{
				String rsp = String.format("系统中没有%s的中院排名数据", c.getZhibiao());
				return ResponseBuilder.build(rsp);
			} else
			{
				String rsp = String.format("%s的全省中院的排名是第%d位", c.getZhibiao(), pm);
				return ResponseBuilder.build(rsp);
			}
		}

		return null;
	}

	private XiaoyueResponse handleHeliqujianMore(ZhibiaoContext c)
	{
		FayuanBean fy = BizServiceClient.getInstance().queryFayuanByRptName(c.getFayuan());//FIXME context 里面保存fayuanbean和zhibiaobean
		ZhibiaoBean zb = BizServiceClient.getInstance().queryZhibiaoByName(c.getZhibiao());
		String hlqjdesc = BizServiceClient.getInstance().queryHeliqujianDesc(zb.getId(), fy.getId());
		
		if(hlqjdesc == null || hlqjdesc.length() == 0)
			return ResponseBuilder.build(ErrorMessage.getErrorDefine("zb.noHeliqujianDefined", zb.getName()));
		return ResponseBuilder.build(fy.getName() + "的" + zb.getName() + "的合理区间是" + hlqjdesc);
	}
}
