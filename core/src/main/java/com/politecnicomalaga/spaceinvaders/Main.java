package com.politecnicomalaga.spaceinvaders;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import java.util.List;


/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private int iPosYClicked;
    private int iPosXClicked;
    private Texture texnaveAmiga;
    private Texture texenemigo;

    private Texture texdisparo;
    private Texture texdisparoEnemigo;
    private Music music_intro;
    private Music music_playing;
    private Music vgmdisparo;
    private Sound vgmcolision;

    private NaveAmiga jugador;



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

    int totalNavesVivas = 0;

    // TODO: Afectará a la velocidad de movimiento del batallón,
    //          cadencia de tiro, etc.
    int gameLevel = 1;




    @Override
    public void create() {
        texdisparo = new Texture("bullet.png");
        texdisparoEnemigo = new Texture("enemyBullet.png");
        texnaveAmiga = new Texture("ship.png");
        texenemigo = new Texture("enemy.png");
        jugador = new NaveAmiga(Gdx.graphics.getWidth()/2, 0,1,30, 30,texnaveAmiga,true, ObjetoVolador.direccion.IZQ,3);

        music_playing = Gdx.audio.newMusic(Gdx.files.internal("cadetOST.mp3"));
        vgmdisparo = Gdx.audio.newMusic(Gdx.files.internal("shoot.mp3"));
        batch = new SpriteBatch();
        music_playing.setLooping(true);
        music_playing.play();


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


        // by aisd. Ver con Samuel y Raúl.
        batallon.procesarDisparosAmigos(jugador.getDisparos());


        // by aisd (Angel)
        // Llamamos al mé_to_do draw del batallón que
        //      - Devuelve el total de naves vivas.
        //      - Controla movimientos, etc.
        totalNavesVivas = batallon.draw(batch);

        if (totalNavesVivas == 0){
            // todo: GAME OVER
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
