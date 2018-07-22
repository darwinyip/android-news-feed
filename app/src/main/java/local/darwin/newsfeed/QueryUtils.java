package local.darwin.newsfeed;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    private static Date createDate(String stringDate) {
        Date date;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(stringDate);
        } catch (ParseException exception) {
            Log.e(LOG_TAG, "Error with creating Date", exception);
            return null;
        }
        return date;
    }

    private static URL createUrl(String stringUrl) {
        URL url;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
            return null;
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        Log.d(LOG_TAG, "Making HTTP request...");
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, String.valueOf(urlConnection.getResponseCode()));
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link Article} objects that has been built up from
     * parsing a JSON response.
     */
    private static List<Article> extractFeaturesFromJson(String ArticleJSON) {
        Log.d(LOG_TAG, "Extracting Article results...");
        if (TextUtils.isEmpty(ArticleJSON)) {
            return null;
        }
        Log.d(LOG_TAG, ArticleJSON);
        // Create an empty ArrayList that we can start adding Articles to
        List<Article> Articles = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // build up a list of Article objects with the corresponding data.
            JSONObject jsonObject = new JSONObject(ArticleJSON);
            JSONArray results = jsonObject.getJSONObject("response").getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject c = results.getJSONObject(i);
                String sectionName = c.getString("sectionName");
                Date webPublicationDate = createDate(c.getString("webPublicationDate"));
                String webTitle = c.getString("webTitle").trim();
                Uri webUrl = Uri.parse(c.getString("webUrl"));
                String authors;
                if (c.has("tags")) {
                    JSONArray tags = c.getJSONArray("tags");
                    List<String> authorList = new ArrayList<>();
                    for (int j = 0; j < tags.length(); j++) {
                        String author = tags.getJSONObject(j).getString("webTitle");
                        authorList.add(author);
                    }
                    authors = String.join(", ", authorList);
                    Log.d(LOG_TAG, authors);

                    Articles.add(new Article(sectionName, webPublicationDate, webTitle, webUrl, authors));
                }
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the Article JSON results", e);
        }

        // Return the list of Articles
        return Articles;
    }

    public static List<Article> fetchArticleData(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
            return null;
        }

        return extractFeaturesFromJson(jsonResponse);
    }
}
