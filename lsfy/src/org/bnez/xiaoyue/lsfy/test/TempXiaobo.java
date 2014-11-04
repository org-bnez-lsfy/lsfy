package org.bnez.xiaoyue.lsfy.test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.bnez.wwwreader.base.TagLocator;
import org.bnez.wwwreader.base.TagPath;
import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class TempXiaobo
{

	private List<List<String>> _result;
	
	public TempXiaobo()
	{
		_result = new ArrayList<List<String>>();
	}

	public void parse() throws IOException, SAXException
	{
		String html = FileUtils.readFileToString(new File("e:/aasss.html"));
		DOMParser parser = new DOMParser();
		ByteArrayInputStream stream = new ByteArrayInputStream(html.getBytes("UTF-8"));
		parser.parse(new InputSource(stream));
		Document doc = parser.getDocument();

		TagPath tp = null;
		tp = new TagPath("html|body|table#table_0");// |tbody|tr#detail|td#table_0_td_23
		TagLocator tl = new TagLocator(doc, tp);
		List<Node> nodes = tl.locate();
		if (nodes == null || nodes.size() == 0)
			return;

		for (int tb = 0; tb < nodes.size(); tb++)
		{
			TagLocator tbll = new TagLocator(nodes.get(tb), new TagPath("tbody|tr#detail"));
			List<Node> trs = tbll.locate();
			for (int tr = 0; tr < trs.size(); tr++)
			{
				List<String> record = new ArrayList<String>();
				NodeList tds = trs.get(tr).getChildNodes();
				for (int i = 0; i < tds.getLength(); i++)
				{
					Node n = tds.item(i);
					if(n.getNodeName().equalsIgnoreCase("TD"))
						record.add(n.getTextContent().trim());
				}
				_result.add(record);
			}
		}
		
		for(List<String> line : _result)
			System.out.println(line.size() + " - " + line);
	}
	
	public static void main(String[] args) throws IOException, SAXException
	{
		new TempXiaobo().parse();
	}
}
