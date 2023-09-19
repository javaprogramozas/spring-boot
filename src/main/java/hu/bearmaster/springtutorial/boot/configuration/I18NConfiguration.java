package hu.bearmaster.springtutorial.boot.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

@Configuration
public class I18NConfiguration {

    @Bean
    public LocaleResolver localeResolver() {
        return new CookieLocaleResolver("user-language");
    }

}
