package mx.edu.ittepic.marcos.tpdm_u4_practica2_marcos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.util.Random;

public class Mosca {
    int x = 10;
    int y = 10;
    int movX = 25;
    int movY = 25;

    Lienzo lienzo;
    Bitmap mosca;

    public Mosca (Lienzo lienzo, int imagen){
        this.mosca = BitmapFactory.decodeResource(lienzo.getResources(),imagen);
        this.lienzo = lienzo;

        Random aleatorio = new Random(System.currentTimeMillis());
        x = aleatorio.nextInt(400)+1;
        y = aleatorio.nextInt(800)+1;
        Random aleatorio1 = new Random(System.currentTimeMillis());
        movX = aleatorio1.nextInt(50)+1;
        movY = aleatorio1.nextInt(30)+1;
    }

    private void actualizar() {
        // bordes horizontal
        if (x > lienzo.getWidth() - mosca.getWidth() - movX) {
            movX = -movX;
        }
        if (x + movX< 0) {
            movX = 15;
        }
        x = x + movX;

        // bordes vertical
        if (y > lienzo.getHeight() - mosca.getHeight() - movY) {
            movY = -movY;
        }
        if (y + movY< 70) {
            movY = 15;
        }
        y = y + movY;
    }

    public void pintar (Canvas c){
        actualizar();
        c.drawBitmap(mosca,x,y,null);
    }

    public boolean golpe(int dedoX, int dedoY){
        if(dedoX >= x && dedoX <= x + mosca.getWidth()){
            if(dedoY >= y && dedoY <= y + mosca.getHeight()){
                return true;
            }
        }
        return false;
    }
}
