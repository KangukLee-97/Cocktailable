package com.example.cocktail;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
    private Button selectButton;
    private TextView cocktailName;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question1);

        exitButton = (Button)findViewById(R.id.question1_exit_btn);
        exitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showExitMessage();
            }
        });

        cocktailName = (TextView)findViewById(R.id.question1_title);

        ArrayList<String> cockNameArr = new ArrayList<String>();
        db = FirebaseFirestore.getInstance();
        db.collection("cocktail")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                String name = document.getData().get("Name").toString();
                                cockNameArr.add(name);   // 리스트에 칵테일 이름들 추가하기
                            }
                        } else {
                            Log.w("tag", "Error getting documents", task.getException());
                        }

                        int length = cockNameArr.size();
                        int randomIdx = (int)(Math.random() * (length+1));
                        cocktailName.setText("Cocktail Name: " + cockNameArr.get(randomIdx).toString());
                    }
                });

        selectButton = findViewById(R.id.question1_select_btn);
        selectButton.setOnClickListener(new View.OnClickListener(){
            // 글래스 선택하기 버튼 클릭시 -> activity_glass.xml로 이동
            @Override
            public void onClick(View v){
                startGlassActivity();
            }
        });
    }

    // 종료하기 버튼 클릭시 종료할 것인지 물어보는 function
    public void showExitMessage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("안내");
        builder.setMessage("종료하시면 처음부터 응시하셔야합니다. 종료하시겠습니까?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        builder.setPositiveButton("예", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                // 예를 눌렀을 경우? => Main으로 다시?
            }
        });

        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 아니오 버튼을 눌렀을 경우? => 그대로 진행
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // Glass activity로 이동
    private void startGlassActivity() {
        Intent intent=new Intent(this, GlassActivity.class);
        startActivity(intent);
    }
}