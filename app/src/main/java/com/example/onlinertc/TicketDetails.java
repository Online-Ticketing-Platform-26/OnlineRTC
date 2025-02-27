package com.example.onlinertc;

public class TicketDetails {
    private String UserID;
    private String BoardingPoint;
    private String DestinationPoint;
    private String Fare;
    private String UID;
    private String DateTime;

    public String getValidatedBy() {
        return ValidatedBy;
    }

    public void setValidatedBy(String validatedBy) {
        ValidatedBy = validatedBy;
    }

    public String getValidationTime() {
        return ValidationTime;
    }

    public void setValidationTime(String validationTime) {
        ValidationTime = validationTime;
    }

    private String ValidatedBy;
    private String ValidationTime;

    public boolean isStatus() {
        return Status;
    }

    public void setStatus(boolean status) {
        Status = status;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    private boolean Status;

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getBoardingPoint() {
        return BoardingPoint;
    }

    public void setBoardingPoint(String boardingPoint) {
        this.BoardingPoint = boardingPoint;
    }

    public String getDestinationPoint() {
        return DestinationPoint;
    }

    public void setDestinationPoint(String destinationPoint) {
        this.DestinationPoint = destinationPoint;
    }

    public String getFare() {
        return Fare;
    }

    public void setFare(String fare) {
        Fare = fare;
    }


}
