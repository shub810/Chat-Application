import java.awt.*;
import java.util.*;
import java.net.*;
import javax.swing.*;
import java.io.*;
import java.awt.event.*;
import static java.lang.System.out;

public class  ChatClient extends JFrame implements ActionListener {
    PrintWriter printw;
    String pass;
    JButton bExit;
    Socket client;
    BufferedReader buffR;
    String uname;
    JTextArea  Msg;
    JTextField jtfIn;
    
    public ChatClient(String uname,String servername) throws Exception {
        super(uname);
        this.uname = uname;
        client  = new Socket(servername,1234);
        buffR = new BufferedReader( new InputStreamReader( client.getInputStream()) ) ;
        printw = new PrintWriter(client.getOutputStream(),true);
        printw.println(uname);
        buildInterface();
        new MessagesThread().start();
    }
    
    public void buildInterface() {
        
        bExit = new JButton("Exit Chat");
        Msg = new JTextArea();
        Msg.setRows(10);
        Msg.setColumns(50);
        Msg.setEditable(false);
        jtfIn  = new JTextField(50);
        JScrollPane sp = new JScrollPane(Msg, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(sp,"Center");
        JPanel bp = new JPanel( new FlowLayout());
        bp.add(jtfIn);
        bp.add(bExit);
        add(bp,"South");
        jtfIn.addActionListener(this);
        jtfIn.setText("");
        bExit.addActionListener(this);
        setSize(500,300);
        setVisible(true);
        pack();
    }
    
    public void actionPerformed(ActionEvent evt) {
        if ( evt.getSource() == bExit ) {
            printw.println("END");  
            System.exit(0);
        } else {
            printw.println(jtfIn.getText());
            jtfIn.setText("");
        }
    }
    
    public static void main(String ... args) {
        
        String name = JOptionPane.showInputDialog(null,"Your USERNAME : ", "New User",
             JOptionPane.PLAIN_MESSAGE);
        String pas = JOptionPane.showInputDialog(null,"Common Password : ", "USERNAME : "+name, JOptionPane.PLAIN_MESSAGE);
         
        while (!pas.matches("abcd123")) {
            pas = JOptionPane.showInputDialog(null,"                      Wrong Password"+"\n"+"Common Password Again : ", name, JOptionPane.PLAIN_MESSAGE);
        }
        String servername = "localhost";  
        try {
            new ChatClient( name ,servername);
        } 
        catch(Exception exce) {
            out.println( "<---Error---> " + exce.getMessage());
        }
        
    }
    class  MessagesThread extends Thread {
        public void run() {
            String line;
            try {
                while(true) {
                    line = buffR.readLine();
                    Msg.append(line + "\n");
                } 
            } catch(Exception exce) {}
        }
    }
} 