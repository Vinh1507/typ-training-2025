package com.vn.typ_tuan1.service.impl;

import com.vn.typ_tuan1.service.INotificationService;

public class NotificationService implements INotificationService {

    @Override
    public void sendNotification(String msg) {
        System.out.println(String.format("Gửi thông báo đến người dùng với nội dung: %s", msg));
    }


    
}
