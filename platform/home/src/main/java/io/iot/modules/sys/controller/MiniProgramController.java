package io.iot.modules.sys.controller;

import com.alibaba.fastjson.JSONObject;
import io.renren.common.exception.RRException;
import io.renren.common.utils.R;
import io.renren.modules.sys.service.MiniProgramService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @author xupeng
 *
 */
@Slf4j
@Controller
@RequestMapping("/miniProgram")
public class MiniProgramController {
	@Autowired
	private MiniProgramService miniProgramService;
	
	@PostMapping("miniLogin")
	@ResponseBody
	public R miniLogin(@RequestBody Map<String, Object> params) {
		System.err.println(params.toString());
		String jscode = (String)params.get("code");
		String ymlCode = (String)params.get("ymlCode");
		String encryptedData = (String)params.get("encryptedData");
		String iv = (String)params.get("iv");
		if (StringUtils.isBlank(ymlCode)) {
			log.error("clientType为空，未找到对应的小程序配置");
			throw new RRException("小程序异常，请联系管理员");
		}
		JSONObject result = miniProgramService.code2Session(jscode,ymlCode,encryptedData,iv);
		return R.ok().put("data", result);
	}
	
}
