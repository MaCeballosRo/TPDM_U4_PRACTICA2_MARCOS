package mx.edu.ittepic.marcos.tpdm_u4_practica2_marcos;

import android.annotation.SuppressLint;
import android.graphics.Canvas;

public class Loop extends Thread {
    private Lienzo lienzo;
    private boolean funcionando = false;

    public Loop(Lienzo lienzo){
        this.lienzo = lienzo;
    }

    public void funcionar(boolean x){
        funcionando = x;
    }

    @SuppressLint("WrongCall")
    public void run(){
        while(funcionando){
            Canvas c = null;
            try{
                c= lienzo.getHolder().lockCanvas();
                synchronized (lienzo.getHolder()){
                    lienzo.onDraw(c);
                }
            }finally {
                if(c !=null){
                    lienzo.getHolder().unlockCanvasAndPost(c);
                }
            }
        }
    }
}
