package org.bnez.xiaoyue.lsfy.report;

import org.apache.log4j.Logger;
import org.bnez.lsfy.service.BizServiceClient;
import org.bnez.lsfy.service.ReportPeriod;
import org.bnez.xiaoyue.lsfy.db.ReportDB;

public class Report_5_2 implements ReportJobBuilder
{
	private static final Logger _logger = Logger.getLogger(Report_5_2.class);
	
	public void buildJob()
	{
		String[] zb = { "同期执结率", "执结率", "月均存案工作量", "当前存案工作量", "平均执行天数", "平均执限天数", "委托评估拍卖变卖平均天数", "委托评估平均天数",
				"委托拍卖平均天数", "委托变卖平均天数", "超过60天委托评估拍卖变卖未完成数", "执行款发放平均天数", "执行款延期未发放率", "执行案款延期发放率", "18个月以上未结案数",
				"12个月以上未结案数", "人均结案数", "实际执行率", "执结案中的实际执行率", "执行标的清偿率", "初执标的清偿率", "和解标的清偿率所占比例", "劳动报酬三废标的清偿率",
				"执行标的清偿率（旧）", "执行异议率", "执行异议成立率", "执行异议办理平均天数", "执行复议率", "执行复议撤销、改正率", "执行复议办理平均天数", "违法制裁率" };
		for (String z : zb)
		{

			ReportPeriodBuilder rdb = ReportPeriodBuilder.getInstance();
			for (ReportPeriod rp : rdb.build())
			{
				ReportCondition rc = new ReportCondition();
				rc.setFolderName("5、执行分析主题");
				rc.setReportName("5.2执行评估数据");
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
		new ReportDB().query("aaa", BizServiceClient.getInstance().getCurrentYearReportPeriod());
		//new ReportDB().query("aaaaaaaaaaa", new ReportPeriod(rdb.getYearStart() , rdb.getCurrentTo()));
		new Report_5_2().buildJob();
		ReportJobScheduler.getInstance().start();
	}
}
