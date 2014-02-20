package com.altamedia.appData;

import java.util.ArrayList;
import java.util.TreeSet;

import com.altamedia.GCM.ServerUtilities;
import com.altamedia.bmsaltamedia.GCMIntentService;
import com.altamedia.bmsaltamedia.ListView_Reminder;
import com.altamedia.bmsaltamedia.R;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class reminderArrayAdapter extends ArrayAdapter<reminderData> {
	Activity context = null;
	ArrayList<reminderData> myArray = null;
	int layoutId;
	private static final int TYPE_ITEM = 0;
	private static final int TYPE_SEPARATOR = 1;
	private static final int TYPE_MAX_COUNT = 2;
	private TreeSet<Integer> mSeparatorsSet = new TreeSet<Integer>();

	public reminderArrayAdapter(Activity context, int layoutId,
			ArrayList<reminderData> arr) {
		super(context, layoutId, arr);
		this.context = context;
		this.layoutId = layoutId;
		this.myArray = arr;
	}

	public void addSeparatorItem(final reminderData item) {
		myArray.add(item);
		// save separator position
		mSeparatorsSet.add(myArray.size() - 1);
		notifyDataSetChanged();
	}

	@Override
	public int getItemViewType(int position) {
		return mSeparatorsSet.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
	}

	@Override
	public int getViewTypeCount() {
		return TYPE_MAX_COUNT;
	}

	@Override
	public int getCount() {
		return myArray.size();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		int type = getItemViewType(position);
		final reminderData emp = myArray.get(position);
		switch (type) {
		case TYPE_ITEM:
			convertView = inflater.inflate(layoutId, null);
			if (myArray.size() > 0 && position >= 0) {

				final TextView txtdisplay = (TextView) convertView
						.findViewById(R.id.item_txtTitle);

				txtdisplay.setText(emp.title);

				final CheckBox item_btn = (CheckBox) convertView
						.findViewById(R.id.item_checkBox);

				item_btn.setTag(emp);
				if (!emp.isComplete()) {
					item_btn.setChecked(false);
				} else {
					item_btn.setChecked(true);
				}
				if (emp.canComplete == 1) {
					item_btn.setEnabled(true);
				} else {
					item_btn.setEnabled(false);
				}
				final TextView txt_content = (TextView) convertView
						.findViewById(R.id.item_txtContent);
				txt_content.setText(emp.content);
				final TextView txt_date = (TextView) convertView
						.findViewById(R.id.item_txt_Date);
				txt_date.setText(emp.getDate("dd/MM/yyyy"));
				
				item_btn.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						CheckBox cb = (CheckBox) v;
						reminderData remind = (reminderData) cb.getTag();
						int index = myArray.indexOf(remind);
						Log.d("remiderData", Integer.toString(index) + " "
								+ remind.toString());

						if (cb.isChecked()) {
							boolean flag = ServerUtilities.complete(remind,
									ListView_Reminder.user.user_id,
									ListView_Reminder.user.access_token);
							Log.d("flag", flag + "");
							if (flag) {
								GCMIntentService.reLoadLayout(context);
							} else if (ServerUtilities.Err != "") {
								cb.setChecked(false);
								Toast.makeText(
										v.getContext(),
										v.getContext().getString(
												R.string.Err_complete),
										Toast.LENGTH_LONG).show();
								Log.e("complete", ServerUtilities.Err);
							} else {
								Log.e("complete", "khong the complete");
							}
						} else {
							remind.setStatus(0);
						}

					}
				});			

			}
			break;
		case TYPE_SEPARATOR:
			convertView = inflater.inflate(R.layout.title_item, null);
			convertView.setBackgroundColor(emp.color);
			final TextView txt_title_ground = (TextView) convertView
					.findViewById(R.id.txt_title_group);
			txt_title_ground.setText(emp.title);

			// holder.textView =
			// (TextView)convertView.findViewById(R.id.textSeparator);
			break;

		}
		// Vì View là Object là dạng tham chiếu đối tượng, nên
		// m�?i sự thay đổi của các object bên trong convertView
		// thì nó cũng biết sự thay đổi đó
		return convertView;// trả v�? View này, tức là trả luôn
							// v�? các thông số mới mà ta vừa thay đổi
	}
}
