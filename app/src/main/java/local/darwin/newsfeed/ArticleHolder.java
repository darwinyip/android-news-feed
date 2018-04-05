package local.darwin.newsfeed;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

class ArticleHolder extends RecyclerView.ViewHolder {

    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private TextView webTitle;
    private TextView webPublicationDate;
    private Uri webUrl;

    ArticleHolder(final View itemView) {
        super(itemView);
        webTitle = itemView.findViewById(R.id.web_title);
        webPublicationDate = itemView.findViewById(R.id.web_published_date);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, webUrl);
                itemView.getContext().startActivity(intent);
            }
        });
    }

    void setArticle(Article article) {
        webTitle.setText(article.getWebTitle());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        webPublicationDate.setText(dateFormat.format(article.getWebPublicationDate()));
        webUrl = article.getWebUrl();
    }
}
