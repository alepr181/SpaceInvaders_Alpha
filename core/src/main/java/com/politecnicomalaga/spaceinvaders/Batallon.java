package com.politecnicomalaga.spaceinvaders;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Batallon {

    private ArrayList<Escuadron> escuadrones;

    private int vel_horizontal = 1;
    int posInicialY = 0;
    int posInicialX = 0;
    int gameLevel;


    // Los escuadrones recibirán como argumento una instancia del batallón que los contiene,
    //      para que puedan solicitar el cambio de dirección (izq/der)
    private ObjetoVolador.direccion direccion;
    private boolean peticionCambioDireccion = false;


    // para generar disparos.

    private long ultimoDisparo = 0;
    private long cadencia = 1000; // Un disparo cada 1.000 milisegundos = 1 segundos.
    private Random random;




    // Constructor
    public Batallon(
        String baseName_fileName_texture,
        int totalEscuadrones, int totalNavesPorEscuadron,
        int naveEnemigaAncho, int naveEnemigaAlto,
        int screenWidth, int screenHeight,
        Texture texdisparo,
        int gameLevel) {


        random = new Random();

        direccion = ObjetoVolador.direccion.IZQ;

        // Se reserva memoria suficiente para tantos escuadrones como
        // indica totalEscuadrones. Esto implica que el ArrayList ya dispondrá
        // memoria suficiente para la cantidad de métodos add que ejecutaremos
        // sin nueva reasignación de memoria, optimizando el proceso.
        escuadrones = new ArrayList<>(totalEscuadrones);

        posInicialY = (int) screenHeight / 2;
        posInicialX = (int) (screenWidth - (totalNavesPorEscuadron * naveEnemigaAncho) - ((totalNavesPorEscuadron +1 ) * naveEnemigaAncho / 2) ) / 2;




        // Creamos los objetos escuadron.

        for (int i = 0; i < totalEscuadrones; i++){

            escuadrones.add (new Escuadron(
                    "enemy.png",
                    totalNavesPorEscuadron,
                    posInicialX,
                    posInicialY,
                    vel_horizontal, naveEnemigaAncho, naveEnemigaAlto, screenWidth, screenHeight,
                    texdisparo
                )
            );  // add

            posInicialY += naveEnemigaAlto + (int) naveEnemigaAlto / 5;  // El siguiente escuadrón estará más arriba.

            this.gameLevel = gameLevel;
        } // for

    }  // Constructor





    /**
     * Este me_to_do:
     *               Ejecuta el mé_to_do draw de todos los batallones.
     *
     * @param batch: Objeto donde dibujar efectivamente en pantalla.
     * @return.....: Retorna la cantidad de naves VIVAS del batallón.
     */
    public int draw(SpriteBatch batch){

        int totalNaves = 0;

        for (int i = 0; i < escuadrones.size(); i++){

            // Ejecutamos draw() que nos devuelve el total de naves que le quedan.
            totalNaves +=  escuadrones.get(i).draw(batch, this, this.gameLevel); // direcció inicial.
        }
        if ( this.peticionCambioDireccion == true) {
            this.cambiarDireccion();
        }

        disparar();

        return totalNaves;
    }  // draw()



    public void pedirCambioDireccion(){
        this.peticionCambioDireccion = true;
    }


    private void cambiarDireccion(){
        // El cambio de dirección lo habrá pedido algún escuadrón y todos
        //      lo hacen al mismo tiempo.
        // Cuando ello ocurra, todos los escuadrones deben bajar
        //      y es lo primero que vamos a hacer:
        for (Escuadron escuadron : this.escuadrones){
            escuadron.bajarPosicion();
        }


        // Ahora cambiamos la dirección del batallon:
        this.peticionCambioDireccion = false;
        if (this.direccion == ObjetoVolador.direccion.DER) {
            this.direccion = ObjetoVolador.direccion.IZQ;
        }
        else {
            this.direccion = ObjetoVolador.direccion.DER;
        }
    }  // cambiarDirección

    public ObjetoVolador.direccion getDireccion(){
        return this.direccion;
    }






    private void disparar(){

        if (TimeUtils.millis() - ultimoDisparo > cadencia) {
            ultimoDisparo = TimeUtils.millis();

            // Elegimos un escuadrón al azar para que dispare.
            // Buscamos uno al azar que tenga naves vivas.
            do {
                int i = random.nextInt(escuadrones.size());
                if (escuadrones.get(i).getTotalNavesVivas() > 0) {
                    escuadrones.get(i).disparar();
                    break;  // Ha disparado --> salimos del bucle
                }
            } while (true);   // bucle infinito. Salimos con break cuando se haya dado orden de disparar.
        }   // if

    }  //  disparar()



    //Se utiliza en Main para comprobar colisiones.
    public ArrayList<Escuadron> getEscuadrones(){
        return this.escuadrones;
    }

}  // fin clase
