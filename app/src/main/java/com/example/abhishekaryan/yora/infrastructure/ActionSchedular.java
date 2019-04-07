package com.example.abhishekaryan.yora.infrastructure;
import android.os.Handler;
import java.util.ArrayList;
import java.util.HashMap;

public class ActionSchedular {

    private final YoraApplication application;
    private final Handler handler;
    private final ArrayList<TimedCallback> timedCallbacks;
    private final HashMap<Class, Runnable> onResumeActions;
    private boolean isPaused;


    public ActionSchedular(YoraApplication application){
        this.application=application;
        handler=new Handler();
        timedCallbacks=new ArrayList<>();
        onResumeActions=new HashMap<>();
    }

    public void onPause(){
        isPaused=true;

    }
    public void onResume(){
        isPaused=false;
        for (TimedCallback callback:timedCallbacks){
            callback.schdule();
        }
        for(Runnable runnable:onResumeActions.values()){
            runnable.run();
        }

        onResumeActions.clear();


    }
    public void invokeonResume(Class cls,Runnable runnable){

        if(!isPaused){

            runnable.run();
            return;
        }
        onResumeActions.put(cls, runnable);

    }
    public void postDelayed(Runnable runnable, long millisecounds){

        handler.postDelayed(runnable,millisecounds);

    }
    public void invokeEveryMilliSecounds(Runnable runnable, long millisecounds){

        invokeEveryMilliSecounds(runnable,millisecounds,true);

    }
    public void invokeEveryMilliSecounds(Runnable runnable, long millisecounds, boolean runImdiately){

        TimedCallback callback=new TimedCallback(runnable,millisecounds);
        timedCallbacks.add(callback);

        if(runImdiately){
            callback.run();
        }
        else {
            postDelayed(callback,millisecounds);
        }

    }
    public void postEveryMilliSecounds(Object request, long millisecounds){
        postEveryMilliSecounds(request,millisecounds,true);

    }
    public void postEveryMilliSecounds(final Object request, long millisecounds, boolean postImdiately){

        invokeEveryMilliSecounds(new Runnable() {
            @Override
            public void run() {
                application.getBus().post(request);
            }
        },millisecounds,postImdiately);

    }
    private class TimedCallback implements Runnable{

        private final Runnable runnable;
        private final long deley;

        public TimedCallback(Runnable runnable, long delay){
            this.runnable=runnable;
            this.deley=delay;

        }

        @Override
        public void run() {

            if(isPaused)
                return;
            runnable.run();
            schdule();

        }

        public void schdule(){
            handler.postDelayed(this,deley);

        }


    }
}
