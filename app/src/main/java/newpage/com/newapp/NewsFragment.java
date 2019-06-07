package newpage.com.newapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class NewsFragment extends Fragment implements Runnable{
    private static final String TAG ="run:";
    //变量
    Handler handler;


    //重载onCreateView
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.swufe_news, container);
    }

    //重载onActivityCreated方法,完成对控件的初始化，添加事件监听等
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        Thread t = new Thread(this);
        t.start();

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==5){
                    String str = (String) msg.obj;
                    Log.i(TAG, "handleMessage: getMessage msg = " + str);
                    //show.setText(str);
                }
                super.handleMessage(msg);
            }

        };



    }


    //run方法，实现子线程
    @Override
    public void run() {

        //获取网络数据
        URL url = null;
        try {
            url = new URL("https://www.swufe.edu.cn/1456.html");
            HttpsURLConnection http = (HttpsURLConnection) url.openConnection();
            InputStream in = http.getInputStream();

            String html = inputStream2String(in);
            Document doc = Jsoup.parse(html);

            Bundle bundle = new Bundle();
            //提取文章链接
            Elements links = doc.select("a.red");
            Elements dateElements = doc.select("span.f-right");
            Log.i(TAG,"dateElements:"+dateElements.size() );
            //定义文章标题列表
            String titles[] = new String[links.size()];
            //定义具体链接
            String linkStrs[] = new String[links.size()];
            //定义日期列表
            String dates[] = new String[dateElements.size()];

            int i = 0;
            for(Element link:links){
                titles[i] = link.text();
                linkStrs[i] = "https://www.swufe.edu.cn/"+link.attr("href");
                Log.i(TAG, "run: titles=" + titles[i]);
                Log.i(TAG, "run: link=" + linkStrs[i]);
                i++;
            }
            //Log.i(TAG, "dateElements=" + dateElements);


           for(int j=0;j<dateElements.size();j++){
               dates[j] = dateElements.get(j).text();
               Log.i(TAG, "dates=" + dates[j]);

            }


            //存入bundle中
            bundle.putStringArray("title",titles);
            bundle.putStringArray("linStrs",linkStrs);
            bundle.putStringArray("dates",dates);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }




    }
    //将输入流InputStream转换为String
    private String inputStream2String(InputStream inputStream) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream, "gb2312");
        while (true) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0)
                break;
            out.append(buffer, 0, rsz);
        }
        return out.toString();
    }
}
