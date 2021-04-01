package world.laf;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class ProxyScannerTask extends Thread {
	
	private static int DELAY = 60000 * 5; // 5 minutos
	private static String CONFIG = "./config.txt";
	
	public void run() {
		while (true) {
			try {
				System.gc();
				System.out.println("Processing...");
				
				this.processConfig();
				
				Thread.sleep(ProxyScannerTask.DELAY);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void processConfig() throws Exception {
		try (Stream<String> stream = Files.lines(Paths.get(ProxyScannerTask.CONFIG))) {
	        stream.forEach(url -> processUrl(url));
		}
	}
	
	private void processUrl(String url) {
		url = url.trim();
		if ((url.isEmpty())) return;
		
		ProxyWorkerTask worker = new ProxyWorkerTask(url);
		worker.start();
	}

}
