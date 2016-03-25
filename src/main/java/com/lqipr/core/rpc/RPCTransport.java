package com.lqipr.core.rpc;

import org.apache.thrift.transport.TFastFramedTransport;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

public enum RPCTransport {
	DEFAULT {
        public TTransport get(String ip,int port)
        {
        	return new TSocket(ip,port);
        }
    },
    FRAMED {
    	public TTransport get(String ip,int port)
        {
        	return new TFramedTransport(new TSocket(ip,port));
        }
    },
    FAST {
    	public TTransport get(String ip,int port)
        {
    		return new TFastFramedTransport(new TSocket(ip,port));
        }
    };
	
	public abstract TTransport get(String ip,int port);

}
