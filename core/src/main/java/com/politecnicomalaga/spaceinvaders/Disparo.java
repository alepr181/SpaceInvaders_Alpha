package com.politecnicomalaga.spaceinvaders;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Disparo extends ObjetoVolador{

    private boolean esAmigo;

    public Disparo(int iPosX, int iPosY, int vel, int iAncho, int iAlto, Texture imagen) {
        super(iPosX, iPosY, vel, iAncho, iAlto, imagen);
        this.esAmigo = esAmigo;
    }

    public void draw(SpriteBatch sp) {
        sp.draw(getImagen(),getiPosX(),getiPosY(),getiAncho(),getiAlto());
    }



}

