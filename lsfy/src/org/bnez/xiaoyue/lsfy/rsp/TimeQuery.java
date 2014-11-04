package org.bnez.xiaoyue.lsfy.rsp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.bnez.xiaoyue.lsfy.XiaoyueResponse;

public class TimeQuery
{
	private static final SimpleDateFormat sdd = new SimpleDateFormat("yyyy年M月d日");
	private static final SimpleDateFormat sdt = new SimpleDateFormat("H点m分s秒");
	private static final String fmt = "现在是%s，星期%s，北京时间%s";

	private Date _now;
	private String _dayOfWeek;

	public TimeQuery(String combName, String combString)
	{
		_now = new Date();
	}

	public XiaoyueResponse response()
	{
		buildDayOfWeek();

		String rsp = String.format(fmt, sdd.format(_now), _dayOfWeek, sdt.format(_now));

		XiaoyueResponse xr = ResponseBuilder.build(rsp);
		return xr;
	}

	private void buildDayOfWeek()
	{
		Calendar c = Calendar.getInstance();
		c.setTime(_now);
		int dow = c.get(Calendar.DAY_OF_WEEK);

		_dayOfWeek = String.valueOf(dow - 1); // start from sunday, monday == 2
		if (dow == Calendar.SUNDAY)
			_dayOfWeek = "天";
	}

	public static void main(String[] args)
	{
		XiaoyueResponse xr = new TimeQuery(null, null).response();
		System.out.println(xr.getSpeak());
	}
}
