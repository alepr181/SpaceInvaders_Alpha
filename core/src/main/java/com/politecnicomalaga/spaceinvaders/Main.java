package com.politecnicomalaga.spaceinvaders;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;  // by aisd Tiempo para youLost

import java.util.ArrayList;
import java.util.List;


/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private int iPosYClicked;
    private int iPosXClicked;
    private Texture texnaveAmiga;
    private Texture texenemigo;
    private NaveEnemiga enemigo;
    private Texture texdisparo;
    private Texture texdisparoEnemigo;
    private Music music_intro;
    private Music music_playing;
    private Music vgmdisparo;
    private Sound vgmcolision;
    private Texture gameover;
    private Texture gamewin;
    private NaveAmiga jugador;
    boolean boolGameOver;
    long tiempoInicioGameOver;


    // by aisd (Angel)
    private Batallon batallon;  //by aisc.   Alejandro lo llamó enemigos. Cambio el nombre de la variable.
    /*
    int neAncho = 30;  // Nave Enemiga, Ancho.
    int neAlto = 30;  // Nave Enemiga, Alto.
     */


    /*
    int totalEscuadrones = 3;
    int totalNavesPorEscuadron = 8;
     */

    int totalNavesVivas;

    // TODO: Afectará a la velocidad de movimiento del batallón,
    //          cadencia de tiro, etc.
    int gameLevel = 1;


    @Override
    public void create() {
        texdisparo = new Texture("bullet.png");
        texdisparoEnemigo = new Texture("bullet.png");
        texnaveAmiga = new Texture("ship.png");
        texenemigo = new Texture("enemy.png");
        jugador = new NaveAmiga(Gdx.graphics.getWidth()/2, 0,1,30, 30,texnaveAmiga,true, ObjetoVolador.direccion.IZQ,3);
        //enemigo = new NaveEnemiga(Gdx.graphics.getWidth()/2, 0,1,30, 30,texenemigo,true, ObjetoVolador.direccion.IZQ);
        music_playing = Gdx.audio.newMusic(Gdx.files.internal("cadetOST.mp3"));
        vgmdisparo = Gdx.audio.newMusic(Gdx.files.internal("shoot.mp3"));
        batch = new SpriteBatch();
        music_playing.setLooping(true);
        music_playing.play();
        //samuel
        gameover = new Texture("gameover2.jpeg");
        gamewin = new Texture("win.png");


        // by aisd (Angel)
        batallon = new Batallon(
            "enemy",
            3, 8, 30, 30,
            Gdx.graphics.getWidth(),
            Gdx.graphics.getHeight(),
            texdisparo,
            gameLevel
        );
    }  // fin create()



    @Override
    public void render() {

        ScreenUtils.clear(0, 0, 0, 0);
        batch.begin();


        if (!boolGameOver){

            batch.draw(jugador.getImagen(), jugador.getiPosX(), jugador.getiPosY(), jugador.getiAncho(), jugador.getiAlto());

            if (Gdx.input.isTouched()) {
                iPosXClicked = Gdx.input.getX();
                iPosYClicked = Gdx.input.getY();

                if (iPosYClicked > Gdx.graphics.getHeight() / 2) {
                    if (iPosXClicked < jugador.getiPosX()) {
                        jugador.setDireccion(ObjetoVolador.direccion.IZQ);
                    } else {
                        jugador.setDireccion(ObjetoVolador.direccion.DER);
                    }
                    jugador.moverse(jugador.getDireccion());
                } else {  // clic mitad inferior.
                    if (Gdx.input.justTouched()) {
                        jugador.disparar(texdisparo);
                        vgmdisparo.play();
                    }
                }
            }
            if (jugador.isEstaVivo()) {
                jugador.drawDisparos(batch);
                jugador.updateDisparos();
            }

            totalNavesVivas = batallon.draw(batch);
            if (totalNavesVivas == 0){
                batch.draw(gamewin, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            }



            // Colisiones de disparos del jugador con el batallón
            for(int i = 0; i < jugador.getDisparos().size(); i++) {
                Disparo disparo = jugador.getDisparos().get(i);
                for (Escuadron escuadron : batallon.getEscuadrones()) {
                    for (NaveEnemiga nave : escuadron.getNaves()) {
                        if (nave.colisionar(nave, disparo)) {
                            //sonido choque
                            //vgmcolision.play();
                            //elimino nave enemiga y disparo jugador
                            escuadron.getNaves().remove(nave);
                            jugador.getDisparos().remove(disparo);
                            totalNavesVivas--;
                            break;
                        }
                    }
                }
            }

            //si colisiona jugador con nave enemiga
            for(int i=0 ; i<batallon.getEscuadrones().size(); i++){
                for(int j=0; j<batallon.getEscuadrones().get(i).getNaves().size(); j++){
                    if(jugador.colisionar(jugador,batallon.getEscuadrones().get(i).getNaves().get(j))){
                        batch.draw(gameover,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());;
                        break;
                    }
                }
            }
            //si colisiona jugador con disparo enemigo
            for(int i=0 ; i<batallon.getEscuadrones().size(); i++){
                for(int j=0; j<batallon.getEscuadrones().get(i).getNaves().size(); j++){
                    //poner condicion para que solo se ejecute si hay disparos
                    if(batallon.getEscuadrones().get(i).getNaves().get(j).getDisparos()!=null){
                        for(int k=0; k<batallon.getEscuadrones().get(i).getNaves().get(j).getDisparos().size(); k++){
                            if(jugador.colisionar(jugador,batallon.getEscuadrones().get(i).getNaves().get(j).getDisparos().get(k))) {
                                System.out.println("colision");

                                // by aisd
                                boolGameOver = true;
                                tiempoInicioGameOver = TimeUtils.millis();

                            }
                        }
                    }
                }
            }

        } // if !boolGameOver
        else { // if boolGameOver == true
            batch.draw(gameover, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

            if (TimeUtils.millis() - tiempoInicioGameOver > 5000){
                Gdx.app.exit();
            }
        }

        batch.end();


    }  // fin render()




    @Override
    public void dispose() {
        batch.dispose();
        texnaveAmiga.dispose();
        texenemigo.dispose();
    }
}
