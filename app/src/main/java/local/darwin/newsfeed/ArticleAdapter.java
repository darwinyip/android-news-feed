package local.darwin.newsfeed;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that adapts Article into a ViewHolder
 */
public class ArticleAdapter extends RecyclerView.Adapter<ArticleHolder> {

    private List<Article> articles;

    ArticleAdapter(ArrayList<Article> articles) {
        this.articles = articles;
    }

    @NonNull
    @Override
    public ArticleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ArticleHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleHolder holder, int position) {
        holder.setArticle(articles.get(position));
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    /**
     * Clears list
     */
    public void clear() {
        articles.clear();
    }

    /**
     * Appends list
     *
     * @param articles List of Articles
     */
    public void addList(List<Article> articles) {
        this.articles.addAll(articles);
    }
}
