package com.example.tvs.inclass09;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MessagesActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView logout, send, attach;
    ListView messageListView;
    LoginResp loginResp;
    EditText messageField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        logout = (ImageView) findViewById(R.id.logout);
        send = (ImageView) findViewById(R.id.send);
        attach = (ImageView) findViewById(R.id.attach);
        messageField = (EditText) findViewById(R.id.messageField);

        messageListView = (ListView) findViewById(R.id.messageListView);

        logout.setOnClickListener(this);
        send.setOnClickListener(this);
        attach.setOnClickListener(this);


        loginResp = (LoginResp) getIntent().getSerializableExtra("RESPONSE");
        ((TextView) findViewById(R.id.bobSmith)).setText(loginResp.getUserFname() + " " + loginResp.getUserLname());
        new MessageTask(this).execute(loginResp.getToken());
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.logout:   MainActivity.editor.remove("TOKEN");
                                finish();
                                break;

            case R.id.send:     String messageText = messageField.getText().toString();
                                if(messageText.length() == 0) {
                                    Toast.makeText(this, "Message is empty", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                new SendMessage()
                                        .execute(MainActivity.ADD_MESSAGE, loginResp.getToken(), messageText);
                                break;
            case R.id.attach:   Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);//
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),200);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 200:   Log.d("demo", "Coming");
                        break;
        }
    }

    public class MessageTask extends AsyncTask<String, Void, ArrayList<Message.MessageDetails>> {

        private final OkHttpClient client = new OkHttpClient();
        Context context;

        public MessageTask(Context context) {
            this.context = context;
        }

        @Override
        protected ArrayList<Message.MessageDetails> doInBackground(String... params) {
            Request request = new Request.Builder()
                    .url(MainActivity.MESSAGE_URL)
                    .header("Authorization", "BEARER " + params[0])
                    .build();

            Response response = null;
            try {
                response = client.newCall(request).execute();
                if (!response.isSuccessful())
                    throw new IOException("Unexpected code " + response);

                Gson gson = new Gson();
                Message messageList = gson.fromJson(response.body().string(), Message.class);
                return messageList.getMessages();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Message.MessageDetails> messageDetails) {
            MessageLVAdapter messageLVAdapter = new MessageLVAdapter(context, R.layout.message_view_adapter, messageDetails);
            messageLVAdapter.setNotifyOnChange(true);
            messageListView.setAdapter(messageLVAdapter);
        }
    }

    public class SendMessage extends AsyncTask<String, Void, AddMessage> {
        private final OkHttpClient client = new OkHttpClient();

        @Override
        protected AddMessage doInBackground(String... params) {
            RequestBody formBody = new FormBody.Builder()
                    .add("Type", "TEXT")
                    .add("Comment", params[2])
                    .add("FileThumbnailId", "")
                    .build();

            Request request = new Request.Builder()
                    .url(params[0])
                    .header("Authorization", "BEARER " + params[1])
                    .post(formBody)
                    .build();

            Response response = null;
            try {
                response = client.newCall(request).execute();
                if (!response.isSuccessful())
                    throw new IOException("Unexpected code " + response);

                Gson gson = new Gson();
                AddMessage addMessage = gson.fromJson(response.body().string(), AddMessage.class);
                return addMessage;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(AddMessage addMessage) {
            if(addMessage.getStatus().toLowerCase().equals("ok")) {
                Toast.makeText(MessagesActivity.this, "Message sent successfully", Toast.LENGTH_SHORT).show();
                messageField.setText(" ");
            }
            else Toast.makeText(MessagesActivity.this, "Sending failed", Toast.LENGTH_SHORT).show();
        }
    }
}
