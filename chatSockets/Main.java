 

import javax.swing.JOptionPane;

public class Main {
    
    public static void main(String[] args) {
        String ip = (String)JOptionPane.showInputDialog("Informe o IP", "192.168.0.");
        int port = Integer.parseInt(JOptionPane.showInputDialog("Informe a Porta", "8080"));
        
        Connection c = new Connection(ip, port);
        
        ChatWindow chatWindow = new ChatWindow(c);
        chatWindow.setLocationRelativeTo(null);
        chatWindow.setVisible(true);
    }
}