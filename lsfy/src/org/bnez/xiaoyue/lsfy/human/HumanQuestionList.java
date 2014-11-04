package org.bnez.xiaoyue.lsfy.human;

import java.util.ArrayList;
import java.util.List;

import org.bnez.xiaoyue.lsfy.human.inter.HumanQuestion;

public class HumanQuestionList
{
	private static HumanQuestionList _instance = null;
	private List<HumanQuestion> _list;

	public static synchronized HumanQuestionList getInstance()
	{
		if(_instance == null)
			_instance = new HumanQuestionList();
		return _instance;
	}
	
	private HumanQuestionList()
	{
		_list = new ArrayList<HumanQuestion>();
	}

	public synchronized void add(String ip, String question)
	{
		_list.add(new HumanQuestion(ip, question));
	}
	
	public synchronized HumanQuestion pick()
	{
		if(_list.size() == 0)
			return null;
		return _list.remove(0);
	}
}
