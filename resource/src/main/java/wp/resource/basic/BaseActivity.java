package wp.resource.basic;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;

import javax.annotation.Nonnull;

import rx.Observable;
import rx.subjects.BehaviorSubject;
import wp.resource.network.StatusInfo;

/**
 * Created by wp on 2017/12/7 0007.
 */

public class BaseActivity<P extends BasicContract.Presenter> extends AppCompatActivity
		implements BasicContract.View<P>, LifecycleProvider<ViewEvent> {
	/** 登录 */
	public static final int LOGIN = 2000;
	
	private final BehaviorSubject<ViewEvent> lifecycleSubject = BehaviorSubject.create();
	protected P basicPresenter;
	
	protected Dialog loadingDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//this.loadingDialog = new LoadingDialog(this);
		lifecycleSubject.onNext(ViewEvent.CREATE);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		lifecycleSubject.onNext(ViewEvent.START);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		lifecycleSubject.onNext(ViewEvent.RESUME);
	}
	
	@Override
	protected void onPause() {
		lifecycleSubject.onNext(ViewEvent.PAUSE);
		super.onPause();
	}
	
	@Override
	protected void onStop() {
		lifecycleSubject.onNext(ViewEvent.STOP);
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		lifecycleSubject.onNext(ViewEvent.DESTROY);
		super.onDestroy();
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
	
	@Override
	public Context getContext() {
		return this;
	}
	
	@Override
	public boolean needLogin() {
		// if (LoginBean.read() != null) {
		// 	return false;
		// }
		
		// AbstractBasicApp.INSTANCE.requestLogin(this, LOGIN);
		
		return true;
	}
	
	@Override
	public void tokenTimeOut() {
		AbstractBasicApp.INSTANCE.requestLogin(this, StatusInfo.STATUS_TOKEN_TIMEOUT);
	}
	
	@Override
	public void tokenNotFound() {
		AbstractBasicApp.INSTANCE.requestLogin(this, StatusInfo.STATUS_TOKEN_NOT_FOUND);
	}
	
	@Override
	public void setBasicPresenter(P basicPresenter) {
		this.basicPresenter = basicPresenter;
	}
	
	@Override
	public void showLoading() {
		if (!this.loadingDialog.isShowing()) {
			this.loadingDialog.show();
		}
	}
	
	@Override
	public void hideLoading() {
		if (this.loadingDialog.isShowing()) {
			this.loadingDialog.dismiss();
		}
	}
	
	@Override
	public void promptMessage(int resId) {
		promptMessage(getString(resId));
	}
	
	@Override
	public void promptMessage(String message) {
		AbstractBasicApp.toast(message);
	}
	
	@Nonnull
	@Override
	public final Observable<ViewEvent> lifecycle() {
		return lifecycleSubject.asObservable();
	}
	
	@Nonnull
	@Override
	public final <L> LifecycleTransformer<L> bindUntilEvent(@Nonnull ViewEvent event) {
		return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
	}
	
	@Nonnull
	@Override
	public final <L> LifecycleTransformer<L> bindToLifecycle() {
		return RxLifecycleMVP.bindView(lifecycleSubject);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == StatusInfo.STATUS_TOKEN_TIMEOUT) {
			if (resultCode == RESULT_OK) {
				AbstractBasicApp.INSTANCE.cleanTop(this);
			} else {
				AbstractBasicApp.INSTANCE.logout(this);
			}
			return;
		}
		if (requestCode == StatusInfo.STATUS_TOKEN_NOT_FOUND) {
			if (resultCode == RESULT_OK) {
				recreate();
			} else {
				finish();
			}
			return;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
