package org.bnez.xiaoyue.lsfy.tdh;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class DangAnShaomiaoQuery extends Tongdahai
{

	private static final Logger _logger = Logger.getLogger(DangAnShaomiaoQuery.class);
	private int _total;
	private int _no;

	public DangAnShaomiaoQuery() throws ClientProtocolException, IOException
	{
		super.tongdahaiLogin();
	}

	public void query() throws ClientProtocolException, IOException, DocumentException
	{
		String totalUrl =    "http://203.150.144.8:82/dagl/webapp/da/dagl/ssajcx_xml.jsp?nndd=&dabm=&ah=&bgqx=&cbbm=&cbrr=&dsr=&ay=&sfsm=&start=0&limit=40&a_dhx_rSeed=1406452674272";
		String shaomiaoUrl = "http://203.150.144.8:82/dagl/webapp/da/dagl/ssajcx_xml.jsp?nndd=&dabm=&ah=&bgqx=&cbbm=&cbrr=&dsr=&ay=&sfsm=1&start=0&limit=40&a_dhx_rSeed=1406454523805";
		_total = queryTotal(totalUrl);
		int sm = queryTotal(shaomiaoUrl);
		_no = _total - sm;
	}

	private int queryTotal(String url) throws DocumentException, ClientProtocolException, IOException
	{
		HttpGet request = new HttpGet(url);
		HttpResponse rsp = _client.execute(request);
		ResponseHandler<String> rh = new BasicResponseHandler();
		String html = rh.handleResponse(rsp);
		return parseCount(html);
	}

	private int parseCount(String html) throws DocumentException
	{
		if (html != null)
			html = html.replaceAll("\r\n", "");
		Document doc = DocumentHelper.parseText(html);

		Element rootElt = doc.getRootElement();
		Element totalEle = (Element) rootElt.elements().get(0);
		String totalStr = totalEle.getText();
		_logger.debug(totalStr);

		return Integer.parseInt(totalStr);
	}

	public int getTotalCount()
	{
		return _total;
	}

	public int getWeiShaoMiaoCount()
	{
		return _no;
	}
	
	public static void main(String[] args) throws ClientProtocolException, IOException, DocumentException
	{
		DangAnShaomiaoQuery q = new DangAnShaomiaoQuery();
		q.query();
		System.out.println(q.getTotalCount() + "/" + q.getWeiShaoMiaoCount());
	}
}
