package org.bnez.xiaoyue.lsfy.report;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.bnez.lsfy.service.ReportPeriod;

public class Report_5_3_2 implements ReportJobBuilder
{
	private static final Logger _logger = Logger.getLogger(Report_5_3_2.class);

	public Report_5_3_2()
	{
	}

	public void buildJob()
	{
		String zhibiao = "财产保全案件自行履行率";

		ReportPeriodBuilder rdb = ReportPeriodBuilder.getInstance();
		for (ReportPeriod rp : rdb.build())
		{
			ReportCondition rc = new ReportCondition();
			rc.setFolderName("5、执行分析主题");
			rc.setReportName("5.3.2财产保全案件自行履行率");
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
		new Report_5_3_2().buildJob();
		ReportJobScheduler.getInstance().start();
		System.out.println("done");
	}
}
