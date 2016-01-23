package brighterbrains.com.brighterbrains;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by DELL on 1/23/2016.
 */
public class ItemViewHolder extends RecyclerView.ViewHolder {
    CardView cv;
    TextView itemName;
    TextView itemDesc;
    ImageView itemPhoto;

    ItemViewHolder(View itemView) {
        super(itemView);
        cv = (CardView)itemView.findViewById(R.id.cv);
        itemName = (TextView)itemView.findViewById(R.id.itemName);
        itemDesc = (TextView)itemView.findViewById(R.id.item_desc);
        itemPhoto = (ImageView)itemView.findViewById(R.id.item_photo);

    }
}

