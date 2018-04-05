package local.darwin.newsfeed;

import android.net.Uri;

import java.util.Date;

/**
 * Model Class for Article.
 */

class Article {
    private String id;
    private Date webPublicationDate;
    private String webTitle;
    private Uri webUrl;

    public Article(String id, Date webPublicationDate, String webTitle, Uri webUrl) {
        this.id = id;
        this.webPublicationDate = webPublicationDate;
        this.webTitle = webTitle;
        this.webUrl = webUrl;
    }

    public String getId() {
        return id;
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
}
