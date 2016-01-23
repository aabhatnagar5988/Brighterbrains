package brighterbrains.com.brighterbrains;

import android.app.backup.BackupAgentHelper;
import android.app.backup.FileBackupHelper;

import java.io.File;

/**
 * Created by DELL on 1/23/2016.
 */
public class BackupAgent extends BackupAgentHelper {
    String DATABASE_NAME = "brighterbrainsdb";
    String DATABASE_FILE_NAME = "brighterbrainsdb.db";

    @Override
    public void onCreate() {
        FileBackupHelper dbs = new FileBackupHelper(this, DATABASE_FILE_NAME);
        addHelper("dbs", dbs);
    }

    @Override
    public File getFilesDir() {
        File path = getDatabasePath(DATABASE_FILE_NAME);
        return path.getParentFile();
    }
}
