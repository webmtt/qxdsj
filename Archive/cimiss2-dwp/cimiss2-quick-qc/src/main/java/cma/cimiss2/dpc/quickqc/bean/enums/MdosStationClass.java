package cma.cimiss2.dpc.quickqc.bean.enums;

public enum MdosStationClass {
    AWS_NATION(0x10, "国家站"),
    AWS_NATION_STD(0x11, "国家基准站"),
    AWS_NATION_BASE(0x12, "国家基本站"),
    AWS_NATION_NORMAL(0x13, "国家一般站"),
    AWS_NATION_NOBODY(0x15, "国家无人站"),
    AWS_REGION(0x20, "区域站"),
    AWS_REGION_PRF(0x21, "区域单雨量站");

    private int code;
    private String description;

    private MdosStationClass(int code, String description) {
        this.code = code;
        this.description = description;
    }
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
