package newpage.com.newapp;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class LectureFragment extends Fragment {
    private static final String TAG = "lecture_run:";
    //变量
    Handler handler;
    String[] titles;
    String[] dates;
    String[] links;
    ListView list;
    List mData = new ArrayList();

    //重载onCreateView
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Thread t = new Thread(this);
        //t.start();
        if (Build.VERSION.SDK_INT >= 11) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().
                    detectDiskWrites().detectNetwork().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
        }


        Bundle str = get();
        titles = str.getStringArray("titles");
        links = str.getStringArray("linStrs");
        dates = str.getStringArray("dates");



        View view = inflater.inflate(R.layout.swufe_lecture, null);
        list = (ListView) view.findViewById(R.id.list_lecture);

        try {
            for (int i = 0; i < titles.length; i++) {

                Map<String, String> item = new HashMap<String, String>();
                //Log.i(TAG, "mData = " + titles[i]);
                item.put("title", titles[i]);

                item.put("date", dates[i]);
                mData.add(item);

            }
        }catch (Exception e){
            Log.i(TAG, "mData E= " + e);
        }
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), mData,
                R.layout.list_item,
                new String[]{"title", "date"}, new int[]{R.id.itemTitle, R.id.itemDate});
        list.setAdapter(adapter);
        return view;
    }

    //重载onActivityCreated方法,完成对控件的初始化，添加事件监听等
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    //run方法，实现子线程

    public Bundle get() {

        //获取网络数据
        URL url = null;
        try {
            url = new URL("https://www.swufe.edu.cn/1458.html");
            HttpsURLConnection http = (HttpsURLConnection) url.openConnection();
            Log.i(TAG, "http= " + http.getResponseCode());
            InputStream in = http.getInputStream();

            String html = inputStream2String(in);
            Document doc = Jsoup.parse(html);

            Bundle bundle = new Bundle();
            //提取文章链接
            Elements links = doc.select("a.red");
            Elements dateElements = doc.select("span.f-right");
            Log.i(TAG, "dateElements:" + dateElements.size());
            //定义文章标题列表
            String titles[] = new String[links.size()];
            //定义具体链接
            String linkStrs[] = new String[links.size()];
            //定义日期列表
            String dates[] = new String[dateElements.size()];

            int i = 0;
            for (Element link : links) {
                titles[i] = link.text();
                linkStrs[i] = "https://www.swufe.edu.cn/" + link.attr("href");

                i++;
            }



            for (int j = 0; j < dateElements.size(); j++) {
                dates[j] = dateElements.get(j).text();


            }


            //存入bundle中

            bundle.putStringArray("titles", titles);
            bundle.putStringArray("linStrs", linkStrs);
            bundle.putStringArray("dates", dates);


            return bundle;


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e){
            Log.i(TAG, "httpE= " + e);
        }


        return null;
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
