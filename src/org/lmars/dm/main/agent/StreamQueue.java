package org.lmars.dm.main.agent;

import io.vertx.core.Handler;

//定制的消息缓存List
public class StreamQueue {
	
	private static final int increaseN = 1024;
	
	private int curElementNum = 0;
	private StreamMessage []elementDatas = null;
	
	public StreamQueue(int reserveSize){
		elementDatas = new StreamMessage[reserveSize];
	}
	
	public synchronized void add(StreamMessage msg){
		if((curElementNum+1) >= elementDatas.length){
			//超出了当前容量，需要进行扩容
			StreamMessage []newElementDatas = new StreamMessage[elementDatas.length + increaseN];
			System.arraycopy(elementDatas, 0, newElementDatas, 0, elementDatas.length);
			elementDatas = newElementDatas;
		}
		elementDatas[curElementNum] = msg;		
		curElementNum++;
	}
	
	public synchronized int size(){
		return curElementNum;
	}
	
	public synchronized void removeAll(){
		curElementNum = 0;
	}
	
	public synchronized int computeSampleN(int sampleN){
		if(sampleN >= curElementNum){
			return curElementNum;
		}else{
			int trueSampleN = 0;
			double resampleRatio = ((double)curElementNum)/((double)sampleN);
			double samplePos = 0;
			for(int i=0;i<sampleN;i++){
				int sindx = (int)Math.floor(samplePos + 0.5);
				if(sindx >=0 && sindx < curElementNum){
					trueSampleN++;
				}
				samplePos+= resampleRatio;
			}
			return trueSampleN;
		}
	}
	
	public synchronized void sample(int sampleN, Handler<StreamMessage> msgHandler){

		if (sampleN >= curElementNum) {
			for (int i = 0; i < curElementNum; i++) {
				StreamMessage msg = elementDatas[i];
				msgHandler.handle(msg);
			}
		} else {
			double resampleRatio = ((double) curElementNum) / ((double) sampleN);
			double samplePos = 0;
			for (int i = 0; i < sampleN; i++) {
				int sindx = (int) Math.floor(samplePos + 0.5);
				if (sindx >= 0 && sindx < curElementNum) {
					msgHandler.handle(elementDatas[sindx]);
				}
				samplePos += resampleRatio;
			}
		}
	}
	
}
