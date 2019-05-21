package mx.edu.ittepic.marcos.tpdm_u4_practica2_marcos;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Message;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.LogRecord;

public class Lienzo extends SurfaceView {
    SurfaceHolder holder;
    Loop loopJuego;
    List<Mosca> moscas;
    List<Mosca> moscas1;
    Mosca jefeFinal = null;
    Handler handler;
    Timer timer;
    int total_moscas = 30;
    int total_tiempo = 60;
    int intentos_jefe = 5;
    int total_tiempo_jefe = 10;
    boolean perdio=false;


    public Lienzo(Context context){
       super(context);
       loopJuego = new Loop(this);
       moscas = new ArrayList<>();
       moscas1 = new ArrayList<>();
       holder = getHolder();
       holder.addCallback(new SurfaceHolder.Callback() {
           @Override
           public void surfaceCreated(SurfaceHolder holder) {
               loopJuego.funcionar(true);
               loopJuego.start();
           }

           @Override
           public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

           }

           @Override
           public void surfaceDestroyed(SurfaceHolder holder) {
                boolean r = true;
                loopJuego.funcionar(false);
                while(r){
                    try{
                        loopJuego.join();
                        r = false;
                    }catch (InterruptedException e){

                    }
                }
           }
       });

       handler = new Handler() {
           public void handleMessage(Message mensaje){

           }
       };

       moscas1.add(new Mosca(this,R.drawable.mosca1));

       timer = new Timer();
       timer.schedule(new TimerTask(){
            public void run(){
                if(perdio){
                    return;
                }
                if(total_moscas<=0 && jefeFinal==null){
                    jefeFinal = crearMosca(R.drawable.jefe);
                }
                total_tiempo--;
                if(total_tiempo<=0 || total_tiempo_jefe <=0){
                    perdio = true;
                }
                if(total_moscas<=0){
                    total_tiempo_jefe--;
                }
                handler.sendEmptyMessage(0);
            }
        },0,1000);

    }
    private Mosca crearMosca(int resource){
        return new Mosca(this,resource);
    }
    protected void onDraw(Canvas c){
        Paint p = new Paint();
        if(c!=null){
            c.drawColor(Color.WHITE);
            moscas = moscas1;
            if(jefeFinal!=null){
                jefeFinal.pintar(c);
            }
            for(Mosca m : moscas){
                m.pintar(c);
            }
            if(intentos_jefe <=0){
                timer.cancel();
                moscas1 = new ArrayList<>();
                jefeFinal = null;
                p.setTextSize(100);
                c.drawText("Has ganado!",getWidth()/2,getHeight()/2,p);
                return;
            }
            if(perdio){
                moscas1 = new ArrayList<>();
                p.setTextSize(100);
                c.drawText("Derrota",getWidth()/2,getHeight()/2,p);
                return;
            }
            if(total_moscas>0){
                p.setStyle(Paint.Style.FILL);
                p.setColor(Color.GRAY);
                c.drawRect(0, 0, getWidth(), 70, p);
                p.setColor(Color.GREEN);
                c.drawRect(0, 0, getWidth() * (total_tiempo / 60f), 70, p);
                p.setColor(Color.BLACK);
                p.setTextSize(60);
                c.drawText("Moscas restantes: " + total_moscas, 10, 50, p);
            }else{
                moscas1=new ArrayList<>();
                p.setStyle(Paint.Style.FILL);
                p.setColor(Color.GRAY);
                c.drawRect(0, 0, getWidth(), 70, p);
                p.setColor(Color.RED);
                c.drawRect(0, 0, getWidth() * (total_tiempo_jefe / 10f), 70, p);
                p.setColor(Color.BLACK);
                p.setTextSize(60);
                c.drawText("Vidas Jefe: " + intentos_jefe, 10, 50, p);
            }

        }
    }

    public boolean onTouchEvent(MotionEvent me){
        int accion = me.getAction();
        int posx = (int)me.getX();
        int posy = (int)me.getY();

        switch(accion){

            case MotionEvent.ACTION_UP:
                if(jefeFinal!=null){
                    if(jefeFinal.golpe(posx,posy)){
                        intentos_jefe--;
                    }
                }
                Mosca mAuxiliar = null;
                for(int i =0;i<moscas1.size();i++){
                    mAuxiliar = moscas1.get(i);
                    if(mAuxiliar.golpe(posx,posy)){
                        moscas1.remove(mAuxiliar);
                        Random r = new Random();
                        int num = r.nextInt(3);
                        for(int k=0;k<num;k++){
                            moscas1.add(crearMosca(R.drawable.mosca1));
                        }
                        total_moscas--;
                    }
                }
                break;
        }
        return true;
    }
}
