package ge.zuraba.android.autofines.adapters;

import ge.zuraba.android.autofines.R;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FinesAdapter extends ArrayAdapter<String> {

	private TextView columnName;

	private TextView columnValue;

	private LayoutInflater inflater;

	private List<String> finesList;

	private String[] colNames;

	public FinesAdapter(Context context, int listView, List<String> fines,
			String[] list) {
		super(context, listView, fines);
		this.finesList = fines;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.colNames = list;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if (view == null) {
			view = inflater.inflate(R.layout.list_items, null);
			columnName = (TextView) view.findViewById(R.id.line1);
			columnValue = (TextView) view.findViewById(R.id.line2);
		}

		columnName.setText(colNames[position]);
		columnValue.setText(finesList.get(position));

		return view;
	}
}
