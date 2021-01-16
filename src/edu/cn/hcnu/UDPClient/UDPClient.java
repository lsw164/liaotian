package edu.cn.hcnu.UDPClient;

import java.io.IOException;
import java.net.*;

public class UDPClient {//接收
    public static void main(String[] args) throws IOException {
        String message="新年快乐me";
        byte[] m=message.getBytes();
        DatagramPacket dp=new DatagramPacket(m,m.length);
        dp.setAddress(InetAddress.getByName("jxxy53"));//计算机名
        dp.setPort(1690);
        DatagramSocket ds=new DatagramSocket();
        ds.send(dp);
    }
}
