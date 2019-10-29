package client;

import constant.FileConstant;
import main.AFS;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import test.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AFSClient {
    public static void main(String[] args) {
        AFS.getInstance().restore();
//        Test test = new Test();
////        test.test_alpha_new();
////        test.test_alpha_copy();
////        test.test_alpha_hex();
//        test.test_alpha_write();
//        String str = "You are unique!";
//        ArrayList<Byte> bytes = new ArrayList<>();
//        for (int i = 0; i < str.getBytes().length; i++) {
//            bytes.add(str.getBytes()[i]);
//        }
//        byte[] b = new byte[bytes.size()];
//        for (int i = 0; i < bytes.size(); i++) {
//            b[i] = bytes.get(i);
//        }
//
//        alpha_write("hello",b,-10,2);
//        alpha_cat("hello");

        System.out.println("请输入指令");
        Scanner in = new Scanner(System.in);

        String instr;
        while (in.hasNext()) {
            instr = in.next();
            if (instr.equals(FileConstant.alpha_new)) {
                System.out.println("输入文件名");
                instr = in.next();
                alpha_new(instr);
            }

            if (instr.equals(FileConstant.alpha_cat)) {
                System.out.println("输入要读取的文件名");
                instr = in.next();;
                alpha_cat(instr);
            }

            if (instr.equals(FileConstant.alpha_write)) {
                System.out.println("输入要写的文件名");
                instr = in.next();
                System.out.println("输入要写的文件内容");

                ArrayList<Byte> bytes = new ArrayList<>();

                String str;
                while (in.hasNextLine()) {
                    str = in.nextLine();

                    if (str.equals("000")) break;
                    for (int i = 0; i < str.getBytes().length; i++) {
                        bytes.add(str.getBytes()[i]);
                    }

                    bytes.add((byte)'\n');
                }


                byte[] b = new byte[bytes.size()];
                for (int i = 0; i < bytes.size(); i++) {
                    b[i] = bytes.get(i);
                }
                alpha_write(instr, b,0,0);
            }

            if (instr.equals(FileConstant.alpha_hex)) {
                System.out.println("输入BlockManagerId");
                String bmId = in.next();
                System.out.println("输入blockId");
                int blockId = in.nextInt();
                alpha_hex(bmId,blockId);
            }

            if (instr.equals(FileConstant.alpha_copy)) {
                System.out.println("输入要复制的文件名");
                String copied = in.next();
                System.out.println("输入复制到的文件名");
                String copy = in.next();
                alpha_copy(copied, copy);
            }
            System.out.println("请输入指令");
        }
    }

    public static void alpha_new(String filename) {
        AFS.getInstance().createFile(filename);
        AFS.getInstance().store();
    }

    public static String alpha_cat(String filename) {
        byte[] b = AFS.getInstance().openFile(filename);
        String str = new String(b);
        System.out.println(str);
        return str;
    }

    public static void alpha_file_hex(String filename) {
        byte[] b = AFS.getInstance().openFile(filename);
        String tmp = null;
        for (int i = 0; i < b.length; i++) {
            tmp = Integer.toHexString(b[i] & 0xFF);
            if (tmp.length() == 1) {
                tmp = "0" + tmp;
            }
            System.out.print(tmp + " ");
            if ((i + 1) % 16 == 0) System.out.println();
        }
    }

    public static void alpha_hex(String bmId, int blockId) {
        byte[] b = AFS.getInstance().getBlockData(bmId, blockId);
        String tmp = null;
        for (int i = 0; i < b.length; i++) {
            tmp = Integer.toHexString(b[i] & 0xFF);
            if (tmp.length() == 1) {
                tmp = "0" + tmp;
            }
            System.out.print(tmp + " ");
            if ((i + 1) % 16 == 0) System.out.println();
        }
    }

    public static void alpha_show_files() {
    }

    public static void alpha_write(String filename, byte[] b,long offset, int where) {
        AFS.getInstance().move(filename, offset, where);
        AFS.getInstance().writeFile(filename, b);
        AFS.getInstance().store();
    }

    public static void alpha_copy(String copied, String copy) {
        AFS.getInstance().copyFile(copied, copy);
        AFS.getInstance().store();
    }


    public static void system_init() {
        AFS.getInstance().system_init();
    }

    public static void alpha_set_iFile_size(String filename,long newSize){
        AFS.getInstance().setIFileSize(filename,newSize);
        AFS.getInstance().store();
    }

}