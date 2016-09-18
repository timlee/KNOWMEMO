package knowmemoAPI;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by ShiangChiLee on 2016/3/13.
 */
public class knowmemoAPI
{
    private static Application APPLICATION;
    private static ConnectivityManager cm = null;
    private static HashMap<String , knowmemoConnection> connectionPool ;

    public static void initAPI(Application app , knowmemoConnection conn)
    {
        if(APPLICATION != null){return;}
        APPLICATION = app;

        cm  = (ConnectivityManager) APPLICATION.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        connectionPool = new HashMap<String , knowmemoConnection>();
        connectionPool.put(app.getPackageName() , conn);

        Log.i("knowmemoAPI" , "init");
    }

    private static void checkEnvironment() throws Exception{
        if(APPLICATION == null){
            throw new Exception("knowmemoAPI need to be initialized");
        }

        if(cm.getActiveNetworkInfo() == null){
            throw new Exception("Cannot find network");
        }

    }

    public static void userLogin(String account , String password , knowmemoCallBackListener callback) throws Exception
    {
        checkEnvironment();
        if(!connectionPool.isEmpty())
        {
            connectionPool.get(APPLICATION.getPackageName()).userLogin(account,password,callback);
        }
        else
        {
            Log.i("knowmemoAPI" , "no connection avaliable");
        }
    }
    public static void userRegister(String account , String oldPassword , String newPassword , String Email , knowmemoCallBackListener callback) throws Exception {
        checkEnvironment();
        if(!connectionPool.isEmpty())
        {
            connectionPool.get(APPLICATION.getPackageName()).userRegister(account, oldPassword, newPassword, Email, callback);
        }
        else
        {
            Log.i("knowmemoAPI" , "no connection avaliable");
        }
    }
    public static void userUpdatePassword(String account,String oldPassword , String newPassword , String newPassword2 , knowmemoCallBackListener callback) throws Exception {
        checkEnvironment();
        if(!connectionPool.isEmpty())
        {
            connectionPool.get(APPLICATION.getPackageName()).userUpdatePassword(account, oldPassword, newPassword, newPassword2, callback);
        }
        else
        {
            Log.i("knowmemoAPI" , "no connection avaliable");
        }
    }
    public static void userForgetPassword(String account , String email , knowmemoCallBackListener callback) throws Exception
    {
        checkEnvironment();
        if(!connectionPool.isEmpty())
        {
            connectionPool.get(APPLICATION.getPackageName()).userForgetPassword(account,email , callback);
        }
        else
        {
            Log.i("knowmemoAPI" , "no connection avaliable");
        }
    }
}
