package ru.woh.api.services;

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

    public YouTube getYoutube() throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        return new YouTube.Builder(httpTransport, JacksonFactory.getDefaultInstance(), null)
            .setApplicationName("woh.ru backend")
            .build();
    }

    public VideoListResponse getListById(String id) throws GeneralSecurityException, IOException {
        var youtube = this.getYoutube();
        if (youtube == null) {
            return null;
        }

        String API_KEY = "AIzaSyDUJnAMzTk9A0M2jo33ORgDR3pwGSIrXkI";
        List request = youtube.videos().list("snippet,id,player,contentDetails");

        return request.setKey(API_KEY).setId(id).execute();
    }
}
