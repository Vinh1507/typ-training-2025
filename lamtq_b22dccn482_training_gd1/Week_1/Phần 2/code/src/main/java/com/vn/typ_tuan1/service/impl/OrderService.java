package com.vn.typ_tuan1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.vn.typ_tuan1.service.EmailService;
import com.vn.typ_tuan1.service.INotificationService;
import com.vn.typ_tuan1.service.IOrderService;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class OrderService implements IOrderService {

    INotificationService INotificationService;
    public OrderService(INotificationService iNotificationService){
        this.INotificationService = iNotificationService;
    }

    @Override
    public void order() {
        INotificationService.sendNotification("Cảm ơn bạn đã mua hàng");
    }
    
}
