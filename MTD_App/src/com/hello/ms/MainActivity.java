package com.hello.ms;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.Intent;
// import android.database.sqlite.SQLiteDatabase;
// import android.database.sqlite.SQLiteOpenHelper;
// import android.database.Cursor;
// import android.database.SQLException;

// import java.io.IOException;

import com.moodstocks.android.MoodstocksError;
import com.moodstocks.android.Scanner;

public class MainActivity extends Activity implements Scanner.SyncListener {

  // Moodstocks API key/secret pair
	private static final String API_KEY = "d5rofl7bu6c3vgyctcpi";
	private static final String API_SECRET = "HURqVRoDyh0yZ96p";

	private boolean compatible = false;
	private Scanner scanner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		compatible = Scanner.isCompatible();
		if (compatible) {
			try {
				this.scanner = Scanner.get();
				scanner.open(this, API_KEY, API_SECRET);
				scanner.sync(this);
			} catch (MoodstocksError e) {
				e.log();
			}
		}

		// DictionaryOpenHelper myDbHelper = new DictionaryOpenHelper(this);
		// // myDbHelper = new DictionaryOpenHelper(this);
		// try {
		// 	myDbHelper.createDataBase();
		// } catch (IOException ioe) {
		// 	throw new Error("Unable to create database");
		// }
		// try {
		// 	myDbHelper.openDataBase();
		// }catch(SQLException sqle){
		// 	throw sqle;
		// }

		// SQLiteDatabase db = myDbHelper.getReadableDatabase();
		// Cursor cursor = db.rawQuery("SELECT code FROM stops WHERE stop_id=?", new String[] {"150DALE"});
		// Log.d("SQL", cursor.getString(0));
		// cursor.close();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (compatible) {
			try {
				scanner.close();
			} catch (MoodstocksError e) {
				e.log();
			}
		}
	}

	@Override
	public void onSyncStart() {
		Log.d("Moodstocks SDK", "Sync will start.");
	}

	@Override
	public void onSyncComplete() {
		try {
			Log.d("Moodstocks SDK", String.format("Sync succeeded (%d image(s))", scanner.count()));
		} catch (MoodstocksError e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onSyncFailed(MoodstocksError e) {
		Log.d("Moodstocks SDK", "Sync error: " + e.getErrorCode());
	}

	@Override
	public void onSyncProgress(int total, int current) {
		int percent = (int) ((float) current / (float) total * 100);
		Log.d("Moodstocks SDK", String.format("Sync progressing: %d%%", percent));
	}

	public void onScanButtonClicked(View view) {
		startActivity(new Intent(this, ScanActivity.class));
	}
}