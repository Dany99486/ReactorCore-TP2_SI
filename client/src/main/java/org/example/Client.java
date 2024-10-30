package org.example;

import org.example.model.Media;
import org.example.util.Stats;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;


public class Client {
    static final String BaseUrl = "http://localhost:8080";
    static final String FileName = "report.txt";

    public static void main(String[] args) {

        StringBuilder report = new StringBuilder();
        WebClient client = WebClient.create(BaseUrl);

        System.out.println("<==== Client ====>\n");

        // Função 1: Títulos e datas de lançamento de todos os itens de mídia
        report.append("### 1. Títulos e datas de lançamento de todos os itens de mídia ###\n");
        client.get()
                .uri("/media")
                .retrieve()
                .bodyToFlux(Media.class)
                .doOnNext(media -> report.append("Title: ").append(media.getTitle())
                        .append(", Release Date: ").append(media.getReleaseDate()).append("\n"))
                .blockLast();
        report.append("\n---\n\n");

        // Função 2: Contagem total de itens de mídia
        report.append("### 2. Contagem total de itens de mídia ###\n");
        client.get()
                .uri("/media")
                .retrieve()
                .bodyToFlux(Media.class)
                .count()
                .doOnNext(count -> report.append("Total media items: ").append(count).append("\n"))
                .block();
        report.append("\n---\n\n");

        // Função 3: Contagem de itens de mídia com classificação média > 8
        report.append("### 3. Contagem de itens de mídia com classificação média > 8 ###\n");
        client.get()
                .uri("/media")
                .retrieve()
                .bodyToFlux(Media.class)
                .filter(media -> media.getAverageRating() > 8)
                .count()
                .doOnNext(count -> report.append("Media items with rating > 8: ").append(count).append("\n"))
                .block();
        report.append("\n---\n\n");

       /* // Função 4: Contagem total de itens de mídia subscritos
        report.append("### 4. Contagem total de itens de mídia subscritos ###\n");
        client.get()
                .uri("/user-media/user/{userId}") // Ajuste o userId conforme necessário
                .retrieve()
                .bodyToFlux(Media.class)
                .count()
                .subscribe(count -> report.append("Total subscribed media items: ").append(count).append("\n"));
        report.append("\n---\n\n");*/

        // Função 5: Itens dos anos 80, ordenados pela média de classificação
        report.append("### 5. Itens dos anos 80, ordenados pela média de classificação ###\n");
        client.get()
                .uri("/media")
                .retrieve()
                .bodyToFlux(Media.class)
                .filter(media -> {
                    LocalDate releaseDate = media.getReleaseDate();
                    return releaseDate.isAfter(LocalDate.of(1980, 1, 1))
                            && releaseDate.isBefore(LocalDate.of(1989,12,31));
                })
                .sort(Comparator.comparing(Media::getAverageRating).reversed())
                .doOnNext(media -> report.append("Title: ").append(media.getTitle())
                        .append(", Release Date: ").append(media.getReleaseDate())
                        .append(", Rating: ").append(media.getAverageRating()).append("\n"))
                .blockLast();
        report.append("\n---\n\n");

        report.append("### 6. Média e desvio padrão das classificações ###\n");
        client.get()
                .uri("/media")
                .retrieve()
                .bodyToFlux(Media.class)
                .map(Media::getAverageRating)
                .reduce(new Stats(), (stats, rating) -> {
                    stats.addValue(rating);
                    return stats;
                })
                .doOnNext(stats -> {
                    report.append("Média das classificações: ").append(stats.getMean()).append("\n")
                            .append("Desvio padrão das classificações: ").append(stats.getStandardDeviation()).append("\n");
                })
                .block(); // Aguarda a conclusão do fluxo // Aguarda a conclusão do fluxo
        report.append("\n---\n\n");

        // Gravação no ficheiro
        saveToFile(FileName, report.toString());
    }

    private static void saveToFile(String filename, String content) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(content);
            System.out.println("Report saved to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
