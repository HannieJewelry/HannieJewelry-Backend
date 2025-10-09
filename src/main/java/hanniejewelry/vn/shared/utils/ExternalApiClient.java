package hanniejewelry.vn.shared.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ExternalApiClient {
    private final WebClient webClient;

    public <T> T get(String url, Map<String, String> headers, ParameterizedTypeReference<T> typeRef) {
        WebClient.RequestHeadersSpec<?> req = webClient.get().uri(url);
        if (headers != null) headers.forEach(req::header);
        return req.accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(typeRef)
                .block();
    }

    public <T> Mono<T> postAsync(String url, Map<String, String> headers, Object body, ParameterizedTypeReference<T> typeRef) {
        WebClient.RequestBodySpec req = webClient.post().uri(url);
        if (headers != null) headers.forEach(req::header);
        return req.contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(typeRef);
    }

    public <T> Mono<T> getAsync(String url, Map<String, String> headers, ParameterizedTypeReference<T> typeRef) {
        WebClient.RequestHeadersSpec<?> req = webClient.get().uri(url);
        if (headers != null) headers.forEach(req::header);
        return req.accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(typeRef);
    }

    public <T> T post(String url, Map<String, String> headers, Object body, ParameterizedTypeReference<T> typeRef) {
        WebClient.RequestBodySpec req = webClient.post().uri(url);
        if (headers != null) headers.forEach(req::header);
        return req.contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(typeRef)
                .block();
    }

    public <T> T put(String url, Map<String, String> headers, Object body, ParameterizedTypeReference<T> typeRef) {
        WebClient.RequestBodySpec req = webClient.put().uri(url);
        if (headers != null) headers.forEach(req::header);
        return req.contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(typeRef)
                .block();
    }

    public <T> T delete(String url, Map<String, String> headers, ParameterizedTypeReference<T> typeRef) {
        WebClient.RequestHeadersSpec<?> req = webClient.delete().uri(url);
        if (headers != null) headers.forEach(req::header);
        return req.accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(typeRef)
                .block();
    }
}
