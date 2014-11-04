package org.bnez.xiaoyue.lsfy.report;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.bnez.lsfy.service.ReportPeriod;

public class Report_7_1_1 implements ReportJobBuilder
{
	private static final Logger _logger = Logger.getLogger(Report_7_1_1.class);

	public Report_7_1_1()
	{
	}

	public void buildJob()
	{
		String zhibiao = "结案均衡度";

		ReportPeriodBuilder rdb = ReportPeriodBuilder.getInstance();
		for (ReportPeriod rp : rdb.build())
		{
			ReportCondition rc = new ReportCondition();
			rc.setFolderName("7、质效评估报表");
			rc.setReportName("7.1.1结案均衡度");
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
		new Report_7_1_1().buildJob();
		ReportJobScheduler.getInstance().start();
		System.out.println("done");
	}
}
