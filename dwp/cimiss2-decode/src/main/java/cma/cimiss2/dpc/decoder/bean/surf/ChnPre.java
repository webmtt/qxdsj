package cma.cimiss2.dpc.decoder.bean.surf;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ChnPre {

    private String dRECORDID;//记录标识

    private String dDATAID;//要素指示码

    private Timestamp dIYMDHM;

    private Timestamp dRYMDHM;

    private Timestamp dUPDATETIME;

    private Timestamp dDATETIME;

    private String vBBB;

    private String v01301;

    private Integer v01300;

    private Double v05001;

    private Double v06001;

    private Double v07001;

    private Integer v02001;

    private Integer v02301;

    private Integer v04001;

    private Integer v04002;

    private Integer v04003;

    private Integer v04004;

    private Integer v04005;

    private Double v13011;

    private Integer q13011;

    private Integer vACODE;



}
