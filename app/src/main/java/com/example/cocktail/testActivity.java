package com.example.cocktail;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.auth.FirebaseAuth;

public class testActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private View drawerView;
    private ListView list;
    private String [] menu = {"레시피","커스텀 레시피","마이페이지","주조기능사","로그아웃"};

    private Button testStartBtn;   // 조주기능사 실기 연습 시작버튼

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bartender_question);

        //맨 위에 툴바 적용
        androidx.appcompat.widget.Toolbar toolbar;
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //navigation menu
        drawerLayout=(DrawerLayout)findViewById(R.id.drawerquestion);
        drawerView=(View)findViewById(R.id.drawer);
        list = (ListView) findViewById (R.id.list);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1 ,menu);
        list.setAdapter (adapter1);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (menu[position]){
                    case "레시피":
                        startRecipeActivity();
                        break;
                    case "마이페이지":
                        startUserInfoActivity();
                        break;
                    case "커스텀 레시피":
                        startCustomActivity();
                        break;
                    case "주조기능사":
                        startTestActivity();
                        break;
                    case "로그아웃":
                        FirebaseAuth.getInstance().signOut();
                        startLoginActivity();
                        break;
                }
            }
        });

        testStartBtn = (Button)findViewById(R.id.question_start_btn);
        testStartBtn.setOnClickListener(new View.OnClickListener(){   // 시작하기 버튼 클릭 -> 첫 번째 문제 시작
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), FirstQuestionActivity.class);
                startActivity(intent);
            }
        });
    }

    //툴바 왼쪽에 아이콘 클릭시 navigation menu 열리도록 만듦
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(drawerView);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(this, MainActivity.class);
        //Main activity에서 뒤로가기를 눌렀을 시 앱 이 종료되도록 여태까지 stack에 쌓였던 activity를 없앰
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    //로그인 activity로 이동
    private void startLoginActivity() {
        Intent intent=new Intent(this, loginActivity.class);
        startActivity(intent);
    }

    //recipe activity로 이동
    private void startRecipeActivity() {
        Intent intent=new Intent(this, RecipeActivity.class);
        startActivity(intent);
    }

    //recipe activity로 이동
    private void startUserInfoActivity() {
        Intent intent=new Intent(this, UserInfoActivity.class);
        startActivity(intent);
    }
    //custom recipe activity로 이동
    private void startCustomActivity() {
        Intent intent=new Intent(this, CustomActivity.class);
        startActivity(intent);
    }
    // test activity로 이동
    private void startTestActivity() {
        Intent intent = new Intent(this, testActivity.class);
        startActivity(intent);
    }
}
