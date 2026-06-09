package br.puc.grafos.extrator;

import org.junit.jupiter.api.Test;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.net.Authenticator;
import java.net.CookieHandler;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.WebSocket;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ServicoGitHubTest {

    @Test
    void buscarResumoRepositorioConverteRespostaGraphQl() {
        ConfiguracaoExtrator config = new ConfiguracaoExtrator(Map.of(
                "GITHUB_TOKEN", "token-falso",
                "GITHUB_REPO_OWNER", "encode",
                "GITHUB_REPO_NAME", "httpx",
                "MAX_ISSUES", "2",
                "MAX_PULLS", "2"
        ));
        ServicoGitHub servico = new ServicoGitHub(config, new HttpClientFalso("""
                {
                  "data": {
                    "repository": {
                      "pullRequests": {
                        "nodes": [
                          {"number": 1, "author": {"login": "alice"}}
                        ]
                      },
                      "issues": {
                        "nodes": [
                          {"number": 2, "author": {"login": "bob"}}
                        ]
                      }
                    }
                  }
                }
                """));

        Map<String, List<Object>> dados = servico.buscarResumoRepositorio();

        assertEquals(1, dados.get("pullRequests").size());
        assertEquals(1, dados.get("issues").size());
    }

    private static class HttpClientFalso extends HttpClient {
        private final String corpo;

        private HttpClientFalso(String corpo) {
            this.corpo = corpo;
        }

        @Override
        public Optional<CookieHandler> cookieHandler() {
            return Optional.empty();
        }

        @Override
        public Optional<Duration> connectTimeout() {
            return Optional.empty();
        }

        @Override
        public Redirect followRedirects() {
            return Redirect.NEVER;
        }

        @Override
        public Optional<ProxySelector> proxy() {
            return Optional.empty();
        }

        @Override
        public SSLContext sslContext() {
            return null;
        }

        @Override
        public SSLParameters sslParameters() {
            return null;
        }

        @Override
        public Optional<Authenticator> authenticator() {
            return Optional.empty();
        }

        @Override
        public HttpClient.Version version() {
            return HttpClient.Version.HTTP_2;
        }

        @Override
        public Optional<Executor> executor() {
            return Optional.empty();
        }

        @Override
        public <T> HttpResponse<T> send(HttpRequest request, HttpResponse.BodyHandler<T> responseBodyHandler)
                throws IOException, InterruptedException {
            @SuppressWarnings("unchecked")
            T body = (T) corpo;
            return new RespostaFalsa<>(request, body);
        }

        @Override
        public <T> CompletableFuture<HttpResponse<T>> sendAsync(
                HttpRequest request,
                HttpResponse.BodyHandler<T> responseBodyHandler
        ) {
            try {
                return CompletableFuture.completedFuture(send(request, responseBodyHandler));
            } catch (IOException | InterruptedException exception) {
                return CompletableFuture.failedFuture(exception);
            }
        }

        @Override
        public <T> CompletableFuture<HttpResponse<T>> sendAsync(
                HttpRequest request,
                HttpResponse.BodyHandler<T> responseBodyHandler,
                HttpResponse.PushPromiseHandler<T> pushPromiseHandler
        ) {
            return sendAsync(request, responseBodyHandler);
        }

        @Override
        public WebSocket.Builder newWebSocketBuilder() {
            throw new UnsupportedOperationException("WebSocket nao e usado no teste.");
        }
    }

    private record RespostaFalsa<T>(
            HttpRequest request,
            T body
    ) implements HttpResponse<T> {

        @Override
        public int statusCode() {
            return 200;
        }

        @Override
        public Optional<HttpResponse<T>> previousResponse() {
            return Optional.empty();
        }

        @Override
        public HttpHeaders headers() {
            return HttpHeaders.of(Map.of(), (key, value) -> true);
        }

        @Override
        public URI uri() {
            return request.uri();
        }

        @Override
        public HttpClient.Version version() {
            return HttpClient.Version.HTTP_2;
        }

        @Override
        public Optional<SSLSession> sslSession() {
            return Optional.empty();
        }
    }
}
