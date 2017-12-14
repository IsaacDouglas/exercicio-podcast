package br.ufpe.cin.if710.podcast.domain;

import java.io.Serializable;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class ItemFeed implements Serializable{

    @PrimaryKey(autoGenerate = true)
    private int id;

    private final String title;
    private final String link;
    private final String pubDate;
    private final String description;
    private final String downloadLink;
    private String uri;
    private Integer timePaused;


    public ItemFeed(String title, String link, String pubDate, String description, String downloadLink) {
        this.title = title;
        this.link = link;
        this.pubDate = pubDate;
        this.description = description;
        this.downloadLink = downloadLink;
        this.uri = "";
        this.timePaused = 0;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getDescription() {
        return description;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri){
        this.uri = uri;
    }

    @Override
    public String toString() {
        return title;
    }

    public Integer getTimePaused() {
        return timePaused;
    }

    public void setTimePaused(Integer timePaused) {
        this.timePaused = timePaused;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}