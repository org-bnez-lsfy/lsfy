package org.bnez.xiaoyue.lsfy.rsp;

import org.apache.log4j.Logger;
import org.bnez.xiaoyue.blur.inter.MatchResult;
import org.bnez.xiaoyue.lsfy.XiaoyueResponse;

public class CombHandler extends Handler
{
	@SuppressWarnings("unused")
	private static final Logger _logger = Logger.getLogger(CombHandler.class);

	@SuppressWarnings("unused")
	private String _original;

	public CombHandler(String original, MatchResult comb)
	{
		_original = original;
		_combName = comb.getName();
		_combString = comb.getCombination();
	}

	public XiaoyueResponse response()
	{
		if (isAsk("ls_other_repeat"))
			return handleRepeat();

		return handleNormalComb();
	}

	private XiaoyueResponse handleRepeat()
	{
		XiaoyueResponse xr = XiaoyueContext.getLastResponse();
		if (xr == null)
			return ResponseBuilder.build("没有找到之前问题的上下文");

		return xr;
	}

	private XiaoyueResponse handleNormalComb()
	{
		if (isBigCat("zb"))
			return new ZhibiaoQueryHandler(_combName, _combString).response();

		if (isBigCat("zbmore"))
			return new ZhibiaoMoreHandler(_combName, _combString).response();// handleZhibiaoMoreQuery();

		if (isBigCat("jt"))
			return new JingtaiHandler(_combName, _combString).response();

		if (isBigCat("other"))
			return new OtherHandler(_combName, _combString).response();

		String c = "您的问题是：" + _combString + "，但是目前我还不知道答案";
		return ResponseBuilder.build(c);
	}

}
