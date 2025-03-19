package com.politecnicomalaga.spaceinvaders;


// Usaré ArrayList en lugar de array porque se ha dicho en el grupo
//      que será mejor valorado, pero en mi criterio personal, puesto
//      que conocemos de antemano que son exactamente 3 escuadrones
//      y cada uno tiene 8 naves amigas creo que sería mejor un array de 2 dimensiones
//      Además la gestión de arrays es más eficiente que la de ArrayLists.


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Batallon {

    private ArrayList<Escuadron> escuadrones;

    private int vel = 2;
    int posInicialY = 0;
    int posInicialX = 0;
    int gameLevel;


    // Los escuadrones recibirán como argumento una instancia del batallón que los contiene,
    //      para que puedan solicitar el cambio de dirección (izq/der)
    private ObjetoVolador.direccion direccion;
    private boolean peticionCambioDireccion = false;


    // para generar disparos.
    private static long tiempoUltimoDisparo = 0;
    final int UN_SEGUNDO = 1000;
    private long tiempoTranscurrido = 0;
    private int cadencia = 10000; // Un disparo cada 10.000 milisegundos = 10 segundos.
    private Random random;


    // No consigo que funcione lo de arriba.
    // Pruebo de otra forma:
    private int decenasSegundosAnterior = -1;
    int segundos = 0;
    int decenasSegundosActual = 0;
    int unidadesSegundosActual = 0;


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
                                vel, naveEnemigaAncho, naveEnemigaAlto, screenWidth, screenHeight,
                                texdisparo
                                )
                            );  // add

            posInicialY += naveEnemigaAlto + (int) naveEnemigaAlto / 5;  // El siguiente escuadrón estará más arriba.

            this.gameLevel = gameLevel;
        } // for

    }  // Constructor


    /**
     * Este me_to_do:
     *      1º Ejecuta el mé_to_do draw de todos los batallones.
     *      2º Retorna la cantidad de naves del batallón
     * @param batch
     * @return
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



    // by aisd. Ver con Samuel y Raúl.
    public void procesarDisparosAmigos(List<Disparo> disparos_amigos){
        for (int i = 0; i < escuadrones.size(); i++){
            escuadrones.get(i).procesarDisparosAmigos(disparos_amigos);
        }
    }



    private void disparar(){
        // Disparará teniendo en cuenta la variable cadencia,
        //  que ahora tiene valor fijo a 10 segundos pero podría
        //  modificarse según avance el juego modificando la variable gameLevel.

        /*  NO FUNCIONA:
        long tiempoActual = System.currentTimeMillis();
        tiempoTranscurrido = TimeUtils.millis() - tiempoTranscurrido;
        if (tiempoActual - tiempoTranscurrido > cadencia*100000000){
            tiempoUltimoDisparo = tiempoActual;
         */


        // Pruebo a disparar cuando cambien el dígito de las decenas de segundos:
        LocalDateTime ahora = LocalDateTime.now();
        segundos = ahora.getSecond();
        decenasSegundosActual = segundos / 10;
        //unidadesSegundosActual = segundos % 10;

        if (decenasSegundosActual != decenasSegundosAnterior){
        //if (unidadesSegundosActual == 0 || unidadesSegundosActual == 5){
            decenasSegundosAnterior = decenasSegundosActual;

            //Elegimos un escuadrón al azar para que dispare:
            int i = random.nextInt(escuadrones.size());
            escuadrones.get(i).disparar();
        }

    }  //  disparar()



}  // fin clase
