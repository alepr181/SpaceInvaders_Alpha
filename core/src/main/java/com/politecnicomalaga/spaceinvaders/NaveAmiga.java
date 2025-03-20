package com.politecnicomalaga.spaceinvaders;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class NaveAmiga extends Nave {
    private int iVidas;

    public NaveAmiga(int iPosX, int iPosY, int vel, int iAncho, int iAlto, Texture imagen, boolean estaVivo, ObjetoVolador.direccion direccion, int iVidas) {
        super(iPosX, iPosY, vel, iAncho, iAlto, imagen, estaVivo, direccion, true);
        this.iVidas = iVidas;
    }
}


