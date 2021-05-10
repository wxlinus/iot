package io.iot.modules.sys.shiro;

import lombok.Data;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author xupeng
 *
 */
@Data
public class WeChatToken implements AuthenticationToken {

	private static final long serialVersionUID = 6157637706638495990L;
	private String wxUserId;
	@Override
	public Object getPrincipal() {
		return wxUserId;
	}

	
	@Override
	public Object getCredentials() {
		return null;
	}


	/**
	 * @param openId
	 */
	public WeChatToken(String wxUserId) {
		super();
		this.wxUserId = wxUserId;
	}
	
	

}
