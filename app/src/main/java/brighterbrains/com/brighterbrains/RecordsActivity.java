package brighterbrains.com.brighterbrains;

import android.app.backup.BackupManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import brighterbrains.com.data.DBAdapter;
import brighterbrains.com.data.ItemDataModel;


public class RecordsActivity extends ActionBarActivity {

    private Toolbar toolbar;
    ImageButton addButton;
    TextView tv;
    RecyclerView rv;
    ArrayList<ItemDataModel> itemsList=new ArrayList<>();
    DBAdapter db;


    //------Get the data from the database----------
    //------Fill the itemsList----------------------
    //------Set Adapter to recyclerview-------------
    private void getData()
    {
        itemsList.clear();
        db=new DBAdapter(this);
        try {
            db.open();
            Cursor c=db.getItems();
            if(c!=null)
            {
                for(int i=0;i<c.getCount();i++)
                {
                    c.moveToPosition(i);
                    int id=c.getInt(0);
                    String name=c.getString(1);
                    String desc=c.getString(2);
                    String location=c.getString(3);
                    String cost=c.getString(4);
                    String image=c.getString(5);

                    ItemDataModel idModel=new ItemDataModel(id,name,desc,image,location,cost);
                    itemsList.add(idModel);
                }
            }

            c.close();
            db.close();
        }
        catch (Exception e)
        {

        }

        if(itemsList.size()>0) {
            RVAdapter adapter = new RVAdapter(itemsList, this);
            rv.setAdapter(adapter);
            tv.setVisibility(View.GONE);
        }
    }

    //-----getData() body ends here-------------------

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        addButton = (ImageButton) findViewById(R.id.imageButton);
        tv=(TextView)findViewById(R.id.TextAct1);
        rv = (RecyclerView)findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddRecordsActivity.isEditMode=false;
                startActivity(new Intent(RecordsActivity.this,AddRecordsActivity.class));
            }
        });
    }
}
