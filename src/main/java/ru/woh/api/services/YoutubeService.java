package ru.woh.api.services;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTube.Videos.List;
import com.google.api.services.youtube.model.VideoListResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Service
public class YoutubeService {
    private final String API_KEY = "AIzaSyDUJnAMzTk9A0M2jo33ORgDR3pwGSIrXkI";

    public YouTube getYoutube() {
        final NetHttpTransport httpTransport;
        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();

            return null;
        }

        return new YouTube.Builder(httpTransport, JacksonFactory.getDefaultInstance(), null)
            .setApplicationName("woh.ru backend")
            .build();
    }

    public VideoListResponse getListById(String id) {
        var youtube = this.getYoutube();
        if (youtube == null) {
            return null;
        }

        List request;
        try {
            request = youtube.videos().list("snippet,id,player,contentDetails");
        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }

        VideoListResponse response;
        try {
            response = request.setKey(this.API_KEY).setId(id).execute();
        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }

        return response;
    }
}
