package com.mobile.liujiucheng.sixninecheng.main;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 我的常用地址
 * @author pc
 *
 */
public class MyPositionActivity extends BaseActivity implements OnClickListener {

	private ImageView mImageView;
	
	private TextView tv_cancal;
	private TextView tv_confirm;
	private TextView name;
	private EditText et_modify;
	private AlertDialog dialog;
	
	private Context context;
	private View mView;
	private TextView mTextView;//修改地址
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_setting_address);
		initView();
	}
	private void initView() {
		context = this;
		mImageView = (ImageView) findViewById(R.id.iv_exit);
		mImageView.setOnClickListener(this);
		mView = View.inflate(context, R.layout.layout_dialog_modify, null);
		mImageView = (ImageView) findViewById(R.id.iv_exit);
		mTextView = (TextView) findViewById(R.id.tv_modification);
		name = (TextView) findViewById(R.id.tv_name);
		if(!TextUtils.isEmpty(SPUtils.getPosition(context))){
			name.setText(SPUtils.getPosition(context));
		}
		mTextView.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.tv_confirm:
			if(!TextUtils.isEmpty(et_modify.getText().toString().trim())){
				name.setText(et_modify.getText().toString().trim());
				SPUtils.dataPosition(et_modify.getText().toString().trim());
				closeDialog();
			}else{
				Toast.makeText(context, this.getResources().getText(R.string.surrounding_content), Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.tv_cancal:
			closeDialog();
		case R.id.iv_exit:
			finish();
			break;
		case R.id.tv_modification:
			//修改
			AlertDialog.Builder builder = new Builder(this);
			if (dialog == null) {
				dialog = builder.create();
			}
			dialog.setView(mView, 0, 0, 0, 0);
			tv_cancal = (TextView) mView.findViewById(R.id.tv_cancal);
			tv_confirm = (TextView) mView.findViewById(R.id.tv_confirm);
			et_modify = (EditText) mView.findViewById(R.id.et_modify);
			
			tv_cancal.setOnClickListener(this);
			tv_confirm.setOnClickListener(this);
			dialog.show();
			WindowManager.LayoutParams params = dialog.getWindow()
					.getAttributes();
			params.width = LayoutParams.MATCH_PARENT;
			params.height = LayoutParams.WRAP_CONTENT;
			dialog.getWindow().setAttributes(params);
			break;
		}
	}
	private void closeDialog() {
		if(dialog != null){
			dialog.dismiss();
		}
	}
}
