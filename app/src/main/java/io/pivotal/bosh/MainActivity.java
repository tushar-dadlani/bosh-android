package io.pivotal.bosh;

import android.app.Activity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.ListIterator;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends Activity {



    private TextView boshInfo;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofit = new Retrofit.Builder()
                .client(getUnsafeOkHttpClient())
                .baseUrl("https://<BOSH-URL>:25555")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        boshInfo = (TextView) findViewById(R.id.bosh_info);

        // prepare call in Retrofit 2.0
        BoshApi boshApi = retrofit.create(BoshApi.class);

        Call<BoshInfo> call = boshApi.getBoshDetails();
        //asynchronous call
        call.enqueue(new Callback<BoshInfo>() {
            @Override
            public void onResponse(Response<BoshInfo> response, Retrofit retrofit) {
                String boshDetails = "name:" + response.body().name + "\nuuid:" + response.body().uuid;
                Log.i("BoshInfo", boshDetails);
                boshInfo.setText(boshDetails);
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();

            }
        });




        final TextView stemcellDetails = (TextView)findViewById(R.id.bosh_stemcells);

        Call<List<BoshStemcell>> callStemcells = boshApi.getStemcells();
        callStemcells.enqueue(new Callback<List<BoshStemcell>>() {
            @Override
            public void onResponse(Response<List<BoshStemcell>> response, Retrofit retrofit) {
                Log.d("Response: ", response.message());
                String stemcells = "";
                ListIterator<BoshStemcell> stemcellIterator = response.body().listIterator();
                while(stemcellIterator.hasNext())
                {
                    BoshStemcell boshStemcell = stemcellIterator.next();
                    stemcells+= "bosh stemcell\nName:\t" + boshStemcell.name;
                    stemcells+= "\nCID\t" + boshStemcell.cid;
                    stemcells+= "\nOS\t" + boshStemcell.operating_system;
                    stemcells+= "\nVersion:\t" + boshStemcell.version;
                }
                stemcellDetails.setText(stemcells);
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();

            }
        });


                // synchronous call would be with execute, in this case you
                // would have to perform this outside the main thread
                // call.execute()

                // to cancel a running request
                // call.cancel();
                // calls can only be used once but you can easily clone them
                //Call<StackOverflowQuestions> c = call.clone();
                //c.enqueue(this);


    }

    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.setSslSocketFactory(sslSocketFactory);
            okHttpClient.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            okHttpClient.interceptors().add(new Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {
                    String credentials = "username:password";
                    // create Base64 encodet string
                    final String basic =
                            "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

                    Request newRequest = chain.request().newBuilder().addHeader("Authorization", basic).build();
                    return chain.proceed(newRequest);

                }
            });

            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



}
