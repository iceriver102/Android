package com.example.sqllite;

import java.util.LinkedList;
import java.util.List;

import com.example.Json.userDataJson;
import com.example.appData.userData;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

	// Database Version
	private static final int DATABASE_VERSION = 1;
	// Database Name
	private static final String DATABASE_NAME = "Alta_BMS";

	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// SQL statement to create book table
		String CREATE_USER_TABLE = "CREATE TABLE user ( "
				+ "id INTEGER PRIMARY KEY AUTOINCREMENT, "+"user_id TEXT, " + "user TEXT, "
				+ "fullName TEXT, " + "pass TEXT, "
				+ "Timestamp DATETIME DEFAULT CURRENT_TIMESTAMP )";
		// create books table
		db.execSQL(CREATE_USER_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older books table if existed
		db.execSQL("DROP TABLE IF EXISTS user");
		this.onCreate(db);
	}

	// ---------------------------------------------------------------------

	/**
	 * CRUD operations (create "add", read "get", update, delete) book + get all
	 * books + delete all books
	 */

	// Books table name
	private static final String TABLE_USER = "user";

	// Books Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_USER_ID="user_id";
	private static final String KEY_USER = "user";
	private static final String KEY_PASS = "pass";
	private static final String KEY_FULLNAME = "fullName";
	private static final String KEY_TIME = "Timestamp";

	private static final String[] COLUMNS = { KEY_ID,KEY_USER_ID,KEY_USER, KEY_FULLNAME,
			KEY_PASS, KEY_TIME };

	public void addUser(userDataJson user) {
		Log.d("addBook", user.toString());
		// 1. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();

		// 2. create ContentValues to add key "column"/value
		ContentValues values = new ContentValues();
		values.put(KEY_USER_ID, user.getUserId());
		values.put(KEY_USER, user.getUserName());
		values.put(KEY_FULLNAME, user.getFullName());
		values.put(KEY_PASS, user.getPass());

		// 3. insert
		db.insert(TABLE_USER, // table
				null, // nullColumnHack
				values); // key/value -> keys = column names/ values = column
							// values

		// 4. close
		db.close();
	}

	public void addUser(userData user) {
		Log.d("addBook", user.toString());
		// 1. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();

		// 2. create ContentValues to add key "column"/value
		ContentValues values = new ContentValues();
		values.put(KEY_USER_ID, user.user_id);
		values.put(KEY_USER, user.getUserName());
		values.put(KEY_FULLNAME, user.fullName);
		values.put(KEY_PASS, user.getPass());
		// 3. insert
		db.insert(TABLE_USER, // table
				null, // nullColumnHack
				values); // key/value -> keys = column names/ values = column
							// values

		// 4. close
		db.close();
	}
	public void emptyUser(){
		SQLiteDatabase db= this.getWritableDatabase();
        db.delete(TABLE_USER, null, null);
	}

	public int countUser() {
		int num = 0;
		try {
			String query = "SELECT  count(*) as num FROM " + TABLE_USER;			
			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(query, null);
			cursor.moveToFirst();
			num = cursor.getInt(0);
		} catch (Exception ex) {

		}
		return num;
	}

	public userData getUser(int id) {

		// 1. get reference to readable DB
		SQLiteDatabase db = this.getReadableDatabase();

		// 2. build query
		Cursor cursor = db.query(TABLE_USER, // a. table
				COLUMNS, // b. column names
				" id = ?", // c. selections
				new String[] { String.valueOf(id) }, // d. selections args
				null, // e. group by
				null, // f. having
				null, // g. order by
				null); // h. limit

		// 3. if we got results get the first one
		if (cursor != null)
			cursor.moveToFirst();

		// 4. build book object
		userData user = new userData();
		user.setId(Integer.parseInt(cursor.getString(0)));
		user.user_id=Integer.parseInt(cursor.getString(1));
		user.setUserName(cursor.getString(2));
		user.fullName = cursor.getString(3);
		user.setPass(cursor.getString(4));
		user.setDate(cursor.getString(5),"dd/MM/yyyy");
		
		Log.d("getUser(" + id + ")", user.toString());

		// 5. return book
		return user;
	}

	// Get All Books
	public List<userData> getAllUsers() {
		List<userData> list_user = new LinkedList<userData>();

		// 1. build the query
		String query = "SELECT  * FROM " + TABLE_USER;

		// 2. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		// 3. go over each row, build book and add it to list
		userData user = null;
		if (cursor.moveToFirst()) {
			do {
				user = new userData();
				user.setId(Integer.parseInt(cursor.getString(0)));
				user.user_id=Integer.parseInt(cursor.getString(1));
				user.setUserName(cursor.getString(2));
				user.fullName = cursor.getString(3);
				user.setPass(cursor.getString(4));
				user.setDate(cursor.getString(5),"yyyy-MM-dd hh:mm:ss");
				Log.d("getUer("+user.user_id+")", user.toString());

				// Add book to books
				list_user.add(user);
			} while (cursor.moveToNext());
		}

		Log.d("getAllUsers()", list_user.toString());

		// return books
		return list_user;
	}

	// Updating single book
	public int updateUser(userData user) {

		// 1. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();

		// 2. create ContentValues to add key "column"/value
		ContentValues values = new ContentValues();
		values.put(KEY_FULLNAME, user.fullName);
		values.put(KEY_PASS, user.getPass());

		// 3. updating row
		int i = db.update(TABLE_USER, // table
				values, // column/value
				KEY_ID + " = ?", // selections
				new String[] { String.valueOf(user.getId()) }); // selection
																// args

		// 4. close
		db.close();
		return i;
	}

	// Deleting single book
	public void deleteUser(userData user) {
		// 1. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();

		// 2. delete
		db.delete(TABLE_USER, KEY_ID + " = ?",
				new String[] { String.valueOf(user.getId()) });

		// 3. close
		db.close();
		Log.d("deleteUser", user.toString());

	}
}
