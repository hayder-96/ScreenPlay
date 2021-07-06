package com.screenplay.myscreenplay.Model;

public class Itemtow {

    private int id;
    private String Scene_location;
    private String Scene_description;
    private String Dialogue;
    private String Number;


    public Itemtow() {
    }

    public Itemtow(int id, String scene_location, String scene_description, String dialogue, String Number) {
        this.id = id;
        Scene_location = scene_location;
        Scene_description = scene_description;
        Dialogue = dialogue;
        this.Number=Number;

    }

    public Itemtow(String scene_location, String scene_description, String dialogue,String number) {
        Scene_location = scene_location;
        Scene_description = scene_description;
        Dialogue = dialogue;
         Number=number;

    }

    public Itemtow(String number,int id) {
        Number = number;
        this.id=id;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getScene_location() {
        return Scene_location;
    }

    public void setScene_location(String scene_location) {
        Scene_location = scene_location;
    }

    public String getScene_description() {
        return Scene_description;
    }

    public void setScene_description(String scene_description) {
        Scene_description = scene_description;
    }

    public String getDialogue() {
        return Dialogue;
    }

    public void setDialogue(String dialogue) {
        Dialogue = dialogue;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }
}

