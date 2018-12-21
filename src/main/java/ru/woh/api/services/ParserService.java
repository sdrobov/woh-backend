package ru.woh.api.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import ru.woh.api.ParserConfiguration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

@Service
public class ParserService {
    private final ParserConfiguration parserConfiguration;

    public static class ParserResponse {
        private String status;
        private String error;

        public ParserResponse() {
        }

        static ParserResponse fromJsonString(String json) {
            ObjectMapper mapper = new ObjectMapper();

            ParserResponse response;
            try {
                response = mapper.readValue(json, ParserResponse.class);
            } catch (IOException e) {
                response = new ParserResponse();
                response.setError("json parse error");
            }

            return response;
        }

        public String getStatus() {
            return this.status;
        }

        public String getError() {
            return this.error;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public void setError(String error) {
            this.error = error;
        }
    }

    public ParserService(ParserConfiguration parserConfiguration) {
        this.parserConfiguration = parserConfiguration;
    }

    public Boolean parseSource() {
        URL url = this.parserConfiguration.getParserUrl();
        HttpURLConnection connection;
        try {
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            return false;
        }

        try {
            connection.setRequestMethod("GET");
        } catch (ProtocolException e) {
            return false;
        }

        int resCode;
        try {
            resCode = connection.getResponseCode();
        } catch (IOException e) {
            return false;
        }

        BufferedReader in;
        try {
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } catch (IOException e) {
            return false;
        }

        StringBuilder response = new StringBuilder();
        String line;

        try {
            while ((line = in.readLine()) != null) {
                response.append(line);
            }

            in.close();
        } catch (IOException e) {
            return false;
        }

        ParserResponse crawlerParseResponse = ParserResponse.fromJsonString(response.toString());

        return (resCode == 200)
            && (crawlerParseResponse.getError() == null || crawlerParseResponse.getError().isEmpty());
    }
}
