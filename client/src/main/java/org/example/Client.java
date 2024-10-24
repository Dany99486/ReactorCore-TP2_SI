package org.example;

import org.example.model.Media;
import org.springframework.web.reactive.function.client.WebClient;

public class Client {

    public static void main(String[] args) {
        System.out.println("<==== Client ====>");

        String BaseUrl = "http://localhost:8080";
        String MyURI = "/media";

        WebClient.create(BaseUrl)
                .get()
                .uri(MyURI)
                .retrieve()
                .bodyToFlux(Media.class)
                .subscribe(System.out::println);

        try {
            Thread.sleep(10*1000);
        } catch (InterruptedException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
