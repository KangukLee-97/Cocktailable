package com.example.cocktail;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FirstQuestionActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    private Button exitButton;
    private Button submitButton;
    private TextView cocktailName;
    private ListView glassList;
    private ArrayList<String> list;
    private ArrayList<String> cockNameArr;

    public static String clickedGlass;
    public static String cockName;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_first);

        exitButton = (Button)findViewById(R.id.question1_exit_btn);
        exitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showExitMessage();
            }
        });

        cocktailName = (TextView)findViewById(R.id.question1_title);

        cockNameArr = new ArrayList<String>();
        db = FirebaseFirestore.getInstance();
        db.collection("cocktail")
                .whereEqualTo("TPO", "Yes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                String name = document.getData().get("Name").toString();
                                cockNameArr.add(name);   // ???????????? ????????? ????????? ????????????
                            }
                        } else {
                            Log.w("tag", "Error getting documents", task.getException());
                        }

                        int length = cockNameArr.size();
                        int randomIdx = (int)(Math.random() * (length+1));
                        // Log.w("tag8", "Result: " + cockNameArr);
                        cockName = cockNameArr.get(randomIdx).toString();
                        cocktailName.setText(cockName);
                    }
                });

        glassList = (ListView)findViewById(R.id.glass_listview);
        list = new ArrayList<String>();

        db.collection("glass")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                String name = document.getData().get("Name").toString();
                                list.add(name);   // ???????????? ????????? ????????? ????????????
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, list);
                            glassList.setAdapter(adapter);

                            // ????????? ????????? ???????????? ??????
                            submitButton = (Button)findViewById(R.id.submit);

                            glassList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id){
                                    clickedGlass = adapterView.getItemAtPosition(position).toString();
                                    // Log.w("tag2", "Result: " + clickedGlass);
                                    submitButton.setOnClickListener(new View.OnClickListener(){   // ????????? ?????? ??? ???????????? ??????
                                        @Override
                                        public void onClick(View v){
                                            db.collection("cocktail")
                                                    .whereEqualTo("Name", cocktailName.getText())
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if(task.isSuccessful()){
                                                                for(QueryDocumentSnapshot document : task.getResult()){
                                                                    if(clickedGlass.equalsIgnoreCase(document.getData().get("Glass").toString())){   // Glass??? ????????? ??????
                                                                        // Toast.makeText(getApplicationContext(), "???????????????", Toast.LENGTH_SHORT).show();
                                                                        // startTpoActivity();
                                                                        startSecondQuestionActivity();
                                                                    }
                                                                    else
                                                                        Toast.makeText(getApplicationContext(), "???????????????! Glass??? ?????? ??????????????????.", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        }
                                                    });
                                        }
                                    });
                                }
                            });
                        } else {
                            Log.w("tag", "Error getting documents", task.getException());
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        showExitMessage();
    }

    // ???????????? ?????? ????????? ????????? ????????? ???????????? function
    public void showExitMessage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("??????");
        builder.setMessage("??????????????? ???????????? ????????????????????????. ?????????????????????????");
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        builder.setPositiveButton("???", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                // ?????? ????????? ??????? => Main?????? ???????
                startMainActivity();
                Toast.makeText(getApplicationContext(), "????????? ?????????????????????.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("?????????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // ????????? ????????? ????????? ??????? => ????????? ??????
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // ?????? ???????????? ??????
    private void startMainActivity() {
        Intent intent=new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // tpo ?????? ????????? ??????
    private void startTpoActivity() {
        Intent intent = new Intent(this, FinalQuestionActivity.class);
        startActivity(intent);
    }

    // ???????????? + ???????????? ????????? ??????
    private void startSecondQuestionActivity() {
        Intent intent = new Intent(this, SecondQuestionActivity.class);
        startActivity(intent);
    }

    // ListView?????? ????????? glass ?????? Return
    public static String getClickedGlass() {
        return clickedGlass;
    }

    // Cocktail ?????? Return
    public static String getCocktailName() { return cockName; }
}