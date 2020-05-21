package ro.pub.cs.systems.eim.practicaltest02;

public class AlarmInformation {
    private String hour;
    private String min;

    public AlarmInformation() {
        this.hour = null;
        this.min = null;
    }
    public AlarmInformation(String hour, String minute) {
        this.hour = hour;
        this.min = minute;
    }
    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }
    public String getMinute() {
        return min;
    }

    public void setMinute(String minute) {
        this.min = minute;
    }
    @Override
    public String toString() {
        return "AlarmInformation{" +
                "ora='" + hour + '\'' +
                ", min='" + min + '\'' +
                '}';
    }
}
