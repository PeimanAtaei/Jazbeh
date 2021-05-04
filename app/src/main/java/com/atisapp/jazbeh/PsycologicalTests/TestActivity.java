package com.atisapp.jazbeh.PsycologicalTests;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atisapp.jazbeh.PsycologicalTests.model.AnswerModel;
import com.atisapp.jazbeh.PsycologicalTests.model.QuestionModel;
import com.atisapp.jazbeh.PsycologicalTests.model.ResultModel;
import com.atisapp.jazbeh.PsycologicalTests.model.TestModel;
import com.atisapp.jazbeh.R;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    private static final String TAG = "TestActivity";
    private Context context;
    private TestAPIService testAPIService;
    private String testId;
    private TestModel myTestModel;
    private List<QuestionModel> questionModelList;
    private List<AnswerModel> answerModelList;
    private List<ResultModel> resultModelList;
    private List<Integer> scoreList = new ArrayList<Integer>();
    LinearLayout.LayoutParams textViewParams;

    private CardView detailBox,questionBox,resultBox;
    private TextView detailTitle,detailDescription,detailQuestionCount,detailViewCount;
    private TextView questionNumber,questionBody;
    private TextView resultText;
    private Button startBtn,finishBtn;
    private ProgressDialog progressDialog;
    private LinearLayout buttonLayout;
    private int textSize;

    private int currentQuestion = 0,totalScore = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        SetupViews();

    }
    private void SetupViews()
    {
        context = TestActivity.this;
        testAPIService = new TestAPIService(context);

        detailBox = findViewById(R.id.test_detail_box);
        questionBox = findViewById(R.id.test_question_box);

        detailTitle = findViewById(R.id.test_card_title);
        detailDescription = findViewById(R.id.test_card_description);
        detailQuestionCount = findViewById(R.id.test_card_count);
        detailViewCount = findViewById(R.id.test_card_view_count);
        startBtn = findViewById(R.id.test_card_start_btn);

        buttonLayout = findViewById(R.id.button_layout);
        questionNumber = findViewById(R.id.test_card_question_number);
        questionBody = findViewById(R.id.test_card_question);

        resultBox = findViewById(R.id.test_result_box);
        resultText = findViewById(R.id.test_card_result);
        finishBtn = findViewById(R.id.test_card_finish_btn);

        Intent intent = getIntent();
        testId = intent.getStringExtra("testId");


        GetSingleTest();
    }

    private void GetSingleTest()
    {

        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("در حال دریافت اطلاعات آزمون");
            progressDialog.show();
        }

        testAPIService.GetSingleTest(testId, new TestAPIService.onGetSingleTest() {
            @Override
            public void onGet(boolean msg, TestModel testModel) {
                SetTestData(testModel);
                if(progressDialog != null)
                    progressDialog.dismiss();
            }
        });
    }

    private void SetTestData(TestModel testModel)
    {
        myTestModel = testModel;
        questionModelList = testModel.getQuestionModelList();
        resultModelList = testModel.getResultModelList();

        detailTitle.setText(testModel.getName());
        detailDescription.setText(testModel.getDescription());
        detailViewCount.setText("این آزمون در مجموع "+testModel.getUserCount()+" بار توسط کاربران دیگر انجام شده است ");
        detailQuestionCount.setText(testModel.getQuestionCount()+" سوال ");

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetQuestion();
                detailBox.setVisibility(View.GONE);
                questionBox.setVisibility(View.VISIBLE);
            }
        });

        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                testAPIService.AddTestCount(testId, new TestAPIService.onAddTestCount() {
                    @Override
                    public void onGet(boolean msg) {
                        Log.i(TAG, "onGet: add test count");
                    }
                });
                finish();
            }
        });
    }

    private void SetQuestion()
    {
        QuestionModel questionModel = questionModelList.get(currentQuestion);


        questionNumber.setText(" سوال شمار "+(currentQuestion+1));
        questionBody.setText(questionModel.getQuestionTitle());

        answerModelList = questionModel.getAnswerModelList();

        if(answerModelList.size()<=3)
        {
            textSize = 17;
        }else {
            textSize = 15;
        }
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/IRANSansMobile_Bold.ttf");
        textViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for (int i=0 ; i<=answerModelList.size() ; i++)
        {
            if (i != answerModelList.size())
            {
                final AnswerModel answerModel = answerModelList.get(i);

                //LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 90);
                textViewParams.setMargins(0,0,0,5);
                Button newButton = new Button(this);
                newButton.setText(answerModel.getAnswerName());
                newButton.setTextColor(Color.WHITE);
                newButton.setBackgroundResource(R.drawable.selector_download_botton);
                newButton.setTextSize(textSize);
                newButton.setGravity(Gravity.CENTER_HORIZONTAL);
                newButton.setLayoutParams(textViewParams);
                newButton.setTypeface(typeface);

                //final int num = i;

                buttonLayout.addView(newButton);

                final int finalI = i;
                newButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(currentQuestion == (questionModelList.size()-1)) {
                            Toast.makeText(context, " نتیجه آزمون روانشناسی شما ", Toast.LENGTH_LONG).show();
                        }
//                       else {
//                            Toast.makeText(context," سوال "+(currentQuestion+2),Toast.LENGTH_SHORT).show();
//                        }

                        buttonLayout.removeAllViews();
                        NextQuestion(answerModelList.get(finalI).getAnswerScore(),1);

                    }
                });
            }
            else if(i == answerModelList.size() && currentQuestion>0) {
                //LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 90);
                textViewParams.setMargins(0,0,0,5);
                Button newButton = new Button(this);
                newButton.setText("سوال قبل");
                newButton.setTextColor(Color.WHITE);
                newButton.setBackgroundResource(R.drawable.selector_botton);
                newButton.setTextSize(textSize);
                newButton.setGravity(Gravity.CENTER_HORIZONTAL);
                newButton.setLayoutParams(textViewParams);
                newButton.setTypeface(typeface);

                buttonLayout.addView(newButton);

                final int finalI = i-1;
                newButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(context," سوال "+(currentQuestion),Toast.LENGTH_SHORT).show();
                        buttonLayout.removeAllViews();
                        NextQuestion(answerModelList.get(finalI).getAnswerScore(),-1);

                    }
                });
            }


        }
    }

    private void NextQuestion(int score,int type)
    {
        if(type == 1)
        {
            //totalScore += score;
            scoreList.add(currentQuestion,score);
            currentQuestion++;

        }else {
            //totalScore -= score;
            currentQuestion--;
            scoreList.remove(currentQuestion);

        }

        CalculateScores();
        if(currentQuestion != (questionModelList.size()))
        {
            SetQuestion();
        }
        else {
            ShowResult();
        }


    }

    private void CalculateScores()
    {
        totalScore = 0;

        for (int i = 0 ; i < scoreList.size() ; i ++)
        {
            totalScore += scoreList.get(i);
        }
        Log.i(TAG, "CalculateScores: "+totalScore);
    }

    private void ShowResult()
    {
        if(myTestModel.getName().equals("آزمون مثلث عشق استرنبرگ"))
        {
            LoveRectangleResult();
        }else if(myTestModel.getName().equals("آزمون سلامت عمومی"))
        {
            GeneralHealthResult();
        }else {
            for(int i =0 ; i<resultModelList.size() ; i ++)
            {
                if(totalScore <= resultModelList.get(i).getResultTotal())
                {
                    resultText.setText(resultModelList.get(i).getResultText());
                    break;
                }
            }

            questionBox.setVisibility(View.GONE);
            resultBox.setVisibility(View.VISIBLE);
        }


    }




    // special tests -------------------------------------------------------------------------------


    private void LoveRectangleResult()
    {
        int love = 0 , sex= 0 , commitment = 0;
        String allPoints = "",loveResult = "",sexResult = "",commitmentResult = "",fullResult = "";


        for (int i =0 ; i<scoreList.size() ; i++)
        {
            if(i<15)
                love += scoreList.get(i);
            else if(i>= 15 && i<30)
                sex+= scoreList.get(i);
            else
                commitment+= scoreList.get(i);
        }

        allPoints = " نمره صمیمیت شما : "+love+"\n"+" نمره هوس و شهوت شما : "+sex+"\n"+" نمره تعهد شما به عشقتان : "+commitment;

        if(love < 70)
            loveResult = "فقدان صمیمیت : این رابطه عذاب آور است . گاهی اوقت انگیزه شدیدی مانند هوس شما را جذب یکدیگر می کند اما سرانجام به یاس و ناکامی منجر می شود . چون قادر به آن نمی باشید که رابطه تان را عمیق تر سازید یا علاقه های قلبی یکدیگر را بشناسید .\n\n";

        if(sex < 70)
            sexResult = "فقدان هوس : این رابطه در خطر فروپاشی قرار ندارد اما در صورتی که شما و کسی که به او علاقمند هستید وارد زندگی مشترک شده اید ، رابطه شما نیاز به خلاقیت و انگیزه برای شعله ور ساختن مجدد عشق می باشد . لازم به ذکر است دو صورتی که شما در یک رابطه غیر رسمی هستید (با فرد مورد نظر ازدواج نکرده اید) نیازی به تقویت بعد شهوانی رابطه خود ندارید زیرا در اکثر مواقع وجود رابطه جنسی در یک رابطه غیر رسمی نتیجه عکس داده و باعث عدم اطمینان و فروپاشی رابطه می شود.\n\n";

        if(commitment < 70)
            commitmentResult = "فقدان تعهد : رابطه شما یک شبه رابطه است . عشق و علاقه شدید حکم فرماست اما عدم امنیت از آن که رابطه تا چه مدت دوام خواهد داشت ، هر دو فرد را مایوس می کند.\n\n";

        if(love >= 70 && sex >= 70 && commitment >= 70)
            fullResult = "عشق + تعهد + هوس : رابطه شما یک نمونه کامل و مطلوب محصوب می شود و دارای منطق درونی خوشایند می باشد .\n لازم به ذکر است وجود هوس و شهوت تنها در رابطه هایی که طرفین وارد زندگی مشترک شده اند (ازدواج کرده اند) ملاکی مهم محسوب می شود . اگر شما در یک رابطه غیر رسمی قرار دارید این گزینه می تواند نتیجه عکس داده و باعث عدم اطمینان و فروپاشی رابطه شما شود . در نتیجه در رابطه های غیر رسمی بعد شهوانی رابطه باید کاملا کنترل شده و محدود باشد.\n\n";


        resultText.setText(allPoints+"\n\n"+loveResult+sexResult+commitmentResult+fullResult);
        questionBox.setVisibility(View.GONE);
        resultBox.setVisibility(View.VISIBLE);
    }

    private void GeneralHealthResult()
    {
        int physic = 0 , stress= 0 , social = 0 , sadness = 0;
        String allPoints = "",physicResult = "",stressResult = "",socialResult = "",sadnessResult = "",fullResult = "";


        for (int i =0 ; i<scoreList.size() ; i++)
        {
            if(i<7)
                physic += scoreList.get(i);
            else if(i>= 7 && i<14)
                stress+= scoreList.get(i);
            else if(i>= 14 && i<21)
                social+= scoreList.get(i);
            else
                sadness+= scoreList.get(i);
        }

        allPoints = " نمره سلامت جسمانی شما : "+physic+"\n"+" نمره استرس و کیفیت خواب شما : "+stress+"\n"+" نمره کنش های اجتماعی شما : "+social+"\n"+" میزان افسردگی شما : "+sadness+"\n"+"توجه داشته باشید هرچه نمره های بالا به عدد صفر نزدیک باشد شرایط سلامتی شما در آن موضوع بهتر است";


        if(physic < 10)
            physicResult = "شرایط جسمی : سلامت جسمانی شما در سطح مناسب و خوبی قرار دارد.\n\n";
        else if(physic >= 10 && physic<15)
            physicResult = "شرایط جسمی : سلامت جسمی شما در سطح متوسط قرار دارد . این دسته از افراد کم و بیش دچار علائم جسمی که ناشی از شرایط نامطلوب روانی و محیطی است ، می شوند.\n\n";
        else
            physicResult = "شرایط جسمی : شرایط جسمی شما در مطلوبی قرار ندارد و باید هر چه سریع تر برای بررسی مشکلات جسمی خود به پزشک متخصص مراجعه نمایید.\n\n";


        if(stress < 10)
            stressResult = "شرایط اضطراب و اختلال خواب : شما از لحاظ حفظ آرامش و کنترل اضطراب در شرایط مطلوبی قرار دارید.\n\n";
        else if(stress >= 10 && stress<15)
            stressResult = "شرایط اضطراب و اختلال خواب : شما گاهی با مشکل اضطراب رو به رو هستید که می تواند بر روی کیفیت خواب شما نیز تاثیر بگذارد . مراجعه به پزشک روانشناس برای شما اجباری نیست ولی می تواند مفید باشد.\n\n";
        else
            stressResult = "شرایط اضطراب و اختلال خواب : این حالت نشان دهنده اضطراب شدید شما در شرایط مختل می باشد . آموزش و یاد گیری روش های محتلف کنترل اضطراب و آرامش ورزی و یا مشورت با یک پزشک روانشناس می تواند برای شما مفید باشد.\n\n";

        if(social < 10)
            socialResult = "شرایط اجتماعی : این حالت نشان دهنده سلامت کامل شما در روابط میان فردی و ارتباطات شما در محیط کار می باشد..\n\n";
        else if(social >= 10 && social<15)
            socialResult = "شرایط اجتماعی : این حالت نشان دهنده سلامت نسبی شما در روابط میان فردی و ارتباطات شما در محیط کاری می باشد. مشورت با یک پزشک روانشناس می توان باعث بهبود این شرایط و موفقیت شغلی شما شود.\n\n";
        else
            socialResult = "شرایط اجتماعی : این حالت نشان دهنده این است که شما در روابط میان فردی و کاریتان دچار مشکل هستید . یادگیری مهارت های میان فردی و اجتماعی توسط یک مشاور و یا پزشک روانشناس می تواند شرایط زندگی و کاری شما را متحول نماید.\n\n";

        if(sadness < 10)
            sadnessResult = "شرایط افسردگی : این حالت نشان دهنده نشاط و سرزندگی شما در زندگی روزمره می باشد.\n\n";
        else if(sadness >= 10 && sadness<15)
            sadnessResult = "شرایط افسردگی : شما گاه دچار حالت افسردگی می شوید . تغییر در روش زندگیتان می تواند باعث تغییر کلی در این شرایط گردد.\n\n";
        else
            sadnessResult = "شرایط افسردگی : شما احتمالا مبتلا به افسردگی هستید . مراجعه به پزشک روانشناس به شما توصیه می شود.\n\n";



        resultText.setText(allPoints+"\n\n"+physicResult+stressResult+socialResult+sadnessResult);
        questionBox.setVisibility(View.GONE);
        resultBox.setVisibility(View.VISIBLE);
    }


}
