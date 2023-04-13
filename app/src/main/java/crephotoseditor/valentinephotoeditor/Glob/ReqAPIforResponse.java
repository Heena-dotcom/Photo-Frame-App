package crephotoseditor.valentinephotoeditor.Glob;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ReqAPIforResponse {
    final static int CONNECTION_TIME_OUT = 20 * 1000;

    private static String getResponseText(InputStream is) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        if (is != null) {
            int line;
            try {
                br = new BufferedReader(new InputStreamReader(is));
                while ((line = br.read()) != -1) {
                    sb.append((char) line);
                }
            } catch (IOException e) {
                StringBuilder error = new StringBuilder();
                error.append("Message = ").append(e.getMessage()).append("Cause = ").append(e.getCause());
                sb = error;
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        StringBuilder error = new StringBuilder();
                        error.append("Message = ").append(e.getMessage()).append("Cause = ").append(e.getCause());
                        sb = error;
                    }
                }
            }
        }
        return sb.toString();
    }

    public static void callGet(String token, String methodName, boolean isJWTNeeded, ResultCallBack resultCallBack,String strAPI) {

        URL url;
        HttpURLConnection urlConnection = null;
        int responseCode = 0;
        try {
            url = new URL(strAPI);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(CONNECTION_TIME_OUT);
            if (isJWTNeeded)
                urlConnection.setRequestProperty("Authorization", "bearer " + token);
            urlConnection.setUseCaches(false);
            urlConnection.setRequestMethod("GET");

            responseCode = urlConnection.getResponseCode();
            if (responseCode == 200) {
                String response = getResponseText(urlConnection.getInputStream());
                urlConnection.disconnect();
                resultCallBack.onSuccess(responseCode, response);
            } else {
                String response = getResponseText(urlConnection.getErrorStream());
                urlConnection.disconnect();
                resultCallBack.onFailure(responseCode, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            resultCallBack.onFailure(responseCode, getResponseText(urlConnection.getErrorStream()));

        }
    }


    public interface ResultCallBack {
        void onSuccess(int responseCode, String strResponse);

        void onCancelled();

        void onFailure(int responseCode, String strResponse);
    }

}