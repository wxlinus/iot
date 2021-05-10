package io.iot.common.config;

import com.jacob.activeX.ActiveXComponent;
import org.springframework.context.annotation.Bean;

/**
 * @author xupeng
 * 设备配置
 */
//@Configuration
public class DeviceConfig {

	@Bean
	public ActiveXComponent getActiveXComponent() {
		return new ActiveXComponent("zkemkeeper.ZKEM.1");
	}
	
}
