package projetuni;

public class Room {
	//Déclaration de la classe
    private int id;
    private String building;
    private String floor;
    private String number;

    public Room(int id, String building, String floor, String number) {
    	//Constructeur 
        this.id = id;
        this.building = building;
        this.floor = floor;
        this.number = number;
    }
    
    //Getters et Setters 
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

}