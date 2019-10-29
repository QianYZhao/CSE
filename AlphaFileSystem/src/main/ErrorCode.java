package main;

import java.util.HashMap;
import java.util.Map;

public class ErrorCode extends RuntimeException {
    public static final int IO_EXCEPTION = 1;
    public static final int CHECKSUM_CHECK_FAILED = 2;
    public static final int IFILE_NOT_EXISTED = 3;
    public static final int IFILE_EXISTED = 4;
    public static final int CURSOR_MOVE_ILLEGAL = 5;

    private static final Map<Integer,String>ErrorCodeMap = new HashMap<>();

    static {
        ErrorCodeMap.put(IO_EXCEPTION,"IO exception");
        ErrorCodeMap.put(CHECKSUM_CHECK_FAILED,"block checksum check fail");
        ErrorCodeMap.put(IFILE_NOT_EXISTED,"IFile not existed");
        ErrorCodeMap.put(IFILE_EXISTED,"IFile has existed");
        ErrorCodeMap.put(CURSOR_MOVE_ILLEGAL,"Cursor move illegal");
    }

    public static String getErrorText(int errorCode){
        return ErrorCodeMap.getOrDefault(errorCode,"invaild");
    }

    private int errorCode;

    public ErrorCode(int errorCode){
        super(String.format("error code '%d' \"%s\"",errorCode,getErrorText(errorCode)));
        this.errorCode = errorCode;
    }

    public int getErrorCode(){
        return errorCode;
    }
}

