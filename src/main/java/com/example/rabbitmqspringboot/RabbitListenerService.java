package com.example.rabbitmqspringboot;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;

@Service
public class RabbitListenerService {

    //this is for simple publishing message
//    @RabbitListener(queues = "Mobile")
//    public void getMessage(Person p) {
//        System.out.println(p.getName());
//    }


    //this is for header exchange
    @RabbitListener(queues = "Mobile")
    public void getMessage(byte[] message) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis = new ByteArrayInputStream(message);
        ObjectInput objectInput = new ObjectInputStream(bis);
        Person p = (Person) objectInput.readObject();
        objectInput.close();
        bis.close();
        System.out.println(p.getName());



    }
}
