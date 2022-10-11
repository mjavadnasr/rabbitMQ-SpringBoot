package com.example.rabbitmqspringboot;

import org.apache.logging.log4j.message.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

@RestController
public class TestController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/publish/{name}")
    public String publish(@PathVariable("name")String name)
    {
        Person p = new Person(1L, name);
        rabbitTemplate.convertAndSend("Mobile", p);
        rabbitTemplate.convertAndSend("Direct-Exchange", "mobile" ,  p);
        rabbitTemplate.convertAndSend("Fanout-Exchange", "" ,  p);
        rabbitTemplate.convertAndSend("Topic-Exchange", "*.mobile.*" ,  p);
        return "success";
    }

    @GetMapping("/publish-header-exchange/{name}")
    public String publishHeader(@PathVariable("name") String name) throws IOException {
        Person p = new Person(1L, name);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput objectOutput = new ObjectOutputStream(bos);
        objectOutput.writeObject(p);
        objectOutput.flush();
        objectOutput.close();

        byte[] byteMessage = bos.toByteArray();
        bos.close();

        org.springframework.amqp.core.Message message = MessageBuilder.withBody(byteMessage)
                .setHeader("item1", "mobile")
                .setHeader("item2", "television")
                .build();

        rabbitTemplate.send("Header-Exchange", "", message);
        return "success";


    }
}
