package com.atisapp.jazbeh.Questionnaire;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import com.atisapp.jazbeh.MainFragment.HomeActivity;
import com.atisapp.jazbeh.R;
import com.atisapp.jazbeh.Storage.Prefs.IdentitySharedPref;

public class QuestionActivity extends AppCompatActivity {

    private static final String TAG = "QuestionActivity";
    private Context context;
    private QuestionSharedPref questionSharedPref;
    private CheckBox age,heart,epilepsy,hysteria,bipolar,hearing;
    private Button questionFinishBtn;
    private TextView file_check_title;
    private QuestionApiService questionApiService;
    private IdentitySharedPref identitySharedPref;
    private QuestionModel questionModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        SetupViews();
        ChangeLanguage();

        questionFinishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                questionModel = new QuestionModel();

                questionModel.setAge(age.isChecked());
                questionModel.setHeartDisease(heart.isChecked());
                questionModel.setEpilepsy(epilepsy.isChecked());
                questionModel.setHysteria(hysteria.isChecked());
                questionModel.setBipolar(bipolar.isChecked());
                questionModel.setHearing(hearing.isChecked());

                questionApiService.sendFile(questionModel,new QuestionApiService.onGetFileResponse() {
                    @Override
                    public void onGet(boolean response) {
                        if(response)
                        {
                            Toast.makeText(context,"پرونده شما با موفقیت بروز رسانی شد",Toast.LENGTH_SHORT);
                            identitySharedPref.setDocument(1);
                            questionSharedPref.setAge(questionModel.getAge());
                            questionSharedPref.setHeartDisease(questionModel.isHeartDisease());
                            questionSharedPref.setEpilepsy(questionModel.isEpilepsy());
                            questionSharedPref.setHysteria(questionModel.isHysteria());
                            questionSharedPref.setBipolar(questionModel.isBipolar());
                            questionSharedPref.setHearing(questionModel.isHearing());
                        }

                        Intent intent = new Intent(QuestionActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

            }
        });

    }

    private void SetupViews()
    {
        context = QuestionActivity.this;
        questionSharedPref = new QuestionSharedPref(context);
        identitySharedPref = new IdentitySharedPref(context);
        questionApiService = new QuestionApiService(context);

        age = (CheckBox) findViewById(R.id.file_check_age);
        heart = (CheckBox) findViewById(R.id.file_check_heartDisease);
        epilepsy = (CheckBox) findViewById(R.id.file_check_epilepsy);
        hysteria = (CheckBox) findViewById(R.id.file_check_hysteria);
        bipolar = (CheckBox) findViewById(R.id.file_check_bipolar);
        hearing = (CheckBox) findViewById(R.id.file_check_hearing);
        file_check_title = (TextView) findViewById(R.id.file_check_title);
        questionFinishBtn = (Button) findViewById(R.id.file_check_finish_btn);



    }

    private void ChangeLanguage()
    {
        if(identitySharedPref.getLanguage().equals("en"))
        {
            age.setText("Your age is under 15 years old");
            heart.setText("If you have a history of heart disease");
            epilepsy.setText("If you have epileptic disease history");
            hysteria.setText("If you have a history of hysteria");
            bipolar.setText("If you have dipolar disorder");
            hearing.setText("If you have a hearing problem");

            file_check_title.setText("Check if there are any problems");
            questionFinishBtn.setText("Finish");

        }
    }
}
