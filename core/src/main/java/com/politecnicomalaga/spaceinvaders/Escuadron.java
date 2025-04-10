package com.politecnicomalaga.spaceinvaders;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Escuadron {

    // Variable compartida por todos los objetos escuadrones. Por eso es static.
    private static ObjetoVolador.direccion direccion;



    private ArrayList<NaveEnemiga> navesEnemigas;
    private int totalNavesVivas;

    private Texture texturaTmp;
    private Texture texdisparo;

    private int naveEnemigaAncho;
    private int naveEnemigaAlto;
    private int screenWidth;
    private Random random;

    // Constructor
    public Escuadron(
        String fileName_texture, int totalNaves, int posX, int posY, int vel,
        int naveEnemigaAncho, int naveEnemigaAlto,
        int screenWidth, int screenHeight,
        Texture texdisparo)
    {


        random = new Random();

        this.naveEnemigaAncho = naveEnemigaAncho;
        this.naveEnemigaAlto = naveEnemigaAlto;
        this.screenWidth = screenWidth;

        navesEnemigas = new ArrayList<>(totalNaves);



        // Al final cada escuadrón tendrá su propia textura, pero inicialmente
        //      pongo la misma a todos, estoy ignorando el primer parámetro
        //      de este constructor:
        texturaTmp = new Texture("enemy.png");
        this.texdisparo = texdisparo;


        // La separación horizontal será en principio anchonave + 1/5
        for (int i = 0; i < totalNaves; i++){

            navesEnemigas.add(
                new NaveEnemiga(posX, posY,
                    vel, naveEnemigaAncho, naveEnemigaAlto, texturaTmp,
                    true,
                    Escuadron.direccion,
                    texdisparo)
            );

            posX += naveEnemigaAncho + (int) naveEnemigaAncho / 5;  // El siguiente estará más a la derecha
        }  // for


    }  // Constructor




    public int draw(SpriteBatch batch, Batallon batallon, int gameLevel){

        int totalNaves = 0;  // para controlar Game Over.



        Escuadron.direccion = batallon.getDireccion();



        // Primero detectamos si alguna nave está cerca del
        //      borde derecho o izquierdo para solicitar
        //      al batallón un cambio de dirección.
        int maxX = -1;
        int minX = this.screenWidth;

        for (int i = 0; i< navesEnemigas.size(); i++){

            NaveEnemiga nave = navesEnemigas.get(i);  // Tomamos una nave del ArrayList.
            if (nave.isEstaVivo()) {
                totalNaves++;
                nave.moverse(Escuadron.direccion); // Calculamos nueva posición (coordenadas x,y)
                nave.draw(batch);  // A cada nave se le encarga la tarea de dibujarse ella misma.

                if (nave.getiPosX() > maxX) { maxX = nave.getiPosX(); }
                if (nave.getiPosX() < minX) { minX = nave.getiPosX(); }

                nave.updateDisparos();      // Se recalculan las posiciones de los disparos.
                nave.drawDisparos(batch);   // Se dibujan en las posiciones calculadas.
            }
            //else {navesEnemigas.remove(i); }
        }  // for



        // Si llegamos al límite derecho o izquierdo bajamos
        // y solicitamos al Batallón el cambio de dirección.
        if (    ( Escuadron.direccion == ObjetoVolador.direccion.DER  &&  maxX > this.screenWidth - (int) this.naveEnemigaAncho )
            ||
            ( Escuadron.direccion == ObjetoVolador.direccion.IZQ  &&  minX < this.naveEnemigaAncho )
        )
        {   // if_inicio

            // Cambiamos de sentido izq/der para la próxima.
            batallon.pedirCambioDireccion();

        }   // if_fin

        this.totalNavesVivas = totalNaves;
        return totalNaves;


    }  // draw()



    protected void bajarPosicion(){
        // Bajamos la posición de cada nave.
        for (int i = 0; i < navesEnemigas.size(); i++){

            // Lo de abajo sí funciona, pero baja muy poco:
            // navesEnemigas.get(i).moverse(ObjetoVolador.direccion.ABAJO);
            navesEnemigas.get(i).setiPosY(navesEnemigas.get(i).getiPosY() - (int) this.naveEnemigaAlto/3);

        }
    }



    protected int getTotalNavesVivas(){ return this.totalNavesVivas;}


    public void disparar(){
        boolean haDisparado = false;

        do {
            int i = random.nextInt(navesEnemigas.size());  // Elegimos nave al azar
            if (navesEnemigas.get(i).isEstaVivo()){
                navesEnemigas.get(i).disparar(texdisparo);
                haDisparado = true;
            }
        } while (haDisparado == false);
    }



    //getNaves
    protected ArrayList<NaveEnemiga> getNaves(){
        return this.navesEnemigas;
    }


}  // clase Escuadron
