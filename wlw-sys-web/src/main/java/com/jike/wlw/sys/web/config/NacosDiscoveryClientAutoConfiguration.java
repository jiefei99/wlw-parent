package com.jike.wlw.sys.web.config;
import com.alibaba.cloud.nacos.ConditionalOnNacosDiscoveryEnabled;
//import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.cloud.nacos.discovery.NacosWatch;
import com.alibaba.nacos.common.utils.StringUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.CommonsClientAutoConfiguration;
import org.springframework.cloud.client.discovery.simple.SimpleDiscoveryClientAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * nacos客户端注册至服务端时，更改服务详情中的元数据
 */
@Configuration
@ConditionalOnNacosDiscoveryEnabled
@AutoConfigureBefore({SimpleDiscoveryClientAutoConfiguration.class, CommonsClientAutoConfiguration.class})
public class NacosDiscoveryClientAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(value = {"spring.cloud.nacos.discovery.watch.enabled"}, matchIfMissing = true)
	public NacosWatch nacosWatch(NacosServiceManager nacosServiceManager, NacosDiscoveryProperties nacosDiscoveryProperties,  ObjectProvider<ThreadPoolTaskScheduler> taskScheduler) {
		String devVersion = System.getProperty("dev.version");
		if (StringUtils.isNotBlank(devVersion)) {
			nacosDiscoveryProperties.getMetadata().put("VERSION", devVersion);
		}
		else {
			try {
				String localIP = InetAddress.getLocalHost().getHostAddress();
				nacosDiscoveryProperties.getMetadata().put("VERSION", localIP);
			} catch (UnknownHostException e) {
			}
		}
		return new NacosWatch(nacosServiceManager, nacosDiscoveryProperties, taskScheduler);
	}
}
