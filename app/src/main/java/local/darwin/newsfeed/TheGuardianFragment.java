package local.darwin.newsfeed;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TheGuardianFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Article>> {

    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private static final String ARTICLE_URL = "https://content.guardianapis.com/theguardian?api-key=f5f4a4ec-6689-4301-b732-914528302c59&show-tags=contributor";
    private ArticleAdapter articleAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        TextView emptyView = rootView.findViewById(R.id.empty_view);
        RecyclerView recyclerView = rootView.findViewById(R.id.fragment_container);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        articleAdapter = new ArticleAdapter(new ArrayList<Article>());
        recyclerView.setAdapter(articleAdapter);

        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(0, null, this);
        } else {
            emptyView.setText(R.string.no_internet_connection);
        }

        return rootView;
    }

    @Override
    public Loader<List<Article>> onCreateLoader(int i, Bundle bundle) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        String fromDate = sharedPreferences.getString(getString(R.string.settings_from_date_key), null);
        String orderBy = sharedPreferences.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));

        Uri baseUri = Uri.parse(ARTICLE_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        if (!fromDate.isEmpty()) {
            uriBuilder.appendQueryParameter("from-date", fromDate);
        }

        uriBuilder.appendQueryParameter("order-by", orderBy);

        String web = uriBuilder.toString();
        Log.d(LOG_TAG, web);
        return new ArticleLoader(getContext(), web);
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articles) {
        articleAdapter.clear();
        articleAdapter.addList(articles);
        articleAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        articleAdapter.clear();
        articleAdapter.notifyDataSetChanged();
    }
}
