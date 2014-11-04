package org.bnez.xiaoyue.lsfy.report;

import org.apache.log4j.Logger;
import org.bnez.lsfy.service.ReportPeriod;

public class Report_5_8 implements ReportJobBuilder
{
	private static final Logger _logger = Logger.getLogger(Report_5_8.class);
	
	public void buildJob()
	{
		String[] zb = { "收案数", "结案数", "未结案数", "违法制裁率", "拘留制裁率", "执限内执结率", "受托案件实际执行率", "向上级法院申请执行率", "向上级法院申请执行案件数",
				"对外委托率", "委托省外法院执行率", "委托案件标的清偿率", "受托执行率", "受托案件标的清偿率", "提级执行案件实际执行率", "指定执行案件实际执行率", "请示案件数量",
				"暂缓执行数量", "非诉行政案件实际执行率", "仲裁裁决不予执行率", "冲裁裁决实际执行率", "公证债权文书不予执行", "公权债券文书实际执行率",
				"人民调解确认案件进入执行程序数及执行收案数的比例", "特殊主体案件数", "农民工工资案件数量", "采取立即执行措施的案件数量", "财产报告的案件数量", "出境通报备案数量", "限制出境数量",
				"通过公安机关查获被执行人数量", "失信信息通报的数量", "失信信息曝光的数量", "执行异议案件转为诉讼的案件数", "参与分配案件转为诉讼的案件数", "司法救助人数", "司法救助金额",
				"暴力抗拒执行案件数量及人身伤亡、财产损失情况", "被执行财产流拍率", "执行案卷归档率" };
		for (String z : zb)
		{

			ReportPeriodBuilder rdb = ReportPeriodBuilder.getInstance();
			for (ReportPeriod rp : rdb.build())
			{
				ReportCondition rc = new ReportCondition();
				rc.setFolderName("5、执行分析主题");
				rc.setReportName("5.8执行调研数据（地市）");
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
		new Report_5_8().buildJob();
		ReportJobScheduler.getInstance().start();
	}
}
