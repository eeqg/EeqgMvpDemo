package wp.resource.basic;

public abstract class BasicPresenter<V extends BasicContract.View> implements BasicContract.Presenter {
	protected V basicView;
	
	public BasicPresenter(V basicView) {
		this.basicView = basicView;
		// noinspection unchecked
		this.basicView.setBasicPresenter(this);
	}
	
	
}
