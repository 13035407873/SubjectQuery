package cn.xuemcu.myapplication1;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private CXWay cxWay = null;
    List<CXSubject> subjectList = null;
    private static final String TAG = "MainActivity";
    private SearchView mSearchView;
    private TextView display;
    private ListView list;
    private AlertDialog mAlertDialog = null;
    ArrayList<String> showlist; //搜索建议显示列表

    private Button btn_ok = null;
    private Button btn_dele = null;
    private ListView lsit_dialog;
    private TextView text_dialog;
    int Position = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewInit();

        View view_Search = View.inflate(getApplicationContext(), R.layout.dailog_query, null);

        mAlertDialog = new AlertDialog.Builder(MainActivity.this).setView(view_Search).create();

        mAlertDialog.setTitle("你选择的题目:");

        Toast.makeText(this, "Test Start!", Toast.LENGTH_SHORT).show();

        cxWay = new CXWay(this);

        //cxWay.SQLUpData();
        try {
            int len = cxWay.GetDataLength();
            if(len > 0)
                Toast.makeText(this, "数据长度:" + len, Toast.LENGTH_SHORT).show();
            else
                cxWay.SQLUpData();
        } catch (Exception e) {
            e.printStackTrace();
        }


//        subjectList = new ArrayList<>();
//
//        //注意，搜索关键字用 % 号扩起来，SQLite要求
//        //选项由 # 分开，使用 split("#")即可获得所有选项
//        subjectList = cxWay.SQLFind(new String[]{"%小脑性共济失调%", "%的描述%"});
//
//        display.setText("两个关键字: %小脑性共济失调%  %的描述%" + "\n"
//            + "搜索到题目数量:" + subjectList.size() + "\n"
//            + "题目:" + subjectList.get(0).Subject + "\n"
//            + "选项:" + subjectList.get(0).Option + "\n"
//            + "答案:" + subjectList.get(0).Answer + "\n"
//        );

//         list.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,showlist));
//
//        list.setTextFilterEnabled(true);//设置list可以被过虑

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                Log.d(TAG, "onQueryTextSubmit:   " + query);
                subjectList = new ArrayList<>();
                showlist = new ArrayList<String>(); //搜索建议显示列表
                String[] sourceStrArrays = query.split(" ");
                for (int i = 0; i < sourceStrArrays.length; i++) {
                    sourceStrArrays[i] = "%"+sourceStrArrays[i]+"%";
                    Log.d(TAG, "onQueryTextSubmit: "+sourceStrArrays[i]);
                }
             //   String[] Find = new String[]{"%"+query+"%","%"+query+"%","%"+query+"%"};
             //   Find[0] = "%"+query+"%";
                //注意，搜索关键字用 % 号扩起来，SQLite要求
                //选项由 # 分开，使用 split("#")即可获得所有选项
                subjectList = cxWay.SQLFind(sourceStrArrays);
                for (int i = 0; i < subjectList.size(); i++) {

                    Log.d(TAG, "getData:          "+subjectList.get(i).Subject);
                    showlist.add(subjectList.get(i).Subject);
                }
                list.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,showlist));
                list.setTextFilterEnabled(true);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Position = position;
                mAlertDialog.show();
                MyHandler.sendEmptyMessage(0x01);


            }
        });




    }

    private void ViewInit(){

        mSearchView = (SearchView) findViewById(R.id.Search);
        display = (TextView) findViewById(R.id.xianshi);
        list = (ListView) findViewById(R.id.list);


        mSearchView.setIconifiedByDefault(true);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.onActionViewExpanded();
        mSearchView.setBackgroundColor(0x22ff00ff);
        mSearchView.setIconifiedByDefault(true);


    }


    private List<String> getData(){

        List<String> data = new ArrayList<String>();
        String Option = subjectList.get(Position).Option;

        String[] sourceStrArray = Option.split("#");
        for (int i = 0; i < sourceStrArray.length; i++) {

            Log.d(TAG, "getData:          "+sourceStrArray[i]);
            data.add(sourceStrArray[i]);
        }
        data.add("答案:"+subjectList.get(Position).Answer);

        return data;

//        for (int i = 0; i < subjectList.size(); i++) {
//
//            Log.d(TAG, "getData:          "+subjectList.get(0).Subject);
//            data.add(subjectList.get(0).Subject);
//        }
//
//        return data;


    }

    private Handler MyHandler = new Handler(){
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            int what = msg.what;
            if (what == 0x01) {    //update

                text_dialog = (TextView) mAlertDialog.findViewById(R.id.display_dialog);
                lsit_dialog = (ListView) mAlertDialog.findViewById(R.id.list_dialog);
                btn_ok = (Button) mAlertDialog.findViewById(R.id.btn_ok);
                btn_dele = (Button) mAlertDialog.findViewById(R.id.btn_dele);

                //        subjectList = cxWay.SQLFind(new String[]{"%小脑性共济失调%", "%的描述%"});

                text_dialog.setText(subjectList.get(Position).Subject + "\n");
                lsit_dialog.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,getData()));
                lsit_dialog.setTextFilterEnabled(true);


                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAlertDialog.cancel();
                    }
                });
                btn_dele.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mAlertDialog.cancel();
                        mSearchView.clearFocus();//清除焦点


                    }
                });

                //   name_query.setText("wuzhiwen");
                if(mAlertDialog.isShowing()){
                    MyHandler.sendEmptyMessageDelayed(0,1000);
                }
            }else {
                //   mAlertDialog.cancel();
            }
        }
    };


}
