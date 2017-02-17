/*
Assignment No: 3
File Name: MainActivity.java
Name: Sanju Kurubara Budi Hall Hiriyanna Gowda
      Sujal T Vijayaraghavan
 */
package uncc.studentregistration;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    EditText editTextName;
    EditText editTextEmail;
    RadioGroup radioGroupDept;
    SeekBar seekBarMood;
    Button buttonSubmit;

    public static final String STUDENT_INFO_KEY = "Student Information";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextName = (EditText)findViewById(R.id.editTextName);
        editTextEmail = (EditText)findViewById(R.id.editTextEmail);
        radioGroupDept = (RadioGroup) findViewById(R.id.radioGroupId);
        seekBarMood = (SeekBar) findViewById(R.id.seekBarMood);
        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);

        buttonSubmit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        String name = editTextName.getText().toString();
        String email = editTextEmail.getText().toString();

        //error handling
        if (name.length() == 0)
        {
            Toast.makeText(MainActivity.this, "Name Should not be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (email.length() == 0)
        {
            Toast.makeText(MainActivity.this, "Please enter email id", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (email.contains("@") == false)
        {
            Toast.makeText(MainActivity.this, "Please enter proper email id", Toast.LENGTH_SHORT).show();
            return;
        }


        String seekBarValue = String.valueOf(seekBarMood.getProgress());
        RadioButton radioButton = (RadioButton) findViewById(radioGroupDept.getCheckedRadioButtonId());
        String dept = (String) radioButton.getText();

        StudentInformation studentInformation = new StudentInformation(name,email,dept,seekBarValue);

        Intent intent = new Intent(MainActivity.this, DisplayActivity.class);
        intent.putExtra(STUDENT_INFO_KEY, studentInformation);
        startActivity(intent);
    }
}
