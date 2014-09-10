package com.example.jidadaohang;

import android.app.DownloadManager.Query;
import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class DataBaseProvider extends ContentProvider {
	//contentProvider
	private SearchDatabase dh;
	private  static final  int QUERY_NAME= 1;
	public static final int INSERT_NAME= 2;
	public static UriMatcher uriMatcher;
	static{
		uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(Contacts.AUTHOR,SearchManager.SUGGEST_URI_PATH_QUERY, QUERY_NAME);
	}
	
	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		dh=new SearchDatabase(getContext(), Contacts.DBNAME);
		return false;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
	    dh.insertContacts(values);
		return null;
	}



	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
	Log.i("debug", "uri--------------"+uri.toString());
		 switch (uriMatcher.match(uri)) {
		case QUERY_NAME:
			   if(selectionArgs==null){
	            	 return null;
	             }		
				return dh.queryContacts(selectionArgs[0]);

		default:
			break;
		}
          return null;

	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}
