package cma.cimiss2.dpc.indb.storm.tools;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.storm.task.TopologyContext;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import io.latent.storm.rabbitmq.Message;
import io.latent.storm.rabbitmq.MessageScheme;

public class BinaryMessageScheme2 implements MessageScheme{

	 /**   
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)   
	 */ 
	private static final long serialVersionUID = 1L;

	public List<Object> deserialize(byte[] ser) {
		String str = "";
		try {
			str = new String(ser, "ISO-8859-1");
			List<Object> list = new ArrayList<Object>();
			list.add(str);
			return list;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	public List<Object> deserialize(Message message) {
		List<Object> list = this.deserialize(message.getBody());
		Map<String, Object> map = (Map<String, Object>)((Message.DeliveredMessage) message).getHeaders();
		String FileName = map.get("FileName").toString();
		String SendTime = map.get("SendTime").toString();
		String CCCC = map.get("CCCC").toString();
//		return list;
		return new Values("scheme", list.get(0).toString(), FileName, SendTime, CCCC);
	}

	@Override
	public Fields getOutputFields() {
		return new Fields("key","message", "FileName", "SendTime","CCCC");
	}

	@Override
	public void open(Map config, TopologyContext context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Object> deserialize(ByteBuffer ser) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
