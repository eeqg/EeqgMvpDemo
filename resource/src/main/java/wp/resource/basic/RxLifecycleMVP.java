package wp.resource.basic;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.OutsideLifecycleException;

import rx.Observable;
import rx.functions.Func1;

import static com.trello.rxlifecycle.RxLifecycle.bind;

public class RxLifecycleMVP {
	
	private RxLifecycleMVP() {
		throw new AssertionError("No instances");
	}
	
	@NonNull
	@CheckResult
	public static <T> LifecycleTransformer<T> bindView(@NonNull final Observable<ViewEvent> lifecycle) {
		return bind(lifecycle, VIEW_LIFECYCLE);
	}
	
	private static final Func1<ViewEvent, ViewEvent> VIEW_LIFECYCLE =
			new Func1<ViewEvent, ViewEvent>() {
				@Override
				public ViewEvent call(ViewEvent lastEvent) {
					switch (lastEvent) {
						case ATTACH:
							return ViewEvent.DETACH;
						case CREATE:
							return ViewEvent.DESTROY;
						case CREATE_VIEW:
							return ViewEvent.DESTROY_VIEW;
						case START:
							return ViewEvent.STOP;
						case RESUME:
							return ViewEvent.PAUSE;
						case PAUSE:
							return ViewEvent.STOP;
						case STOP:
							return ViewEvent.DESTROY_VIEW;
						case DESTROY_VIEW:
							return ViewEvent.DESTROY;
						case DESTROY:
							return ViewEvent.DETACH;
						case DETACH:
							throw new OutsideLifecycleException("Cannot bind to Fragment lifecycle when outside of it.");
						default:
							throw new UnsupportedOperationException("Binding to " + lastEvent + " not yet implemented");
					}
				}
			};
}
