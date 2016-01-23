package brighterbrains.com.brighterbrains.com.utility;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by DELL on 1/23/2016.
 */
public class CommonUtility {

    public void raiseToast(String text, Context con) {

        AlertDialog.Builder builder = new AlertDialog.Builder(con);
        builder.setMessage(text);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });

        builder.show();
    }

    public void ridirect(String text, Context con,
                         DialogInterface.OnClickListener listener,
                         DialogInterface.OnDismissListener dismisslistener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(con);
        builder.setMessage(text);
        builder.setPositiveButton("Ok", listener);
        AlertDialog ad = builder.create();
        ad.setOnDismissListener(dismisslistener);
        ad.show();
    }


    public void redirectButtons(String text, Context con,
                                DialogInterface.OnClickListener listener,
                                DialogInterface.OnDismissListener dismisslistener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(con);
        builder.setMessage(text);
        builder.setPositiveButton("Yes", listener);
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });
        AlertDialog ad = builder.create();
        ad.setOnDismissListener(dismisslistener);

        ad.show();



    }


}
