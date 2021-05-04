package com.atisapp.jazbeh.MainFragment.ui.profile;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import com.atisapp.jazbeh.Questionnaire.QuestionApiService;
import com.atisapp.jazbeh.Questionnaire.QuestionModel;
import com.atisapp.jazbeh.Questionnaire.QuestionSharedPref;
import com.atisapp.jazbeh.R;
import com.atisapp.jazbeh.Storage.Prefs.IdentitySharedPref;

public class UpdateFileActivity extends AppCompatActivity {

    private static final String TAG = "UpdateFileActivity";
    private Context context;
    private QuestionSharedPref questionSharedPref;
    private CheckBox age,heart,epilepsy,hysteria,bipolar,hearing;
    private Button questionFinishBtn;
    private TextView file_check_title;
    private QuestionApiService profileInfoApiService;
    private IdentitySharedPref identitySharedPref;
    private QuestionModel questionModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_file);

        SetupViews();

        questionFinishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateFileInfo();
            }
        });

    }

    private void SetupViews()
    {
        context = UpdateFileActivity.this;
        questionSharedPref = new QuestionSharedPref(context);
        identitySharedPref = new IdentitySharedPref(context);
        profileInfoApiService = new QuestionApiService(context);

        age = (CheckBox) findViewById(R.id.update_file_check_age);
        heart = (CheckBox) findViewById(R.id.update_file_check_heartDisease);
        epilepsy = (CheckBox) findViewById(R.id.update_file_check_epilepsy);
        hysteria = (CheckBox) findViewById(R.id.update_file_check_hysteria);
        bipolar = (CheckBox) findViewById(R.id.update_file_check_bipolar);
        hearing = (CheckBox) findViewById(R.id.update_file_check_hearing);
        file_check_title = (TextView) findViewById(R.id.update_file_check_title);
        questionFinishBtn = (Button) findViewById(R.id.update_file_check_finish_btn);

        SetupData();
    }

    private void SetupData()
    {

        if(questionSharedPref.getAge())
            age.setChecked(true);
        if(questionSharedPref.getHeartDisease())
            heart.setChecked(true);
        if(questionSharedPref.getEpilepsy())
            epilepsy.setChecked(true);
        if(questionSharedPref.getHysteria())
            hysteria.setChecked(true);
        if(questionSharedPref.getBipolar())
            bipolar.setChecked(true);
        if(questionSharedPref.getHearing())
            hearing.setChecked(true);

    }

    private void UpdateFileInfo()
    {
        questionModel = new QuestionModel();

        questionModel.setAge(age.isChecked());
        questionModel.setHeartDisease(heart.isChecked());
        questionModel.setEpilepsy(epilepsy.isChecked());
        questionModel.setHysteria(hysteria.isChecked());
        questionModel.setBipolar(bipolar.isChecked());
        questionModel.setHearing(hearing.isChecked());

        profileInfoApiService.sendFile(questionModel, new QuestionApiService.onGetFileResponse() {
            @Override
            public void onGet(boolean response) {
                if(response)
                {
                    identitySharedPref.setDocument(1);
                    Toast.makeText(context,"پرونده شما با موفقیت بروز رسانی شد",Toast.LENGTH_SHORT);
                    questionSharedPref.setAge(questionModel.getAge());
                    questionSharedPref.setHeartDisease(questionModel.isHeartDisease());
                    questionSharedPref.setEpilepsy(questionModel.isEpilepsy());
                    questionSharedPref.setHysteria(questionModel.isHysteria());
                    questionSharedPref.setBipolar(questionModel.isBipolar());
                    questionSharedPref.setHearing(questionModel.isHearing());
                    finish();
                }
                else {
                    Toast.makeText(context,"اختلال در بروز رسانی پرونده ، لطفا دوباره تلاش نمایید",Toast.LENGTH_SHORT);
                }
            }
        });
    }
}
