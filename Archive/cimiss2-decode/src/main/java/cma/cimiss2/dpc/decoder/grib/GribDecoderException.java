package cma.cimiss2.dpc.decoder.grib;

import java.io.PrintWriter;
import java.io.StringWriter;

import cma.cimiss2.dpc.decoder.grib.Configuration.ErrorCode;

/**
 * 数值预报解码过程中产生的异常
 * @author from dataX of alibaba
 *
 */
public class GribDecoderException extends RuntimeException {

	private static final long	serialVersionUID	= 1L;

	private ErrorCode			errorCode;

	public GribDecoderException(ErrorCode errorCode, String errorMessage) {
		super(errorCode.toString() + " - " + errorMessage);
		this.errorCode = errorCode;
	}

	private GribDecoderException(ErrorCode errorCode, String errorMessage, Throwable cause) {
		super(errorCode.toString() + " - " + getMessage(errorMessage) + " - " + getMessage(cause), cause);

		this.errorCode = errorCode;
	}

	public static GribDecoderException asDataXException(ErrorCode errorCode, String message) {
		return new GribDecoderException(errorCode, message);
	}

	public static GribDecoderException asDataXException(ErrorCode errorCode, String message, Throwable cause) {
		if (cause instanceof GribDecoderException) {
			return (GribDecoderException) cause;
		}
		return new GribDecoderException(errorCode, message, cause);
	}

	public static GribDecoderException asDataXException(ErrorCode errorCode, Throwable cause) {
		if (cause instanceof GribDecoderException) {
			return (GribDecoderException) cause;
		}
		return new GribDecoderException(errorCode, getMessage(cause), cause);
	}

	public ErrorCode getErrorCode() {
		return this.errorCode;
	}

	private static String getMessage(Object obj) {
		if (obj == null) {
			return "";
		}

		if (obj instanceof Throwable) {
			StringWriter str = new StringWriter();
			PrintWriter pw = new PrintWriter(str);
			((Throwable) obj).printStackTrace(pw);
			return str.toString();
		} else {
			return obj.toString();
		}
	}
}