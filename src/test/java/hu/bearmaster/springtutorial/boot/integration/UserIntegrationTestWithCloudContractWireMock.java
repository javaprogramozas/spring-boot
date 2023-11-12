package hu.bearmaster.springtutorial.boot.integration;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;

/**
 * Ez a teszt csak akkor működik ha a "spring-cloud-contract-wiremock" függőség elérhető a pom.xml-ben
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
@ActiveProfiles("test")
@Sql(scripts = "/test-data.sql")
@TestPropertySource(properties = "facts.url=http://localhost:${wiremock.server.port}")
public class UserIntegrationTestWithCloudContractWireMock {

    @Autowired
    WebApplicationContext context;

    private WebClient webClient;

    @BeforeEach
    void setUp() {
        webClient = MockMvcWebClientBuilder.webAppContextSetup(context).build();
    }

    @Test
    void getUsers() throws IOException {

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

        WireMock.stubFor(WireMock.get(WireMock.urlPathEqualTo("/api/v2/facts/today"))
                .withQueryParam("language", WireMock.equalTo("en"))
                .willReturn(WireMock.okJson(responseBody)));

        String baseUrl = "http://localhost:" + context.getEnvironment().getProperty("wiremock.server.port");
        HtmlPage page = webClient.getPage(baseUrl + "/users");

        DomElement factElement = page.getElementById("test-id");
        Assertions.assertThat(factElement.getAttribute("class")).isEqualTo("fact");
        HtmlElement spanElement = factElement.getElementsByTagName("span").get(0);
        Assertions.assertThat(spanElement.getTextContent()).isEqualTo("This is a test fact");

        HtmlElement anchorElement = factElement.getElementsByTagName("a").get(0);
        Assertions.assertThat(anchorElement.getAttribute("href")).isEqualTo("http://test.url/");
        Assertions.assertThat(anchorElement.getTextContent()).isEqualTo("test source");

    }

}
