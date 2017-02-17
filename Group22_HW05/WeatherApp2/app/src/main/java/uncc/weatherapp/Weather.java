package uncc.weatherapp;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sanju on 10/10/2016.
 */

public class Weather implements Serializable{


    /**
     * FCTTIME : {"hour":"19","hour_padded":"19","min":"00","min_unpadded":"0","sec":"0","year":"2016","mon":"10","mon_padded":"10","mon_abbrev":"Oct","mday":"12","mday_padded":"12","yday":"285","isdst":"1","epoch":"1476324000","pretty":"7:00 PM PDT on October 12, 2016","civil":"7:00 PM","month_name":"October","month_name_abbrev":"Oct","weekday_name":"Wednesday","weekday_name_night":"Wednesday Night","weekday_name_abbrev":"Wed","weekday_name_unlang":"Wednesday","weekday_name_night_unlang":"Wednesday Night","ampm":"PM","tz":"","age":"","UTCDATE":""}
     * temp : {"english":"57","metric":"14"}
     * dewpoint : {"english":"51","metric":"11"}
     * condition : Partly Cloudy
     * icon : partlycloudy
     * icon_url : http://icons.wxug.com/i/c/k/nt_partlycloudy.gif
     * fctcode : 2
     * sky : 47
     * wspd : {"english":"11","metric":"18"}
     * wdir : {"dir":"W","degrees":"269"}
     * wx : Partly Cloudy
     * uvi : 0
     * humidity : 80
     * windchill : {"english":"-9999","metric":"-9999"}
     * heatindex : {"english":"-9999","metric":"-9999"}
     * feelslike : {"english":"57","metric":"14"}
     * qpf : {"english":"0.0","metric":"0"}
     * snow : {"english":"0.0","metric":"0"}
     * pop : 0
     * mslp : {"english":"30.06","metric":"1018"}
     */

    private List<HourlyForecastBean> hourly_forecast;

    @Override
    public String toString() {
        return "Weather{" +
                "hourly_forecast=" + hourly_forecast +
                '}';
    }

    public List<HourlyForecastBean> getHourly_forecast() {
        return hourly_forecast;
    }

    public void setHourly_forecast(List<HourlyForecastBean> hourly_forecast) {
        this.hourly_forecast = hourly_forecast;
    }

    public static class HourlyForecastBean implements Serializable{
        /**
         * hour : 19
         * hour_padded : 19
         * min : 00
         * min_unpadded : 0
         * sec : 0
         * year : 2016
         * mon : 10
         * mon_padded : 10
         * mon_abbrev : Oct
         * mday : 12
         * mday_padded : 12
         * yday : 285
         * isdst : 1
         * epoch : 1476324000
         * pretty : 7:00 PM PDT on October 12, 2016
         * civil : 7:00 PM
         * month_name : October
         * month_name_abbrev : Oct
         * weekday_name : Wednesday
         * weekday_name_night : Wednesday Night
         * weekday_name_abbrev : Wed
         * weekday_name_unlang : Wednesday
         * weekday_name_night_unlang : Wednesday Night
         * ampm : PM
         * tz :
         * age :
         * UTCDATE :
         */

        private FCTTIMEBean FCTTIME;
        /**
         * english : 57
         * metric : 14
         */

        private TempBean temp;
        /**
         * english : 51
         * metric : 11
         */

        private DewpointBean dewpoint;
        private String condition;
        private String icon;
        private String icon_url;
        private String fctcode;
        private String sky;
        /**
         * english : 11
         * metric : 18
         */

        private WspdBean wspd;
        /**
         * dir : W
         * degrees : 269
         */

        private WdirBean wdir;
        private String wx;
        private String uvi;
        private String humidity;
        /**
         * english : -9999
         * metric : -9999
         */

        private WindchillBean windchill;
        /**
         * english : -9999
         * metric : -9999
         */

        private HeatindexBean heatindex;
        /**
         * english : 57
         * metric : 14
         */

        private FeelslikeBean feelslike;
        /**
         * english : 0.0
         * metric : 0
         */

        private QpfBean qpf;
        /**
         * english : 0.0
         * metric : 0
         */

        private SnowBean snow;
        private String pop;
        /**
         * english : 30.06
         * metric : 1018
         */

        private MslpBean mslp;

        public FCTTIMEBean getFCTTIME() {
            return FCTTIME;
        }

        public void setFCTTIME(FCTTIMEBean FCTTIME) {
            this.FCTTIME = FCTTIME;
        }

        public TempBean getTemp() {
            return temp;
        }

        public void setTemp(TempBean temp) {
            this.temp = temp;
        }

        public DewpointBean getDewpoint() {
            return dewpoint;
        }

        public void setDewpoint(DewpointBean dewpoint) {
            this.dewpoint = dewpoint;
        }

        public String getCondition() {
            return condition;
        }

