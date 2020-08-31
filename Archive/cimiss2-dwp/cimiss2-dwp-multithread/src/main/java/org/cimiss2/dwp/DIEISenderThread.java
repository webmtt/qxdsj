package org.cimiss2.dwp;


import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.indb.core.tools.RestfulSendData;

import java.util.List;

/**
 * @Description:
 * @Aouthor: xzh
 * @create: 2018-01-11 17:52
 */
public class DIEISenderThread implements Runnable {
    List<RestfulInfo> diei;
    int judge;

    public DIEISenderThread(List<RestfulInfo> diei, int judge) {
        this.diei = diei;
        this.judge = judge;
    }

    @Override
    public void run() {
        int i = RestfulSendData.getInstance("config/RestfulInfo.js").SendData(diei, judge);
        if (judge == 0)
            System.err.println("======di数量=========" + diei.size());
        if (judge == 1)
            System.err.println("======ei数量=========" + diei.size());
    }
}
