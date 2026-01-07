package com.example.qlsv_database.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.*;

@Configuration
public class RabbitMQConfig {
    public static final String QUEUE = "studentQueue";
    public static final String EXCHANGE = "studentExchange";
    public static final String ROUTING_KEY = "student.created";

    @Bean
    public Queue queue() {
        return new Queue(QUEUE, true);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Binding binding(){
        return BindingBuilder.
                bind(queue())
                .to(exchange())
                .with(ROUTING_KEY);
    }
}
