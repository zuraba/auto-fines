package ge.zuraba.android.autofines.adapters;

import ge.zuraba.android.autofines.R;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class VideFinesAdapter extends ArrayAdapter<String> {

	private TextView text;

	private ImageView status;

	private List<String> finesList;

	private List<String> statusList;

	private LayoutInflater inflater;

	private Context c;

	public VideFinesAdapter(Context context, int textViewResourceId,
			List<String> objects, List<String> statuses) {
		super(context, textViewResourceId, objects);
		this.finesList = objects;
		this.statusList = statuses;
		this.c = context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if (view == null) {
			view = inflater.inflate(R.layout.vide_fines, null);
			text = (TextView) view.findViewById(R.id.textView1);
			status = (ImageView) view.findViewById(R.id.imageView1);
		}
		text.setText(finesList.get(position));
		if (statusList.get(position)
				.contains(c.getString(R.string.timely_paid))) {
			status.setImageResource(R.drawable.status_green);
		} else {
			status.setImageResource(R.drawable.status_red);
		}

		return view;
	}
}
