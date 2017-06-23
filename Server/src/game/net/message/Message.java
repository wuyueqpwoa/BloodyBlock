package game.net.message;

import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

/**
 * 消息
 * Created by wuy on 2017/6/22.
 */
public class Message {

	// 来源用户通道ID
	private String sourceUserChannelId;
	// 来源服务器ID
	private String sourceServerId;
	// 目标服务器ID，如果是客户端的消息，一般由登录服务器或网关服务器强写本字段
	private String destinationServerId;
	// 业务名(方法所在类名)
	private String businessName;
	// 调用方法名
	private String invokeMethodName;
	// 参数(MessagePack打包)
	private byte[] parameterBytes;
	// 回调方法名
	private String callbackMethodName;

	public String getSourceUserChannelId() {
		return sourceUserChannelId;
	}

	public void setSourceUserChannelId(String sourceUserChannelId) {
		this.sourceUserChannelId = sourceUserChannelId;
	}

	public String getSourceServerId() {
		return sourceServerId;
	}

	public void setSourceServerId(String sourceServerId) {
		this.sourceServerId = sourceServerId;
	}

	public String getDestinationServerId() {
		return destinationServerId;
	}

	public void setDestinationServerId(String destinationServerId) {
		this.destinationServerId = destinationServerId;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getInvokeMethodName() {
		return invokeMethodName;
	}

	public void setInvokeMethodName(String invokeMethodName) {
		this.invokeMethodName = invokeMethodName;
	}

	public byte[] getParameterBytes() {
		return parameterBytes;
	}

	public void setParameterBytes(byte[] parameterBytes) {
		this.parameterBytes = parameterBytes;
	}

	public String getCallbackMethodName() {
		return callbackMethodName;
	}

	public void setCallbackMethodName(String callbackMethodName) {
		this.callbackMethodName = callbackMethodName;
	}

	@Override
	public String toString() {
		return "Message{" +
				"sourceUserChannelId='" + sourceUserChannelId + '\'' +
				", sourceServerId='" + sourceServerId + '\'' +
				", destinationServerId='" + destinationServerId + '\'' +
				", businessName='" + businessName + '\'' +
				", invokeMethodName='" + invokeMethodName + '\'' +
				", parameter=" + (parameterBytes == null ? null : ByteUtils.toHexString(parameterBytes)) +
				", callbackMethodName='" + callbackMethodName + '\'' +
				'}';
	}
}
