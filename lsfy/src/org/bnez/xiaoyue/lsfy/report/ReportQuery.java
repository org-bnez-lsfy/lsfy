package org.bnez.xiaoyue.lsfy.report;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.bnez.wwwreader.base.TagLocator;
import org.bnez.wwwreader.base.TagPath;
import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ReportQuery
{
	private static final Logger _logger = Logger.getLogger(ReportQuery.class);

	private static final int RETRY_MAX = 10;
	private static final int REDO_MAX = 10;

	private CloseableHttpClient _client;
	private String _conversation;
	private String _actionState;
	private String _parameters;
	private String _tracking;
	private String _caf;
	private List<List<String>> _result;
	private List<String> _cospan;
	private int _redoPost;
	private int _redoQuery;

	public ReportQuery()
	{
	}

	private void init()
	{
		HttpClientBuilder hcb = HttpClientBuilder.create();
		//hcb.setProxy(new HttpHost("127.0.0.1", 8888));
		hcb.setRedirectStrategy(new LaxRedirectStrategy());
		hcb.setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36");
		_client = hcb.build();

		_result = new ArrayList<List<String>>();
		_cospan = new ArrayList<String>();
	}

	public void query(ReportCondition condition) throws ClientProtocolException, IOException, SAXException
	{
		init();
		prelogin();
		login();

		_result.clear();
		_cospan.clear();
		_redoPost = 0;
		_redoQuery = 0;

		Date start = new Date();
		doQuery(condition);
		long tu = new Date().getTime() - start.getTime();
		_logger.debug(condition.toString() + "query: " + _redoQuery + ", retry: " + _redoPost + ", result: "
				+ _result.size() + ",cospan:" + _cospan.size() + ", tu: " + tu);

		if (!hasResult())
			_logger.error("FAILED on " + condition.getReportName());
	}

	private void doQuery(ReportCondition condition) throws ClientProtocolException, IOException, SAXException
	{
		_redoQuery = 0;
		for (; _redoQuery < REDO_MAX; _redoQuery++)
		{
			_redoPost = 0;

			preopen(condition);

			open1(condition);
			if (hasResult())
				return;

			for (; _redoPost < RETRY_MAX; _redoPost++)
			{
				open2(condition);
				if (hasResult())
					return;
			}
		}
	}

	private void prelogin() throws ClientProtocolException, IOException
	{
		String url = "http://203.0.65.3:8080/zjgy_kpi/login.jsp";
		HttpGet request = new HttpGet(url);
		_client.execute(request);
	}

	private void login() throws ClientProtocolException, IOException
	{
		String url = "http://203.0.65.3:8080/zjgy_kpi/loginAction.jsp";
		HttpPost request = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("j_username", "LZ"));
		params.add(new BasicNameValuePair("j_password", "LZ2010"));
		request.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

		HttpResponse response = _client.execute(request);
		ResponseHandler<String> rh = new BasicResponseHandler();
		@SuppressWarnings("unused")
		String html = rh.handleResponse(response);
		// _logger .debug(html);
	}

	private void preopen(ReportCondition condition) throws ClientProtocolException, IOException, SAXException
	{
		String url = "http://203.0.65.3/cognos/cgi-bin/cognos.cgi";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("b_action", "cognosViewer"));
		params.add(new BasicNameValuePair("ui.action", "run"));
		if(condition.getType()==2)
		params.add(new BasicNameValuePair("ui.object", "/content/package[@name='" + condition.getFolderName()
				+ "']/report[@name='" + condition.getReportName() + "']"));
		else
			params.add(new BasicNameValuePair("ui.object", "/content/folder[@name='" + condition.getFolderName()
					+ "']/report[@name='" + condition.getReportName() + "']"));
		params.add(new BasicNameValuePair("ui.name", condition.getReportName()));
		params.add(new BasicNameValuePair("run.outputFormat", ""));
		params.add(new BasicNameValuePair("run.prompt", "true"));
		params.add(new BasicNameValuePair("cv.header", "false"));
		params.add(new BasicNameValuePair("cv.showExport", "true"));
		String p = URLEncodedUtils.format(params, "utf-8");
		p += "&p_P_Court=%EF%BF%BD%EF%BF%BD";
		p += "&m_passportID=101:88d5e31c-b239-9972-6f54-7df54b21314f:2440010096";

		HttpGet request = new HttpGet(url + "?" + p);
		HttpResponse response = _client.execute(request);
		ResponseHandler<String> rh = new BasicResponseHandler();
		String html = rh.handleResponse(response);
		//_logger.debug(html);

		String[] ss = html.split("\n");
		for (String s : ss)
		{
			if (s.contains("window.onload"))
			{
				int start = s.indexOf("({");
				int end = s.indexOf("})");
				String js = s.substring(start + 1, end + 1);
				JSONObject json = JSONObject.fromObject(js);
				// _logger.debug(js);

				_conversation = json.getString("conversation");
				_actionState = json.getString("action_state");
				_parameters = json.getString("parameters");
				_tracking = json.getString("tracking");
				_caf = json.getString("caf");
			}
		}
	}

	private boolean hasResult()
	{
		return _result.size() > 0;
	}

	private void parseResult(String html, ReportCondition condition) throws IOException, SAXException
	{
		String pre = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/></head><body>";
		String post = "</body></html>";

		html = pre + html + post;
		FileUtils.writeStringToFile(new File("d:/tmp/aasss.html"), html);

		DOMParser parser = new DOMParser();
		ByteArrayInputStream stream = new ByteArrayInputStream(html.getBytes("UTF-8"));
		parser.parse(new InputSource(stream));
		Document doc = parser.getDocument();

		TagPath tp = null;
		if (condition.getTableTag() == 3)
			tp = new TagPath("html|body|table#rt_NS_|tbody|tr|td|table|tbody|tr|td|table|tbody|tr|td|table");
		else if (condition.getTableTag() == 2)
			tp = new TagPath("html|body|table#rt_NS_|tbody|tr|td|table|tbody|tr|td|table");
		else if (condition.getTableTag() == 4)
			tp = new TagPath("html|body|table#rt_NS_|tbody|tr|td|table|tbody|tr|td|table|tbody"
					+ "|tr|td|table|tbody|tr|td|div|table|tbody|tr|td|table|");
		else
			tp = new TagPath("html|body|table#rt_NS_|tbody|tr|td|table");

		TagLocator tl = new TagLocator(doc, tp);
		List<Node> nodes = tl.locate();
		if (nodes == null || nodes.size() == 0)
			return;

		for (int tb = 0; tb < nodes.size(); tb++)
		{
			TagLocator tbll = new TagLocator(nodes.get(tb), new TagPath("tbody|tr"));
			List<Node> trs = tbll.locate();
			for (int tr = 0; tr < trs.size(); tr++)
			{
				if (tr < condition.getHeaderCancel())
					continue;
				List<String> record = new ArrayList<String>();
				NodeList tds = trs.get(tr).getChildNodes();
				String s = null;
				Node cos = tds.item(0).getAttributes().getNamedItem("rowspan");
				if (cos != null && !tds.item(0).getTextContent().trim().equals(""))
				{
					s = cos.getTextContent();
					_cospan.add(s);
				}
				for (int i = 0; i < tds.getLength(); i++)
				{
					Node n = tds.item(i);
					record.add(n.getTextContent().trim());
				}
				_result.add(record);
			}
		}

	}

	private void open1(ReportCondition condition) throws ClientProtocolException, IOException, SAXException
	{
		HttpPost request = new HttpPost("http://203.0.65.3/cognos/cgi-bin/cognos.cgi");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("cv.ignoreState", "true"));
		params.add(new BasicNameValuePair("b_action", "cognosViewer"));
		params.add(new BasicNameValuePair("cv.id", "_NS_"));

		params.add(new BasicNameValuePair("ui.primaryAction", "run"));
		params.add(new BasicNameValuePair("cv.header", "false"));
		params.add(new BasicNameValuePair("executionParameters", _parameters));
		if (condition.getZhibiao() != null)
			params.add(new BasicNameValuePair("_autosubmitParameter", "指标"));//
		params.add(new BasicNameValuePair("_promptControl", "prompt"));
		params.add(new BasicNameValuePair("ui.conversation", _conversation));
		params.add(new BasicNameValuePair("m_tracking", _tracking));
		if (condition.getReportName().equals("首页-中院"))
		{
			String[] times = condition.getTo().split("-");
			String time = times[0] + times[1] + times[2];
			params.add(new BasicNameValuePair("p_Date", "<selectChoices><selectOption useValue='"
					+ "[cube首页].[日期].[日期].[日期1]-&gt;:[PC].[@MEMBER].[" + time + "]' displayValue='" + condition.getTo()
					+ "'/></selectChoices>"));
		} else
		{
			params.add(new BasicNameValuePair("p_Date1", buildParamString(condition.getFrom())));
			params.add(new BasicNameValuePair("p_Date2", buildParamString(condition.getTo())));
		}
		if (condition.getFanwei() != null)
			params.add(new BasicNameValuePair("p_p_CXFS", buildParamString(condition.getFanwei())));
		if (condition.getZhibiao() != null)
			params.add(new BasicNameValuePair("p_指标", buildParamString(condition.getZhibiao())));
		if (condition.getCourt() != null)
			params.add(new BasicNameValuePair("p_p_Court", buildParamString(condition.getCourt())));
		if (condition.getMonth() != null)
			params.add(new BasicNameValuePair("p_p_Month", buildParamString(condition.getMonth())));
		if (condition.getYear() != null)
			params.add(new BasicNameValuePair("p_p_Year", buildParamString(condition.getYear())));
		if (condition.getSfft() != null)
			params.add(new BasicNameValuePair("p_p_SFFT", buildParamString(condition.getSfft())));
		params.add(new BasicNameValuePair("ui.cafcontextid", _caf));
		params.add(new BasicNameValuePair("cv.catchLogOnFault", "true"));
		params.add(new BasicNameValuePair("cv.responseFormat", "data"));

		if (condition.getPage() == null || condition.getPage().equals(""))
			params.add(new BasicNameValuePair("ui.action", "forward"));
		else
			params.add(new BasicNameValuePair("ui.action", condition.getPage()));
		request.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

		HttpResponse response = _client.execute(request);
		ResponseHandler<String> rh = new BasicResponseHandler();
		String html = rh.handleResponse(response);
		//_logger.debug(html);

		parseResult(html, condition);
	}

	private String buildParamString(String paramValue)
	{
		if (paramValue == null)
			return "";
		if (paramValue.equals(""))
			return "<selectChoices></selectChoices>";
		String fmt = "<selectChoices><selectOption useValue=\"%s\" displayValue=\"%s\"/></selectChoices>";
		return String.format(fmt, paramValue, paramValue);
	}

	private void open2(ReportCondition condition) throws ClientProtocolException, IOException, SAXException
	{
		HttpPost request = new HttpPost("http://203.0.65.3/cognos/cgi-bin/cognos.cgi");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("cv.ignoreState", "true"));
		params.add(new BasicNameValuePair("b_action", "cognosViewer"));
		params.add(new BasicNameValuePair("cv.id", "_NS_"));

		params.add(new BasicNameValuePair("ui.action", "wait"));
		params.add(new BasicNameValuePair("cv.actionState", _actionState));
		params.add(new BasicNameValuePair("ui.primaryAction", "run"));
		params.add(new BasicNameValuePair("cv.header", "false"));
		params.add(new BasicNameValuePair("executionParameters", _parameters));
		params.add(new BasicNameValuePair("ui.conversation", _conversation));
		params.add(new BasicNameValuePair("m_tracking", _tracking));
		params.add(new BasicNameValuePair("ui.cafcontextid", _caf));
		params.add(new BasicNameValuePair("cv.catchLogOnFault", "true"));
		params.add(new BasicNameValuePair("cv.responseFormat", "data"));

		request.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

		HttpResponse response = _client.execute(request);
		ResponseHandler<String> rh = new BasicResponseHandler();
		String html = rh.handleResponse(response);
		//_logger.debug(html);

		parseResult(html, condition);
	}

	public List<List<String>> getResult()
	{
		return _result;
	}

	public List<String> getCospan()
	{
		return _cospan;
	}
}
