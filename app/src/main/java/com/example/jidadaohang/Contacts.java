package com.example.jidadaohang;

import android.net.Uri;
public class Contacts {
	// Ù–‘

	public static final String AUTHOR="com.example.android.ContactsProvider";
	public static final String TABLENAME="contacts";
	public static final String DBNAME="mycontacts.db";
	public static final String ID="_id";
	public  static final String NAME="name";
	public static final Uri CONTENT_URI=Uri.parse("content://"+AUTHOR);

}
