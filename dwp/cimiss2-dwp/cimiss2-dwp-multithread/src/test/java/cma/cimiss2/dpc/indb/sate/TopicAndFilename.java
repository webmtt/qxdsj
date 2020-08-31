package cma.cimiss2.dpc.indb.sate;

public class TopicAndFilename {

	private String topic;
	private String filename;
	private String filePath;
	
	public TopicAndFilename() {
		super();
	}

	public TopicAndFilename(String topic, String filename, String filePath) {
		super();
		this.topic = topic;
		this.filename = filename;
		this.filePath = filePath;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}