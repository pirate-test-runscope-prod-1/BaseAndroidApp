package packagename.app.com.appname.core.module;

import android.content.Context;
import android.util.Log;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import packagename.app.com.appname.BuildConfig;
import packagename.app.com.appname.R;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

@Module (complete = false, library = true)
public class WebserviceModule {

   public static final int CACHE_SIZE = 25 * 1024 * 1024;

   /* TODO: Add here provides methods for retrofit interfaces. */

   @Provides
   @Singleton
   Picasso providePicasso(Context context) {
      Picasso.Builder imageLoaderBuilder = new Picasso.Builder(context);
      imageLoaderBuilder.executor(Executors.newSingleThreadExecutor());
      return imageLoaderBuilder.build();
   }

   @Provides
   @Singleton
   RestAdapter provideRestAdapter(Context context) {
      RestAdapter.Builder builder = new RestAdapter.Builder();
      builder.setEndpoint(context.getString(R.string.base_url));
      if (BuildConfig.DEBUG) {
         builder.setLogLevel(RestAdapter.LogLevel.FULL);
      } else {
         builder.setLogLevel(RestAdapter.LogLevel.NONE);
      }
      OkHttpClient client = getHttpClient(context);
      builder.setClient(new OkClient(client));
      return builder.build();
   }

   private OkHttpClient getHttpClient(Context context) {
      File cacheDirectory = new File(context.getCacheDir().getAbsolutePath(), "HttpCache");
      Cache cache = null;
      try {
         cache = new Cache(cacheDirectory, CACHE_SIZE);
      } catch (IOException e) {
         Log.e(getClass().getSimpleName(), "Could not create http cache", e);
      }
      OkHttpClient client = new OkHttpClient();
      if (cache != null) {
         client.setCache(cache);
      }
      return client;
   }
}


