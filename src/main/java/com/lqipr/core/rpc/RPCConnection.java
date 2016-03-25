package com.lqipr.core.rpc;

import java.io.Closeable;

public interface RPCConnection<Client> extends Closeable {

	Client getClient();

	boolean isIdle();

	void setIdle(boolean idle);

}