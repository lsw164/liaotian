package edu.cn.hcnu.client;

public class Client{
    public static void main(String[] args) {
        Thread login = new LoginThread();
        login.start();
    }

}