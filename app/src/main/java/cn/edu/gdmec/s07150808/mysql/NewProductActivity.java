package cn.edu.gdmec.s07150808.mysql;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewProductActivity extends Activity {
// Progress Dialog
        private ProgressDialog pDialog;
        JSONParser jsonParser=new JSONParser();
        EditText inputName ;
        EditText inputPrice;
        EditText inputDesc;
// url to create new product
        //创建新纪录的URL
        private static String url_create_product="http://api.androidhive.info/android_connect/create_product.php";
// JSON Node names
        private static final String TAG_SUCCESS="success";

@Override
public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product);
// Edit Text
        inputName=(EditText)findViewById(R.id.inputName);
        inputPrice=(EditText)findViewById(R.id.inputPrice);
        inputDesc=(EditText)findViewById(R.id.inputDesc);
// Create button
        Button btnCreateProduct=(Button)findViewById(R.id.btnCreateProduct);
// button click event
        btnCreateProduct.setOnClickListener(new View.OnClickListener(){
@Override
public void onClick(View view){
// creating new product in background thread
        //创建线程
        new CreateNewProduct().execute();
        }
        });
        }
/**
 * Background Async Task to Create new product
 * 通过异步类进行创建新的记录
 * */
        class CreateNewProduct extends AsyncTask<String, String, String> {
/**
 * Before starting background thread Show Progress Dialog
 * 在开始后台的线程前，显示一个进度条
 */
@Override
protected void onPreExecute(){
        super.onPreExecute();
        //实例化进度条
        pDialog=new ProgressDialog(NewProductActivity.this);
        //进度条的内容
        pDialog.setMessage("Creating Product..");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        //只有调用show的方法后，显示进度条
        pDialog.show();
        }
/**
 * Creating product
 * 创建新纪录 ， 通过获取输入的值，进行储存
 * */
        protected String doInBackground(String...args){
        String name=inputName.getText().toString();
        String price=inputPrice.getText().toString();
        String description=inputDesc.getText().toString();
// Building Parameters
        List<NameValuePair> params=new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("name",name));
        params.add(new BasicNameValuePair("price",price));
        params.add(new BasicNameValuePair("description",description));
// getting JSON Object
// Note that create product url accepts POST method
        JSONObject json=jsonParser.makeHttpRequest(url_create_product,
        "POST",params);
// check log cat fro response
        Log.d("Create Response",json.toString());
// check for success tag
                //查看创建新纪录是否成功插入数据库
        try{
        int success=json.getInt(TAG_SUCCESS);
        if(success==1){
// successfully created product
                //成功插入后，跳转到显示全部记录的页面
        Intent i=new Intent(getApplicationContext(),AllProductsActivity.class);
        startActivity(i);
// closing this screen
        finish();
        }else{
// failed to create product
        }
        }catch(JSONException e){
        e.printStackTrace();
        }
        return null;
        }
/**
 * After completing background task Dismiss the progress dialog
 * 在完成之后，进度条消失
 * **/
        protected void onPostExecute(String file_url){
// dismiss the dialog once done
        pDialog.dismiss();
        }
        }
        }
