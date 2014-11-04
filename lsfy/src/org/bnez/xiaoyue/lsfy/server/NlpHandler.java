package org.bnez.xiaoyue.lsfy.server;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.bnez.lsfy.service.BizServiceClient;
import org.bnez.lsfy.service.ReportPeriod;
import org.bnez.nlp.serv.rmi.NlpServiceClient;
import org.bnez.xiaoyue.lsfy.XiaoyueResponse;
import org.bnez.xiaoyue.lsfy.rsp.ZhibiaoQuery;

public class NlpHandler
{
	private static final Logger _logger = Logger.getLogger(NlpHandler.class);
	private NlpServiceClient _nlpService;
	
	public NlpHandler()
	{
		_nlpService = new NlpServiceClient();
	}

	public XiaoyueResponse handle(String req)
	{
		if(req == null)
			return null;
		
		String nlpRst = _nlpService.parse(req);
		_logger.debug("try NLP Result handle " + nlpRst);
		if(nlpRst == null)
			return null;
		
		try
		{
			JSONObject json = JSONObject.fromObject(nlpRst);
			String cmd = json.getString("cmd");
			// {"cmd":"zhibiao_query","fayuan":"丽中","time":"今年","zhibiao":"结案率"}
			if(cmd.equals("zhibiao_query"))
			{
				String zb = json.getString("zhibiao");
				String fy = json.getString("fayuan");
				String tm = json.getString("time");
				ReportPeriod rp = BizServiceClient.getInstance().transReportQueryTimeStringToPeriod(tm);
				return new ZhibiaoQuery(zb, fy, rp).query();
			}
		} catch (Exception e)
		{
			_logger.error("FAILED " + nlpRst, e);
		}
		
		return null;
	}

}
