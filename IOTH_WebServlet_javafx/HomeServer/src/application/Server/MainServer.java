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
				// ������ü��������
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
			//�� �������̴� ������ ����ž�
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
			MainUnit.MainMsg.setText(MainUnit.MainMsg.getText().concat("����Ȱ��ȭ����\n"));
			try {
				while(serverOn) {					
				final Socket socket = (Socket)server.accept();
				if(socket!=null)
				{
					SocketClient sss=new SocketClient(socket);
					sss.uid=UUID.randomUUID().toString();
					ClientsList.list.add(sss);	
				
				
					MainUnit.MainMsg("������Ȯ��:"+sss.uid);
			//		MainUnit.MainMsg("�����ο�:"+ClientsList.list.size());
				}
				
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			} catch (IOException e) {
				// ���� ������ ���� �� 
				// ���� ������ ���� ��
				
				e.printStackTrace();
			}
		}
	}
}