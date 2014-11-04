package org.bnez.xiaoyue.lsfy.report;

import org.apache.log4j.Logger;
import org.bnez.lsfy.service.ReportPeriod;

public class Report_7_6 implements ReportJobBuilder
{
	private static final Logger _logger = Logger.getLogger(Report_7_6.class);
	public void buildJob()
	{
		String[] zb = { "同期结案率", "结案率", "月均未结案率", "月均存案工作量", "当前存案工作量", "平均审理天数", "平均审限天数", "上诉案件平均移送天数", "上诉案件延期移送率",
				"18个月以上未结案数", "30个月以上未结案数", "民事调撤率", "民事调解率", "民事撤诉率", "一审民事可调撤率", "民事可调撤率", "一审民事行政息诉率", "民事行政上诉率",
				"上诉维持率", "二审改判发回瑕疵率", "二审改判发回率", "申诉率", "申诉裁定再审率", "申诉改判发回率", "民事调解自动履行率", "民商事案件实际履行率", "归档报结率",
				"简易程序适用率", "一审普通程序陪审率" };

		String[] page = { "firstPage", "lastPage" };
		for (String z : zb)
		{

			ReportPeriodBuilder rdb = ReportPeriodBuilder.getInstance();
			for (ReportPeriod rp : rdb.build())
			{
				for (String p : page)
				{
					ReportCondition rc = new ReportCondition();
					rc.setFolderName("7、质效评估报表");
					rc.setReportName("7.6业务庭业绩分析（累计）");
					rc.setZhibiao(z);
					rc.setFrom(rp.getFrom());
					rc.setTo(rp.getTo());
					rc.setType(1); // 用于buildDatas
					// firstPage 拿第一页数据 lastPage拿最后一页数据
					// 下一页是nextPage set到ui.action
					rc.setPage(p);

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
	}

	public static void main(String[] args)
	{
		new Report_7_6().buildJob();
		ReportJobScheduler.getInstance().start();
	}
}
