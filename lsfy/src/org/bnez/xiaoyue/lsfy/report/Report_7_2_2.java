package org.bnez.xiaoyue.lsfy.report;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.bnez.lsfy.service.ReportPeriod;

public class Report_7_2_2 implements ReportJobBuilder
{
	private static final Logger _logger = Logger.getLogger(Report_7_2_2.class);

	public Report_7_2_2()
	{
	}

	public void buildJob()
	{
		String zhibiao = "简易程序适用率";

		ReportPeriodBuilder rdb = ReportPeriodBuilder.getInstance();
		for (ReportPeriod rp : rdb.build())
		{
			ReportCondition rc = new ReportCondition();
			rc.setFolderName("7、质效评估报表");
			rc.setReportName("7.2.2简易程序适用率（不含公告送达缺席判决）");
			rc.setTableTag(3);
			rc.setFrom(rp.getFrom());
			rc.setTo(rp.getTo());
			rc.setZhibiao(zhibiao);

			try
			{
				ReportJobScheduler.getInstance().add(new SingleZhibiaoPerPage(rc));
			} catch (Exception e)
			{
				_logger.error(e.getMessage(), e);
			}
		}
	}

	public static void main(String[] args) throws ClientProtocolException, IOException
	{
		new Report_7_2_2().buildJob();
		ReportJobScheduler.getInstance().start();
		System.out.println("done");
	}
}
