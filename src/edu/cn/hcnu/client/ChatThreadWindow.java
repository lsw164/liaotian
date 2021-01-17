package edu.cn.hcnu.client;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.*;
import java.sql.*;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


/**
 * 聊天线程
 */
public class ChatThreadWindow {
    private String name;
    JComboBox cb;//组合框
    private JFrame f;
    JTextArea ta;
    private JTextField tf;
    private static int total;// 在线人数统计
    DatagramSocket ds;

    public ChatThreadWindow(String name, DatagramSocket ds) {
        this.ds = ds;
        this.name=name;
        f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(600, 400);
        f.setTitle("聊天室" + " - " + name + "     当前在线人数:" + ++total);
        f.setLocation(300, 200);
        ta = new JTextArea();
        JScrollPane sp = new JScrollPane(ta);
        ta.setEditable(false);
        tf = new JTextField();
        tf.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {//按下键
        if(e.getKeyCode()==KeyEvent.VK_ENTER){
          String m= (String) cb.getSelectedItem();
          if("All".equals(m)){
              //群聊
             sendAll();
          }else{
              //私聊
              sends();
          }
        }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        cb = new JComboBox();
        cb.addItem("All");
        JButton jb = new JButton("发送文件");
        JPanel pl = new JPanel(new BorderLayout());
        pl.add(cb);
        pl.add(jb, BorderLayout.WEST);
        JPanel p = new JPanel(new BorderLayout());
        p.add(pl, BorderLayout.WEST);
        p.add(tf);
        f.getContentPane().add(p, BorderLayout.SOUTH);
        f.getContentPane().add(sp);
        f.setVisible(true);
        GetMessageThread getMessageThread = new GetMessageThread(this);
        getMessageThread.start();
        showAll();
        /*
        提示XXX进入聊天室
         */
        showXXXIntoChatRoom();
      sends();
    }
    public void sends() {   //群聊的实现
        String url = "jdbc:oracle:thin:@localhost:1521:orcl";
        String username_db = "ls164";
        String password_db = "12306";
        PreparedStatement pstmt = null;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, username_db, password_db);
            String sql = "SELECT username,ip,port FROM users WHERE status='online' ";
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String username = rs.getString("USERNAME");
                String ip = rs.getString("IP");
                int port = rs.getInt("PORT");
                System.out.println(ip);
                System.out.println(port);
                byte[] ipB = new byte[4];

                String ips[] = ip.split("\\.");
                for (int i = 0; i < ips.length; i++) {
                    ipB[i] = (byte) Integer.parseInt(ips[i]);
                }
                if (!username.equals(name)) {//name为第一次登录的名字
                    String message =tf.getText();
                    byte[] m = message.getBytes();
                    DatagramPacket dp = new DatagramPacket(m, m.length);
                    dp.setAddress(InetAddress.getByAddress(ipB));
                    dp.setPort(port);
                    DatagramSocket ds = new DatagramSocket();
                    ds.send(dp);//投递
                }
            }
        } catch (SQLException | SocketException | UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sendAll() {   //群聊的实现
        String url = "jdbc:oracle:thin:@localhost:1521:orcl";
        String username_db = "ls164";
        String password_db = "12306";
        PreparedStatement pstmt = null;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, username_db, password_db);
            String sql = "SELECT username,ip,port FROM users WHERE status='online' ";
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String username = rs.getString("USERNAME");
                String ip = rs.getString("IP");
                int port = rs.getInt("PORT");
                System.out.println(ip);
                System.out.println(port);
                byte[] ipB = new byte[4];

                String ips[] = ip.split("\\.");
                for (int i = 0; i < ips.length; i++) {
                    ipB[i] = (byte) Integer.parseInt(ips[i]);
                }
                if (!username.equals(name)) {//name为第一次登录的名字
                    String message =tf.getText();
                    byte[] m = message.getBytes();
                    DatagramPacket dp = new DatagramPacket(m, m.length);
                    dp.setAddress(InetAddress.getByAddress(ipB));
                    dp.setPort(port);
                    DatagramSocket ds = new DatagramSocket();
                    ds.send(dp);//投递
                }
            }
        } catch (SQLException | SocketException | UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showAll() {
        String url = "jdbc:oracle:thin:@localhost:1521:orcl";
        String username_db = "ls164";
        String password_db = "12306";
        PreparedStatement pstmt = null;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, username_db, password_db);
            String sql = "SELECT username,ip,port FROM users WHERE status='online' ";
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String username = rs.getString("USERNAME");
                String ip = rs.getString("IP");
                int port = rs.getInt("PORT");
                System.out.println(ip);
                System.out.println(port);
                byte[] ipB = new byte[4];

                String ips[] = ip.split("\\.");
                for (int i = 0; i < ips.length; i++) {
                    ipB[i] = (byte) Integer.parseInt(ips[i]);
                }
                if (!username.equals(name)) {//name为第一次登录的名字
                    String message = username + "正在聊天室"+"\n";
                   ta.append(message);
                   cb.addItem(username);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void showXXXIntoChatRoom() {
        String url = "jdbc:oracle:thin:@localhost:1521:orcl";
        String username_db = "ls164";
        String password_db = "12306";
        PreparedStatement pstmt = null;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, username_db, password_db);
            String sql = "SELECT username,ip,port FROM users WHERE status='online' AND username!=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,name);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String username = rs.getString("USERNAME");
                String ip = rs.getString("IP");
                int port = rs.getInt("PORT");
                System.out.println(ip);
                System.out.println(port);
                byte[] ipB = new byte[4];

                String ips[] = ip.split("\\.");
                for (int i = 0; i < ips.length; i++) {
                    ipB[i] = (byte) Integer.parseInt(ips[i]);
                }
                if (!username.equals(name)) {
                    String message = name + "进入了聊天室"+"\n";
                    total++;
                    byte[] m = message.getBytes();
                    DatagramPacket dp = new DatagramPacket(m, m.length);
                    dp.setAddress(InetAddress.getByAddress(ipB));
                    dp.setPort(port);
                    DatagramSocket ds = new DatagramSocket();
                    ds.send(dp);//投递
                }
            }
        } catch (SQLException | UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}