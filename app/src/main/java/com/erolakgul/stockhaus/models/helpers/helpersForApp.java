package com.erolakgul.stockhaus.models.helpers;

import android.content.Context;

import com.erolakgul.stockhaus.core.db.sqlite.users;
import com.erolakgul.stockhaus.service.sqlite.servicePoints;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class helpersForApp {

    public static boolean isEmpty(String str){
        int _StrLength = str.length();

        if (_StrLength == 0) return true;

        return false;//isEmpty yes it is empty (true)
    };

    // email veya password ün doğruluğu
    public static boolean emailOrPasswordValidate(String _email,String _password) {
        boolean valid = true;

        if (_email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(_email).matches()) {
            //_emailText.setError("enter a valid email address");
            valid = false;
        } else {
            //_emailText.setError(null);
        }

        if (_password.isEmpty() || _password.length() < 4 || _password.length() > 10) {
            //_passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            //_passwordText.setError(null);
        }

        return valid;
    }

    public static String getIpAddress(){
        /*WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        return ip;*/
        return "::1";
    }

    // uygulama ayağa kalkarken data girilir
    public static String InitialUser(Context context, servicePoints point){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        users entity = new users();

        entity.setName("erol");
        entity.setSurname("akgül");
        entity.setMail("erol@gmail.com");
        entity.setPassword("12345");
        entity.setPhone_number("05376023325");

        entity.setAge("32"); //Integer.parseInt(editText4.getText().toString());
        entity.setCompany("01");
        entity.setRole("5000");

        entity.setSignable(false);
        entity.setActive(true);
        entity.setOnline(false);
        entity.setChangedBy(entity.getMail());
        entity.setChangedDate(dateFormat.format(Calendar.getInstance().getTime()));
        entity.setCreatedBy(entity.getMail());
        entity.setCreateDate(dateFormat.format(Calendar.getInstance().getTime()));
        entity.setIpAddress(getIpAddress());

        boolean _isSaved = point.get_userRepository().UDM_isUser(entity.getMail(),context);
        // eğer veri db de yoksa kaydetsin
        if (!_isSaved) {
            point.get_userRepository().Rep_Create(entity, context);
        }

        return "Ok";
    }

}
