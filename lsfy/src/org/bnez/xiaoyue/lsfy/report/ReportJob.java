package org.bnez.xiaoyue.lsfy.report;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.xml.sax.SAXException;

public interface ReportJob
{
	public void run() throws ClientProtocolException, IOException, SAXException;

	public String getName();
}
