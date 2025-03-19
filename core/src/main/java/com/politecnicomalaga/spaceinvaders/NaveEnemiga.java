package com.politecnicomalaga.spaceinvaders;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class NaveEnemiga extends Nave{

    private Texture textura;


    // Constructor
    public NaveEnemiga(int iPosX, int iPosY,
                       int vel, int iAncho, int iAlto, Texture imagen,
                       boolean estaVivo,
                       ObjetoVolador.direccion direccion,
                       Texture texdisparo) {

        super(iPosX, iPosY, vel, iAncho, iAlto, imagen, estaVivo, direccion, false);

        textura = imagen;



    }



    public void draw(SpriteBatch batch){
        batch.draw(this.textura, this.getiPosX(), this.getiPosY(), this.getiAncho(), this.getiAlto());
    }










} // class NaveEnemiga

