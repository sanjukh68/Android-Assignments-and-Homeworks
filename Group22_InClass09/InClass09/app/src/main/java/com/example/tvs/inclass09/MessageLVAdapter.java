package com.example.tvs.inclass09;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MessageLVAdapter extends ArrayAdapter<Message.MessageDetails> {

    Context context;
    int resource;
    List<Message.MessageDetails> objects;
    ImageView thumbnail;


    public MessageLVAdapter(Context context, int resource, List<Message.MessageDetails> objects) {
        super(context, resource, objects);

        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(resource, parent, false);
        }

        if(objects.get(position).getType().equals("TEXT")) {
            ((TextView) convertView.findViewById(R.id.message)).setText(objects.get(position).getComment() + "");
//            (convertView.findViewById(R.id.thumbnail)).setVisibility(View.GONE);
        }
        else {
//            (convertView.findViewById(R.id.message)).setVisibility(View.GONE);
            thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);
            new GetImage()
                    .execute(MainActivity.FILE_URL + objects.get(position).getFileThumbnailId(), MainActivity.sharedPref.getString("TOKEN", "null"));
        }

        ((TextView) convertView.findViewById(R.id.senderName))
                .setText(objects.get(position).getUserFname() + objects.get(position).getUserLname());

        try {
            ((TextView) convertView.findViewById(R.id.timeSent))
                    .setText(new PrettyTime().format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                            .parse(objects.get(position).getCreatedAt())));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    public class GetImage extends AsyncTask<String, Void, Bitmap> {
        private final OkHttpClient client = new OkHttpClient();

        @Override
        protected Bitmap doInBackground(String... params) {
            Request request = new Request.Builder()
                    .url(params[0])
                    .header("Authorization", "BEARER " + params[1])
                    .build();

            Response response = null;
            try {
                response = client.newCall(request).execute();
                if (!response.isSuccessful())
                    throw new IOException("Unexpected code " + response);

                InputStream in = response.body().byteStream();
                Bitmap image = BitmapFactory.decodeStream(in);
                return image;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            thumbnail.setImageBitmap(bitmap);
        }
    }
}
