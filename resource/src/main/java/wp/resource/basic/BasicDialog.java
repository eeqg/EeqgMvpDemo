package wp.resource.basic;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.StyleRes;

import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;

import javax.annotation.Nonnull;

import rx.Observable;
import rx.subjects.BehaviorSubject;

public abstract class BasicDialog<P extends BasicContract.Presenter> extends Dialog
		implements BasicContract.View<P>, LifecycleProvider<ViewEvent> {
	private final BehaviorSubject<ViewEvent> lifecycleSubject = BehaviorSubject.create();
	protected P basicPresenter;
	
	public BasicDialog(Context context) {
		super(context);
	}
	
	public BasicDialog(Context context, @StyleRes int themeResId) {
		super(context, themeResId);
	}
	
	public BasicDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		lifecycleSubject.onNext(ViewEvent.START);
	}
	
	@Override
	protected void onStop() {
		lifecycleSubject.onNext(ViewEvent.STOP);
		super.onStop();
	}
	
	@Override
	public boolean needLogin() {
		return false;
	}
	
	@Override
	public void tokenTimeOut() {
	}
	
	@Override
	public void tokenNotFound() {
	}
	
	@Override
	public void setBasicPresenter(P basicPresenter) {
		this.basicPresenter = basicPresenter;
	}
	
	@Override
	public void showLoading() {
	}
	
	@Override
	public void hideLoading() {
	}
	
	@Override
	public void promptMessage(int resId) {
		promptMessage(getContext().getString(resId));
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
}
