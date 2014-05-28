package za.co.prescient.activity.model;


public class Guest {

    private Long hotelId;

    public Guest(Long hotelId)
    {
        this.hotelId=hotelId;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }
}
