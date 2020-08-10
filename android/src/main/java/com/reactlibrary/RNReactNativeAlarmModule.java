
package com.reactlibrary.alarm;

import com.facebook.react.bridge.*;

import android.content.Intent;
import android.provider.AlarmClock;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ArrayList;

public class RNReactNativeAlarmModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;
  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
  public RNReactNativeAlarmModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "RNReactNativeAlarm";
  }
  
  @ReactMethod
  public void setAlarm(String[] entities, Promise promise){
    ArrayList<String> specialWords = new ArrayList<String>();
    specialWords.add("after");
    specialWords.add("in");
    specialWords.add("for");
    if (isNumeric(entities[0])){
        if(entities.length > 1 && ("pm".equals(entities[1].toLowerCase()) || "am".equals(entities[1].toLowerCase()))){
            if("pm".equals(entities[1].toLowerCase()))
                createAlarm(Integer.parseInt(entities[0]) + 12, 0);
            else
                createAlarm(Integer.parseInt(entities[0]), 0);
        }else{
            createAlarm(Integer.parseInt(entities[0]), 0);
        }
        promise.resolve(true);
    }else if(isTime(entities[0])){
        if(entities.length > 1 && "pm".equals(entities[1].toLowerCase()))
            createAlarm(Integer.parseInt(entities[0].split(":")[0]) + 12, Integer.parseInt(entities[0].split(":")[1]));
        else
            createAlarm(Integer.parseInt(entities[0].split(":")[0]), Integer.parseInt(entities[0].split(":")[1]));
        promise.resolve(true);
    }else if(specialWords.contains(entities[0].toLowerCase())){
        try {
            int hours = Integer.parseInt(entities[1]);
            int newHours = hours + Integer.parseInt(simpleDateFormat.format(Calendar.getInstance().getTime()).substring(0, 2));
            newHours = (newHours > 24) ? (newHours - 24):(newHours);
            createAlarm(newHours, Integer.parseInt(simpleDateFormat.format(Calendar.getInstance().getTime()).substring(3, 5)));
            promise.resolve(true);
        }catch (Exception e){
          promise.resolve(false);
        }
    }else{
      promise.resolve(false);
    }   
 }

private boolean isNumeric(String strNum) {
    if (strNum == null) {
        return false;
    }
    try {
        double d = Double.parseDouble(strNum);
    } catch (NumberFormatException nfe) {
        return false;
    }
    return true;
}
private boolean isTime(String time) {
    try {
        String[] timeParts = time.split(":");
        Integer hours = Integer.parseInt(timeParts[0]);
        Integer Minutes = Integer.parseInt(timeParts[1]);
    }catch (Exception e){
        return false;
    }
    return true;
}
  public void createAlarm(int hour, int minutes){
      Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
      intent.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
      intent.putExtra(AlarmClock.EXTRA_HOUR, hour);
      intent.putExtra(AlarmClock.EXTRA_MINUTES, minutes);
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      reactContext.startActivity(intent);
  }
}