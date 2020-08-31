package cma.cimiss2.dpc.indb.storm.tools;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.List;

import org.apache.storm.spout.Scheme;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

/**
 * Created by root on 2017/06/16.
 */
public class BinaryMessageScheme  implements Scheme {
	
    public List<Object> deserialize(byte[] bytes) {
        try {
            String msg = new String(bytes,"ISO-8859-1");
            String msg_0 = "scheme";
            return new Values(msg_0,msg);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Fields getOutputFields() {
        return new Fields("key","message");
    }

	@Override
	public List<Object> deserialize(ByteBuffer ser) {
		// TODO Auto-generated method stub
		return null;
	}

}
