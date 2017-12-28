package wp.resource.basic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;

import javax.annotation.Nonnull;

import rx.Observable;
import rx.subjects.BehaviorSubject;
import wp.resource.network.StatusInfo;

import static android.app.Activity.RESULT_OK;

public abstract class BasicFragment<P extends BasicContract.Presenter> extends Fragment
		implements BasicContract.View<P>, LifecycleProvider<ViewEvent> {
	private final BehaviorSubject<ViewEvent> lifecycleSubject = BehaviorSubject.create();
	
	protected P basicPresenter;
	
	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		lifecycleSubject.onNext(ViewEvent.ATTACH);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		lifecycleSubject.onNext(ViewEvent.CREATE);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		lifecycleSubject.onNext(ViewEvent.CREATE_VIEW);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		lifecycleSubject.onNext(ViewEvent.START);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		lifecycleSubject.onNext(ViewEvent.RESUME);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		lifecycleSubject.onNext(ViewEvent.PAUSE);
	}
	
	@Override
	public void onStop() {
		super.onStop();
		lifecycleSubject.onNext(ViewEvent.STOP);
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		lifecycleSubject.onNext(ViewEvent.DESTROY_VIEW);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		lifecycleSubject.onNext(ViewEvent.DESTROY);
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		lifecycleSubject.onNext(ViewEvent.DETACH);
	}
	
	@Override
	public Context getContext() {
		return super.getContext();
	}
	
	@Override
	public boolean needLogin() {
		// if (LoginBean.read() != null) {
		// 	return false;
		// }
		//
		// AbstractBasicApp.INSTANCE.requestLogin(getContext(), LOGIN);
		
		return true;
	}
	
	@Override
	public void tokenTimeOut() {
		AbstractBasicApp.INSTANCE.requestLogin(getContext(), StatusInfo.STATUS_TOKEN_TIMEOUT);
	}
	
	@Override
	public void tokenNotFound() {
		AbstractBasicApp.INSTANCE.requestLogin(getContext(), StatusInfo.STATUS_TOKEN_NOT_FOUND);
	}
	
	@Override
	public void setBasicPresenter(P basicPresenter) {
		this.basicPresenter = basicPresenter;
	}
	
	@Override
	public void showLoading() {
		Activity activity = getActivity();
		if (activity instanceof BasicContract.View) {
			((BasicContract.View) activity).showLoading();
		}
	}
	
	@Override
	public void hideLoading() {
		Activity activity = getActivity();
		if (activity instanceof BasicContract.View) {
			((BasicContract.View) activity).hideLoading();
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
	public Observable<ViewEvent> lifecycle() {
		return lifecycleSubject.asObservable();
	}
	
	@Nonnull
	@Override
	public <T> LifecycleTransformer<T> bindUntilEvent(@Nonnull ViewEvent event) {
		return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
	}
	
	@Nonnull
	@Override
	public <T> LifecycleTransformer<T> bindToLifecycle() {
		return RxLifecycleMVP.bindView(lifecycleSubject);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == StatusInfo.STATUS_TOKEN_TIMEOUT) {
			Activity activity = getActivity();
			if (activity == null) {
				return;
			}
			if (resultCode == RESULT_OK) {
				activity.recreate();
			} else {
				AbstractBasicApp.INSTANCE.logout(activity);
			}
			return;
		}
		if (requestCode == StatusInfo.STATUS_TOKEN_NOT_FOUND) {
			Activity activity = getActivity();
			if (activity == null) {
				return;
			}
			if (resultCode == RESULT_OK) {
				activity.recreate();
			} else {
				activity.finish();
			}
			return;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
