package com.mx.cqbookstore.http;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/20.
 */

public class SoapUtil {

    public String doCall(LinkedHashMap pramers, Handler handler,String action){
        String result;
        SoapObject soapObject = new SoapObject(Config.SPACENMAE, action);
        Iterator<Map.Entry<String,Object>> iter=pramers.entrySet().iterator();
        while (iter.hasNext())
        {
            Map.Entry<String, Object> entry =  iter .next();
            soapObject.addProperty(entry.getKey(), entry.getValue());
        }
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);
        HttpTransportSE httpTransportSE = new HttpTransportSE(Config.HOST);
        System.out.println("设置完毕,准备开启服务");
        try {
            httpTransportSE.call(Config.SPACENMAE+action, envelope);
            System.out.println("调用WebService服务成功");
            //获得服务返回的数据,并且开始解析
            SoapObject object = (SoapObject) envelope.bodyIn;
            System.out.println("获得服务数据");
            result = object.getProperty(0).toString();
            Log.e("result:",result);
            //handler.sendEmptyMessage(0x001);
            System.out.println("发送完毕");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("调用WebService服务失败:");
            Message msg=Message.obtain();
            msg.what=MyHandler.REQFEIL;
            msg.obj="调用WebService服务失败";
            handler.sendMessage(msg);
            return null;
        }

    }
}
