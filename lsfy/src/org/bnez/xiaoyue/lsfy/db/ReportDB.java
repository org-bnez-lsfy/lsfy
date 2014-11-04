package org.bnez.xiaoyue.lsfy.db;

import java.util.List;

import org.apache.log4j.Logger;
import org.bnez.lsfy.service.ReportPeriod;
import org.bnez.xiaoyue.lsfy.util.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class ReportDB
{
	@SuppressWarnings("unused")
	private static final Logger _logger = Logger.getLogger(ReportDB.class);

	public ReportData query(String zhibiao, String fayuan, ReportPeriod period)
	{
		Session s = HibernateUtil.currentSession();
		Criteria c = s.createCriteria(ReportData.class);
		c.add(Restrictions.eq("zhibiao", zhibiao));
		if(fayuan != null)
			c.add(Restrictions.eq("koujing4", fayuan));
		c.add(Restrictions.eq("from", period.getFrom()));
		c.add(Restrictions.eq("to", period.getTo()));
		@SuppressWarnings("unchecked")
		List<ReportData> list = c.list();
		ReportData rd = null;
		if(list.size() > 0)
			rd = list.get(0);
		HibernateUtil.closeSession();
		return rd;
	}

	public List<ReportData> query(String zhibiao, ReportPeriod period)
	{
		Session s = HibernateUtil.currentSession();
		Criteria c = s.createCriteria(ReportData.class);
		c.add(Restrictions.eq("zhibiao", zhibiao));
		c.add(Restrictions.eq("from", period.getFrom()));
		c.add(Restrictions.eq("to", period.getTo()));
		@SuppressWarnings("unchecked")
		List<ReportData> list = c.list();
		HibernateUtil.closeSession();
		return list;
	}

	public List<ReportData> queryExistsFy(String zhibiao, ReportPeriod period)
	{
		Session s = HibernateUtil.currentSession();
		Criteria c = s.createCriteria(ReportData.class);
		c.add(Restrictions.eq("zhibiao", zhibiao));
		c.add(Restrictions.eq("from", period.getFrom()));
		c.add(Restrictions.eq("to", period.getTo()));
		
		@SuppressWarnings("unchecked")
		List<ReportData> list = c.list();
		HibernateUtil.closeSession();
		return list;
	}

	public static void main(String[] args)
	{
		StringBuilder sb = new StringBuilder();
		String en = "%60%63%120%109%108%32%118%101%114%115%105%111%110%61%34%49%46%48%34%32%101%110%99%111%100%105%110%103%61%34%71%66%50%51%49%50%34%63%62%60%82%79%79%84%62%60%67%79%78%32%70%73%69%76%68%61%34%40%89%81%83%77%34%32%86%65%76%85%69%61%34%78%85%76%76%32%79%82%32%89%81%83%77%61%39%39%32%111%114%32%89%81%83%77%61%39%110%117%108%108%39%41%34%32%84%89%80%69%61%34%81%73%34%32%79%80%84%61%34%73%83%34%47%62%60%47%82%79%79%84%62";
		for(String cc : en.split("%"))
		{
			System.out.println(cc);
			if(cc.equals(""))
				continue;
			
			char c = (char)(Integer.valueOf(cc).intValue());
			System.out.println(c);
			sb.append(c);
		}
		System.out.println(sb);
	}
}
