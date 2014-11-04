package org.bnez.xiaoyue.lsfy.rsp;

public abstract class Handler
{

	protected String _combName;
	protected String _combString;

	protected boolean isBigCat(String bigcat)
	{
		String[] ss = _combName.split("_");
		if (ss.length >= 2)
			return ss[1].equals(bigcat);
		return false;
	}

	protected boolean isAsk(String combName)
	{
		return _combName.equals(combName);
	}

	protected boolean isStart(String regx)
	{
		return _combName.startsWith(regx);
	}
}