        public void setCondition(String condition) {
            this.condition = condition;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getIcon_url() {
            return icon_url;
        }

        public void setIcon_url(String icon_url) {
            this.icon_url = icon_url;
        }

        public String getFctcode() {
            return fctcode;
        }

        public void setFctcode(String fctcode) {
            this.fctcode = fctcode;
        }

        public String getSky() {
            return sky;
        }

        public void setSky(String sky) {
            this.sky = sky;
        }

        public WspdBean getWspd() {
            return wspd;
        }

        public void setWspd(WspdBean wspd) {
            this.wspd = wspd;
        }

        public WdirBean getWdir() {
            return wdir;
        }

        public void setWdir(WdirBean wdir) {
            this.wdir = wdir;
        }

        public String getWx() {
            return wx;
        }

        public void setWx(String wx) {
            this.wx = wx;
        }

        public String getUvi() {
            return uvi;
        }

        public void setUvi(String uvi) {
            this.uvi = uvi;
        }

        public String getHumidity() {
            return humidity;
        }

        public void setHumidity(String humidity) {
            this.humidity = humidity;
        }

        public WindchillBean getWindchill() {
            return windchill;
        }

        public void setWindchill(WindchillBean windchill) {
            this.windchill = windchill;
        }

        public HeatindexBean getHeatindex() {
            return heatindex;
        }

        public void setHeatindex(HeatindexBean heatindex) {
            this.heatindex = heatindex;
        }

        public FeelslikeBean getFeelslike() {
            return feelslike;
        }

        public void setFeelslike(FeelslikeBean feelslike) {
            this.feelslike = feelslike;
        }

        public QpfBean getQpf() {
            return qpf;
        }

        public void setQpf(QpfBean qpf) {
            this.qpf = qpf;
        }

        public SnowBean getSnow() {
            return snow;
        }

        public void setSnow(SnowBean snow) {
            this.snow = snow;
        }

        public String getPop() {
            return pop;
        }

        public void setPop(String pop) {
            this.pop = pop;
        }

        public MslpBean getMslp() {
            return mslp;
        }

        public void setMslp(MslpBean mslp) {
            this.mslp = mslp;
        }

        public static class FCTTIMEBean implements Serializable{
            private String civil;

            public String getCivil() {
                return civil;
            }

            public void setCivil(String civil) {
                this.civil = civil;
            }
        }

        public static class TempBean implements Serializable{
            private String english;
            private String metric;

            public String getEnglish() {
                return english;
            }

            public void setEnglish(String english) {
                this.english = english;
            }

            public String getMetric() {
                return metric;
            }

            public void setMetric(String metric) {
                this.metric = metric;
            }
        }

        public static class DewpointBean implements Serializable{
            private String english;
            private String metric;

            public String getEnglish() {
                return english;
            }

            public void setEnglish(String english) {
                this.english = english;
            }

            public String getMetric() {
                return metric;
            }

            public void setMetric(String metric) {
                this.metric = metric;
            }
        }

        public static class WspdBean implements Serializable{
            private String english;
            private String metric;

            public String getEnglish() {
                return english;
            }

            public void setEnglish(String english) {
                this.english = english;
            }

            public String getMetric() {
                return metric;
            }

            public void setMetric(String metric) {
                this.metric = metric;
            }
        }

        public static class WdirBean implements Serializable{
            private String dir;
            private String degrees;

            public String getDir() {
                return dir;
            }

            public void setDir(String dir) {
                this.dir = dir;
            }

            public String getDegrees() {
                return degrees;
            }

            public void setDegrees(String degrees) {
                this.degrees = degrees;
            }
        }

        public static class WindchillBean implements Serializable{
            private String english;
            private String metric;

            public String getEnglish() {
                return english;
            }

            public void setEnglish(String english) {
                this.english = english;
            }

            public String getMetric() {
                return metric;
            }

            public void setMetric(String metric) {
                this.metric = metric;
            }
        }

        public static class HeatindexBean implements Serializable{
            private String english;
            private String metric;

            public String getEnglish() {
                return english;
            }

            public void setEnglish(String english) {
                this.english = english;
            }

            public String getMetric() {
                return metric;
            }

            public void setMetric(String metric) {
                this.metric = metric;
            }
        }

        public static class FeelslikeBean implements Serializable{
            private String english;
            private String metric;

            public String getEnglish() {
                return english;
            }

            public void setEnglish(String english) {
                this.english = english;
            }

            public String getMetric() {
                return metric;
            }

            public void setMetric(String metric) {
                this.metric = metric;
            }
        }

        public static class QpfBean implements Serializable{
            private String english;
            private String metric;

            public String getEnglish() {
                return english;
            }

            public void setEnglish(String english) {
                this.english = english;
            }

            public String getMetric() {
                return metric;
            }

            public void setMetric(String metric) {
                this.metric = metric;
            }
        }

        public static class SnowBean implements Serializable{
            private String english;
            private String metric;

            public String getEnglish() {
                return english;
            }

            public void setEnglish(String english) {
                this.english = english;
            }

            public String getMetric() {
                return metric;
            }

            public void setMetric(String metric) {
                this.metric = metric;
            }
        }

        public static class MslpBean implements Serializable{
            private String english;
            private String metric;

            public String getEnglish() {
                return english;
            }

            public void setEnglish(String english) {
                this.english = english;
            }

            public String getMetric() {
                return metric;
            }

            public void setMetric(String metric) {
                this.metric = metric;
            }
        }
    }
}
