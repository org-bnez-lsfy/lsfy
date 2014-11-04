package org.bnez.xiaoyue.lsfy.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.bnez.lsfy.service.BizServiceClient;
import org.bnez.lsfy.service.ReportPeriod;
import org.bnez.xiaoyue.lsfy.common.Config;

public class ReportPeriodBuilder
{

	@SuppressWarnings("unused")
	private static final Logger _logger = Logger.getLogger(ReportPeriodBuilder.class);
	
	private static ReportPeriodBuilder _instance = null;
	private boolean _buildOld;
	
	public synchronized static ReportPeriodBuilder getInstance()
	{
		if(_instance == null)
			_instance = new ReportPeriodBuilder();
		return _instance;
	}

	private ReportPeriodBuilder()
	{
		_buildOld = Config.getInstance().getBool("report.buildOld");
	}

	public Collection<ReportPeriod> build()
	{
		if(_buildOld)
		{
			Collection<ReportPeriod> all = BizServiceClient.getInstance().getAllPosiablePeriod();
			return all;
		}
		
		List<ReportPeriod> some = new ArrayList<ReportPeriod>();
		some.add(BizServiceClient.getInstance().getCurrentYearReportPeriod());
		return some;
	}

	public static void main(String[] args)
	{
		Collection<ReportPeriod> ps = new ReportPeriodBuilder().build();
		for (ReportPeriod p : ps)
			System.out.println(p.toChinese() + " - " + p.toString());
	}

}
