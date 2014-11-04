package org.bnez.xiaoyue.lsfy.rsp;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.log4j.Logger;
import org.bnez.xiaoyue.lsfy.XiaoyueResponse;
import org.bnez.xiaoyue.lsfy.common.Config;

public class OtherHandler extends Handler
{
	private static final Logger _logger = Logger.getLogger(OtherHandler.class);

	public OtherHandler(String combName, String combString)
	{
		_combName = combName;
		_combString = combString;
	}

	public XiaoyueResponse response()
	{
		if (isAsk("ls_other_time"))
			return new TimeQuery(_combName, _combString).response();

		if (isStart("ls_other_video"))
			return handleVideo(_combName, _combString);

		if (isAsk("ls_other_anjian"))
			return handleAnjian();

		if (isAsk("ls_other_anjian_query"))
			return handleAnjianQuery();

		if (isStart("ls_other_dangan"))
			return new DangAnHandler(_combName, _combString).response();

		if (isStart("ls_other_xiaci"))
			return new XiaciHandler(_combName, _combString).response();

		if (isAsk("ls_other_ajzspt"))
			return handleAjzspt();// 案件展示平台

		if (isAsk("ls_other_zjgysearch"))
			return handleZjgysearch(); // 高院统一检索平台
		
		if (isAsk("ls_other_zjgysearch_qiangjie")) // 检索抢劫犯罪案件信息
			return handleZjgysearchQiangjie();
		
		if(isAsk("ls_other_zjgysearch_weixianjiashi")) // 检索危险驾驶案件
			return handleZjgysearchWeixianjiashi();

		if (isAsk("ls_other_loudewei"))
			return handleLoudewei(); // 业绩档案-楼德卫

		_logger.error("UNKNOWN other " + _combName);
		return ResponseBuilder.build("发生系统错误");
	}

	private XiaoyueResponse handleLoudewei()
	{
		XiaoyueResponse xr = ResponseBuilder.build("为您打开楼德卫法官的业绩档案", null, "openLoudewei");
		return xr;
	}

	private XiaoyueResponse handleZjgysearch()
	{
		XiaoyueResponse xr = ResponseBuilder.build("为您打开浙江法院统一检索平台", "http://203.0.65.5:8081/zjgy-search/");
		return xr;
	}

	private XiaoyueResponse handleZjgysearchQiangjie()
	{
		String url = "http://203.0.65.5:8081/zjgy-search/?q=";
		try
		{
			url = url + URLEncoder.encode("抢劫犯罪", "utf-8");
		} catch (UnsupportedEncodingException e)
		{
			_logger.error(e.getMessage(), e);
		}
		XiaoyueResponse xr = ResponseBuilder.build("为您检索抢劫犯罪案件", url);
		return xr;
	}

	private XiaoyueResponse handleZjgysearchWeixianjiashi()
	{
		String url = "http://203.0.65.5:8081/zjgy-search/?q=";
		try
		{
			url = url + URLEncoder.encode("危险驾驶", "utf-8");
		} catch (UnsupportedEncodingException e)
		{
			_logger.error(e.getMessage(), e);
		}
		XiaoyueResponse xr = ResponseBuilder.build("为您检索危险驾驶案件", url);
		return xr;
	}

	private XiaoyueResponse handleAjzspt()
	{
		XiaoyueResponse xr = ResponseBuilder.build("为您打开浙江法院案件展示平台", "http://203.0.64.138:8080/ajzspt/");
		return xr;
	}

	private XiaoyueResponse handleAnjianQuery()
	{
		XiaoyueResponse xr = ResponseBuilder.build("为您打开全市案件综合信息，请在页面完成后输入案号", null, "openAnjianQuery");
		return xr;
	}

	private XiaoyueResponse handleAnjian()
	{
		// XiaoyueResponse xr = ResponseBuilder.build("请输入案件号",
		// Config.getInstance().getString("url.openAnjian"));
		XiaoyueResponse xr = ResponseBuilder.build("为您打开案件信息，请在页面完成后输入案号", null, "openAnjian");
		return xr;
	}

	private XiaoyueResponse handleVideo(String name, String comb)
	{
		if (isAsk("ls_other_video_open"))
		{
			XiaoyueResponse xr = ResponseBuilder.build("请输入案件号", Config.getInstance().getString("url.openVideo"));
			return xr;
		} else if (isAsk("ls_other_video_map"))
		{
			XiaoyueResponse xr = ResponseBuilder.build("为您打开庭审直播录像", null, "openVideoMapLive");
			return xr;
		} else if (isAsk("ls_other_video_mapvod"))
		{
			XiaoyueResponse xr = ResponseBuilder.build("为您打开庭审点播录像", null, "openVideoMapVod");
			return xr;
		} else
		{
			_logger.error("UNKNOWN video command " + _combName);
			return ResponseBuilder.build("发生系统错误");
		}
	}
}
