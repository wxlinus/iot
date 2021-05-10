package io.iot.modules.sys.controller;

import com.alibaba.fastjson.JSON;
import io.renren.common.components.WebSocketMessage;
import io.renren.common.components.WebSocketServer;
import io.renren.common.utils.R;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xupeng
 *
 */
@Controller
@RequestMapping("socketTest")
public class WebSocketController {

	//页面请求
	@ResponseBody
	@GetMapping("/socket/{cid}")
	public R socket(@PathVariable String cid) {
		ModelAndView mav=new ModelAndView("/socket");
		mav.addObject("cid", cid);
		return R.ok();
	}
	
	@ResponseBody
	@RequestMapping("/socket/push")
	public R pushToWeb(String message) {
		try {
			List<String> userIds = new ArrayList<>();
//			userIds.add("1");
			userIds.add("2");
			WebSocketMessage webSocketMessage = JSON.parseObject(message, WebSocketMessage.class);
			WebSocketServer.sendInfo(webSocketMessage,null);
		} catch (IOException e) {
			e.printStackTrace();
			return R.error("#"+e.getMessage());
		}  
		return R.ok();
	} 
}
