package projetuni;

public class Reservation {
	//Définition de la classe et des attributs
    private Room room;
    private String date;
    private String time;
    private String promo;
    private String responsible;

    public Reservation(Room room, String date, String time, String promo, String responsible) {
    	//Constructeur
        this.room = room;
        this.date = date;
        this.time = time;
        this.promo = promo;
        this.responsible = responsible;
    }

    // Getters and Setters(accéder et de modifier les attributs de la classe)
    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPromo() {
        return promo;
    }

    public void setPromo(String promo) {
        this.promo = promo;
    }

    public String getResponsible() {
        return responsible;
    }

    public void setResponsible(String responsible) {
        this.responsible = responsible;
    }

    @Override
    public String toString() {
    	//fournir une représentation sous forme de chaîne de caractères pour Reservation
        return "Reservation{" +
                "room=" + room +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", promo='" + promo + '\'' +
                ", responsible='" + responsible + '\'' +
                '}';
    }
}
