package com.lqipr.core.rpc;

import org.apache.thrift.protocol.*;
import org.apache.thrift.transport.TTransport;

public enum RPCProtocol {
	DEFAULT {
        public TProtocol get(TTransport trans)
        {
        	return new TBinaryProtocol(trans);
        }
    },
    COMPACT {
    	public TProtocol get(TTransport trans)
        {
        	return new TCompactProtocol(trans);
        }
    },
    TUPLE {
    	public TProtocol get(TTransport trans)
        {
        	return new TTupleProtocol(trans);
        }
    },
    JSON {
    	public TProtocol get(TTransport trans)
        {
        	return new TJSONProtocol(trans);
        }
    };
	
	public abstract TProtocol get(TTransport trans);

}
