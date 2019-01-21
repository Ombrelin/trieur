package fr.thetrieur.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class Logger {
	private static Logger INSTANCE;
	private static String LOGS_FILE_NAME = "logs.txt";
	private PrintWriter writer;
	
	private Logger() {
		try {
			writer = new PrintWriter(new FileWriter(LOGS_FILE_NAME),true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Logger getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new Logger();
		}
		return INSTANCE;
	}
	
	public void log(String message) {
		String output = new Date() + " : " + message;
		writer.println(output);
		System.out.println(output);
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		writer.close();
	}
	
	
}
