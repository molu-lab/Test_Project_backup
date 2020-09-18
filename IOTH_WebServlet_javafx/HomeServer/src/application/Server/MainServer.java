package application.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

import application.MainUnit;
import application.Message.MessageAnalyzer;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class MainServer {

	private int port=1025;
	public boolean serverOn=false;
	//private SSLServerSocketFactory sslserversocketfactory;
	//private SSLServerSocket server;
	private ServerSocket server;
	
	public Thread forServer=null;

	
	public MainServer(int port) {
		this.port=port;

		forServer=new Thread(new Server());
		
		//sslserversocketfactory= (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
	}
	
	public void start() {
		if(!forServer.isAlive()&&!serverOn)
		{	
			try {
				server=new ServerSocket(this.port);
				
			//	server=(SSLServerSocket) sslserversocketfactory.createServerSocket(this.port);
				
				serverOn=true;	
				forServer=new Thread(new Server());	
				forServer.start();
			} catch (IOException e) {
				// 서버객체생성실패
				e.printStackTrace();
			}

			
		}else
		{
			return;
		}
	}
	public void stop() {
		if(forServer.isAlive()||serverOn)
		{
			//잘 진행중이던 서버를 멈출거야
			serverOn=false;
			try {
			
		
				if(!server.isClosed())server.close();				
				if(!ClientsList.list.isEmpty())ClientsList.AllClose();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			forServer.stop();
		}else
		{
			return ;
		}
	}
	
	
	
	
	
	
	class Server implements Runnable{
		public void run() {
			MainUnit.MainMsg.setText(MainUnit.MainMsg.getText().concat("서버활성화성공\n"));
			try {
				while(serverOn) {					
				final Socket socket = (Socket)server.accept();
				if(socket!=null)
				{
					SocketClient sss=new SocketClient(socket);
					sss.uid=UUID.randomUUID().toString();
					ClientsList.list.add(sss);	
				
				
					MainUnit.MainMsg("접속자확인:"+sss.uid);
			//		MainUnit.MainMsg("현재인원:"+ClientsList.list.size());
				}
				
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			} catch (IOException e) {
				// 서버 생성의 실패 시 
				// 서버 연결대기 실패 시
				
				e.printStackTrace();
			}
		}
	}
}
