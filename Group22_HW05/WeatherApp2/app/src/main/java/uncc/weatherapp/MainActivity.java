package uncc.weatherapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    ListView favoriteList;
    EditText textViewCity;
    EditText textViewState;
    Button buttonSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        favoriteList = (ListView) findViewById(R.id.istViewFavorites);
        favoriteList.setEmptyView((TextView)findViewById(R.id.empty));

        textViewCity = (EditText)findViewById(R.id.editTextCityName);
        textViewState = (EditText)findViewById(R.id.editTextState);

        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.buttonSubmit)
                {
                    StringBuilder url = new StringBuilder("http://api.wunderground.com/api/a55aa943dce36c56/hourly/q/");
                    String state = String.valueOf(textViewState.getText());
                    Log.d("demo", "state "+state);
                    url.append(state);
                    url.append("/");
                    url.append(textViewCity.getText().toString().trim());
                    url.append(".json");

                    Log.d("demo", url.toString());
                    new WeatherData(MainActivity.this).execute(url.toString());
                }
            }
        });
    }
}
