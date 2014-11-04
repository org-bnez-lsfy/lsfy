package org.bnez.xiaoyue.lsfy.report;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class ReportAll
{
	protected static final Logger _logger = Logger.getLogger(ReportAll.class);

	public static void main(String[] args)
	{
		new ReportAll().run();
	}

	public void run()
	{
		List<ReportJobBuilder> builders = new ArrayList<ReportJobBuilder>();
		//builders.add(new Report_0_0()); // 还有bug
		builders.add(new Report_5_2_1());
		builders.add(new Report_5_2_2());
		builders.add(new Report_5_2());
		builders.add(new Report_5_3_1());
		builders.add(new Report_5_3_2());
		builders.add(new Report_5_3());
		builders.add(new Report_5_4_1());
		builders.add(new Report_5_4());
		builders.add(new Report_5_5());
		//builders.add(new Report_5_7()); // 与5-2,5-3指标重复，暂时屏蔽掉
		//builders.add(new Report_5_8());
		builders.add(new Report_7_1_1());
		builders.add(new Report_7_1_2());
		builders.add(new Report_7_1_3());
		builders.add(new Report_7_1_4());
		builders.add(new Report_7_1());
		builders.add(new Report_7_2_1());
		builders.add(new Report_7_2_2());
		builders.add(new Report_7_2_3());
		builders.add(new Report_7_2());
		//builders.add(new Report_7_6()); // 指标有重复等
		
		for(ReportJobBuilder jb : builders)
			jb.buildJob();
		_logger.debug("total job " + ReportJobScheduler.getInstance().getJobs());
		
		ReportJobScheduler.getInstance().start();
	}
}
