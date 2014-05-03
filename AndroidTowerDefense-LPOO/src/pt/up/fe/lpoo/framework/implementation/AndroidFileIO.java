package pt.up.fe.lpoo.framework.implementation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import pt.up.fe.lpoo.framework.FileIO;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Environment;
import android.preference.PreferenceManager;

/**
 * Information about the implementation of the framework can be found at
 *  http://www.kilobolt.com/day-6-the-android-game-framework-part-ii.html
 */
public class AndroidFileIO implements FileIO {
    Context context;
    AssetManager assets;
    String externalStoragePath;
    String appPath;

    public AndroidFileIO(Context context) {
        this.context = context;
        this.assets = context.getAssets();
        this.externalStoragePath = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + File.separator;
        this.appPath = null;
    }
    
    @Override
    public void setAppPath(String appPath){
    	File f = new File(externalStoragePath + appPath);
    	if(!f.exists())
    		f.mkdirs();
    	this.appPath = appPath;
    }
    
    @Override
    public InputStream readAsset(String file) throws IOException {
        return assets.open(file);
    }

    @Override
    public InputStream readFile(String file) throws IOException {
    	String completePath = externalStoragePath;
    	if(this.appPath != null)
    		completePath += appPath;
    	completePath += file;
        return new FileInputStream(completePath);
    }
    
    @Override
    public OutputStream writeFile(String file) throws IOException {
    	String completePath = externalStoragePath;
    	if(this.appPath != null)
    		completePath += appPath;
    	completePath += file;
        return new FileOutputStream(completePath);
    }
    
    public SharedPreferences getSharedPref() {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}