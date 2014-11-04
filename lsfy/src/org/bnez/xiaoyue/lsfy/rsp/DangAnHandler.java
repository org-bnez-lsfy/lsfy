package org.bnez.xiaoyue.lsfy.rsp;

import org.apache.log4j.Logger;
import org.bnez.xiaoyue.lsfy.XiaoyueResponse;
import org.bnez.xiaoyue.lsfy.tdh.DangAnShaomiaoQuery;

public class DangAnHandler
{

	private static final Logger _logger = Logger.getLogger(DangAnHandler.class);
	@SuppressWarnings("unused")
	private String _combString;
	private String _combName;

	public DangAnHandler(String combName, String combString)
	{
		_combName = combName;
		_combString = combString;
	}

	public XiaoyueResponse response()
	{
		if (_combName.equals("ls_other_dangan_open"))
		{
			//XiaoyueResponse xr = ResponseBuilder.build("请输入案件号", Config.getInstance().getString("url.openDangan"));
			XiaoyueResponse xr = ResponseBuilder.build("正在为您打开档案查询", null, "openDangan");
			return xr;
		}
		if (_combName.equals("ls_other_dangan_weishaomiao"))
		{
			try
			{
				DangAnShaomiaoQuery q = new DangAnShaomiaoQuery();
				q.query();
				int total = q.getTotalCount();
				int no = total - q.getWeiShaoMiaoCount();
				String spk = String.format("审判系统中共有档案%d个，其中未扫描影像的有%d个", total, no);
				XiaoyueResponse xr = ResponseBuilder.build(spk);
				return xr;
			} catch (Exception e)
			{
				_logger.error(e.getMessage(), e);
				String spk = "查询时发生未知的系统异常，请联系管理员";
				XiaoyueResponse xr = ResponseBuilder.build(spk);
				return xr;
			}
		}
		return null;
	}

}
