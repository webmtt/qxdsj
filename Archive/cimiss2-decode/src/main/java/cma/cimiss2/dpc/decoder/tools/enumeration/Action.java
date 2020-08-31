package cma.cimiss2.dpc.decoder.tools.enumeration;

/**
 * Created by xzh on 2017/11/03.
 */
public enum Action {
    ACCEPT,  // 处理成功
    RETRY,   // 可以重试的错误
    REJECT,  // 无需重试的错误

}
