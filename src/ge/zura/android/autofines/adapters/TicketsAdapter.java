package ge.zura.android.autofines.adapters;

import ge.zura.android.autofines.R;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TicketsAdapter extends ArrayAdapter<String> {

	private TextView startDate, endDate;

	private ArrayList<String> startDT, endDT;

	private LayoutInflater inflater;

	public TicketsAdapter(Context context, int resource, List<String> tickets) {
		super(context, resource, tickets);

		startDT = new ArrayList<String>();
		endDT = new ArrayList<String>();

		for (String s : tickets) {
			startDT.add(s.substring(0, s.indexOf("_")));
			endDT.add(s.substring(s.indexOf("_") + 1, s.length()));
		}

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// return super.getView(position, convertView, parent);
		if (view == null) {
			view = inflater.inflate(R.layout.list_tickets, null);
			startDate = (TextView) view.findViewById(R.id.textView1);
			endDate = (TextView) view.findViewById(R.id.textView2);
		}

		startDate.setText(startDT.get(position));
		endDate.setText(endDT.get(position));

		return view;
	}
}
