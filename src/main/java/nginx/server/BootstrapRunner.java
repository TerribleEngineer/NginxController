package nginx.server;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

public class BootstrapRunner implements Runnable {

	Logger log = Logger.getLogger(getClass());
	
	public BootstrapRunner(){
		
	}
	
	@Override
	public void run() {
		log.debug("Starting bootstrap process...");
		ProcessBuilder pb = new ProcessBuilder("/bin/bash", "./bootstrap.sh");
		pb.directory(new File("/"));
		
		try {
			Process proc = pb.start();
			String errors = IOUtils.toString(proc.getErrorStream());
			String input = IOUtils.toString(proc.getInputStream());
			
			if(input != null && input.length() != 0){
				log.debug("Input Capture:");
				log.debug(input);
				log.debug("End Input Capture.");
			}
			
			if(errors != null && errors.length() != 0){
				log.error("Error Capture:");
				log.error(errors);
				log.error("End Error Capture");
			}
			
		} catch (IOException e) {
			log.error("error executing the bootstrap process", e);
		}
	}

}
