package com.example.kaitlynwright.tidetable;

//import java.text.SimpleDateFormat;

public class TideItem {
    private String high_low = null;
    private String date = null;
    private String day = null;
    private  String time = null;
    private String pred_in_cm = null;
    private String pred_in_ft = null;

    // getters & setters
    public void setHigh_low(String h_l) { this.high_low = h_l; }

    public String getHigh_low() { return high_low; }

    public void setDate(String date) { this.date = date; }

    public String getDate() { return date; }

    public void setDay(String day) { this.day = day; }

    public String getDay() { return day; }

    public void setTime(String time) { this.time = time; }

    public String getTime() { return time; }

    public void setPredCm(String pred) { this.pred_in_cm = pred; }

    public String getPredCm() { return pred_in_cm; }

    public void setPredFt(String pred) { this.pred_in_ft = pred;}

    public String getPredFt() { return pred_in_ft; }


}
