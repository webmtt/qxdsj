package cma.cimiss2.dpc.decoder.bean.xml;

public class XmlIndexObject {
    private int index;
    private int startCharAt;
    private int endCharAt;
    private boolean parseToNum;
    private int parseStartAt;
    private int parseEndAt;
    private boolean lengthControl;
    private int valueLengh;
    private String seatChar;
    private String seatAt;
    private boolean subString;



    public boolean isSubString() {
        return subString;
    }

    public void setSubString(boolean subString) {
        this.subString = subString;
    }

    public int getStartCharAt() {
        return startCharAt;
    }

    public void setStartCharAt(int startCharAt) {
        this.startCharAt = startCharAt;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getEndCharAt() {
        return endCharAt;
    }

    public int getValueLengh() {
        return valueLengh;
    }

    public String getSeatAt() {
        return seatAt;
    }

    public void setSeatAt(String seatAt) {
        this.seatAt = seatAt;
    }

    public String getSeatChar() {
        return seatChar;
    }

    public void setSeatChar(String seatChar) {
        this.seatChar = seatChar;
    }

    public void setValueLengh(int valueLengh) {
        this.valueLengh = valueLengh;
    }

    public void setEndCharAt(int endCharAt) {
        this.endCharAt = endCharAt;
    }


    public boolean isLengthControl() {
        return lengthControl;
    }

    public void setLengthControl(boolean lengthControl) {
        this.lengthControl = lengthControl;
    }

    public boolean isParseToNum() {
        return parseToNum;
    }

    public void setParseToNum(boolean parseToNum) {
        this.parseToNum = parseToNum;
    }

    public int getParseEndAt() {
        return parseEndAt;
    }

    public void setParseEndAt(int parseEndAt) {
        this.parseEndAt = parseEndAt;
    }


    public int getParseStartAt() {
        return parseStartAt;
    }

    public void setParseStartAt(int parseStartAt) {
        this.parseStartAt = parseStartAt;
    }



    public XmlIndexObject(){
        index = -1;
        subString = false;
        startCharAt = -1;
        endCharAt = -1;
        parseToNum = false;
        parseStartAt = -1;
        parseEndAt = -1;
        lengthControl = false;
        valueLengh = -1;
        seatChar = "";
        seatAt = "R";
    }

}
