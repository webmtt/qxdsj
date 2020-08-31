package cma.cimiss2.dpc.indb.storm.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * <b>说明：</b><br/>
 * <li>ini配置文件读取工具类，注释以“#”开头</li>
 * 
 * @author wangzunpeng
 *
 */
public class IniReader {

	protected HashMap<String, Properties> sections = new HashMap<String, Properties>();
	private transient String currentSecion;
	private transient Properties current;

	/**
	 * 构造函数
	 * 
	 * @param is
	 * @throws IOException
	 */
	public IniReader(InputStream is) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		read(reader);
		reader.close();
	}

	/**
	 * 读取文件
	 * 
	 * @param reader
	 * @throws IOException
	 */
	protected void read(BufferedReader reader) throws IOException {
		String line;
		while ((line = reader.readLine()) != null) {
			// 以#为注释符号
			if (line.startsWith(";"))
				continue;
			parseLine(line);
		}
	}

	/**
	 * 解析配置文件行
	 * 
	 * @param line
	 */
	protected void parseLine(String line) {
		line = line.trim();
		if(line.length() > 0){
			char s = line.trim().charAt(0);   
			if(s == 65279){    //65279是空字符   
			  if(line.length() > 1){   
				  line = line.substring(1); 
			  }   
			}
		}
		if (line.matches("\\[.*\\]")) {
			currentSecion = line.replaceFirst("\\[(.*)\\]", "$1");
			current = new Properties();
			sections.put(currentSecion.trim(), current);
		} else if (line.matches(".*=.*")) {
			if (current != null) {
				int i = line.indexOf('=');
				String name = line.substring(0, i).trim();
				String value = line.substring(i + 1).trim();
				current.setProperty(name, value);
			}
		}
	}

	/**
	 * 获取值
	 * 
	 * @param section
	 * @param name
	 * @return
	 */
	public String getValue(String section, String name) {
		Properties p = sections.get(section);
		if (p == null) {
			return null;
		}
		String value = p.getProperty(name);
		return value;
	}

	/**
	 * 是否包含key
	 * 
	 * @param section
	 * @param name
	 * @return
	 */
	public boolean containsKey(String section, String key) {
		Properties p = sections.get(section);
		return p.contains(key);
	}

	/**
	 * 根据section名称获取其下边的内容
	 * 
	 * @param section
	 * @return
	 */
	public Properties get(String section) {
		return sections.get(section);
	}

	/**
	 * 获取ini文件的所有内容
	 * 
	 * @return
	 */
	public Map<String, Properties> get() {
		return sections;
	}
}
