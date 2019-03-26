package ru.woh.api;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import ru.woh.api.models.Source;

import java.net.MalformedURLException;
import java.net.URL;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "parser")
public class ParserConfiguration {
    private String host;
    private String port;

    public ParserConfiguration() {
    }

    public URL getParserUrl(Source source) {
        String urlString = String.format("http://%s:%s/?sourceId=%d", this.host, this.port, source.getId());

        URL url;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            url = null;
        }

        return url;
    }

    public String getHost() {
        return this.host;
    }

    public String getPort() {
        return this.port;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
