package com.kenzie.appserver.Dto;

import com.kenzie.appserver.Dto.Media;

import java.util.List;

public class Page {
    private List<Media> media;

    public List<Media> getMedia() {
        return media;
    }

    public void setMedia(List<Media> media) {
        this.media = media;
    }
}