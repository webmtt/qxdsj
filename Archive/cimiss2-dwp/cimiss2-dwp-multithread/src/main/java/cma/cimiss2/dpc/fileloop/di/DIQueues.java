package cma.cimiss2.dpc.fileloop.di;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import cma.cimiss2.dpc.fileloop.bean.FileDi;

public class DIQueues {
	
	public static BlockingQueue<FileDi> queue = new LinkedBlockingQueue<FileDi>();;

}
