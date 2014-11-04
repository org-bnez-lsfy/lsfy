package org.bnez.xiaoyue.lsfy.report;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.bnez.xiaoyue.lsfy.util.HibernateUtil;
import org.xml.sax.SAXException;

public class ZhibiaoOnSelect implements ReportJob
{
	private static final Logger _logger = Logger.getLogger(ZhibiaoOnSelect.class);
	private ReportQuery _report;
	private ReportDB _db;
	private ReportCondition _condition;

	public ZhibiaoOnSelect(ReportCondition rc)
	{
		_condition = rc;
		_report = new ReportQuery();
		_db = new ReportDB();
	}

	public String getName()
	{
		String n = _condition.getFolderName() + " " + _condition.getReportName() + " " + _condition.getZhibiao() + " "
				+ _condition.getFrom() + "," + _condition.getTo();
		if (_condition.getPage() != null)
			n += " " + _condition.getPage();
		return n;
	}

	public void run() throws ClientProtocolException, IOException, SAXException
	{
		String rpt = _condition.getReportName();
		String zhibiao = _condition.getZhibiao();
		if (zhibiao.equals("收案数") || zhibiao.equals("结案数") || zhibiao.equals("未结案数"))
		{
			if (rpt.equals("5.3执行调研数据"))
				_condition.setZb("执行" + _condition.getZhibiao());
			if (rpt.equals("5.8执行调研数据（地市）"))
				_condition.setZb("地市执行" + _condition.getZhibiao());
			
		}
		if (zhibiao.equals("人均结案数") || zhibiao.equals("月均存案工作量"))
		{
			if (rpt.equals("5.2执行评估数据"))
				_condition.setZb("执行" + _condition.getZhibiao());
			if (rpt.equals("5.7执行评估数据（地市）"))
				_condition.setZb("地市执行" + _condition.getZhibiao());
		}
		if (zhibiao.equals("法定（正常）审限内结案率"))
			_condition.setZb("法定正常审限内结案率");
		if (zhibiao.equals("18个月以上未结案数") && rpt.equals("5.2执行评估数据"))
			_condition.setZb("18个月以上未执结案数");
		if (zhibiao.equals("12个月以上未结案数") && rpt.equals("5.2执行评估数据"))
			_condition.setZb("12个月以上未执结案数");
		if (_condition.getZb() == null || _condition.getZb().equals(""))
			_condition.setZb(_condition.getZhibiao());

		// 如果不需要抽数据，直接返回
		if (!new NeedQuery(_condition.getFrom(), _condition.getTo(), _condition.getZhibiao()).isNeed())
			return;
		
		List<ReportData> datas = buildDatas(_condition);

		HibernateUtil.currentSession();
		if (datas.size() > 0)
		{
			if (rpt.equals("首页-中院")){
				String[] zb = { "全市收案数", "全市结案数", "全市未结案数" };
				for (int i = 0; i < zb.length; i++)
					_db.delete(_condition.getFrom(), _condition.getTo(), zb[i]);
			}else{
				_db.delete(_condition.getFrom(), _condition.getTo(), _condition.getZb());
			}
			_db.save(datas);
		}
		HibernateUtil.closeSession();
	}

