package uncc.studentregistration;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String STUDENT_INFO_ID_KEY = "Student Info Id";
    public static final String STUDENT_INFO_CHANGED_KEY = "Student Info changed Id";
    public static final int REQUEST_CODE = 100;

    TextView textViewName;
    TextView textViewEmail;
    TextView textViewDept;
    TextView textViewMood;
    ImageView imageViewNameEdit;
    ImageView imageViewEmailEdit;
    ImageView imageViewDeptEdit;
    ImageView imageViewMoodEdit;
    StudentInformation studentInformation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewEmail = (TextView) findViewById(R.id.textViewEmail);
        textViewDept = (TextView) findViewById(R.id.textViewDept);
        textViewMood = (TextView) findViewById(R.id.textViewMood);

        studentInformation = (StudentInformation) getIntent().getExtras().getSerializable(MainActivity.STUDENT_INFO_KEY);
        textViewName.setText(getString(R.string.stringId_name) + ": " + studentInformation.getName());
        textViewEmail.setText(getString(R.string.stringIdEmail) + ": " + studentInformation.getEmail());
        textViewDept.setText(getString(R.string.stringIdDepartment) + ": " + studentInformation.getDepartment());
        textViewMood.setText(getString(R.string.stringIdMoodDisplay) + ": " + studentInformation.getMood() + "% Positive");

        imageViewNameEdit = (ImageView)findViewById(R.id.imageViewName);
        imageViewNameEdit.setOnClickListener(this);

        imageViewEmailEdit = (ImageView)findViewById(R.id.imageViewEmail);
        imageViewEmailEdit.setOnClickListener(this);

        imageViewDeptEdit = (ImageView)findViewById(R.id.imageViewDept);
        imageViewDeptEdit.setOnClickListener(this);

        imageViewMoodEdit = (ImageView)findViewById(R.id.imageViewMood);
        imageViewMoodEdit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent("uncc.studentregistration.intent.action.VIEW");
        intent.putExtra(DisplayActivity.STUDENT_INFO_ID_KEY, v.getId());
        switch(v.getId())
        {
            case R.id.imageViewName:
                intent.putExtra(DisplayActivity.STUDENT_INFO_CHANGED_KEY, studentInformation.getName());
                break;
            case R.id.imageViewEmail:
                intent.putExtra(DisplayActivity.STUDENT_INFO_CHANGED_KEY, studentInformation.getEmail());
                break;
            case R.id.imageViewDept:
                intent.putExtra(DisplayActivity.STUDENT_INFO_CHANGED_KEY, studentInformation.getDepartment());
                break;
            case R.id.imageViewMood:
                intent.putExtra(DisplayActivity.STUDENT_INFO_CHANGED_KEY, studentInformation.getMood());
                break;

        }
        startActivityForResult(intent,REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_CODE)
    {
        if( resultCode == RESULT_OK)
        {
            String id = data.getExtras().getString(DisplayActivity.STUDENT_INFO_ID_KEY);
            String value = (String) data.getExtras().getString(DisplayActivity.STUDENT_INFO_CHANGED_KEY);
            switch(Integer.parseInt(id))
            {
                case R.id.imageViewName:
                    textViewName.setText(getString(R.string.stringId_name) + ": " + value);
                    studentInformation.setName(value);
                    break;
                case R.id.imageViewEmail:
                    textViewEmail.setText(getString(R.string.stringIdEmail) + ": " + value);
                    studentInformation.setEmail(value);
                    break;
                case R.id.imageViewDept:
                    textViewDept.setText(getString(R.string.stringIdDepartment) + ": " + value);
                    studentInformation.setDepartment(value);
                    break;
                case R.id.imageViewMood:
                    textViewMood.setText(getString(R.string.stringIdMoodDisplay) + ": " + value + "% Positive");
                    studentInformation.setMood(value);
                    break;
                default:
                    break;
            }
        }
        else
        {
            Toast.makeText(DisplayActivity.this, "No changes done", Toast.LENGTH_SHORT).show();
        }
    }

    }
}
