package org.tutorials.wproject1;

import com.mongodb.reactivestreams.client.MongoClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@ComponentScan({"org.tutorials.wproject1"})
@EntityScan("org.tutorials.wproject1.model")
@EnableReactiveMongoRepositories("org.tutorials.wproject1.repository")
public class Spring5ReactiveApplication {
	public static void main(String[] args) {
            SpringApplication.run(Spring5ReactiveApplication.class, args);
	}
/** 
        @Autowired
        MongoClient mongoClient;

        @Bean
        public ReactiveMongoTemplate reactiveMongoTemplate() {
           return new ReactiveMongoTemplate(mongoClient, "service");
        }
        **/

}
