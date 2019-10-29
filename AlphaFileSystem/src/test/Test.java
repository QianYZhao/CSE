package test;

import client.AFSClient;

import java.util.ArrayList;

public class Test {
    public void test_alpha_new() {
        String filename = "test";
        AFSClient.alpha_cat(filename);
    }

    public void test_alpha_write(){
        String str = "No one can reach out to others in the same way that you can. No one can speak your words. No one can convey your meanings. No one can comfort others with your kind of comfort. No one can bring your kind of understanding to another person. No one can be cheerful and light-hearted3 and joyous4 in your way. No one can smile your smile. No one else can bring the whole unique impact of you to another human being.";
        ArrayList<Byte> bytes = new ArrayList<>();
        for (int i = 0; i < str.getBytes().length; i++) {
            bytes.add(str.getBytes()[i]);
        }
        byte[] b = new byte[bytes.size()];
        for (int i = 0; i < bytes.size(); i++) {
            b[i] = bytes.get(i);
        }

        AFSClient.alpha_write("bt",b,0,0);
        AFSClient.alpha_cat("bt");
    }

    public void test_alpha_hex(){
        String bmId = "bm-2";
        int blockId = 1;
        AFSClient.alpha_hex(bmId,blockId);
    }

    public void test_alpha_copy(){
        String copiedName = "hello";
        String copyname = "copyhello";

        AFSClient.alpha_copy(copiedName,copyname);
        AFSClient.alpha_cat("copyhello");
    }
}
