package org.bnez.xiaoyue.lsfy.tdh;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.bnez.wwwreader.base.TagLocator;
import org.bnez.wwwreader.base.TagPath;
import org.cyberneko.html.parsers.DOMParser;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

public class ReportQueryXiaci extends Tongdahai
{
	private static final Logger _logger = Logger.getLogger(ReportQueryXiaci.class);

	private List<List<String>> _result;
	private List<String> dmList = new ArrayList<String>();
	private List<String> reasonList = new ArrayList<String>();
	private List<String> dm2List = new ArrayList<String>();
	private List<String> ysList = new ArrayList<String>();
	private XiaciRepository tdhr = new XiaciRepository();
	List<Xiaci> tdhList = new ArrayList<Xiaci>();
	List<Xiaci> tdhListAll = new ArrayList<Xiaci>();

	public ReportQueryXiaci() throws ClientProtocolException, IOException
	{
		_result = new ArrayList<List<String>>();

		tongdahaiLogin();
		open1();
		open();
		isXiaCi();
	}

	private void open() throws ClientProtocolException, IOException
	{
		String url = "http://203.150.144.8:81/court/webapp/court/ajgl/ajzs/ajzs_layer2_composite.jsp";
		for (int i = 0; i < dmList.size(); i++)
		{
			HttpPost request = new HttpPost(url);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("ahdm", dmList.get(i)));
			params.add(new BasicNameValuePair("jdxh", "02"));
			params.add(new BasicNameValuePair("ajbm", "12"));
			request.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

			HttpResponse response = _client.execute(request);
			ResponseHandler<String> rh = new BasicResponseHandler();
			String html = rh.handleResponse(response);
			// _logger.debug(html);

			try
			{
				ByteArrayInputStream stream = new ByteArrayInputStream(html.getBytes("utf-8"));
				DOMParser parser = new DOMParser();
				parser.setProperty("http://cyberneko.org/html/properties/default-encoding", "utf-8");
				parser.parse(new InputSource(stream));
				org.w3c.dom.Document doc = parser.getDocument();
				TagPath tp = new TagPath("html|body|table|tbody|tr|td|table#tbl|tbody|tr|td.tdShow");
				TagLocator tl = new TagLocator(doc, tp);
				List<Node> tds = tl.locate();
				for (Node td : tds)
				{
					String n = td.getTextContent().trim();
					if (n != null && n.startsWith("事由或原因"))
					{
						// td的第一个兄弟节点是text，所以应该取再下一个兄弟节点
						Node ttd = td.getNextSibling();
						if (ttd == null)
							continue;
						Node rtd = ttd.getNextSibling();
						if (rtd != null && rtd.getTextContent() != null)
							reasonList.add(rtd.getTextContent().trim());
					}
				}
				// _logger.debug(reasonList);
				// _logger.debug(dmList);
			} catch (Exception e)
			{
				_logger.error(e.getMessage(), e);
			}
		}

	}

	private void isXiaCi() throws ClientProtocolException, IOException
	{
		for (int i = 0; i < reasonList.size(); i++)
		{
			boolean notXiaci = reasonList.get(i).contains("非瑕疵");
			if (reasonList.get(i).contains("瑕疵") && notXiaci == false)
			{
				dm2List.add(dmList.get(i / 2));
			}
		}
		_logger.debug(dm2List);
		String url = "http://203.150.144.8:81/court/webapp/court/ajgl/ajzs/ajzs_layer2_composite.jsp";
		for (int i = 0; i < dm2List.size(); i++)
		{
			HttpPost request = new HttpPost(url);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("ahdm", dm2List.get(i)));
			params.add(new BasicNameValuePair("jdxh", "01"));
			params.add(new BasicNameValuePair("ajbm", "12"));
			request.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

			HttpResponse response = _client.execute(request);
			ResponseHandler<String> rh = new BasicResponseHandler();
			String html = rh.handleResponse(response);

			try
			{
				ByteArrayInputStream stream = new ByteArrayInputStream(html.getBytes("utf-8"));
				DOMParser parser = new DOMParser();
				parser.setProperty("http://cyberneko.org/html/properties/default-encoding", "utf-8");
				parser.parse(new InputSource(stream));
				org.w3c.dom.Document doc = parser.getDocument();
				TagPath tp = new TagPath("html|body|table|tbody|tr|td|table#tbl|tbody|tr|td.tdShow");
				TagLocator tl = new TagLocator(doc, tp);
				List<Node> tds = tl.locate();
				for (Node td : tds)
				{
					String n = td.getTextContent().trim();
					if (n != null && n.startsWith("原审案号") || n.startsWith("原审法院"))
					{
						Node ttd = td.getNextSibling();
						if (ttd == null)
							continue;
						Node rtd = ttd.getNextSibling();
						if (rtd != null && rtd.getTextContent() != null)
							ysList.add(rtd.getTextContent().trim());
					}
				}
			} catch (Exception e)
			{
				_logger.error(e.getMessage(), e);
			}
		}
		tdhListAll = tdhr.queryAll();
		Date a = new Date();
		for (int i = 0; i < ysList.size() / 2; i++)
		{
			Xiaci tdh = new Xiaci();
			tdh.setYsah(ysList.get(i * 2));
			tdh.setYsfy(ysList.get(i * 2 + 1));
			tdh.setCreateAt(a);
			tdh.setStatus(0);// 修改
			boolean ah = false;
			for (Xiaci t : tdhListAll)
			{
				if (t.getYsah().equals(tdh.getYsah()))
				{
					ah = true;
					break;
				}
			}
			if (ah == true)
			{
			} else
			{
				tdhList.add(tdh);
			}
		}
		_logger.debug(tdhList.size());
		tdhr.add(tdhList);
	}

	private void open1() throws ClientProtocolException, IOException
	{
		String url = "http://203.150.144.8:81/court/webapp/court/cxfx/ajcx_xml.jsp";
		HttpPost request = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("cxdj", "more"));// fydm=332500
		params.add(new BasicNameValuePair("gj_ajxxzh", ""));
		params.add(new BasicNameValuePair("ajxxnd", "2014"));
		params.add(new BasicNameValuePair("gj_ajxxzhCode", ""));
		params.add(new BasicNameValuePair("gj_ajxxspz", ""));
		params.add(new BasicNameValuePair("gj_ajxxcbr", ""));

		params.add(new BasicNameValuePair("gj_ajxxsjy", ""));
		params.add(new BasicNameValuePair("scr", ""));
		params.add(new BasicNameValuePair("spr", ""));
		params.add(new BasicNameValuePair("hytcy", ""));
		params.add(new BasicNameValuePair("psy", ""));
		params.add(new BasicNameValuePair("jq", "jq"));
		params.add(new BasicNameValuePair("mh", "mh"));
		params.add(new BasicNameValuePair("gj_jafs",
				"09_02312-2%252C09_02312-3%252C09_03304-2%252C09_03304-3%252C09_04304-2%252C09_04304-3"));
		params.add(new BasicNameValuePair("gj_sjclx", "jatj"));
		params.add(new BasicNameValuePair("gj_sjcrq1", "2014-01"));
		params.add(new BasicNameValuePair("gj_sjcrq2", "2014-07"));
		params.add(new BasicNameValuePair("qjah", "0"));
		params.add(new BasicNameValuePair("xla", "0"));
		params.add(new BasicNameValuePair("ajzt1", "0"));
		params.add(new BasicNameValuePair("ajzt2", "0"));
		params.add(new BasicNameValuePair("ajzt3", "1"));
		params.add(new BasicNameValuePair("ajzt4", "1"));
		params.add(new BasicNameValuePair("ajzt5", "1"));
		params.add(new BasicNameValuePair("ajzt6", "1"));
		params.add(new BasicNameValuePair("ajzt7", "1"));
		params.add(new BasicNameValuePair("ajzt8", "0"));
		params.add(new BasicNameValuePair("ajzt9", "1"));
		params.add(new BasicNameValuePair("ajxz1", "0"));
		params.add(new BasicNameValuePair("ajxz2", "0"));
		params.add(new BasicNameValuePair("ajxz6", "0"));
		params.add(new BasicNameValuePair("ajxz7", "0"));
		params.add(new BasicNameValuePair("ajxz8", "0"));// 异常
		params.add(new BasicNameValuePair("ajxz9", "0"));
		params.add(new BasicNameValuePair("dysjSg", "0"));
		params.add(new BasicNameValuePair("dysjSa", "0"));
		params.add(new BasicNameValuePair("dysjSt", "0"));
		params.add(new BasicNameValuePair("dysjSw", "0"));
		params.add(new BasicNameValuePair("rmpsy", "0"));
		params.add(new BasicNameValuePair("gxyy", "0"));
		params.add(new BasicNameValuePair("bq", "0"));
		params.add(new BasicNameValuePair("mqzz", "0"));
		params.add(new BasicNameValuePair("sx", "0"));
		params.add(new BasicNameValuePair("sfjz", "0"));
		params.add(new BasicNameValuePair("gtsslx", "0"));
		params.add(new BasicNameValuePair("ykzxnr", "0"));
		params.add(new BasicNameValuePair("jtsslx", "0"));
		params.add(new BasicNameValuePair("xfm", "0"));
		params.add(new BasicNameValuePair("sj", "0"));
		params.add(new BasicNameValuePair("qxsl", "0"));
		params.add(new BasicNameValuePair("qxxp", "0"));
		params.add(new BasicNameValuePair("dtcp", "0"));
		params.add(new BasicNameValuePair("szwttj", "0"));
		params.add(new BasicNameValuePair("swcn", "0"));
		params.add(new BasicNameValuePair("xzxt", "0"));
		params.add(new BasicNameValuePair("fzrgdct", "0"));
		params.add(new BasicNameValuePair("fddbrctf", "0"));
		params.add(new BasicNameValuePair("gj_bhxfbg", "0"));
		params.add(new BasicNameValuePair("cpwsswzt", "0"));
		params.add(new BasicNameValuePair("sfsc", "0"));
		params.add(new BasicNameValuePair("snong", "0"));
		params.add(new BasicNameValuePair("sfkt", "0"));
		params.add(new BasicNameValuePair("gkkt", "0"));
		params.add(new BasicNameValuePair("fbgg", "0"));
		params.add(new BasicNameValuePair("dtxp", "0"));
		params.add(new BasicNameValuePair("sfpq", "0"));
		params.add(new BasicNameValuePair("ktbl", "0"));
		params.add(new BasicNameValuePair("sflx", "0"));
		params.add(new BasicNameValuePair("ahcxjd", "jq"));
		params.add(new BasicNameValuePair("start", "1"));
		params.add(new BasicNameValuePair("limit", "200"));
		params.add(new BasicNameValuePair("a_dhx_rSeed", "1405047216557"));
		request.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

		HttpResponse response = _client.execute(request);
		ResponseHandler<String> rh = new BasicResponseHandler();
		String html = rh.handleResponse(response);
		if (html != null)
			html = html.replaceAll("\r\n", "");
		// _logger.debug(html);

		Document doc = null;
		try
		{
			doc = DocumentHelper.parseText(html);
		} catch (DocumentException e)
		{
			_logger.error(e.getMessage(), e);
		}

		Element rootElt = doc.getRootElement();
		@SuppressWarnings("rawtypes")
		Iterator rowIt = rootElt.elementIterator("row");
		while (rowIt.hasNext())
		{
			Element rowEle = (Element) rowIt.next();
			String ahdm = rowEle.attribute("id").getText();
			dmList.add(ahdm);
		}
		_logger.debug(dmList.size());

		// _logger.debug("'");

	}

	public List<List<String>> getResult()
	{
		return _result;
	}

	public static void main(String args[]) throws ClientProtocolException, IOException
	{
		ReportQueryXiaci tongdahai = new ReportQueryXiaci();
		System.out.println(tongdahai.getResult());
	}

}
