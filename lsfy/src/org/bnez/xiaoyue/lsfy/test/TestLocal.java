package org.bnez.xiaoyue.lsfy.test;

import org.bnez.nlp.seg.rmi.SegConfig;
import org.bnez.xiaoyue.lsfy.XiaoyueResponse;
import org.bnez.xiaoyue.lsfy.common.Config;
import org.bnez.xiaoyue.lsfy.server.LsfyHandler;

public class TestLocal
{
	static {
		String segip = Config.getInstance().getString("seg.ip", "localhost");
		SegConfig conf = SegConfig.getInstance();
		conf.setIp(segip);
	}
	
	private static void t(String q)
	{
		LsfyHandler h = LsfyHandler.getInstance();
		XiaoyueResponse xr = h.handle(q, "203.152.148.223");
		System.out.println(xr.toString());
		System.out.println("-----------------------------------------------------------------------------------------");
	}

	public static void main(String[] args)
	{
//		t("我院今年的结案率是多少");
//		t("丽中今年的结案率是多少");
//		t("丽水两级法院今年的结案率是多少");
//		t("松阳呢");

		t("本院今年的结案率是多少");
	}
}
