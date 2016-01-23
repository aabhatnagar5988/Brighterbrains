package brighterbrains.com.brighterbrains;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.backup.BackupManager;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.channels.FileChannel;

import brighterbrains.com.brighterbrains.com.utility.CommonUtility;
import brighterbrains.com.brighterbrains.com.utility.ImageUtility;
import brighterbrains.com.data.DBAdapter;
import brighterbrains.com.data.ItemDataModel;


public class AddRecordsActivity extends ActionBarActivity {

    private Toolbar toolbar;
    ImageView itemImage;
    EditText editname,
            editdesc,
            editlocation,
            editcost;

    LinearLayout updateLayout;
    Button save,update,delete;

    CommonUtility cv;
    DBAdapter db;
    String imagePath = "";
    ImageUtility iUtility;

    private BackupManager backupManager;

    //-----Used for the edit Mode-------------
    public static boolean isEditMode;
    public static ItemDataModel itemDataModel;
    //----------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getUIComponents();
        db = new DBAdapter(this);
        cv = new CommonUtility();
        iUtility=new ImageUtility(this);
        backupManager = new BackupManager(getBaseContext());
        setclickAction();
        if(isEditMode)
        {
            initUIComponents();
        }
    }

    //------Fill the data for the edit mode---
    private void initUIComponents()
    {
      imagePath=itemDataModel.Image;
      itemImage.setImageBitmap(iUtility.getImage(imagePath,false));
      editname.setText(itemDataModel.Name);
      editdesc.setText(itemDataModel.Description);
      editcost.setText(itemDataModel.Cost);
      editlocation.setText(itemDataModel.Location);

      save.setVisibility(View.GONE);
      updateLayout.setVisibility(View.VISIBLE);
    }
    //---initUIComponents() body ends here----

    //-----Initialize the UI Components from resource files----
    private void getUIComponents() {
        setContentView(R.layout.activity_addrecord);
        toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        itemImage = (ImageView) findViewById(R.id.itemImage);
        editname = (EditText) findViewById(R.id.editname);
        editdesc = (EditText) findViewById(R.id.editdesc);
        editlocation = (EditText) findViewById(R.id.editlocation);
        editcost = (EditText) findViewById(R.id.editcost);
        save = (Button) findViewById(R.id.save);
        update = (Button) findViewById(R.id.update);
        delete = (Button) findViewById(R.id.delete);
        updateLayout= (LinearLayout) findViewById(R.id.updateLayout);
    }
   //-----------getUIComponents() ends here-----------------------

    //---------Set up click listener for the buttons----------
    private void setclickAction() {
        itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImageOptionDialog();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAction();
            }
        });
    }

    //----------setclickAction() ends here---------------------

    //----Validate the data if all correct insert into the database------
    private void validateData() {
        String name = editname.getText().toString();
        String location = editlocation.getText().toString();
        String desc = editdesc.getText().toString();
        String cost = editcost.getText().toString();

        if (TextUtils.isEmpty(name)) {
            cv.raiseToast("Please enter Name.", this);
        } else if (TextUtils.isEmpty(location)) {
            cv.raiseToast("Please enter location.", this);
        } else if (TextUtils.isEmpty(cost)) {
            cv.raiseToast("Please enter cost.", this);
        } else if (TextUtils.isEmpty(desc)) {
            cv.raiseToast("Please enter description.", this);
        } else if (TextUtils.isEmpty(imagePath)) {
            cv.raiseToast("Please select image.", this);
        } else {
            try {
                String message="Record Added Successfully";
                db.open();
                if(isEditMode)
                {
                  db.update(itemDataModel._id,name, desc, location, imagePath, cost);
                  message="Record Updated Successfully";
                }

                else
                db.insertItem(name, desc, location, imagePath, cost);

                db.close();


                cv.ridirect(message, this, null, new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        finish();
                    }
                });
            } catch (Exception e) {

            }
        }
    }


    //---------Delete the Item from Database-------------------
    private void deleteAction()
    {
        {
            cv.redirectButtons("Do you want to delete this item?", AddRecordsActivity.this, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try
                    {
                        db.open();
                        db.deleteItem(itemDataModel._id);
                        db.close();

                        cv.ridirect("Item Deleted Successfully",AddRecordsActivity.this,null,new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                finish();
                            }
                        });
                    }

                    catch (Exception e)
                    {

                    }
                }
            }, null);
        }
    }

    //-------deleteAction Body ends here------------------------
    //---------validateData() body ends here---------------------

    //-----Show selection to user between click image or select from gallery-----
    public void addImageOptionDialog() {
        final String[] items = new String[]{"Take from camera",
                "Select from gallery"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.select_dialog_item, items);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Picture");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) { // pick from
                // camera
                if (item == 0) {
                    dialog.dismiss();
                    takePhoto();

                } else { // pick from file
                    dialog.dismiss();
                    pickPhoto();

                }
            }
        });

        builder.show();
    }
  //-------------showDialog1() ends here----------------------------



    //----Capture image from camera---------------------------------
    private File photo1;
    public void takePhoto() {
        if (!android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this,
                    "Please insert SDcard for capturing photo.",
                    Toast.LENGTH_SHORT).show();
        } else {
            photo1=iUtility.getImageFile();
            try {

               /* String fileName = Image_name1 + ".jpg";
                ContentValues values = new ContentValues();
                values.put(MediaStore.MediaColumns.TITLE, fileName);
                values.put(MediaStore.Images.ImageColumns.DESCRIPTION, "Image capture by camera");*/

                Intent cameraIntent = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);

                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photo1));
                cameraIntent.putExtra("return-data", true);

                startActivityForResult(cameraIntent, 4);
            } catch (Exception e) {
                Toast.makeText(this, "" + e, Toast.LENGTH_LONG).show();
            }
        }

    }

    //---------takePhoto() body ends here--------------------

    //---Pick Image from Gallery-----------------------------
    public void pickPhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(intent, "Complete action using"), 3);
    }
    //-----pickPhoto() body ends here------------------------

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 3) {
                Uri selectedImage = data.getData();

                if (Build.VERSION.SDK_INT < 19) {
                    String[] filePathColumn = { MediaStore.MediaColumns.DATA };

                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);

                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imagePath = cursor.getString(columnIndex);
                    validatePath();
                    itemImage.setImageBitmap(iUtility.getImage(imagePath,false));
                    cursor.close();

                }

                else {

                    ParcelFileDescriptor parcelFileDescriptor;
                    try {
                        parcelFileDescriptor = getContentResolver()
                                .openFileDescriptor(selectedImage, "r");
                        FileDescriptor fileDescriptor = parcelFileDescriptor
                                .getFileDescriptor();
                        imagePath = iUtility.getImagePath(selectedImage);
                        validatePath();
                        itemImage.setImageBitmap(iUtility.getImage(imagePath, false));
                    } catch (Exception e) {
                        // TODO: handle exception
                        System.out.print(">>>>>"+e);
                    }
                }

            }

            else {
                imagePath = photo1.getAbsolutePath();
                validatePath();
                itemImage.setImageBitmap(iUtility.getImage(imagePath, false));
            }
        }
    }


    //----Validates if the selected images is not in brighter brains folder it copies it there.
    private void validatePath()
    {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED))

        {
            if (!imagePath.contains("brighterbrains")) {
                File source = new File(imagePath);
                File dest = iUtility.getImageFile();
                try {
                    iUtility.copyFile(source, dest);
                    imagePath = dest.getAbsolutePath();
                } catch (Exception e) {
                    System.out.println("error>>>>>" + e);
                }
            }
        }
    }
    //----validatePath() body ends here---------------------------


    @Override
    protected void onDestroy() {
        super.onDestroy();
        backupManager.dataChanged();
    }
}