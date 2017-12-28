package wp.resource.network;

public class StatusInfo {
	/** 成功 */
	public static final int STATUS_SUCCESS = 0;
	/** Token超时 */
	public static final int STATUS_TOKEN_TIMEOUT = 1004;
	/** Token未设置 */
	public static final int STATUS_TOKEN_NOT_FOUND = 1006;
	/** 自定义错误 */
	public static final int STATUS_CUSTOM_ERROR = 2001;
	
	/** 状态码 */
	public int statusCode;
	/** 状态消息 */
	public String statusMessage;
	
	/**
	 * 构造方法
	 */
	public StatusInfo() {
	}
	
	/**
	 * 构造方法
	 *
	 * @param statusCode 状态码
	 */
	public StatusInfo(int statusCode) {
		this.statusCode = statusCode;
	}
	
	/**
	 * 判断是否请求成功
	 *
	 * @return true请求成功
	 */
	public boolean isSuccessful() {
		return statusCode == STATUS_SUCCESS;
	}
	
	/**
	 * 判断是否特殊处理
	 *
	 * @return true特殊处理
	 */
	public boolean isOther() {
		return statusCode == STATUS_TOKEN_TIMEOUT
				|| statusCode == STATUS_TOKEN_NOT_FOUND;
	}
	
	/**
	 * 判断是否Token超时
	 *
	 * @return trueToken超时
	 */
	public boolean isTokenTimeOut() {
		return statusCode == STATUS_TOKEN_TIMEOUT;
	}
	
	/**
	 * 判断是否需要登录
	 *
	 * @return true需要登录
	 */
	public boolean isTokenNotFound() {
		return statusCode == STATUS_TOKEN_NOT_FOUND
				&& statusMessage.equals("参数： token 不能为空");
	}
}
