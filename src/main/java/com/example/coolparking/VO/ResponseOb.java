package com.example.coolparking.VO;

import lombok.Data;

import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.CopyOnWriteArraySet;

@Data
public class ResponseOb {
//    public static HttpServletResponse response;
    private String UUID = "";
    private HttpServletResponse response;

    private static CopyOnWriteArraySet<ResponseOb> responseSet = new CopyOnWriteArraySet<>();

    public static void addResponse(String UUID, HttpServletResponse response){
        ResponseOb responseOb = new ResponseOb(UUID,response);
        responseSet.add(responseOb);
    }

    public static void remResponse(String UUID){
        for(ResponseOb responseOb: responseSet){
            if(responseOb.getUUID().equals(UUID)){
                responseSet.remove(responseOb);
            }
        }
    }

    public static HttpServletResponse get(String UUID){
        for(ResponseOb responseOb: responseSet){
            if(responseOb.getUUID().equals(UUID)){
                return responseOb.getResponse();
            }
        }
        return null;
    }

    public ResponseOb(String UUID, HttpServletResponse response) {
        this.UUID = UUID;
        this.response = response;
    }
}
