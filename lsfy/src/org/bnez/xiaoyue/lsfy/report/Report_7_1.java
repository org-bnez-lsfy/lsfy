package org.bnez.xiaoyue.lsfy.report;

import org.apache.log4j.Logger;
import org.bnez.lsfy.service.ReportPeriod;

public class Report_7_1 implements ReportJobBuilder
{
	private static final Logger _logger = Logger.getLogger(Report_7_1.class);
	
	public void buildJob()
	{
		// String[] zb = { "同期结案率"};
		String[] zb = { "同期结案率", "结案率", "月均未结案率", "月均存案工作量", "平均审理天数", "平均审限天数", "上诉案件平均移送天数", "上诉案件延期移送率", "人均结案数",
				"法官人均结案数", "一线法官人均结案数", "18个月以上未结案数", "30个月以上未结案数", "12个月以上未结案数", "民事调撤率", "民事调解率", "民事撤诉率",
				"一审民事可调撤率", "民事可调撤率", "一审民事行政息诉率", "民事行政上诉率", "上诉维持率", "二审改判发回瑕疵率", "二审改判发回率", "申诉率", "申诉裁定再审率",
				"申诉改判发回率", "民事调解自动履行率", "民商事案件实际履行率", "缺项超期未归率", "庭审录像率", "归档报结率" };
		for (String z : zb)
		{
			ReportPeriodBuilder rdb = ReportPeriodBuilder.getInstance();
			for (ReportPeriod rp : rdb.build())
			{
				ReportCondition rc = new ReportCondition();
				rc.setFolderName("7、质效评估报表");
				rc.setReportName("7.1审判评估数据");
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
		new Report_7_1().buildJob();
		ReportJobScheduler.getInstance().start();
	}
}
