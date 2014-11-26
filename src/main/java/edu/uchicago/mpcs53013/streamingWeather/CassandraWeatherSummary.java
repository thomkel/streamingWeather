package edu.uchicago.mpcs53013.streamingWeather;

import java.io.Serializable;
import java.util.Date;

import com.google.common.base.Objects;

import edu.uchicago.mpcs53013.weatherSummary.WeatherSummary;

public class CassandraWeatherSummary implements Serializable {
	private Integer station;
	private Boolean fog;
	private Boolean hail;

	private Boolean rain;
	private Boolean snow;
	private Boolean thunder;
	private Boolean tornado;

	public CassandraWeatherSummary() {}
	
	CassandraWeatherSummary(WeatherSummary weatherSummary) {
		this.station = weatherSummary.station;
		this.fog = weatherSummary.fog;
		this.hail = weatherSummary.hail;
		this.rain = weatherSummary.rain;
		this.snow = weatherSummary.snow;
		this.thunder = weatherSummary.thunder;
		this.tornado = weatherSummary.tornado;
	}

	public Integer getStation() {
		return station;
	}

	public void setStation(Integer station) {
		this.station = station;
	}

	public Boolean getFog() {
		return fog;
	}

	public void setFog(Boolean fog) {
		this.fog = fog;
	}

	public Boolean getHail() {
		return hail;
	}

	public void setHail(Boolean Hail) {
		this.hail = hail;
	}

	public Boolean getRain() {
		return rain;
	}

	public void setRain(Boolean rain) {
		this.rain = rain;
	}

	public Boolean getSnow() {
		return snow;
	}

	public void setSnow(Boolean snow) {
		this.snow = snow;
	}

	public Boolean getThunder() {
		return thunder;
	}

	public void setThunder(Boolean thunder) {
		this.thunder = thunder;
	}

	public Boolean getTornado() {
		return tornado;
	}

	public void setTornado(Boolean tornado) {
		this.tornado = tornado;
	}


	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.add("station", station)
				.add("fog", fog)
				.add("hail", hail)
				.add("rain", rain)
				.add("thunder", thunder)
				.add("tornado", tornado)
				.toString();
	}

}
