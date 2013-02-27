package ge.zura.android.autofines.activities;

import ge.zura.android.autofines.adapters.FinesAdapter;
import ge.zura.android.autofines.R;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.MenuItem;

import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ListView;

public class FineDetailActivity extends SherlockListActivity {
	ListView detailList;
	private FinesAdapter adapter;
	LayoutInflater inflater;
	private String status;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.Theme_Sherlock);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finedetail);

		detailList = (ListView) findViewById(R.id.listView1);
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		final String[] colNames = new String[] { "ქვითარი / ნომერი:",
				"გვარი სახელი:", "თარიღი:", "მუხლი:", "თანხა:", "მდგომარეობა:" };
		savedInstanceState = this.getIntent().getExtras();
		Document doc = Jsoup.parse(savedInstanceState.getString("rowElement"));
		Elements elements = doc.select(".col");

		ArrayList<String> colStrings = new ArrayList<String>();

		for (Element el : elements) {
			if (el.text().equals("")) {
				continue;
			}
			colStrings.add(el.text());
		}

		status = colStrings.get(5);
		adapter = new FinesAdapter(this, R.layout.finedetail, colStrings,
				colNames);
		setListAdapter(adapter);

		ActionBar actionBar = getSupportActionBar();
		if (actionBar == null) {
			return;
		}

		if (status.contains("გადახდილია")) {
			actionBar.setIcon(R.drawable.status_green);
		} else {
			actionBar.setIcon(R.drawable.status_red);
		}
		actionBar.show();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent intent = new Intent(this, FinesActivity.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}