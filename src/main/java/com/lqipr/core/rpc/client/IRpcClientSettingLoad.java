package com.lqipr.core.rpc.client;

import java.util.Map;

/**
 * 
 * @ClassName: IRpcClientSettingLoad
 * @Description: 加载RPC的客户端
 * @author wangdong
 * @date 2015年7月15日 下午5:25:17
 *
 */
public interface IRpcClientSettingLoad {
	Map<String, String> load(String name);
}
