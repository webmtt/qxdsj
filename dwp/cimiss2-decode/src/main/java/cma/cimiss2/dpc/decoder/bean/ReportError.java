package cma.cimiss2.dpc.decoder.bean;

// TODO: Auto-generated Javadoc
/**
 * The Class ReportError.
 *
 * @Description: error of Test message 一条报文的解码错误信息
 * @Aouthor: xzh
 * @create: 2017-11-20 18:19
 */
public class ReportError {
    
    /** 错误信息. */
    String message;
    
    /** 错误位置，行. */
    int positionx;
    
    /** 错误位置，列. */
    int positiony;
    
    /** 错误片段摘录. */
    String segment;

    /**
     * Gets the message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message.
     *
     * @param message the new message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets the positionx.
     *
     * @return the positionx
     */
    public int getPositionx() {
        return positionx;
    }

    /**
     * Sets the positionx.
     *
     * @param positionx the new positionx
     */
    public void setPositionx(int positionx) {
        this.positionx = positionx;
    }

    /**
     * Gets the positiony.
     *
     * @return the positiony
     */
    public int getPositiony() {
        return positiony;
    }

    /**
     * Sets the positiony.
     *
     * @param positiony the new positiony
     */
    public void setPositiony(int positiony) {
        this.positiony = positiony;
    }

    /**
     * Gets the segment.
     *
     * @return the segment
     */
    public String getSegment() {
        return segment;
    }

    /**
     * Sets the segment.
     *
     * @param segment the new segment
     */
    public void setSegment(String segment) {
        this.segment = segment;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return message + " at (row " + positionx + ",column " + positiony + "), segment: <pre>" + segment
                + "</pre>";
    }
}
