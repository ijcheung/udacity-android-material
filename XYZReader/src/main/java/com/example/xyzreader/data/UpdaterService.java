package com.example.xyzreader.data;

import android.app.IntentService;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.RemoteException;
import android.text.format.Time;
import android.util.Log;

import com.example.xyzreader.api.ArticlesApi;
import com.example.xyzreader.model.Article;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UpdaterService extends IntentService {
    private static final String TAG = "UpdaterService";

    public static final String BROADCAST_ACTION_STATE_CHANGE
            = "com.example.xyzreader.intent.action.STATE_CHANGE";
    public static final String EXTRA_REFRESHING
            = "com.example.xyzreader.intent.extra.REFRESHING";

    public UpdaterService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final Time time = new Time();

        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null || !ni.isConnected()) {
            Log.w(TAG, "Not online, not refreshing.");
            return;
        }

        sendStickyBroadcast(
                new Intent(BROADCAST_ACTION_STATE_CHANGE).putExtra(EXTRA_REFRESHING, true));

        ArticlesApi articlesApi = new Retrofit.Builder()
                .baseUrl(ArticlesApi.BASE_ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(ArticlesApi.class);

        Call<List<Article>> call = articlesApi.getArticles();
        call.enqueue(new Callback<List<Article>>() {
            @Override
            public void onResponse(Response<List<Article>> response) {
                ArrayList<ContentProviderOperation> cpo = new ArrayList<ContentProviderOperation>();
                Uri dirUri = ItemsContract.Items.buildDirUri();
                List<Article> articles = response.body();

                // Delete all items
                cpo.add(ContentProviderOperation.newDelete(dirUri).build());

                for (Article article : articles) {
                    ContentValues values = new ContentValues();
                    values.put(ItemsContract.Items.SERVER_ID, article.getId());
                    values.put(ItemsContract.Items.AUTHOR, article.getAuthor());
                    values.put(ItemsContract.Items.TITLE, article.getTitle());
                    values.put(ItemsContract.Items.BODY, article.getBody());
                    values.put(ItemsContract.Items.THUMB_URL, article.getThumb());
                    values.put(ItemsContract.Items.PHOTO_URL, article.getPhoto());
                    values.put(ItemsContract.Items.ASPECT_RATIO, article.getAspectRatio());
                    time.parse3339(article.getPublishedDate());
                    values.put(ItemsContract.Items.PUBLISHED_DATE, time.toMillis(false));
                    cpo.add(ContentProviderOperation.newInsert(dirUri).withValues(values).build());

                }
                try {
                    getContentResolver().applyBatch(ItemsContract.CONTENT_AUTHORITY, cpo);
                } catch (RemoteException | OperationApplicationException e) {
                    Log.e(TAG, "Error updating content.", e);
                }
                sendStickyBroadcast(
                        new Intent(BROADCAST_ACTION_STATE_CHANGE).putExtra(EXTRA_REFRESHING, false));
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, "Error fetching content.");
                sendStickyBroadcast(
                        new Intent(BROADCAST_ACTION_STATE_CHANGE).putExtra(EXTRA_REFRESHING, false));

            }
        });
    }
}