import java.net.*;
import java.io.*;
import static java.lang.System.out;
import java.util.*;

public class  ChatServer {
  Vector<String> username = new Vector<String>();
  Vector<HandleClient> user_clients = new Vector<HandleClient>();

  public void process() throws Exception  {
      ServerSocket server = new ServerSocket(1234,10);
      out.println("!!.....SERVER STARTED.....!!");
      while( true) {
         Socket client = server.accept();
         HandleClient c = new HandleClient(client);
         user_clients.add(c);
     }
  }
  public static void main(String ... args) throws Exception {
      new ChatServer().process();
  } 

  public void boradcast(String username, String message)  {
	
        for ( HandleClient c : user_clients )
           if ( ! c.getUserName().equals(username) )
              c.sendMessage(username,message);
    }

  class  HandleClient extends Thread {
        String name_client = "";
	BufferedReader in;
	PrintWriter outp;

	public HandleClient(Socket  client) throws Exception {
        
             in = new BufferedReader( new InputStreamReader( client.getInputStream())) ;
             outp = new PrintWriter ( client.getOutputStream(),true);

             name_client  = in.readLine();
             username.add(name_client); 
             start();
        }

        public void sendMessage(String uname,String  msg)  {
	    outp.println( uname + " :-> " + msg);
	}
		
        public String getUserName() {  
            return name_client; 
        }
        public void run()  {
    	     String line;
	     try    {
                while(true)   {
		 line = in.readLine();
		 if ( line.equals("......END......") ) {
                       user_clients.remove(this);
                       username.remove(name_client);
                       break;
                 }
		 boradcast(name_client,line); 
	       } 
	     } 
	     catch(Exception exce) {
	       System.out.println(exce.getMessage());
	     }
        } 
   } 
}