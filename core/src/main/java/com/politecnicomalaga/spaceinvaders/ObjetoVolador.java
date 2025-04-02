package com.politecnicomalaga.spaceinvaders;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class ObjetoVolador {
    // ... (resto de atributos y métodos de la clase ObjetoVolador) ...
    private int iPosX;
    private int iPosY;
    private int Vel;
    private int iAncho;
    private int iAlto;

    private Texture imagen;
    public enum direccion {
        ARRIBA, ABAJO, IZQ, DER;

    }

    public ObjetoVolador(int iPosX, int iPosY, int vel, int iAncho, int iAlto, Texture imagen) {
        this.iPosX = iPosX;
        this.iPosY = iPosY;
        Vel = vel;
        this.iAncho = iAncho;
        this.iAlto = iAlto;
        this.imagen = imagen;
    }


    public Texture getImagen() {
        return imagen;
    }

    public int getiPosX() {
        return iPosX;
    }

    public int getiPosY() {
        return iPosY;
    }

    public int getVel() {
        return Vel;
    }

    public int getiAncho() {
        return iAncho;
    }

    public int getiAlto() {
        return iAlto;
    }
    public void setiPosX(int iPosX) {
        this.iPosX = iPosX;
    }

    public void setiPosY(int iPosY) {
        this.iPosY = iPosY;
    }


    public void setVel(int vel) {
        Vel = vel;
    }

    public void moverse(Nave.direccion d) {
        switch (d) {
            case ARRIBA:
                this.setiPosY(this.getiPosY() + this.getVel());

                break;
            case ABAJO:
                this.setiPosY(this.getiPosY() - this.getVel());
                break;
            case IZQ:
                this.setiPosX(this.getiPosX() - this.getVel());
                break;
            case DER:
                this.setiPosX(this.getiPosX() + this.getVel());
                break;
        }
    }

    public boolean colisionar(ObjetoVolador otroObjeto, ObjetoVolador otro) {
        // Crear rectángulos para la nave y el disparo para conocer su tamaño
        Rectangle naveRect = new Rectangle(otroObjeto.getiPosX(), otroObjeto.getiPosY(), otroObjeto.getiAncho(), otroObjeto.getiAlto());
        Rectangle otroRect = new Rectangle(otro.getiPosX(), otro.getiPosY(), otro.getiAncho(), otro.getiAlto());
        // Verificar si se tocan
        return naveRect.overlaps(otroRect);
    }


}
