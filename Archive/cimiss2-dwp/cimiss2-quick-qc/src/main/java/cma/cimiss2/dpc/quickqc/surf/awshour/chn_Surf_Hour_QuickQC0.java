package cma.cimiss2.dpc.quickqc.surf.awshour;

import java.time.LocalDateTime;

/**
* 按质控的逻辑，生成质控码
**/
public class chn_Surf_Hour_QuickQC0 {

    public static void main(String[] args) {

        LocalDateTime date = LocalDateTime.parse("2019-08-22T23:15:30");
        System.out.println(date.plusHours(8));
        System.out.println(date);
    }



}
