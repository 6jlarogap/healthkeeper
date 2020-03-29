package com.health.main;

import java.text.SimpleDateFormat;
import java.util.List;

import com.health.data.User;
import com.health.db.DB;
import com.health.db.UserDB;
import com.health.dialog.AnonimUserListDialogFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class LoginListFragment extends Fragment  {
	
	private ListView lvLogin;
	private Button btnAddAccount;
	private Button btnRegister;
	private Button btnAnonimSignIn;
	
	private View loginFormView;
	private View loginStatusView;
	private TextView tvLoginStatusMessage;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		LinearLayout wrapper = new LinearLayout(getActivity());
		final AuthorizeActivity baseActivity = (AuthorizeActivity) getActivity();
		View v = (View) inflater.inflate(R.layout.login_list_fragment, wrapper, true);
		
		this.loginFormView = v.findViewById(R.id.login_list_form);
		this.loginStatusView = v.findViewById(R.id.login_status);
		this.tvLoginStatusMessage = (TextView) v.findViewById(R.id.login_status_message);
		
		this.lvLogin = (ListView) v.findViewById(R.id.lvLogin);
		this.btnAddAccount = (Button) v.findViewById(R.id.btnAddAccount);
		this.btnRegister = (Button) v.findViewById(R.id.btnRegister);
		this.btnAnonimSignIn = (Button) v.findViewById(R.id.btnAnonimSignIn);		
		this.btnAnonimSignIn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				baseActivity.showProgress(true, loginFormView, loginStatusView, tvLoginStatusMessage, R.string.login_progress_signing_in);
				//((AuthorizeActivity)getActivity()).loginAnonimUser();
				AnonimUserListDialogFragment newFragment = AnonimUserListDialogFragment.newInstance();
				newFragment.setTargetFragment(LoginListFragment.this, AuthorizeActivity.ANONIM_LIST_DIALOG_FRAGMENT);
				newFragment.show(getFragmentManager(), "dialog");
				baseActivity.showProgress(false, loginFormView, loginStatusView, tvLoginStatusMessage, R.string.login_progress_signing_in);
			}
		});
		this.btnAddAccount.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((AuthorizeActivity)getActivity()).openLoginFragment(null);
			}
		});
		this.btnRegister.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((AuthorizeActivity)getActivity()).openRegistrationFragment();				
			}
		});
		
		List<User> userList =  UserDB.getRealUsers();
		User[] users = userList.toArray(new User[userList.size()]);
		UserAdapter userAdapter = new UserAdapter(getActivity(), users);
		this.lvLogin.setAdapter(userAdapter);
		this.lvLogin.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    			final User user = (User) lvLogin.getAdapter().getItem(position);
    			if(user.isAskPassword()){
    				showUserPasswordDialog(baseActivity, user);
    			} else {
    				baseActivity.showProgress(true, loginFormView, loginStatusView, tvLoginStatusMessage, R.string.login_progress_signing_in);
    				((AuthorizeActivity) getActivity()).signIn(user);
    			}
				
            }
		});
		
		return v;
	}
	
	public void showUserPasswordDialog(final AuthorizeActivity baseActivity, final User user){
		if(user.isAskPassword()){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
            alertDialog.setTitle(user.getFullName());            
            LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rootView = inflater.inflate(R.layout.enter_password_dialog, null);
            final EditText etPassword = (EditText) rootView.findViewById(R.id.etPassword);
            final CheckBox cbStorePassword = (CheckBox) rootView.findViewById(R.id.cbStorePassword);
            alertDialog.setView(rootView);
            alertDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {					
				@Override
				public void onClick(DialogInterface dialog, int which) {
					String password = etPassword.getText().toString();
					if(!TextUtils.isEmpty(password)){
						baseActivity.showProgress(true, loginFormView, loginStatusView, tvLoginStatusMessage, R.string.login_progress_signing_in);
			            user.setPassword(password);
			            if(cbStorePassword.isChecked()){
			            	user.setIsStorePassword(1);
			            } else {
			            	user.setIsStorePassword(0);
			            }
			            ((AuthorizeActivity) getActivity()).signIn(user);	
					}
					dialog.cancel();
				}
			});
            alertDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {					
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();					
				}
			});
            alertDialog.show();
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}
	
	private class UserAdapter extends ArrayAdapter<User> {
		
		private SimpleDateFormat sdf;
		
		public UserAdapter(Context context, User[] users) {
			super(context, R.layout.item_login_list, users);
			this.sdf = new SimpleDateFormat("yyyy/MM/dd");
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final User user = getItem(position);
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_login_list, null);
			}
			final ImageView ivStorePassword = (ImageView) convertView.findViewById(R.id.ivStorePassword);			
			((TextView) convertView.findViewById(R.id.tvName)).setText(String.format("%s %s %s", user.getLName(), user.getFName(), user.getMName()));
			((TextView) convertView.findViewById(R.id.tvBirthDate)).setText(sdf.format(user.getBirthDate()));
			if(user.isAskPassword()){
				ivStorePassword.setBackgroundResource(R.drawable.lock);
			} else {
				ivStorePassword.setBackgroundResource(R.drawable.unlock);
			}
			ivStorePassword.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(!user.isAskPassword()){
						user.setIsStorePassword(0);
						DB.db().newSession().getUserDao().update(user);						
						ivStorePassword.setBackgroundResource(R.drawable.lock);
					}
				}
			});
			return convertView;
		}
	}
	
	public void showProgress(final boolean show, int statusMessageId) {
		AuthorizeActivity baseActivity = ((AuthorizeActivity)getActivity());
		baseActivity.showProgress(show, this.loginFormView, this.loginStatusView, this.tvLoginStatusMessage, statusMessageId);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		AuthorizeActivity baseActivity = ((AuthorizeActivity)getActivity());
		baseActivity.onActivityResult(requestCode, resultCode, data);
	}

}
