package com.politecnicomalaga.spaceinvaders;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

public class Nave extends ObjetoVolador {
    private boolean estaVivo;
    private direccion direccion;

    private List<Disparo> disparos;

    public Nave(int iPosX, int iPosY, int vel, int iAncho, int iAlto, Texture imagen, boolean estaVivo, ObjetoVolador.direccion direccion) {
        super(iPosX, iPosY, vel, iAncho, iAlto, imagen);
        this.estaVivo = estaVivo;
        this.direccion = direccion;
        disparos = new ArrayList<Disparo>();
    }

    public boolean isEstaVivo() {
        return estaVivo;
    }

    public ObjetoVolador.direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(ObjetoVolador.direccion direccion) {
        this.direccion = direccion;
    }

    public void disparar(Texture imagen) {
        Disparo nuevo = new Disparo(this.getiPosX() + 11, 20, 2, 6, 20, imagen);
        disparos.add(nuevo);
    }

    public void drawDisparos(SpriteBatch sp) {
        for (int i = 0; i < disparos.size(); i++) {
            disparos.get(i).draw(sp);
        }
    }

    public void updateDisparos() {
        for (int i = 0; i < disparos.size(); i++) {
            disparos.get(i).setiPosY(disparos.get(i).getiPosY() + disparos.get(i).getVel());
        }
    }

}
