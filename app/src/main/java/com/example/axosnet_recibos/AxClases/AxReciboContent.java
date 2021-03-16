package com.example.axosnet_recibos.AxClases;

public class AxReciboContent {

    private int id;
    private String provider;
    private String amount;
    private String comment;
    private String emissionDate;
    private String currencyCode;

    public AxReciboContent(int id, String provider, String amount, String comment, String emissionDate, String currencyCode) {
        this.id = id;
        this.provider = provider;

        if(provider.equals("null"))
            this.provider = "-";
        else
            this.provider = provider;

        this.amount = amount;

        if(comment.equals("null") || comment.equals("Sin comentario"))
            this.comment = "-";
        else
            this.comment = comment;

        this.emissionDate = emissionDate;
        this.currencyCode = currencyCode;
    }

    @Override
    public String toString() {
        return "provider=" + provider +
                "&amount=" + amount +
                "&comment=" + comment +
                "&emission_date=" + emissionDate +
                "&currency_code=" + currencyCode;
    }

    public String toStringUpdt() {
        return "id="+ String.valueOf(id)+
                "&provider=" + provider +
                "&amount=" + amount +
                "&comment=" + comment +
                "&emission_date=" + emissionDate +
                "&currency_code=" + currencyCode;
    }

    public int getId() {
        return id;
    }
    public String getProvider() {
        return provider;
    }
    public String getAmount() {
        return amount;
    }
    public String getComment() {
        return comment;
    }
    public String getEmissionDate() {
        return emissionDate;
    }
    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setProvider(String provider) {
        this.provider = provider;
    }
    public void setAmount(String amount) {
        this.amount = amount;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public void setEmissionDate(String emissionDate) {
        this.emissionDate = emissionDate;
    }
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

}