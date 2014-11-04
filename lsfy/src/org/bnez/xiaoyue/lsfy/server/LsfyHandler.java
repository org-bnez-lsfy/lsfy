package org.bnez.xiaoyue.lsfy.server;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

import org.apache.log4j.Logger;
import org.bnez.xiaoyue.blur.inter.BlurMatchServiceClient;
import org.bnez.xiaoyue.blur.inter.MatchResult;
import org.bnez.xiaoyue.lsfy.XiaoyueResponse;
import org.bnez.xiaoyue.lsfy.common.Config;
import org.bnez.xiaoyue.lsfy.common.ErrorMessage;
import org.bnez.xiaoyue.lsfy.human.HumanQuestionList;
import org.bnez.xiaoyue.lsfy.rsp.CombHandler;
import org.bnez.xiaoyue.lsfy.rsp.ResponseBuilder;
import org.bnez.xiaoyue.lsfy.rsp.XiaoyueContext;

public class LsfyHandler
{
	private static final Logger _logger = Logger.getLogger(LsfyHandler.class);
	private static LsfyHandler _instance = null;

	private BlurMatchServiceClient _blurMatchService;
	private boolean _useHumanAnswer;
	private NlpHandler _nlpHandler;

	public static synchronized LsfyHandler getInstance()
	{
		if (_instance == null)
			_instance = new LsfyHandler();
		return _instance;
	}

	private LsfyHandler()
	{
		String blurip = Config.getInstance().getString("nlp.ip");
		int blurport = Config.getInstance().getInt("nlp.port");
		_blurMatchService = new BlurMatchServiceClient(blurip, blurport);
		_useHumanAnswer = Config.getInstance().getBool("human.use");
		_nlpHandler = new NlpHandler();
	}

	public XiaoyueResponse handle(Object received, String ip)
	{
		Date start = new Date();
		XiaoyueResponse rsp = null;

		try
		{
			rsp = response(received, ip);
			XiaoyueContext.setLastResponse(rsp);
		} catch (Exception e)
		{
			String err = received == null ? e.getMessage() : received.toString();
			_logger.error(err, e);

			rsp = ResponseBuilder.build("发生系统错误");
		}
		long tu = new Date().getTime() - start.getTime();
		Recorder.record(received, rsp, ip, tu);

		_logger.info("response [tu " + tu + "] " + rsp);
		return rsp;
	}

	private XiaoyueResponse response(Object received, String ip)
	{
		SecurityVali sv = new SecurityVali();
		if (!sv.vali(ip))
			return ResponseBuilder.build("未授权访问，请联系管理员！");
		
		XiaoyueContext.setCurrentThreadUser(sv.getUser());

		if (received == null)
			return handleNullReceived();

		String[] ss = received.toString().split("\\|");
		if (ss.length == 0)
			return handleNullReceived();

		String req = ss[0];

		// FIXME 两个线程，谁先返回非null结果，就采用谁的
		XiaoyueResponse xr = _nlpHandler.handle(req);
		MatchResult comb = _blurMatchService.match(req);
		if (comb == null)
		{
			if(xr != null)
				return xr;
			else
				return handleBlurMatchFailed(ip, req);
		}
		
		if (comb.getName() != null)
			return handleCombination(req, comb);

		return handleCombNotFound(req);
	}

	private XiaoyueResponse handleBlurMatchFailed(String ip, String req)
	{
		_logger.error("get NULL from blur match for " + req);
		if (_useHumanAnswer)
		{
			HumanQuestionList.getInstance().add(ip, req);
			// return ResponseBuilder.buildEmpty();
		}

		// TODO 打开楼德卫法官的业绩档案，临时代码
		if (isLoudewei(req))
		{
			return handleLoudewei(req);
		}
		// TODO 抢劫案件查询，在高院综合查询系统中
		if (isQueryQiangjie(req))
		{
			return handleQiangjieQuery();
		}
		// TODO 危险驾驶查询，在高院综合查询系统中
		if (isQueryWeixianjiashi(req))
		{
			return handleWeixianjiashiQuery();
		}

		return ResponseBuilder.build(ErrorMessage.getErrorDefine("nlpNull"));
	}

	private XiaoyueResponse handleWeixianjiashiQuery()
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

	private boolean isQueryWeixianjiashi(String req)
	{
		// 查一下危险驾驶的案件
		int m = 0;
		if (req.indexOf("查") >= 0 || req.indexOf("检索") >= 0)
			m++;
		if (req.indexOf("危险") >= 0)
			m++;
		if (req.indexOf("驾驶") >= 0)
			m++;
		if (req.indexOf("案件") >= 0)
			m++;
		return m >= 3;
	}

	private XiaoyueResponse handleQiangjieQuery()
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

	private boolean isQueryQiangjie(String req)
	{
		// 检索抢劫犯罪案件信息
		int m = 0;
		if (req.indexOf("查") >= 0 || req.indexOf("检索") >= 0)
			m++;
		if (req.indexOf("抢劫") >= 0)
			m++;
		if (req.indexOf("犯罪") >= 0)
			m++;
		if (req.indexOf("案件") >= 0)
			m++;
		return m >= 3;
	}

	private XiaoyueResponse handleLoudewei(String req)
	{
		XiaoyueResponse xr = ResponseBuilder.build("为您打开楼德卫法官的业绩档案", null, "openLoudewei");
		return xr;
	}

	private boolean isLoudewei(String req)
	{
		String s = req.replaceAll("，", "").replaceAll("。", "");
		_logger.debug("check loudewei " + s + " " + s.length());
		if (s.length() >= 12 && s.length() <= 14 && s.endsWith("档案"))
			return true;
		return false;
	}

	private XiaoyueResponse handleCombNotFound(String rev)
	{
		_logger.error("combination NOT found for " + rev);
		return ResponseBuilder.build(ErrorMessage.getErrorDefine("combNotFound"));
	}

	private XiaoyueResponse handleNullReceived()
	{
		_logger.error("received NULL object");
		return ResponseBuilder.NULL;
	}

	private XiaoyueResponse handleCombination(String req, MatchResult rst)
	{
		return new CombHandler(req, rst).response();
	}

}
