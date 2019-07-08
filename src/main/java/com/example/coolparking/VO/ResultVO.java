package com.example.coolparking.VO;

import lombok.Data;

@Data
public class ResultVO {
    int code;
    String msg;

    public ResultVO(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
