package hu.bearmaster.springtutorial.boot.integration;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.io.IOException;
import java.util.Map;

/**
 * Ez a teszt csak akkor működik ha a "org.wiremock:wiremock:3.3.1" függőség elérhető a pom.xml-ben
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(scripts = "/test-data.sql")
@ContextConfiguration(initializers = UserIntegrationTestWithHtmlUnitAndWireMock.MyTestConfig.class)
public class UserIntegrationTestWithHtmlUnitAndWireMock {

    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
            .options(WireMockConfiguration.wireMockConfig()
                    .dynamicPort())
            .configureStaticDsl(true)
            .build();

    static class MyTestConfig implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext context) {
            Map<String, Object> overriddenProperties = Map.of("facts.url", wireMockExtension.baseUrl());
            MapPropertySource mockPropertySource = new MapPropertySource(
                    "wiremock-properties", overriddenProperties
            );
            context.getEnvironment().getPropertySources().addFirst(mockPropertySource);
        }
    }

    @Autowired
    private WebClient webClient;

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

        HtmlPage page = webClient.getPage("/users");

        DomElement factElement = page.getElementById("cde16cdf75768df10344af2769c28741");
        Assertions.assertThat(factElement.getAttribute("class")).isEqualTo("fact");
        HtmlElement spanElement = factElement.getElementsByTagName("span").get(0);
        Assertions.assertThat(spanElement.getTextContent()).isEqualTo("In Bangladesh, kids as young as 15 can be jailed for cheating on their finals!");

        HtmlElement anchorElement = factElement.getElementsByTagName("a").get(0);
        Assertions.assertThat(anchorElement.getAttribute("href")).isEqualTo("http://www.djtech.net/humor/useless_facts.htm");
        Assertions.assertThat(anchorElement.getTextContent()).isEqualTo("djtech.net");

    }

}
