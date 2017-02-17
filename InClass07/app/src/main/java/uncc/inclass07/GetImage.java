/*
Assignment: 07
Name: Sanju Kurubara Budi Hall Hriyanna Gowda
 */
package uncc.inclass07;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by TVS on 23/9/16.
 */

public class GetImage extends AsyncTask<String, Void, Bitmap> {
    MainActivity context;
    NewsDetails newsContext;
    LinearLayout layout;
    int setImageView;

    public GetImage(MainActivity context,int iv) {
        this.context = context;
        //this.layout = layout;
        setImageView= iv;
    }
    public GetImage(NewsDetails context){//,ImageView iv) {
        this.newsContext = context;
        //this.layout = layout;
        //setImageView= iv;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        Bitmap image;
        try {
            URL imageUrl = new URL(params[0]);
            HttpURLConnection connexion = (HttpURLConnection) imageUrl.openConnection();
            connexion.setRequestMethod("GET");
            connexion.connect();
            int responseCode = connexion.getResponseCode();
            if(responseCode != HttpURLConnection.HTTP_OK) return null;
            image = BitmapFactory.decodeStream(imageUrl.openStream());
            return image;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap response) {
        if(context!=null)
        {
            ((ImageView)context.findViewById(setImageView)).setImageBitmap(response);
            context.setImage(response);
        }
        else {
            //  setImageView.setImageBitmap(response);
            newsContext.setNewsImage(response);
        }

    }
}
