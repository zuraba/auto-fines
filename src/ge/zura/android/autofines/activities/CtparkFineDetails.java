package ge.zura.android.autofines.activities;

import ge.zura.android.autofines.R;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class CtparkFineDetails extends Activity {
	
	private String url = "http://ct-park.ge/ticket/";
//	String fullUrl = "http://chemikucha.ge/reports/2345";
	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.Theme_Sherlock);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);
		
		savedInstanceState = this.getIntent().getExtras();
		String fullUrl = url + savedInstanceState.getString("fines");
				
		webView = (WebView)findViewById(R.id.webView1);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.loadUrl(fullUrl);
	}
}
