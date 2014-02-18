package com.example.appData;

import java.util.ArrayList;

import com.example.bmsaltamedia.R;

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
		// chỉ là test thôi, bạn có thể bỏ If đi
		if (myArray.size() > 0 && position >= 0) {
			// dòng lệnh lấy TextView ra để hiển thị Mã và tên lên
			final TextView txtdisplay = (TextView) convertView
					.findViewById(R.id.item_txtTitle);
			// lấy ra nhân viên thứ position
			final reminderData emp = myArray.get(position);
			txtdisplay.setText(emp.title);
			
			// lấy ImageView ra để thiết lập hình ảnh cho đúng
			//final CheckBox check_item = (CheckBox) convertView
				//	.findViewById(R.id.check_item);
			final ToggleButton item_btn=(ToggleButton) convertView.findViewById(R.id.item_toggleBtn);
			//check_item.setTag(emp);
			item_btn.setTag(emp);
			if (emp.getStatus() == 0) {
				item_btn.setChecked(false);
				//check_item.setChecked(false);
			} else if (emp.getStatus() == 1) {
				//check_item.setChecked(true);
				//check_item.setEnabled(false);
				item_btn.setChecked(true);
				item_btn.setEnabled(false);
			} else {
				item_btn.setEnabled(false);
				//check_item.setEnabled(false);
			}
			item_btn.setOnClickListener( new View.OnClickListener(){
				public void onClick(View v) {  
					ToggleButton cb = (ToggleButton) v ;
					 reminderData remind=(reminderData)cb.getTag();
					 int index=myArray.indexOf(remind);
					 Log.d("remiderData",Integer.toString(index)+" "+remind.toString());
					 if(cb.isChecked()){
						 remind.setStatus(1);
						 cb.setEnabled(false);
					 }else{
						 remind.setStatus(0);
					 }	
					 myArray.set(index, remind);
					 
					 		
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
