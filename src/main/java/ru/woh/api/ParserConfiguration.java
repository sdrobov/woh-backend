package ru.woh.api;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.net.MalformedURLException;
import java.net.URL;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "parser")
@Getter
@Setter
@NoArgsConstructor
public class ParserConfiguration {
    private String host;
    private String port;

    public URL getParserUrl() {
        String urlString = String.format("http://%s:%s", this.host, this.port);

        URL url;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            url = null;
        }

        return url;
    }
}
