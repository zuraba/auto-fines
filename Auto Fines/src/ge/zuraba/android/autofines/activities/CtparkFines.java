package ge.zuraba.android.autofines.activities;

import ge.zuraba.android.autofines.R;
import ge.zuraba.android.autofines.adapters.TicketsAdapter;
import ge.zuraba.android.autofines.adapters.VideFinesAdapter;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class CtparkFines extends SherlockActivity implements OnClickListener, OnItemClickListener {

	private ListView finesList;

	private Boolean ticketsChk;

	private ArrayList<String> fines;

	private TicketsAdapter adaperTickets;

	private TextView header, from, to;

	private VideFinesAdapter adapterFines;

	private WebView webView;

	private Button btnPay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.Theme_Sherlock);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ctparkfines);

		ActionBar actionBar = getSupportActionBar();
		if (actionBar == null) {
			return;
		}

		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setIcon(R.drawable.icon);

		webView= (WebView)findViewById(R.id.webView1);
		finesList = (ListView) findViewById(R.id.listView1);
		btnPay = (Button) findViewById(R.id.pay);
		header = (TextView) findViewById(R.id.textView1);
		from = (TextView) findViewById(R.id.textView2);
		to = (TextView) findViewById(R.id.textView3);

		savedInstanceState = getIntent().getExtras();
		ticketsChk = savedInstanceState.getBoolean("checkbox");

		ArrayList<String> tickets = null;
		ArrayList<String> statuses = null;

		if (ticketsChk) {
			tickets = getTickets(getDocument());
			header.setText(getResources().getString(R.string.tickets));
			adaperTickets = new TicketsAdapter(this,
					android.R.layout.simple_list_item_2, tickets);
			btnPay.setVisibility(View.VISIBLE);
			from.setVisibility(View.VISIBLE);
			to.setVisibility(View.VISIBLE);
		} else {
			fines = getFines(getDocument());
			statuses = getStatus(getDocument());
		}
		adapterFines = new VideFinesAdapter(this,
				android.R.layout.simple_list_item_1, fines, statuses);

		finesList.setAdapter(ticketsChk ? adaperTickets : adapterFines);
		finesList.setOnItemClickListener(this);
		btnPay.setOnClickListener(this);
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

	private Document getDocument() {
		Bundle extras = this.getIntent().getExtras();
		return Jsoup.parse(extras.getString("plateNumber"));
	}

	private ArrayList<String> getFines(Document doc) {
		Elements elementsFines = doc.select(".ticket-list").get(1)
				.select(".ticket-list").select("tr");
		ArrayList<String> fines = new ArrayList<String>();

		for (Element el : elementsFines) {
			fines.add(el.select("td").get(1).text());
		}
		return fines;
	}

	private ArrayList<String> getStatus(Document doc) {
		Elements elementStatus = doc.select(".ticket-list").get(1)
				.select(".ticket-list").select("tr");
		ArrayList<String> statuses = new ArrayList<String>();

		for (Element el : elementStatus) {
			statuses.add(el.select("td").get(3).text());
		}

		return statuses;
	}

	private ArrayList<String> getTickets(Document doc) {
		Elements elementsTickets = doc.select(".item-list").select("tbody")
				.select("tr");
		ArrayList<String> tickets = new ArrayList<String>();

		for (Element el : elementsTickets) {
			tickets.add(el.select("td").get(1).text() + "_"
					+ el.select("td").get(2).text());
		}
		return tickets;
	}

	public Date dueDate(Document doc) {
		String dateGeo = null;
		Elements elements = doc.select(".item-list").select("tbody")
				.select("tr");

		for (Element el : elements) {
			dateGeo = el.select("td").get(2).text();
		}
		HashMap<Integer, String> months = new HashMap<Integer, String>();
		months.put(1, " იანვარი, ");
		months.put(2, " თებერვალი, ");
		months.put(3, " მარტი, ");
		months.put(4, " აპრილი, ");
		months.put(5, " მაისი, ");
		months.put(6, " ივნისი, ");
		months.put(7, " ივლისი, ");
		months.put(8, " აგვისტო, ");
		months.put(9, " სექტემბერი, ");
		months.put(10, " ოქტომბერი, ");
		months.put(11, " ნოემბერი, ");
		months.put(12, " დეკემბერი, ");

		for (Entry<Integer, String> entry : months.entrySet()) {
			if (dateGeo.contains(entry.getValue())) {
				dateGeo = dateGeo.replace(entry.getValue(), "/"
						+ entry.getKey().toString() + "/");
			}
		}

		SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
		Date newDate = null;
		try {
			newDate = dateformat.parse(dateGeo);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return newDate;
	}

	public void calendarEvent(Date beginDate, Date endDate) {
		Intent intent = new Intent(Intent.ACTION_EDIT);
		intent.setType("vnd.android.cursor.item/event");
		intent.putExtra("beginTime", beginDate.getTime());
		intent.putExtra("rrule", "FREQ=DAILY;COUNT=1");
		intent.putExtra("hasAlarm", 1);
		intent.putExtra("endTime", endDate.getTime() + 15 * 3600 * 1000);
		intent.putExtra("title", "განაახლე სი-თი პარკის პარკირების ბილეთი");
		startActivity(intent);
	}

	@Override
	public void onClick(View view) {
		Date endDate = dueDate(getDocument());
		Date startDate = new Date(endDate.getTime() - (57 * 3600 * 1000));
		calendarEvent(startDate, endDate);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent details = new Intent(view.getContext(), CtparkFineDetails.class);
		details.putExtra("fines", fines.get(position));
		startActivity(details);
	}
}
