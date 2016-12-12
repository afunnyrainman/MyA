package com.example.user.mya.Utils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

import com.example.user.mya.entity.Xiangxi_Entity;
import com.example.user.mya.entity.one_entity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2016/11/9.
 */
public class DateUtils {
    /**
     * name是缓存用的
     */
    public static List<one_entity> date = new ArrayList<one_entity>();

    public static List<one_entity> getDateList(final Context context, final String url, final String name, final Handler handler) {
        if (Utils.isOnline(context)) {
            SimpleHUD
                    .showLoadingMessage(context, "正在查询...", false);
            AsyncTask<String, Long, String> task = new AsyncTask<String, Long, String>() {
                /**
                 * 查询
                 */
                @Override
                protected String doInBackground(String... s) {
                    // TODO Auto-generated method stub

                    Document doc = null;
                    try {
                        doc = Jsoup.connect(url).get();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return doc.toString();
                }

                // 查询成功
                @Override
                protected void onPostExecute(String result) {
                    // TODO Auto-generated method stub
                    super.onPostExecute(result);
                    SimpleHUD.dismiss();
                    if (Utils.Nonull(result)) {
                        SharedPreferencesUtils.SavaSharedPreferences(context, SharedPreferencesUtils.VALUEONENAME,
                                SharedPreferencesUtils.ONENAME + name, result);
                        getDate(result, name);

                    } else {
                        Utils.showOnlinError(context);
                    }
                }
            };
            task.execute();
        } else {
            Utils.showOnlinError(context);
        }

        return date;
    }

    /**
     * 解析数据
     *
     * @param result
     */
    public static List<one_entity> getDate(String result, String name) {
        date.clear();
        Document doc = Jsoup.parse(result);
        Elements elements = doc.select("ul.bookIndex > li");

        if (elements.size() != 0) {
            Elements textitem;
            Elements imgel;
            one_entity itemE;
            for (Element element : elements) {
                itemE = new one_entity();
                textitem = element.getElementsByAttributeValue("class", "text");
                imgel = element.select("a.ablum");
                String href = SharedPreferencesUtils.HREFTOU + imgel.attr("href");
                imgel = element.select("a.ablum > img");
                String text = textitem.text();
                String src = imgel.attr("src");
                if (src == null || src.equals("") || src.equals("null")) {
                    imgel = element.select("a.ablum > p >img");
                    src = imgel.attr("src");
                }
                if (!src.substring(0, 4).equals("http")) {
                    src = SharedPreferencesUtils.HREFTOU + src;
                }
                itemE.setType(one_entity.LIEBIAO);
                itemE.setSrc(src);
                itemE.setTitle(text);
                itemE.setHref(href);
                date.add(itemE);
            }
        } else {
            one_entity itemE;
            Elements elements1 = doc.select("a.articleTitle");
            for (Element element : elements1) {
                itemE = new one_entity();
                String text = element.text();
                String href = element.attr("href");
                itemE.setTitle(text);
                href = SharedPreferencesUtils.HREFTOU + href;
                itemE.setHref(href);
                itemE.setType(one_entity.XIANGXI);
                date.add(itemE);
            }
        }
        return date;
    }

    /**
     * 获得详细的数据
     *
     * @param result
     */
    public static Xiangxi_Entity getXiangxiDate(String result) {
        Xiangxi_Entity xiangxi_entity = new Xiangxi_Entity();
        Document doc = Jsoup.parse(result);
        Elements div = doc.select("DIV[ID=f_title1] >h1");
        String title = div.text();
        if (Utils.Nonull(title)) {//一种详细的情况
            Elements content = doc.select("DIV[ID=f_article] >p");

            StringBuffer sb=new StringBuffer();
           // String contenttext = "";
            for (Element element : content) {
               // contenttext += element.select("p").text() + "\n   ";//有P标签换行并且写一个有三个空格
                sb.append(element.select("p").text() + "\n   ");
            }

            xiangxi_entity.setTitle(div.text());
            xiangxi_entity.setContent(sb.toString());
        } else {//不是的话就是另一种情况

            Elements div1 = doc.select("div[class=shareBox] >h1");
            String title1 = div1.text();
            StringBuffer sb=new StringBuffer();
           // String contenttext1 = "";
            if (Utils.Nonull(title1)) {
                Elements content1 = doc.select("div[class=articleText] >p");
                for (Element element : content1) {
                    //contenttext1 += element.select("p").text() + "\n   ";
                    sb.append(element.select("p").text() + "\n   ");
                }

                xiangxi_entity.setTitle(div1.text());
                xiangxi_entity.setContent(sb.toString());
            } else {//解析html失败
                xiangxi_entity.setTitle("查询失败!");
                xiangxi_entity.setContent("查询失败!");
            }
        }


        return xiangxi_entity;
    }

}
