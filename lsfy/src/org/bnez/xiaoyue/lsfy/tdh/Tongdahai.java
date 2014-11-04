package org.bnez.xiaoyue.lsfy.tdh;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;

public abstract class Tongdahai
{
	protected CloseableHttpClient _client;

	protected void tongdahaiLogin() throws ClientProtocolException, IOException
	{
		init();
		prelogin();
		login();
	}

	private void init()
	{
		HttpClientBuilder hcb = HttpClientBuilder.create();
		//hcb.setProxy(new HttpHost("127.0.0.1", 8888));
		hcb.setRedirectStrategy(new LaxRedirectStrategy());
		hcb.setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36");
		_client = hcb.build();
	}

	private void prelogin() throws ClientProtocolException, IOException
	{
		String url = "http://203.150.144.8/frame/court.jsp";
		HttpGet request = new HttpGet(url);
		_client.execute(request);
	}

	private void login() throws ClientProtocolException, IOException
	{
		String url = "http://203.150.144.8/frame/login";
		HttpPost request = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("j_username", "zhurj"));// fydm=332500
		params.add(new BasicNameValuePair("fydm", "332500"));
		params.add(new BasicNameValuePair("j_password", "12345"));
		request.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

		HttpResponse response = _client.execute(request);
		ResponseHandler<String> rh = new BasicResponseHandler();
		@SuppressWarnings("unused")
		String html = rh.handleResponse(response);
		// _logger.debug(html);
	}
}
