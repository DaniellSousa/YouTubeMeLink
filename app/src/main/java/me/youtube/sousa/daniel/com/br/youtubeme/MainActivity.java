package me.youtube.sousa.daniel.com.br.youtubeme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import Config.Config;

public class MainActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    private static final int RECOVERY_REQUEST = 1;
    private YouTubePlayerView youTubeView;
    private AppCompatEditText fieldUrlVideo;
    private String urlVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.urlVideo = "";
        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        fieldUrlVideo = (AppCompatEditText) findViewById(R.id.txtUrlVideo);
    }

    //Caso a requisição de iniciar o video seja sucedida.
    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if(!b) {
            youTubePlayer.cueVideo(this.urlVideo); // cueVideo(String var1) recebe o id do video que tem no final do link.
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) { //Caso a requisição de iniciar o video seja mal sucedida.
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            String error = String.format(getString(R.string.player_error), errorReason.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RECOVERY_REQUEST) {
            runVideo();
        }
    }

    // Método para o botão de executar o video.
    public void runVideoFromURL(View view) {
        if(!fieldUrlVideo.getText().toString().equals("")) {
            this.urlVideo = montarUrl(fieldUrlVideo.getText().toString()); //Preenche a varável urlVideo com o id do video, pois será colada a url inteira do video.
            runVideo(); //Método que chama o método para iniciar o carregamento do video.
        } else {
            Toast.makeText(this, "Cole a url...", Toast.LENGTH_LONG).show();
        }
    }

    private String montarUrl(String urlField) { // Método que recebe a URL e retorna apenas o id do video.
        String urlAux = "";
        String url = "";
        for(int i = urlField.length(); i > 0 ; i--) { //Percorre de tras pra frente...
            urlAux += urlField.charAt(i - 1);

            if(urlAux.length() == 11) { //e após pegar os 11 últimos caracteres, inverte a ordem dos elementos do id, já que foi capturado de trás para frente. E sai do for.
                url = inverterUrl(urlAux);
                break;
            }
        }
        return url;
    }

    private String inverterUrl(String urlAux) { // Inverte os elementos do id do video passado por parâmetro.
        String url = "";
        for(int i = urlAux.length(); i > 0; i--)
            url += urlAux.charAt(i - 1);
        return url;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    YouTubePlayer.Provider getYouTubePlayerProvider() { //retorna um provider responsável por chamar o método de iniciar a busca pelo video.
        return this.youTubeView;
    }

    void runVideo() {// método de iniciar a busca pelo video.
        getYouTubePlayerProvider().initialize(Config.YOUTUBE_API_KEY, this);
    }
}
