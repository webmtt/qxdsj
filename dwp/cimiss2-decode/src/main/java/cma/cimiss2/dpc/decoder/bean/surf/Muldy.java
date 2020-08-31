package cma.cimiss2.dpc.decoder.bean.surf;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Muldy {

    private String dRecordId;//记录标识

    private Timestamp observationDataDate;

    private String stationNumber;

    private Double longitudeFv;

    private Double latitudeFv;

    private Double observationPlaceEvaluation;

    private Double temp;

    private Double pres;

    private Double aveWd2min;

    private Double aveWs2min;

    private Double weightAccRainCount;

    private Double relaHumi;

    private Double visibility;

    private String snowDepth;

    private Double land5cmTemp;

    private Double land10cmTemp;

    private String grI;

    private String t1;

    private String t4;

    private String t5;

    private String t6;

    private Double aveWd10min;

    private Double aveWs10min;

    private String aveWs2min15;

    private String aveWs10min15;

    private Double landIrTemp;
}
