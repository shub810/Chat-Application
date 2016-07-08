import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class Ser extends JFrame implements ActionListener {
     static ServerSocket server;
     static Socket connection;
     JPanel pan;
     JTextField NMessage;
     JTextArea ChatHis;
     JButton Sendto;
     DataInputStream dis;
     DataOutputStream dos;

 public Ser() throws UnknownHostException, IOException {

      pan = new JPanel();
      NMessage = new JTextField();
      ChatHis = new JTextArea();
      Sendto = new JButton("ENTER");

      this.setSize(515, 515);
      this.setVisible(true);
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      pan.setLayout(null);
      
      this.add(pan);
      ChatHis.setBounds(20, 20, 450, 360);
      pan.add(ChatHis);
      NMessage.setBounds(120, 400, 350, 30);
      pan.add(NMessage);
      
      Sendto.setBounds(20, 400, 90, 30);
      pan.add(Sendto);
      this.setTitle("Server");
      Sendto.addActionListener(this);
      server = new ServerSocket(2000, 1, InetAddress.getLocalHost());
      ChatHis.setText("!!....Wait for Client to accept....!!");
      
      connection = server.accept();
      ChatHis.setText(ChatHis.getText() + "\n" + "Client Found for Connection.");
      
      while (true) {
           try {
                DataInputStream dis = new DataInputStream(connection.getInputStream());
                String string = dis.readUTF();
                ChatHis.setText(ChatHis.getText() + "\n" + "CLIENT : " + string);
           }
           catch (Exception e1) {
                ChatHis.setText(ChatHis.getText() + "\n"
                  + "!!SENDING FAILED : Network Error");
                try {
                     Thread.sleep(3000);
                     System.exit(0);
                } 
                catch (InterruptedException e) {

                    e.printStackTrace();
                }
            }
        }
    }

 public void actionPerformed(ActionEvent e) {
  
  if ((e.getSource() == Sendto) && (NMessage.getText() != "")) {
        ChatHis.setText(ChatHis.getText() + "\n" + "YOU : " + NMessage.getText());
       try {
            DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
            dos.writeUTF(NMessage.getText());
       } 
       catch (Exception ex) {
            try {
                 Thread.sleep(3000);
                 System.exit(0);
            }
            catch (InterruptedException ex1) {
                  ex1.printStackTrace();
            }
       }
       NMessage.setText("");
      }
 }
     public static void main(String[] args) throws UnknownHostException,IOException {
        new Ser();
     }
}