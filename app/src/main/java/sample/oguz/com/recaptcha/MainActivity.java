package sample.oguz.com.recaptcha;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{
    // Google ReCAPTCHA sistemine giriş yapmanızla verilen key değerlerini buraya giriyoruz
    final String SiteKey = "/*Your Site Key*/\n";
    final String SecretKey  = "/*Your Secret Key*/";
    private GoogleApiClient mGoogleApiClient;

    Button btnRequest;
    TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvResult = (TextView)findViewById(R.id.result);
        btnRequest = (Button)findViewById(R.id.request);
        btnRequest.setOnClickListener(RqsOnClickListener);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(SafetyNet.API)
                .addConnectionCallbacks(MainActivity.this)
                .addOnConnectionFailedListener(MainActivity.this)
                .build();

        mGoogleApiClient.connect();
    }

    View.OnClickListener RqsOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            tvResult.setText("");

            SafetyNet.SafetyNetApi.verifyWithRecaptcha(mGoogleApiClient, SiteKey)
                    .setResultCallback(new ResultCallback<SafetyNetApi.RecaptchaTokenResult>() {
                        @Override
                        public void onResult(SafetyNetApi.RecaptchaTokenResult result) {
                            Status status = result.getStatus();

                            if ((status != null) && status.isSuccess()) {

                                tvResult.setText("Verification successful...");
                                // Kullanıcıyı tanıyıp robot olmadıgını dönen reCAPTCHA servisinin basarılı oldugu
                                //bölüm.Kullanıcının geri donen token bilgisini almak icinde
                                //  result.getTokenResult() kullanmalısınız


                                //Kullanıcının geri donen token bilgisini boş olup olmadgı kontrolu
                                if (!result.getTokenResult().isEmpty()) {
                                    //Token deger dönüyor

                                }else{
                                    //Token deger dönmuyor ...
                                }
                            } else {

                                Log.e("MY_APP_TAG", "Error occurred " +
                                        "when communicating with the reCAPTCHA service.");
                                // status.getStatusCode() kodunu kullanarak reCAPTCHA servisinden dönen hataları tespit edebilirsiniz
                            }
                        }
                    });

        }
    };

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Toast.makeText(this, "onConnected()", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this,
                "onConnectionSuspended: " + i,
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this,
                "onConnectionFailed():\n" + connectionResult.getErrorMessage(),
                Toast.LENGTH_LONG).show();
    }


}
