package org.example;

import org.example.model.Media;
import org.example.model.User;
import org.example.util.MediaUserCount;
import org.example.util.Stats;
import org.example.util.UserMediaInfo;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
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
        report.append("\n\n\n");

        // Função 2: Contagem total de itens de mídia
        report.append("### 2. Contagem total de itens de mídia ###\n");
        client.get()
                .uri("/media")
                .retrieve()
                .bodyToFlux(Media.class)
                .count()
                .doOnNext(count -> report.append("Total media items: ").append(count).append("\n"))
                .block();
        report.append("\n\n\n");

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
        report.append("\n\n\n");

        // Função 4: Contagem total de itens de mídia subscritos
        report.append("### 4. Contagem total de itens de mídia subscritos ###\n");
        client.get()
                .uri("/media") // Ajuste o userId conforme necessário
                .retrieve()
                .bodyToFlux(Media.class)
                .filter(media -> media.getUserIds() != null && !media.getUserIds().isEmpty())
                .count()
                .doOnNext(count -> report.append("Total subscribed media items: ").append(count).append("\n"))
                .block();
        report.append("\n\n\n");

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
        report.append("\n\n\n");

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
                .doOnNext(stats ->
                        report.append("Média das classificações: ").append(stats.getMean()).append("\n")
                            .append("Desvio padrão das classificações: ").append(stats.getStandardDeviation()).append("\n")
                )
                .block(); // Aguarda a conclusão do fluxo // Aguarda a conclusão do fluxo
        report.append("\n\n\n");

        // Função 7: Nome do item de mídia mais antigo
        report.append("### 7. Nome do item de mídia mais antigo ###\n");
        client.get()
                .uri("/media") // Acesse todos os itens de mídia
                .retrieve()
                .bodyToFlux(Media.class)
                .sort(Comparator.comparing(Media::getReleaseDate)) // Ordena pela data de lançamento
                .next() // Pega apenas o próximo item após a ordenação (o mais antigo)
                .doOnNext(media -> report.append("Oldest media item: ").append(media.getTitle()).append("\n"))
                .block(); // Aguarda a conclusão do fluxo
        report.append("\n\n\n");

        // Função 8: Média de usuários por item de mídia
        report.append("### 8. Média de usuários por item de mídia ###\n");
        client.get()
                .uri("/media") // Obtem todos os itens de mídia
                .retrieve()
                .bodyToFlux(Media.class)
                .map(media -> media.getUserIds() != null ? media.getUserIds().size() : 0) // Conta o número de usuários para cada item
                .reduce(new long[]{0, 0}, (acc, count) -> {
                    acc[0] += count; // Soma total de usuários
                    acc[1]++; // Conta todos os itens de mídia
                    return acc;
                })
                .map(acc -> {
                    long totalUsers = acc[0]; // Total de usuários somados
                    long totalItems = acc[1];  // Total de itens de mídia
                    // Calcula a média ou retorna 0 se não houver itens
                    return totalItems == 0 ? 0.0 : (double) totalUsers / totalItems;
                })
                .doOnNext(average -> report.append("Average users per media item: ").append(average).append("\n"))
                .block(); // Aguarda a conclusão do fluxo
        report.append("\n\n\n");


        // Função 9: Nome e número de usuários por item de mídia, ordenados pela idade dos usuários em ordem decrescente
        report.append("### 9. Nome e número de usuários por item de mídia, ordenados pela idade dos usuários em ordem decrescente ###\n");
        client.get()
                .uri("/media") // Endpoint para obter todos os itens de mídia
                .retrieve()
                .bodyToFlux(Media.class)
                .flatMap(media -> {
                    MediaUserCount mediaUserCount = new MediaUserCount();
                    mediaUserCount.setTitle(media.getTitle());
                    mediaUserCount.setUserCount(media.getUserIds() != null ? media.getUserIds().size() : 0);
                    // Processa os usuários e atualiza o relatório gradualmente
                    return client.get()
                            .uri("/user") // Obtém todos os usuários
                            .retrieve()
                            .bodyToFlux(User.class)
                            .filter(user -> user.getMediaIds() != null && user.getMediaIds().contains(media.getId()))
                            .sort(Comparator.comparing(User::getAge).reversed())
                            .doOnNext(mediaUserCount::addUser)
                            .then(Mono.just(mediaUserCount)); // Retorna o MediaUserCount atualizado
                })
                .doOnNext(mediaUserCount -> {
                    // Adiciona título e número de usuários ao relatório para cada item de mídia
                    report.append("Title: ").append(mediaUserCount.getTitle())
                            .append(", Number of users: ").append(mediaUserCount.getUserCount()).append("\n");
                    for(User user: mediaUserCount.getUsers()){
                        report.append("  - Name: ").append(user.getName())
                                    .append(", Age: ").append(user.getAge()).append("\n");
                    }
                })
                .blockLast(); // Aguarda a conclusão de todo o fluxo
        report.append("\n\n\n");

        report.append("### 10. Complete data of all users, by adding the names of subscribed media items ###\n");
        client.get()
                .uri("/user") // Endpoint para obter todos os usuários
                .retrieve()
                .bodyToFlux(User.class)
                .flatMap(user -> {
                    UserMediaInfo userInfo = new UserMediaInfo(user.getName(), user.getAge(),user.getGender());

                    return client.get()
                            .uri("/media") // Endpoint para obter todas as mídias
                            .retrieve()
                            .bodyToFlux(Media.class)
                            .filter(media -> media.getUserIds() != null && media.getUserIds().contains(user.getId())) // Mantém as mídias inscritas pelo utilizador
                            .doOnNext(media -> userInfo.addMediaTitle(media.getTitle())) // Adiciona o título da mídia ao UserMediaInfo
                            .then(Mono.just(userInfo)); // Retorna o UserMediaInfo completo
                })
                .doOnNext(userInfo -> {
                    // Adiciona os dados do usuário e os títulos das mídias ao relatório
                    report.append("User: ").append(userInfo.getName())
                            .append(", Age: ").append(userInfo.getAge())
                            .append(", Gender: ").append(userInfo.getGender()).append("\n");
                    for (String title : userInfo.getMediaTitles()) {
                        report.append("  - Subscribed Media: ").append(title).append("\n");
                    }
                })
                .blockLast(); // Aguarda a conclusão de todo o fluxo
        report.append("\n\n\n");

        // Gravação no ficheiro
        saveToFile(report.toString());

    }

    private static void saveToFile(String content) {
        try (FileWriter writer = new FileWriter(Client.FileName)) {
            writer.write(content);
            System.out.println("Report saved to " + Client.FileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
