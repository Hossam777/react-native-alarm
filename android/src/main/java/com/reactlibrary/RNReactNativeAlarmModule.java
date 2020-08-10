
package com.reactlibrary;

import com.facebook.react.bridge.*;

import android.content.Intent;
import android.provider.AlarmClock;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ArrayList;

public class RNReactNativeAlarmModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;

  public RNReactNativeAlarmModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "RNReactNativeAlarm";
  }

  @ReactMethod
    public void addAlarm(Promise promise, int hour, int minutes){
        createAlarm(hour, minutes);
        promise.resolve(true);
    }
    @ReactMethod
    public void addAlarm(Promise promise, int hour, int minutes, String AMorPM){
        if("pm".equals(AMorPM.toLowerCase())){
            createAlarm(hour + 12, minutes);
        }else{
            createAlarm(hour, minutes);
        }
        promise.resolve(true);
    }
    @ReactMethod
    public void addAlarm(Promise promise, String format){
        ArrayList<String> specialWords = new ArrayList<String>();
        specialWords.add("after");
        specialWords.add("in");
        specialWords.add("for");
        String[] words = format.split(" ");
        if(specialWords.contains(words[0].toLowerCase())){
            if(words.length >= 2){
                try {
                    int hours = Integer.parseInt(words[1]);
                    int newHours = hours + Integer.parseInt(simpleDateFormat.format(Calendar.getInstance().getTime()).substring(0, 2));
                    newHours = (newHours > 24) ? (newHours - 24):(newHours);
                    createAlarm(newHours, Integer.parseInt(simpleDateFormat.format(Calendar.getInstance().getTime()).substring(3, 5)));
                }catch (Exception e){
                    promise.resolve(false);
                }
            }
        }
        promise.resolve(true);
    }

    public void createAlarm(int hour, int minutes){
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
        intent.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
        intent.putExtra(AlarmClock.EXTRA_HOUR, hour);
        intent.putExtra(AlarmClock.EXTRA_MINUTES, minutes);
        reactContext.startActivity(intent);
    }
}