package org.bnez.xiaoyue.lsfy.rsp;

import org.apache.log4j.Logger;
import org.bnez.lsfy.service.BizServiceClient;
import org.bnez.lsfy.service.FayuanBean;
import org.bnez.xiaoyue.lsfy.XiaoyueResponse;
import org.bnez.xiaoyue.lsfy.db.User;

public class FayuanParser
{
	private static final Logger _logger = Logger.getLogger(FayuanParser.class);
	
	private boolean _hasError = false;
	private XiaoyueResponse _response = null;

	public String parse(String someString)
	{
		String fy = pickFayuan(someString);

		if (fy == null)
		{
			User u = XiaoyueContext.getCurrentThreadUser();
			FayuanBean fydb = BizServiceClient.getInstance().queryFayuan(u.getFayuanId());
			if (fydb == null)
			{
				setError(ResponseBuilder.build("您的问题中没有指定法院，系统也无法确定您的归属法院，请明确法院名称"));
				return null;
			}

			fy = fydb.getRptName();
		}

		String normalFy = fy;

		if (normalFy.equals("本院"))
		{
			User u = XiaoyueContext.getCurrentThreadUser();
			if (u == null)
			{
				setError(ResponseBuilder.build("无法确定您的归属法院，请直接使用法院名称对指标进行提问"));
				return null;
			}

			FayuanBean fydb = BizServiceClient.getInstance().queryFayuan(u.getFayuanId());
			if (fydb == null)
			{
				setError(ResponseBuilder.build("无法确定您的归属法院，请直接使用法院名称对指标进行提问"));
				return null;
			}

			normalFy = fydb.getRptName();
		}
		return normalFy;
	}

	private void setError(XiaoyueResponse response)
	{
		_response = response;
		_hasError = true;
	}

	private String pickFayuan(String someString)
	{
		return BizServiceClient.getInstance().parseReportQueryFayuanName(someString);
	}
	
	public boolean hasError()
	{
		return _hasError;
	}
	
	public XiaoyueResponse getErrorResponse()
	{
		return _response;
	}

	public static void main(String[] args)
	{
		System.out.println(new FayuanParser().parse("今年全省基层的结案率是多少"));
		System.out.println(new FayuanParser().parse("今年全省结案率是多少"));
		System.out.println(new FayuanParser().parse("今年本院的结案率是多少"));
	}
}
