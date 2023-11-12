package hu.bearmaster.springtutorial.boot.service;

import hu.bearmaster.springtutorial.boot.model.response.FactResponse;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.Locale;

class UselessFactServiceTest {

    private MockWebServer mockWebServer;

    private UselessFactService service;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        String serverUrl = mockWebServer.url("/").toString();
        service = new UselessFactService(WebClient.builder(), serverUrl);
    }

    @Test
    void getFact() throws InterruptedException {
        String responseBody = """
                {
                    "id": "test-id",
                    "text": "This is a test fact",
                    "source": "test source",
                    "source_url": "http://test.url/",
                    "language": "en",
                    "permalink": "http://permalink.test.url/"
                }
                """;
        MockResponse response = new MockResponse()
                .setResponseCode(200)
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .setBody(responseBody);
        mockWebServer.enqueue(response);

        FactResponse result = service.getFact("today", Locale.ENGLISH);

        Assertions.assertThat(result.getId()).isEqualTo("test-id");
        Assertions.assertThat(result.getText()).isEqualTo("This is a test fact");
        Assertions.assertThat(result.getSource()).isEqualTo("test source");
        Assertions.assertThat(result.getSourceUrl()).isEqualTo("http://test.url/");
        Assertions.assertThat(result.getLanguage()).isEqualTo("en");
        Assertions.assertThat(result.getPermalink()).isEqualTo("http://permalink.test.url/");

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        HttpUrl requestUrl = recordedRequest.getRequestUrl();
        Assertions.assertThat(requestUrl.encodedPath()).isEqualTo("/api/v2/facts/today");
        Assertions.assertThat(requestUrl.queryParameter("language")).isEqualTo("en");
        Assertions.assertThat(recordedRequest.getHeader("Accept")).isEqualTo("application/json");
        Assertions.assertThat(recordedRequest.getMethod()).isEqualTo("GET");
    }

    @AfterEach
    void shutdown() throws IOException {
        if (mockWebServer != null) {
            mockWebServer.shutdown();
        }
    }

}