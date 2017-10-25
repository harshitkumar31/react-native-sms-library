package hashit.reactnative.sms;

import android.widget.Toast;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.util.HashMap;
import java.util.Map;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Module extends ReactContextBaseJavaModule {
  
  private static final String DURATION_SHORT_KEY = "SHORT";
  private static final String DURATION_LONG_KEY = "LONG";
  
  public Module(ReactApplicationContext reactContext) {
    super(reactContext);
  }
  
  @Override
  public String getName() {
    return "SmsManager";
  }
  
  @Override
  public Map<String, Object> getConstants() {
    final Map<String, Object> constants = new HashMap<>();
    constants.put(DURATION_SHORT_KEY, Toast.LENGTH_SHORT);
    constants.put(DURATION_LONG_KEY, Toast.LENGTH_LONG);
    return constants;
  }
  
  @ReactMethod
  public void show(String message, int duration) {
    Toast.makeText(getReactApplicationContext(), message, duration).show();
  }
  
  @ReactMethod 
  public void readSmses(int skip, int limit, Callback fail, Callback success){
    
    try{
      Cursor cursor = getCurrentActivity().getContentResolver().query(Uri.parse("content://sms/"),null, null, null, null);
      
      JSONArray messages = new JSONArray();
      int c = 0;
      while (cursor.moveToNext()) {
        c++;
        // Long dateTime = Long.parseLong(cursor.getString(cursor.getColumnIndex("date")));
        // String message = cursor.getString(cursor.getColumnIndex("body"));
        JSONObject json;
        json = getJsonFromCursor(cursor);
        messages.put(json);
        
        
        
      }
      cursor.close();
      try {
        success.invoke(c, messages.toString());
      } catch(Exception err){
        fail.invoke(err);
      }
    } catch(JSONException e){
      fail.invoke(e);
    }
  }
}