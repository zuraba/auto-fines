package ge.zuraba.android.autofines.activities;

import ge.zuraba.android.autofines.R;
import ge.zuraba.android.autofines.adapters.VideFinesAdapter;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.MenuItem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class FinesActivity extends SherlockListActivity implements
		OnItemClickListener {

	private ListView finesList;

	private Elements rowElements;

	private VideFinesAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.Theme_Sherlock);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finelist);

		ActionBar actionBar = getSupportActionBar();
		if (actionBar == null) {
			return;
		}

		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setIcon(R.drawable.icon);
		actionBar.show();

		// header = (TextView) findViewById(R.id.header);
		finesList = (ListView) findViewById(android.R.id.list);

		getFines();
	}

	public void getFines() {
		Bundle extras = this.getIntent().getExtras();

		Document doc = Jsoup.parse(extras.getString("httpResponse"));
		// String fineHeader = doc.select(".head").get(1).text();
		rowElements = doc.select(".row");

		// header.setText(fineHeader);
		ArrayList<String> rowStrings = new ArrayList<String>();
		ArrayList<String> statuses = new ArrayList<String>();

		for (Element el : rowElements) {
			rowStrings.add(el.select("span").get(1).text().replace(" ", " / "));
			statuses.add(el.select("span").get(6).text());
		}

		adapter = new VideFinesAdapter(this, R.layout.finelist, rowStrings,
				statuses);
		finesList.setAdapter(adapter);
		finesList.setOnItemClickListener(this);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent intent = new Intent(this, VideoPenaltiesActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		Intent detailView = new Intent(view.getContext(),
				FineDetailActivity.class);
		detailView
				.putExtra("rowElement", rowElements.get(position).outerHtml());
		startActivity(detailView);
	}
}
