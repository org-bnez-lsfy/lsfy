package org.bnez.xiaoyue.lsfy.report;

import org.apache.log4j.Logger;
import org.bnez.lsfy.service.ReportPeriod;

public class Report_7_2 implements ReportJobBuilder
{
	private static final Logger _logger = Logger.getLogger(Report_7_2.class);

	public void buildJob()
	{
		// String[] zb = { "收案数" };
		String[] zb = { "收案数", "结案数", "未结案数", "法定（正常）审限内结案率", "立案变更率", "简易程序适用率", "当庭裁判率", "一审普通程序陪审率", "二审开庭审理率",
				"请示案件数", "归档率", "超3月归档案件数" };
		for (String z : zb)
		{

			ReportPeriodBuilder rdb = ReportPeriodBuilder.getInstance();
			for (ReportPeriod rp : rdb.build())
			{
				ReportCondition rc = new ReportCondition();
				rc.setFolderName("7、质效评估报表");
				rc.setReportName("7.2审判调研数据");
				rc.setZhibiao(z);
				rc.setFrom(rp.getFrom());
				rc.setTo(rp.getTo());

				try
				{
					ReportJobScheduler.getInstance().add(new ZhibiaoOnSelect(rc));
				} catch (Exception e)
				{
					_logger.error(e.getMessage(), e);
				}
			}
		}
	}

	public static void main(String[] args)
	{
		new Report_7_2().buildJob();
		ReportJobScheduler.getInstance().start();
	}
}
