package newpage.com.newapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
//import android.view.Menu;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;

//import newpage.com.newapp.R;


public class FrameActivity extends FragmentActivity {

    private Fragment mFragments[];
    private RadioGroup radioGroup;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    //private RadioButton rbtHome,rbtFunc,rbtSetting;

    RadioButton rbtNews;
    RadioButton rbtInfo;
    RadioButton rbtLecture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_frame);
        mFragments = new Fragment[3];
        fragmentManager = getSupportFragmentManager();
        mFragments[0] = fragmentManager.findFragmentById(R.id.fragment_news);
        mFragments[1] = fragmentManager.findFragmentById(R.id.fragment_info);
        mFragments[2] = fragmentManager.findFragmentById(R.id.fragment_lecture);

        fragmentTransaction = fragmentManager.beginTransaction().hide(mFragments[0]).hide(mFragments[1]).hide(mFragments[2]);
        fragmentTransaction.show(mFragments[0]).commit();

         rbtNews = (RadioButton) findViewById(R.id.radioNews);
        rbtInfo = (RadioButton)findViewById(R.id.radioInfo);
        rbtLecture = (RadioButton)findViewById(R.id.radioLecture);
        rbtNews.setBackgroundResource(R.drawable.shape3);

        radioGroup = (RadioGroup)findViewById(R.id.bottomGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.i("radioGroup", "checkId=" + checkedId);
                fragmentTransaction = fragmentManager.beginTransaction().hide(mFragments[0]).hide(mFragments[1]).hide(mFragments[2]);

                rbtNews.setBackgroundResource(R.drawable.shape2);
                rbtInfo.setBackgroundResource(R.drawable.shape2);
                rbtLecture.setBackgroundResource(R.drawable.shape2);
                switch(checkedId){
                    case R.id.radioNews:
                        //显示news
                        fragmentTransaction.show(mFragments[0]).commit();
                        rbtNews.setBackgroundResource(R.drawable.shape3);
                        break;
                    case R.id.radioInfo:
                        //显示info
                        fragmentTransaction.show(mFragments[1]).commit();
                        rbtInfo.setBackgroundResource(R.drawable.shape3);
                        break;
                    case R.id.radioLecture:
                        //显示lecture
                        fragmentTransaction.show(mFragments[2]).commit();
                        rbtLecture.setBackgroundResource(R.drawable.shape3);
                        break;
                    default:
                        break;
                }
            }
        });
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.frame, menu);
        return true;
    }
*/
}