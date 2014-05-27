package za.co.prescient.activity.model;


import java.util.Date;


public class ItcsTagRead {
    private Integer id;
    private String guestCard;
    private String zoneId;
    private Double xCoordRead;
    private Double yCoordRead;
    private Date tagReadDatetime;



    public ItcsTagRead(Integer id,String guestCard,String zoneId,Double xCoordRead,Double yCoordRead)
    {
        this.id=id;
        this.guestCard=guestCard;
        this.zoneId=zoneId;
        this.xCoordRead=xCoordRead;
        this.yCoordRead=yCoordRead;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGuestCard() {
        return guestCard;
    }

    public void setGuestCard(String guestCard) {
        this.guestCard = guestCard;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public Double getxCoordRead() {
        return xCoordRead;
    }

    public void setxCoordRead(Double xCoordRead) {
        this.xCoordRead = xCoordRead;
    }

    public Double getyCoordRead() {
        return yCoordRead;
    }

    public void setyCoordRead(Double yCoordRead) {
        this.yCoordRead = yCoordRead;
    }

    public Date getTagReadDatetime() {
        return tagReadDatetime;
    }

    public void setTagReadDatetime(Date tagReadDatetime) {
        this.tagReadDatetime = tagReadDatetime;
    }
}