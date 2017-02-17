package uncc.inclass5;

/*
Juan Monsalve   800867107
Avinash Varanasi 800918984
Sanju Kurubara Budi Hall Hiriyanna Gowda 800953525
Aninditha /madishetty 800936606
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList <String> key_list = new ArrayList<String>();
    TextView view1;
    ImageView left, right;
    int Index = 1;
    String [] vals_List;
    ProgressDialog pd1,pd2;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_new);
        view1 = (TextView)findViewById(R.id.textView3);
        left = (ImageView) findViewById(R.id.imageLeft);
        right = (ImageView) findViewById(R.id.imageRight);
        pd2 = new ProgressDialog(this);
        pd2.setMessage("Loading Photo...");
        pd1 = new ProgressDialog(this);
        pd1.setMessage("Loading Dictionary...");


        key_list.add("UNCC");
        key_list.add("Android");
        key_list.add("WInter");
        key_list.add("Aurora");
        key_list.add("Wonders");

        left.setEnabled(false);
        right.setEnabled(false);


        if (isNetworkConnected()) {
            findViewById(R.id.buttonGo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                    final CharSequence[] cs = key_list.toArray(new CharSequence[key_list.size()]);
                    builder.setTitle("Choose a Keyword");
                    builder.setItems(cs, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            view1.setText(cs[i]);
                            new GetStringArray().execute("http://dev.theappsdr.com/apis/photos/index.php?keyword="+cs[i].toString());



                        }
                    });

                    builder.show();
                }
            });
            right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(Index < vals_List.length-1){
                        Index++;
                        new GetBitmap().execute(vals_List[Index]);

                    }else if(Index == vals_List.length-1){
                        Index = 1;
                        new GetBitmap().execute(vals_List[Index]);
                    }
                }
            });


            left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(Index > 1){
                        Index--;
                        new GetBitmap().execute(vals_List[Index]);

                    }else if(Index == 1){
                        Index = vals_List.length-1;

                        new GetBitmap().execute(vals_List[Index]);
                    }
                }
            });




        } else
            Toast.makeText(getApplicationContext(), "No Network Connection", Toast.LENGTH_LONG).show();


    }






    private class GetStringArray extends AsyncTask<String,Void,String []>
    {
        BufferedReader reader = null;
        @Override
        protected String [] doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                publishProgress();
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                reader = new BufferedReader (new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = "";
                while((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                vals_List = sb.toString().split(";");
                Bitmap image = BitmapFactory.decodeStream(connection.getInputStream());
                return  vals_List;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
            }
            return null;
        }

        @Override
        protected void onPostExecute(String [] s) {
            pd1.dismiss();
            if (s != null)
            {
                Log.d("demo","entered"+s.length);
                if(s.length > 2){
                    left.setEnabled(true);
                    right.setEnabled(true);
                    new GetBitmap().execute(s[1]);
                }else if (s.length <= 2){
                    Log.d("demo","no"+s.length);
                    Toast.makeText(getApplicationContext(),"No Image is Found",Toast.LENGTH_LONG).show();
                }
            }
            else
                Toast.makeText(getApplicationContext(),"No Image is Found",Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            pd1.show();
        }
    }

    private class GetBitmap extends AsyncTask<String, Void,Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                publishProgress();
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                Bitmap image = BitmapFactory.decodeStream(connection.getInputStream());
                return image;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap s) {
            pd2.dismiss();

            if (s != null) {
                ImageView iv = (ImageView) findViewById(R.id.imageView3);
                iv.setImageBitmap(s);
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            pd2.show();
        }
    }

    private boolean isNetworkConnected()
    {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
        {
            return true;
        }
        return false;
    }
}
