package brighterbrains.com.brighterbrains;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import brighterbrains.com.brighterbrains.com.utility.ImageUtility;
import brighterbrains.com.data.ItemDataModel;

/**
 * Created by DELL on 1/23/2016.
 */
public class RVAdapter extends RecyclerView.Adapter<ItemViewHolder>{


    ArrayList<ItemDataModel> itemList;
    Context context;
    public RVAdapter(ArrayList<ItemDataModel> itemList,Context con) {
        this.itemList = itemList;
        this.context=con;
    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_item, viewGroup, false);
        ItemViewHolder pvh = new ItemViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder itemViewHolder, int i) {
        itemViewHolder.itemName.setText(itemList.get(i).Name);
        itemViewHolder.itemDesc.setText(itemList.get(i).Description);
        itemViewHolder.itemPhoto.setImageBitmap(new ImageUtility(context).getImage(itemList.get(i).Image,true));
        itemViewHolder.cv.setTag(itemList.get(i));
        itemViewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemDataModel idm=(ItemDataModel)v.getTag();
                AddRecordsActivity.isEditMode=true;
                AddRecordsActivity.itemDataModel=idm;
                context.startActivity(new Intent(context,AddRecordsActivity.class));
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}