package cma.cimiss2.dpc.quickqc.bean.enums;

/**
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
 * <b>Description:</b><br>
 * @author When6passBye
 * @version 1.0
 * @Note
0	数据正确	通过质量控制，未发现数据异常；或数据虽异常，但最终确认数据正确
1	数据可疑	通过质量控制，发现数据异常，且未明确数据正确还是错误
2	数据错误	通过质量控制，确认数据错误
3	数据为订正值	原数据明显偏离真值，但在一定范围内可参照使用。在原数据基础上通过偏差订正等方式重新获取的更正数据
4	数据为修改值	原数据因错误或缺测而完全不可用，通过与原数据完全无关的替代方式重新获取的更正数据
5	预留
6	预留
7	无观测任务	按规定，台站无相应要素数据观测任务
8 	数据缺测	该项数据应观测，但因各种原因数据缺测
9	数据未作质量控制	该数据未进行质量控制
 * <b>ProjectName:</b> cimiss2-quick-qc
 * <br><b>PackageName:</b> cma.cimiss2.dpc.quickqc.bean
 * <br><b>ClassName:  质控吗定义   </b> QualityCode
 * <br><b>Date:</b> 2019年8月16日 上午11:23:29
 */
public enum Quality {

    Z_QC_OK(0, "通过质量控制，未发现数据异常；或数据虽异常，但最终确认数据正确"),
    Z_QC_DOUBT(1, "通过质量控制，发现数据异常，且未明确数据正确还是错误"), // 偏高
    Z_QC_DOUBT_NEG(-1, "通过质量控制，发现数据异常，且未明确数据正确还是错误"), // 偏低
    Z_QC_ERROR(2, "通过质量控制，确认数据错误"), // 偏高
    Z_QC_ERROR_NEG(-2, "通过质量控制，确认数据错误"), // 偏低
    Z_QC_CORRECT(3, "原数据明显偏离真值，但在一定范围内可参照使用。在原数据基础上通过偏差订正等方式重新获取的更正数据"),
    Z_QC_MOD(4, "原数据因错误或缺测而完全不可用，通过与原数据完全无关的替代方式重新获取的更正数据"),
    Z_QC_NO_TASK(7, "按规定，台站无相应要素数据观测任务"),
    Z_QC_LACK(8, "该项数据应观测，但因各种原因数据缺测"),
    Z_QC_NO_QC(9, "未作数据质量控制"),
    Z_QC_NOT_NECESSARY(10, "要素不参与此项质量控制"),
    Z_QC_INVALID(11, "质控过程发生错误");

    private int code;
    private String description;

    Quality(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static Quality getQualityByCode(int code) {
        for (Quality q : Quality.values()) {
            if (q.getCode() == code) {
                return q;
            }
        }
        throw new IllegalArgumentException();
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
