package reservation.app;

import javafx.scene.control.Button;

public class ButtonData {

    ButtonData(Button button_obj, String event_name, String participants_list, String day, int week, String begin_time, int begin, String end_time, int end) {
        this.button_obj = button_obj;
        this.event_name = event_name;
        this.participants_list = participants_list;
        this.day = day;
        this.week = week;
        this.begin_time = begin_time;
        this.begin = begin;
        this.end_time = end_time;
        this.end = end;
    }

    public Button getButton_obj() {return button_obj;}

    public String getEvent_name() {return event_name;}

    public String getParticipants_list() {return participants_list;}

    public String getDay() {return day;}

    public int getWeek() {return week;}

    public String getBegin_time() {return begin_time;}

    public int getBegin() {return begin;}

    public String getEnd_time() {return end_time;}

    public int getEnd() {return end;}

    Button button_obj;
    String event_name;
    String participants_list;
    String day;
    int week;
    String begin_time;
    int begin;
    String end_time;
    int end;
}