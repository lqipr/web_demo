package com.lqipr.core.rpc.client.impl;


import com.lqipr.core.rpc.client.IRpcClientSettingLoad;
import com.lqipr.core.util.INIConfiguration;

import java.io.IOException;
import java.util.Map;

public class RpcClientFileSettingLoadImpl implements IRpcClientSettingLoad {

	@Override
	public Map<String, String> load(String name) {
		INIConfiguration config=new INIConfiguration();
		try {
			config.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("properties/poolConfig.properties"));
			Map<String,String> map=config.getSection(name);
			return map;
		} catch (IOException e) {
			throw new ExceptionInInitializerError(e);
		}
	}

}
