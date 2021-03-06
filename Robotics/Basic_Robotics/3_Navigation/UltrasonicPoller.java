package ev3Navigation;

import lejos.robotics.SampleProvider;

//
//  Control of the wall follower is applied periodically by the 
//  UltrasonicPoller thread.  The while loop at the bottom executes
//  in a loop.  Assuming that the us.fetchSample, and cont.processUSData
//  methods operate in about 20mS, and that the thread sleeps for
//  50 mS at the end of each loop, then one cycle through the loop
//  is approximately 70 mS.  This corresponds to a sampling rate
//  of 1/70mS or about 14 Hz.
//


public class UltrasonicPoller extends Thread{
	private SampleProvider us;
	private float[] usData;
	private int distance;
	private Object lock;
	
	public UltrasonicPoller(SampleProvider us, float[] usData) {
		this.us = us;
		this.usData = usData;
		this.distance = 0;
		lock = new Object();
	}
	
	public void run() {

		while (true) {
			us.fetchSample(usData,0);							// acquire data
			synchronized(lock) {
				this.distance=(int)(usData[0]*100.0);			// extract from buffer, cast to int
			}
			try { Thread.sleep(50); } catch(Exception e){}		// Poor man's timed sampling
		}
	}
	
	public int getDistance() {
		int d;
		synchronized(lock) {
			d = this.distance;
		}
		return d;
	}

}
