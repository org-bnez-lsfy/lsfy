package org.bnez.xiaoyue.lsfy.idcard;

import org.bnez.xiaoyue.lsfy.db.Rule;
import org.bnez.xiaoyue.lsfy.db.RuleRepository;
import org.bnez.xiaoyue.lsfy.db.User;

public class UserRuleChecker
{

	private User _user;

	public UserRuleChecker(User user)
	{
		_user = user;
	}

	public boolean hasRule(String ruleCode)
	{
		if(_user == null)
			return false;
		
		String rs = _user.getRule();
		if(rs == null)
			return false;
		
		Rule rule = RuleRepository.getInstance().queryByCode(ruleCode);
		if(rule == null)
			return false;
		
		String rid = String.valueOf(rule.getId());
		for(String idStr : rs.split(","))
		{
			if(idStr.equals(rid))
				return true;
		}
		return false;
	}

}
