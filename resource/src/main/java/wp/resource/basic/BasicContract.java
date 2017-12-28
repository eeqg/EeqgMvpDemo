package wp.resource.basic;

import android.content.Context;

import com.trello.rxlifecycle.LifecycleTransformer;

public interface BasicContract {
	
	interface View<P extends BasicContract.Presenter> {
		
		<L> LifecycleTransformer<L> bindUntilEvent(ViewEvent event);
		
		<L> LifecycleTransformer<L> bindToLifecycle();
		
		Context getContext();
		
		boolean needLogin();
		
		void tokenTimeOut();
		
		void tokenNotFound();
		
		void setBasicPresenter(P presenter);
		
		void showLoading();
		
		void hideLoading();
		
		void promptMessage(int resId);
		
		void promptMessage(String message);
	}
	
	interface Presenter {
		
	}
}
