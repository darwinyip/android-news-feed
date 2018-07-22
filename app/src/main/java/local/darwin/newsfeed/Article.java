package local.darwin.newsfeed;

import android.net.Uri;

import java.util.Date;

/**
 * Model Class for Article.
 */

class Article {
    private String sectionName;
    private Date webPublicationDate;
    private String webTitle;
    private Uri webUrl;
    private String authors;

    public Article(String sectionName, Date webPublicationDate, String webTitle, Uri webUrl, String authors) {
        this.sectionName = sectionName;
        this.webPublicationDate = webPublicationDate;
        this.webTitle = webTitle;
        this.webUrl = webUrl;
        this.authors = authors;
    }

    public String getSectionName() {
        return sectionName;
    }

    public Date getWebPublicationDate() {
        return webPublicationDate;
    }

    public String getWebTitle() {
        return webTitle;
    }

    public Uri getWebUrl() {
        return webUrl;
    }

    public String getAuthors() {
        return authors;
    }
}
