package com.verifalia.api.test.utils;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by yar 09.09.2009
 */
public class MiniHTTPD implements Runnable {
	private String message;
	private int port;
	private Thread thread = new Thread(this);
	private boolean running;
	
	public MiniHTTPD(int port, String message) {
		this.port = port;
		this.message = message;
	}
	
	public synchronized void start() {
		running = true;
		thread.start();
	}
	
	public synchronized void stop() {
		running = false;
	}
	
	public synchronized boolean isRunning() {
		return running;
	}

    public void  run() {
        ServerSocket ss = null;
		try {
			ss = new ServerSocket(port);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
        while (isRunning()) {
            Socket s = null;
			try {
				s = ss.accept();
			} catch (IOException e) {
				e.printStackTrace();
			}
            System.out.println("Client accepted.");
            try {
				new Thread(new SocketProcessor(s)).start();
			} catch (Throwable e) {
				e.printStackTrace();
			}
        }
    }


	private class SocketProcessor implements Runnable {

        private Socket s;

        private SocketProcessor(Socket s) throws Throwable {
            this.s = s;
        }

        public void run() {
            try {
                readInputHeaders();
                writeResponse(message);
            } catch (Throwable t) {
                t.printStackTrace();
            } finally {
                try {
                    s.close();
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
            System.out.println("Client processing finished.");
        }

        private void writeResponse(String text) throws Throwable {
            String response = "HTTP/1.1 200 OK\r\n" +
                    "Server: YarServer/2009-09-09\r\n" +
                    "Content-Type: application/json\r\n" +
                    "Content-Length: " + text.length() + "\r\n" +
                    "Connection: close\r\n\r\n" 
                    + text;
            OutputStream os = s.getOutputStream();
            os.write(response.getBytes("UTF-8"));
            os.flush();
        }

        private void readInputHeaders() throws Throwable {
            InputStream is = s.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
	            while(true) {
	                String s = br.readLine();
	                if(s == null || s.trim().length() == 0) {
	                    break;
	                }
	                else System.out.println("Header: " + s);
	            }
        }
    }
}
