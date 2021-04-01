package world.laf;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;

public class ProxyWorkerTask extends Thread {
	
	private static final Pattern IP_PORT = Pattern.compile("(\\d+\\.\\d+\\.\\d+\\.\\d+)");
	private String url;
	
	protected ProxyWorkerTask(String url) {
		this.url = url;
	}
	
	@Override
	public void run() {
		try {
			// Carregar URL
			Connection conn = Jsoup.connect(this.url);
			Document doc = conn.get();
			String content = doc.body().text();
			
			// Processar lista
			this.process( content);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void process(String content) throws Exception {
		Matcher m = ProxyWorkerTask.IP_PORT.matcher(content);
		while (m.find()) this.block(m.group());
	}
	
	private void block(String address) throws Exception {
		String ruleName = "Block " + address;
		
		Process delete = Runtime.getRuntime().exec("cmd /c netsh advfirewall firewall delete rule name=\"" + ruleName + "\""); 
		delete.waitFor();
		
		Process block = Runtime.getRuntime().exec("cmd /c netsh advfirewall firewall add rule name=\"" + ruleName + "\" dir=in protocol=any interface=any action=block remoteip=" + address); 
		block.waitFor();
	}

}
