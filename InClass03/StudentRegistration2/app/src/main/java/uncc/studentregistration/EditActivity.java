/*
Assignment No: 3
File Name: EditActivity.java
Name: Sanju Kurubara Budi Hall Hiriyanna Gowda
      Sujal T Vijayaraghavan
 */
package uncc.studentregistration;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

public class EditActivity extends AppCompatActivity implements View.OnClickListener{

    EditText editTextName;
    EditText editTextEmail;
    RadioGroup radioGroupDept;
    SeekBar seekBarMood;
    Button buttonSave;
    TextView textViewDepartment;
    TextView textViewMood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        editTextName = (EditText)findViewById(R.id.editTextName);
        editTextEmail = (EditText)findViewById(R.id.editTextEmail);
        radioGroupDept = (RadioGroup) findViewById(R.id.radioGroupId);
        seekBarMood = (SeekBar) findViewById(R.id.seekBarMood);
        textViewDepartment = (TextView)findViewById(R.id.textViewDepartment);
        textViewMood = (TextView)findViewById(R.id.textViewMood);
        buttonSave = (Button) findViewById(R.id.buttonSubmit);

        buttonSave.setOnClickListener(this);

        int id = (int) getIntent().getExtras().getSerializable(DisplayActivity.STUDENT_INFO_ID_KEY);
        String value = (String) getIntent().getExtras().getSerializable(DisplayActivity.STUDENT_INFO_CHANGED_KEY);

        switch (id)
        {
            case R.id.imageViewName:
                editTextEmail.setVisibility(View.INVISIBLE);
                radioGroupDept.setVisibility(View.INVISIBLE);
                seekBarMood.setVisibility(View.INVISIBLE);
                textViewDepartment.setVisibility(View.INVISIBLE);
                textViewMood.setVisibility(View.INVISIBLE);
                editTextName.setText(value);
                break;
            case R.id.imageViewEmail:
                editTextName.setVisibility(View.INVISIBLE);
                radioGroupDept.setVisibility(View.INVISIBLE);
                seekBarMood.setVisibility(View.INVISIBLE);
                textViewDepartment.setVisibility(View.INVISIBLE);
                textViewMood.setVisibility(View.INVISIBLE);
                editTextEmail.setText(value);
                break;
            case R.id.imageViewDept:
                editTextName.setVisibility(View.INVISIBLE);
                editTextEmail.setVisibility(View.INVISIBLE);
                seekBarMood.setVisibility(View.INVISIBLE);
                textViewMood.setVisibility(View.INVISIBLE);

                if (value.equals("SIS"))
                {
                    radioGroupDept.check(R.id.radioButtonSIS);
                }
                else if (value.equals("CS"))
                {
                    radioGroupDept.check(R.id.radioButtonCS);
                }
                else if (value.equals("BIO"))
                {
                    radioGroupDept.check(R.id.radioButtonBIO);
                }
                else
                {
                    radioGroupDept.check(R.id.radioButtonOthers);
                }
                break;
            case R.id.imageViewMood:
                editTextName.setVisibility(View.INVISIBLE);
                editTextEmail.setVisibility(View.INVISIBLE);
                radioGroupDept.setVisibility(View.INVISIBLE);
                textViewDepartment.setVisibility(View.INVISIBLE);
                seekBarMood.setProgress(Integer.parseInt(value.toString()));
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        int id = (int) getIntent().getExtras().getSerializable(DisplayActivity.STUDENT_INFO_ID_KEY);
        intent.putExtra(DisplayActivity.STUDENT_INFO_ID_KEY, String.valueOf(id));
        if (editTextName.getVisibility() == View.VISIBLE)
        {
            intent.putExtra(DisplayActivity.STUDENT_INFO_CHANGED_KEY, editTextName.getText().toString());
            setResult(RESULT_OK, intent);
        }
        else if (editTextEmail.getVisibility() == View.VISIBLE)
        {
            intent.putExtra(DisplayActivity.STUDENT_INFO_CHANGED_KEY, editTextEmail.getText().toString());
            setResult(RESULT_OK, intent);
        }
        else if (radioGroupDept.getVisibility() == View.VISIBLE)
        {
            String value = null;
            if (radioGroupDept.getCheckedRadioButtonId() == R.id.radioButtonSIS)
            {
                value = "SIS";
            }
            else if (radioGroupDept.getCheckedRadioButtonId() == R.id.radioButtonCS)
            {
                value = "CS";
            }
            else if (radioGroupDept.getCheckedRadioButtonId() == R.id.radioButtonBIO)
            {
                value = "BIO";
            }
            else if (radioGroupDept.getCheckedRadioButtonId() == R.id.radioButtonOthers)
            {
                value = "Others";
            }
            intent.putExtra(DisplayActivity.STUDENT_INFO_CHANGED_KEY, value);
            setResult(RESULT_OK, intent);
        }
        else if (seekBarMood.getVisibility() == View.VISIBLE)
        {
            intent.putExtra(DisplayActivity.STUDENT_INFO_CHANGED_KEY, String.valueOf(seekBarMood.getProgress()));
            setResult(RESULT_OK, intent);
        }
        else
        {
            setResult(RESULT_CANCELED);
        }

        finish();
    }
}
