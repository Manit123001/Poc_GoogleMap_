package app.akexorcist.sharedpreferences;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

public class Main extends Activity {
	final String PREF_NAME = "LoginPreferences";
	final String KEY_USERNAME = "Username";
	final String KEY_REMEMBER = "RememberUsername";

	SharedPreferences sp;
	SharedPreferences.Editor editor;
	
	EditText edtUsername;
	CheckBox cbRemember;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		sp = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		editor = sp.edit();
		
		edtUsername = (EditText)findViewById(R.id.edtUsername);
		edtUsername.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) { }
			public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
			public void afterTextChanged(Editable s) {
				editor = sp.edit();
				editor.putString(KEY_USERNAME, s.toString());
				editor.commit();
			}
		});
		
		cbRemember = (CheckBox)findViewById(R.id.cbRemember);
		cbRemember.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				editor.putBoolean(KEY_REMEMBER, isChecked);
				editor.commit();
			}
		});
		
		boolean isRemember = sp.getBoolean(KEY_REMEMBER, false); 
		cbRemember.setChecked(isRemember);
		
		if(isRemember) {
			String username = sp.getString(KEY_USERNAME, "");
			edtUsername.setText(username);
		}
	}
}
