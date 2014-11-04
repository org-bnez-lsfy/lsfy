package org.bnez.xiaoyue.lsfy.report;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.bnez.xiaoyue.lsfy.util.HibernateUtil;
import org.xml.sax.SAXException;

public class SingleZhibiaoPerPage implements ReportJob
{
	private static final Logger _logger = Logger.getLogger(SingleZhibiaoPerPage.class);
	private ReportQuery _query;
	private ReportCondition _condition;

	public SingleZhibiaoPerPage(ReportCondition rc)
	{
		_condition = rc;
		_query = new ReportQuery();
	}

	public String getName()
	{
		return _condition.getFolderName() + " " + _condition.getReportName() + " " + _condition.getZhibiao() + " "
				+ _condition.getFrom() + "," + _condition.getTo();
	}

	public void run()
	{
		// 如果不需要抽数据，直接返回
		if (!new NeedQuery(_condition.getFrom(), _condition.getTo(), _condition.getZhibiao()).isNeed())
			return;
		
		try
		{
			queryFromReportSystem();
		} catch (Exception e)
		{
			_logger.error(e.getMessage(), e);
		}
	}

	private void queryFromReportSystem() throws ClientProtocolException, IOException, SAXException
	{

		_query.query(_condition);
		List<List<String>> result = _query.getResult();
		if (result.size() == 0)
			return;

		List<ReportData> datas = new ArrayList<ReportData>();
		for (List<String> record : result)
		{
			ReportData data = buildData(_condition, record, _condition.getZhibiao());
			datas.add(data);
		}

		RecordOrder ro = new RecordOrder(datas);
		ro.order();

		HibernateUtil.currentSession();
		try
		{
			ReportDB db = new ReportDB();
			db.delete(_condition.getFrom(), _condition.getTo(), _condition.getZhibiao());
			db.save(datas);
		} catch (Exception e)
		{
		} finally
		{
			HibernateUtil.closeSession();
		}
	}

	private ReportData buildData(ReportCondition rc, List<String> record, String zhibiao)
	{
		ReportData data = new ReportData();
		data.setZhibiao(zhibiao);
		data.setKoujing4(record.get(1));
		data.setFrom(rc.getFrom());
		data.setTo(rc.getTo());
		data.setValue(record.get(2));
		data.setTongbi(record.get(3));
		Integer order = null;
		try
		{
			order = Integer.parseInt(record.get(0));
		} catch (Exception e)
		{
		}
		data.setPaimingQuansheng(order);
		data.setInsertAt(new Date());

		return data;
	}

	public static void main(String args[])
	{
		short s = 1;
		s += 1;
		System.out.println(s);

	}

}
