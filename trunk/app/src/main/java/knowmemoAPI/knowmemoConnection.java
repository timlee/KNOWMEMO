package knowmemoAPI;

import org.json.JSONObject;

/**
 * Created by ShiangChiLee on 2016/3/13.
 */
public class knowmemoConnection
{

    public String url = "http://140.127.74.211:8080/knowmemo/userservice.php";;

    public void userLogin(String account , String password , final knowmemoCallBackListener callback)
    {
        JSONObject header = new JSONObject();
        JSONObject postData = new JSONObject();

        try
        {
            postData.put("action", "login");
            postData.put("account" , account);
            postData.put("password" , password);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        new knowmemoAsyncTask(new knowmemoCallBackListener() {
            @Override
            public void onSuccess(JSONObject result)
            {
                try
                {
                    if(callback == null){
                        return;
                    }
                    if(result.getInt("responseCode") == -1) //登入失敗
                    {
                        callback.onFail(result);
                    }
                    else if(result.getInt("responseCode") == 0) //登入成功
                    {
                        callback.onSuccess(result);
                    }
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }

            }

            @Override
            public void onFail(JSONObject result) {

            }

        }).execute(url , header.toString() , postData.toString());
    }
    public void userRegister(String account , String oldPassword , String newPassword , String Email , final knowmemoCallBackListener callback)
    {
        JSONObject header = new JSONObject();
        JSONObject postData = new JSONObject();

        try
        {
            postData.put("action" , "register");
            postData.put("account" , account);
            postData.put("password" , oldPassword);
            postData.put("password2" , newPassword);
            postData.put("email" , Email);

            new knowmemoAsyncTask(new knowmemoCallBackListener() {
                @Override
                public void onSuccess(JSONObject result)
                {
                    try
                    {
                        if(callback == null)
                        {
                            return;
                        }
                        if(result.getInt("responseCode") == -1 || result.getInt("responseCode") == -2 ||
                           result.getInt("responseCode") == -3) //註冊失敗
                        {
                            callback.onFail(result);
                        }
                        else if(result.getInt("responseCode") == 0) //註冊成功
                        {
                            callback.onSuccess(result);
                        }
                    }
                    catch(Exception ex)
                    {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFail(JSONObject result) {

                }


            }).execute(url, header.toString(), postData.toString());

        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    public void userUpdatePassword(String account,String oldPassword , String newPassword , String newPassword2 , final knowmemoCallBackListener callback)
    {
        JSONObject header = new JSONObject();
        JSONObject postData = new JSONObject();

        try
        {
            postData.put("action" , "update");
            postData.put("account" , account);
            postData.put("oldPassword" , oldPassword);
            postData.put("newPassword" , newPassword);
            postData.put("newPassword2", newPassword2);

            new knowmemoAsyncTask(new knowmemoCallBackListener() {
                @Override
                public void onSuccess(JSONObject result)
                {
                    try
                    {
                        if(callback == null)
                        {
                            return;
                        }
                        if(result.getInt("responseCode") == -1 || result.getInt("responseCode") == -2 ||
                           result.getInt("responseCode") == -3) //修改失敗,資料有誤
                        {
                            callback.onFail(result);
                        }
                        if(result.getInt("responseCode") == 0) //修改成功
                        {
                            callback.onSuccess(result);
                        }
                    }
                    catch(Exception ex)
                    {
                        ex.printStackTrace();
                    }

                }

                @Override
                public void onFail(JSONObject result) {

                }

            }).execute(url , header.toString() , postData.toString());
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    public void userForgetPassword(String account , String email , final knowmemoCallBackListener callback)
    {
        JSONObject header = new JSONObject();
        JSONObject postData = new JSONObject();

        try
        {
            postData.put("action", "forgetpwd");
            postData.put("account" , account);
            postData.put("email" , email);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        new knowmemoAsyncTask(new knowmemoCallBackListener() {
            @Override
            public void onSuccess(JSONObject result)
            {
                try
                {
                    if(callback == null){
                        return;
                    }
                    if(result.getInt("responseCode") == -1) //登入失敗
                    {
                        callback.onFail(result);
                    }
                    else if(result.getInt("responseCode") == 0) //登入成功
                    {
                        callback.onSuccess(result);
                    }
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }

            }

            @Override
            public void onFail(JSONObject result) {

            }

        }).execute(url , header.toString() , postData.toString());
    }
}
