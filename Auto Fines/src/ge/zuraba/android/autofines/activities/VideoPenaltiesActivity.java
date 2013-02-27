package ge.zuraba.android.autofines.activities;

import ge.zuraba.android.autofines.R;
import ge.zuraba.android.autofines.preferences.Preferences;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class VideoPenaltiesActivity extends SherlockActivity implements
		OnClickListener, OnSharedPreferenceChangeListener, TextWatcher {

	private Button btnPolice, btnCtpark, btnSearchCT, btnSearchPO;

	private EditText plateNumCT, docNo, vehicleNum;

	private LinearLayout layoutCtPark, layoutPolice;

	private CheckBox tickets;

	private SharedPreferences sharedPrefs;

	private final String urlCT = "http://ct-park.ge/ka/pay";

	private final String urlPO = "http://videos.police.ge/submit-index.php";

	private int textLength;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.Theme_Sherlock);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		ActionBar actionBar = getSupportActionBar();
		if (actionBar == null) {
			return;
		}

		actionBar.show();
		actionBar.setIcon(R.drawable.icon);

		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

		layoutCtPark = (LinearLayout) findViewById(R.id.linearLayout1);
		layoutPolice = (LinearLayout) findViewById(R.id.linearLayout2);

		tickets = (CheckBox) findViewById(R.id.checkBox1);

		docNo = (EditText) findViewById(R.id.docNo);
		vehicleNum = (EditText) findViewById(R.id.vehicleNum);
		plateNumCT = (EditText) findViewById(R.id.plateNumCT);
		plateNumCT.requestFocus();

		btnPolice = (Button) findViewById(R.id.btnPolice);
		btnCtpark = (Button) findViewById(R.id.btnCtpark);
		btnSearchCT = (Button) findViewById(R.id.btnSearchCT);
		btnSearchPO = (Button) findViewById(R.id.btnSearchPO);

		plateNumCT.setFilters(new InputFilter[] { new InputFilter.AllCaps(),
				new InputFilter.LengthFilter(9) });
		vehicleNum.setFilters(new InputFilter[] { new InputFilter.AllCaps(),
				new InputFilter.LengthFilter(9) });
		docNo.setFilters(new InputFilter[] { new InputFilter.AllCaps(),
				new InputFilter.LengthFilter(9) });

		plateNumCT.addTextChangedListener(this);
		vehicleNum.addTextChangedListener(this);
		btnSearchPO.setOnClickListener(this);
		btnSearchCT.setOnClickListener(this);
		btnPolice.setOnClickListener(this);
		btnCtpark.setOnClickListener(this);

		if (sharedPrefs.getString("plate", "").length() > 0) {
			String ser = sharedPrefs.getString("plate", "").substring(0, 3);
			String num = sharedPrefs.getString("plate", "").substring(3, 6);
			plateNumCT.setText(ser + " - " + num);
			vehicleNum.setText(ser + " - " + num);
			docNo.setText(sharedPrefs.getString("techPass", ""));
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.btnPolice) {
			if (layoutPolice.getVisibility() == View.VISIBLE) {
				layoutPolice.setVisibility(View.GONE);
				return;
			}
			layoutPolice.setVisibility(View.VISIBLE);
			layoutCtPark.setVisibility(View.GONE);
		} else if (id == R.id.btnCtpark) {
			if (layoutCtPark.getVisibility() == View.VISIBLE) {
				layoutCtPark.setVisibility(View.GONE);
				return;
			}
			layoutCtPark.setVisibility(View.VISIBLE);
			layoutPolice.setVisibility(View.GONE);
		} else if (id == R.id.btnSearchCT) {
			if (!isNetAvailable()) {
				Toast.makeText(this, R.string.netError, Toast.LENGTH_LONG)
						.show();
				return;
			}
			if (plateNumCT.getText().length() == 0) {
				return;
			}
			new AsyncTask<Void, Void, String>() {
				private ProgressDialog dialog;

				@Override
				protected void onPreExecute() {
					dialog = new ProgressDialog(VideoPenaltiesActivity.this);
					dialog.setMessage("იტვირთება");
					dialog.show();
				}

				@Override
				protected String doInBackground(Void... params) {
					return getResponse(urlCT, plateNumCT.getText().toString(),
							"");
				}

				@Override
				protected void onPostExecute(String result) {
					if (result.contains("არც ერთი ჯარიმა")) {
						Toast.makeText(getBaseContext(),
								R.string.fine_not_found, Toast.LENGTH_LONG)
								.show();
						dialog.dismiss();
						return;
					} else {
						Intent ctparkFines = new Intent(
								VideoPenaltiesActivity.this, CtparkFines.class);
						ctparkFines.putExtra("plateNumber", result);
						ctparkFines.putExtra("checkbox",
								tickets.isChecked() ? true : false);
						startActivity(ctparkFines);
					}
					if (dialog.isShowing()) {
						dialog.dismiss();
					}
				}
			}.execute();
		} else if (id == R.id.btnSearchPO) {
			if (!isNetAvailable()) {
				Toast.makeText(this, R.string.netError, Toast.LENGTH_LONG)
						.show();
				return;
			}
			if (docNo.getText().length() == 0
					|| vehicleNum.getText().length() == 0) {
				return;
			}
			new AsyncTask<Void, Void, String>() {
				private ProgressDialog dialog;

				@Override
				protected void onPreExecute() {
					dialog = new ProgressDialog(VideoPenaltiesActivity.this);
					dialog.setMessage("იტვირთება");
					dialog.show();
				}

				@Override
				protected String doInBackground(Void... params) {
					if (getResponse(urlPO, vehicleNum.getText().toString(),
							docNo.getText().toString()).contains(
							getText(R.string.fine_not_found))) {
						return null;
					}
					return getResponse(urlPO, vehicleNum.getText().toString(),
							docNo.getText().toString());
				}

				@Override
				protected void onPostExecute(String result) {
					if (result != null) {
						Intent videoFines = new Intent(
								VideoPenaltiesActivity.this,
								FinesActivity.class);
						videoFines.putExtra("httpResponse", result);
						startActivity(videoFines);
					} else {
						Toast.makeText(getBaseContext(),
								R.string.fine_not_found, Toast.LENGTH_LONG)
								.show();
						dialog.dismiss();
						return;
					}
					if (dialog.isShowing()) {
						dialog.dismiss();
					}
				}
			}.execute();
		}
	}

	public SharedPreferences prefs() {
		PreferenceManager.getDefaultSharedPreferences(this)
				.registerOnSharedPreferenceChangeListener(this);
		return PreferenceManager.getDefaultSharedPreferences(this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) {

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == R.id.item_settings) {
			this.startActivity(new Intent(this, Preferences.class));
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = this.getSupportMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	private String postData(String url, String vehicleNumber, String passNum)
			throws ClientProtocolException, IOException {

		String fineOrTicket = tickets.isChecked() ? "parking" : "fine";
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		List<NameValuePair> pairs = new ArrayList<NameValuePair>(2);

		try {
			if (passNum.equals("")) {
				pairs.add(new BasicNameValuePair("IsPostBack", "true"));
				pairs.add(new BasicNameValuePair("activeOption", fineOrTicket));
				pairs.add(new BasicNameValuePair("CarPlateNumber",
						vehicleNumber));
			} else {
				pairs.add(new BasicNameValuePair("documentNo", passNum));
				pairs.add(new BasicNameValuePair("vehicleNo2", vehicleNumber));
			}
			post.setEntity(new UrlEncodedFormEntity(pairs));
		} catch (Exception e) {
			e.printStackTrace();
		}
		ResponseHandler<String> handler = new BasicResponseHandler();
		return client.execute(post, handler);
	}

	private String getResponse(String url, String text1, String text2) {
		try {
			return postData(url, text1.replace(" - ", ""), text2);
		} catch (ClientProtocolException e) {
			return e.getMessage();
		} catch (IOException e) {
			return e.getMessage();

		}
	}

	private boolean isNetAvailable() {
		ConnectivityManager connection = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetinfo = connection.getActiveNetworkInfo();
		return activeNetinfo != null;
	}

	@Override
	public void afterTextChanged(Editable s) {
		if (s.length() == 3 && textLength < s.length()) {
			s.append(" - ");
			plateNumCT.setSelection(plateNumCT.getText().length());
			vehicleNum.setSelection(vehicleNum.getText().length());
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		textLength = s.length();
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}
}
