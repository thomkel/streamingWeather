package edu.uchicago.mpcs53013.streamingWeather;

import java.util.Date;

public class FlightAndWeatherSummary {
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getDest() {
		return dest;
	}
	public void setDest(String dest) {
		this.dest = dest;
	}
	public Date getWhen() {
		return when;
	}
	public void setWhen(Date when) {
		this.when = when;
	}
	public int getDelay() {
		return delay;
	}
	public void setDelay(int delay) {
		this.delay = delay;
	}
	public boolean isFog() {
		return fog;
	}
	public void setFog(boolean fog) {
		this.fog = fog;
	}
	public boolean isRain() {
		return rain;
	}
	public void setRain(boolean rain) {
		this.rain = rain;
	}
	public boolean isHail() {
		return hail;
	}
	public void setHail(boolean hail) {
		this.hail = hail;
	}
	public boolean isSnow() {
		return snow;
	}
	public void setSnow(boolean snow) {
		this.snow = snow;
	}
	public boolean isThunder() {
		return thunder;
	}
	public void setThunder(boolean thunder) {
		this.thunder = thunder;
	}
	public boolean isTornado() {
		return tornado;
	}
	public void setTornado(boolean tornado) {
		this.tornado = tornado;
	}
	String origin;
	String dest;
	Date when;
	int delay;
	boolean fog;
	boolean rain;
	boolean hail;
	boolean snow;
	boolean thunder;
	boolean tornado;
}
