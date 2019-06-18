package com.maxpilotto.markdownview.util;

import android.os.AsyncTask;

import com.maxpilotto.markdownview.MarkDownView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created on 17/06/2019 at 15:08
 *
 * @author Max Pilotto (github.com/maxpilotto, maxpilotto.com)
 */
public class HttpRequest extends AsyncTask<Void,Void,String[]> {


    public interface Handler{
        void onResult(String... results);
    }

    private String[] urls;
    private Handler handler;

    public HttpRequest(String... urls){
        this.urls = urls;
    }

    @Override
    protected String[] doInBackground(Void... voids) {
        String[] results = new String[urls.length];

        for (int i = 0; i < urls.length; i++) {
            String url = urls[i];

            results[i] = sendRequest(url);
        }

        return results;
    }

    @Override
    protected void onPostExecute(String[] strings) {
        super.onPostExecute(strings);

        handler.onResult(strings);
    }

    public void send(Handler handler){
        this.handler = handler;

        execute();
    }

    private String sendRequest(String url){
        try{
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            Scanner scanner = new Scanner(connection.getInputStream());
            StringBuilder result = new StringBuilder();

            while (scanner.hasNextLine()){
                result.append(scanner.nextLine());
                result.append(MarkDownView.LINE_SEPARATOR);
            }

            return result.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
