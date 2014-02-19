package com.example.appData;

import java.util.ArrayList;

import com.example.bmsaltamedia.ListView_Reminder;
import com.example.bmsaltamedia.R;
import com.google.GCM.ServerUtilities;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class reminderArrayAdapter extends ArrayAdapter<reminderData> {
	Activity context = null;
	ArrayList<reminderData> myArray = null;
	int layoutId;

	public reminderArrayAdapter(Activity context, int layoutId,
			ArrayList<reminderData> arr) {
		super(context, layoutId, arr);
		this.context = context;
		this.layoutId = layoutId;
		this.myArray = arr;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		convertView = inflater.inflate(layoutId, null);

		if (myArray.size() > 0 && position >= 0) {

			final TextView txtdisplay = (TextView) convertView
					.findViewById(R.id.item_txtTitle);

			final reminderData emp = myArray.get(position);
			txtdisplay.setText(emp.title);

			final CheckBox item_btn = (CheckBox) convertView
					.findViewById(R.id.item_toggleBtn);

			item_btn.setTag(emp);
			if (!emp.isComplete()) {
				item_btn.setChecked(false);

			} else if (!emp.isComplete()) {
				item_btn.setChecked(true);
				item_btn.setEnabled(false);
			}
			if (emp.canComplete == 1) {
				item_btn.setEnabled(true);
			} else {
				item_btn.setEnabled(false);
			}
			item_btn.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					CheckBox cb = (CheckBox) v;
					reminderData remind = (reminderData) cb.getTag();
					int index = myArray.indexOf(remind);
					Log.d("remiderData",
							Integer.toString(index) + " " + remind.toString());

					if (cb.isChecked()) {
						boolean flag = ServerUtilities.complete(remind,
								ListView_Reminder.user.user_id);
						if (flag) {
							cb.setEnabled(false);
							myArray.remove(remind);
							notifyDataSetChanged();
						}else{
							cb.setChecked(false);
							Toast.makeText(v.getContext(), v.getContext().getString(R.string.Err_complete), Toast.LENGTH_LONG).show();
							Log.i("complete", "khong the complete");
						}
					} else {
						remind.setStatus(0);
					}				

				}
			});
			final TextView txt_content = (TextView) convertView
					.findViewById(R.id.item_txtContent);
			txt_content.setText(emp.content);
			final TextView txt_date = (TextView) convertView
					.findViewById(R.id.item_txt_Date);
			txt_date.setText(emp.getDate("dd/MM/yyyy"));

		}
		// Vì View là Object là dạng tham chiếu đối tượng, nên
		// mọi sự thay đổi của các object bên trong convertView
		// thì nó cũng biết sự thay đổi đó
		return convertView;// trả về View này, tức là trả luôn
							// về các thông số mới mà ta vừa thay đổi
	}

}
