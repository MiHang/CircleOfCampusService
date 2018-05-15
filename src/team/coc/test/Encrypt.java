package team.coc.test;


import rasencrypt.encrypt.RSAEncrypt;
import rasencrypt.util.RSAEncryptBuilder;

import java.util.Map;

public class Encrypt {
    public static void main(String[]args){

        Map<String, String> map = RSAEncryptBuilder.createRSAKey();
        RSAEncrypt rsaEncrypt = new RSAEncrypt();
        String eHexStr = rsaEncrypt.encrypt("Hello World!");
        System.out.println("eHexStr = " + eHexStr);
        String dStr = rsaEncrypt.decrypt(eHexStr);
        System.out.println("dStr = " + dStr);

    }
}
