package org.bnez.xiaoyue.lsfy.rsp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.bnez.lsfy.service.BizServiceClient;
import org.bnez.lsfy.service.FayuanBean;
import org.bnez.xiaoyue.lsfy.XiaoyueResponse;
import org.bnez.xiaoyue.lsfy.common.Config;
import org.bnez.xiaoyue.lsfy.db.User;
import org.bnez.xiaoyue.lsfy.tdh.Xiaci;
import org.bnez.xiaoyue.lsfy.tdh.XiaciRepository;

public class XiaciHandler extends Handler
{

	private String _curYear;

	public XiaciHandler(String combName, String combString)
	{
		_combName = combName;
		_combString = combString;
		_curYear = getCurrentYear();
	}

	public XiaoyueResponse response()
	{
		if(isAsk("ls_other_xiaci_mine"))
			return responseMine();
		if(isAsk("ls_other_xiaci_all"))
			return responseAll();
		return null;
	}

	private XiaoyueResponse responseMine()
	{
		User u = XiaoyueContext.getCurrentThreadUser();
		if(u == null)
			return ResponseBuilder.build("无法确定当前用户信息");
		
		Long fid = u.getFayuanId();
		if(fid == null)
			return ResponseBuilder.build("当前用户没有配置归属法院");
		
		FayuanBean fy = BizServiceClient.getInstance().queryFayuan(fid);
		if(fy == null)
			return ResponseBuilder.build("当前用户配置的法院信息不正确");
		
		String tdhfy = fy.getTdhName();
		if(tdhfy == null)
			return ResponseBuilder.build("当前用户配置的法院信息不完整");
		
		List<Xiaci> all = new XiaciRepository().queryAll();
		List<Xiaci> mine = new ArrayList<Xiaci>();
		for(Xiaci x : all)
			if(x.getYsfy().equals(tdhfy) && isOfCurYear(x))
				mine.add(x);
		
		String spk = buildSpk(fy.getName(), mine.size());
		String url = null;
		if(mine.size()>0)
			url = String.format(Config.getInstance().getString("url.xiaciMine"), _curYear, fy.getId());
		return ResponseBuilder.build(spk, url);
	}

	private XiaoyueResponse responseAll()
	{
		List<Xiaci> all = new XiaciRepository().queryAll();
		List<Xiaci> cury = new ArrayList<Xiaci>();
		for(Xiaci x : all)
			if(isOfCurYear(x))
				cury.add(x);

		String spk = buildSpk("丽水两级法院", cury.size());
		String url = null;
		if(cury.size()>0)
			url = String.format(Config.getInstance().getString("url.xiaciAll"), _curYear);
		return ResponseBuilder.build(spk, url);
	}

	private String buildSpk(String fy, int size)
	{
		String r = "今年" + fy;
		if(size>0)
			return r + "有" + size + "个瑕疵案件";
		else
			return r + "还没有瑕疵案件";
	}

	private boolean isOfCurYear(Xiaci x)
	{
		return x.getYsah().startsWith("(" + _curYear +")") || x.getYsah().startsWith("（" + _curYear + "）");
	}

	private String getCurrentYear()
	{
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		return String.valueOf(c.get(Calendar.YEAR));
	}
	
	public static void main(String[] args)
	{
	}

}