	private List<ReportData> buildDatas(ReportCondition condition) throws ClientProtocolException, IOException,
			SAXException
	{
		condition.setFanwei("全省");
		_report.query(condition);
		List<List<String>> result = _report.getResult();
		List<ReportData> datas = new ArrayList<ReportData>();

		if (result.size() == 0)
			return datas;

		if (condition.getType() == 1)
		{
			List<String> cospan = _report.getCospan();
			List<Integer> cospanInt = new ArrayList<Integer>();
			for (int j = 0; j < cospan.size(); j++)
			{
				int a = Integer.parseInt(cospan.get(j));
				cospanInt.add(a);
			}
			int j = 0;
			if (condition.getPage().equals("firstPage"))
				j = 10;
			for (Integer cos : cospanInt)
			{
				for (int i = j; i < cos + j; i++)
				{
					List<String> recordQS = result.get(i);
					ReportData data = new ReportData();
					data.setZhibiao(condition.getZhibiao());
					if (i == j)
					{
						data.setKoujing4(recordQS.get(0));
						data.setKoujing3(recordQS.get(1));
						data.setKoujing2(recordQS.get(2));
						data.setFrom(condition.getFrom());
						data.setTo(condition.getTo());
						data.setValue(recordQS.get(3));
					} else
					{
						data.setKoujing4(result.get(j).get(0));
						data.setKoujing3(recordQS.get(0));
						data.setKoujing2(recordQS.get(1));
						data.setFrom(condition.getFrom());
						data.setTo(condition.getTo());
						data.setValue(recordQS.get(2));
					}

					Integer order = null;
					try
					{
						order = Integer.parseInt(recordQS.get(0));
					} catch (Exception e)
					{
					}
					data.setPaimingQuansheng(order);
					data.setCondition(condition.toJson());
					data.setInsertAt(new Date());
					_logger.debug(data.getInsertAt());
					handleDualZhibiao(condition, data);
					datas.add(data);
				}
				j = cos + j;
			}
		} else if (condition.getType() == 2)
		{
			String[] zhibiao = { "全市收案数", "全市结案数", "全市未结案数" };
			Integer count = 1;
			for (int i = 0; i < zhibiao.length; i++)
			{
				List<String> recordQS = result.get(20);
				ReportData data = new ReportData();
				data.setZhibiao(zhibiao[i]);
				data.setFrom(condition.getFrom());
				data.setTo(condition.getTo());
				data.setValue(recordQS.get(count));
				data.setTongbi(recordQS.get(count + 1));
				Integer order = null;
				try
				{
					order = Integer.parseInt(recordQS.get(0));
				} catch (Exception e)
				{
				}
				data.setPaimingQuansheng(order);
				data.setCondition(condition.toJson());
				data.setInsertAt(new Date());
				handleDualZhibiao(condition, data);
				datas.add(data);
				count = count + 2 * (i + 1);
			}

		} else
		{
			for (int i = 0; i < result.size(); i++)
			{
				List<String> recordQS = result.get(i);
				ReportData data = new ReportData();
				data.setZhibiao(condition.getZhibiao());
				data.setKoujing4(recordQS.get(1));
				data.setFrom(condition.getFrom());
				data.setTo(condition.getTo());
				data.setValue(recordQS.get(2));
				data.setTongbi(recordQS.get(3));
				Integer order = null;
				try
				{
					order = Integer.parseInt(recordQS.get(0));
				} catch (Exception e)
				{
				}
				data.setPaimingQuansheng(order);
				data.setCondition(condition.toJson());
				data.setInsertAt(new Date());
				handleDualZhibiao(condition, data);
				datas.add(data);
			}
		}

		RecordOrder ro = new RecordOrder(datas);
		ro.order();

		return datas;
	}

	private void handleDualZhibiao(ReportCondition condition, ReportData data)
	{
		String rpt = condition.getReportName();
		String zhibiao = condition.getZhibiao();
		if (zhibiao.equals("收案数") || zhibiao.equals("结案数") || zhibiao.equals("未结案数"))
		{
			if (rpt.equals("5.3执行调研数据"))
				data.setZhibiao("执行" + data.getZhibiao());
			if (rpt.equals("5.8执行调研数据（地市）"))
				data.setZhibiao("地市执行" + data.getZhibiao());
		}
		if (zhibiao.equals("人均结案数") || zhibiao.equals("月均存案工作量"))
		{
			if (rpt.equals("5.2执行评估数据"))
				data.setZhibiao("执行" + data.getZhibiao());
			if (rpt.equals("5.7执行评估数据（地市）"))
				data.setZhibiao("地市执行" + data.getZhibiao());
		}
		if (zhibiao.equals("法定（正常）审限内结案率"))
			data.setZhibiao("法定正常审限内结案率");
		if (zhibiao.equals("18个月以上未结案数") && rpt.equals("5.2执行评估数据"))
			data.setZhibiao("18个月以上未执结案数");
		if (zhibiao.equals("12个月以上未结案数") && rpt.equals("5.2执行评估数据"))
			data.setZhibiao("12个月以上未执结案数");

	}

}
