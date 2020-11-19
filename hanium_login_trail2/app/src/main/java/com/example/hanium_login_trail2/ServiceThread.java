package com.example.hanium_login_trail2;

import android.app.Service;
import android.os.Handler;

public class ServiceThread extends Thread {
    Handler handler;
    boolean isRun = true;

    public ServiceThread(Handler handler){
        this.handler = handler;
    }

    public void stopForever() {
        synchronized (this){
            this.isRun = false;
        }
    }

    public void run() {
        while(isRun){
            handler.sendEmptyMessage(0);
            //TODO : 서비스가 실행되면 (쓰레드가 시작되고) 이 곳에서 10초마다 핸들러로 메세지를 전달하게된다.
            // 즉, 핸들러가 메세지를 받으면 알람을 발생시킨다.
            // 여기서 10초조건을 위경도를 받아서 IN, OUT 체크를 해서 OUT이면 핸들러로 메세지를 보내면 될것같다.
            // 필요한 것(데이터리스트 받아오는것 - 위경도, & 메세지 보낼때 위경도랑 환자이름 HANDLER로 보내기??(위경도보내면 GEOCODER로 주소띄우고, 이름 띄우고)
            // IN&OUT CHECK 함수 여기로 옮기기 + DISTANCE계산함수도
            // 1. 쓰레드에서 데이터베이스에서 위경도를 받아오기  -> 2. 받아온 위경도로 INOUT계산 -> OUT일시 HANDLER로 메세지 보내기 -> HANDLER에서 시스템 알람 띄우기. -> 누르면 MAIN_FRAGMENT MAP 띄우기
            try{
                Thread.sleep((10000));
            } catch (Exception e){}
        }
    }
}
