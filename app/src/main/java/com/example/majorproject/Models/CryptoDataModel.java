package com.example.majorproject.Models;

public class CryptoDataModel {

    String id;
    String symbol;
    String name;
    String image;
    Double current_price;
    Long market_cap;
    String market_cap_rank;
    Long fully_diluted_valuation;
    Long total_volume;
    Double high_24h;
    Double low_24h;
    Double price_change_24h;
    Double price_change_percentage_24h;
    Long market_cap_change_24h;
    Double market_cap_change_percentage_24h;
    Double circulating_supply;
    Double total_supply;
    Double max_supply;
    Long ath;
    Double ath_change_percentage;
    String ath_date;
    Double atl;
    Double atl_change_percentage;
    String atl_date;
    String last_updated;
    String type;

    public CryptoDataModel() {
    }

    public CryptoDataModel(String id, String symbol, String name, String image, Double current_price, Long market_cap, String market_cap_rank, Long fully_diluted_valuation, Long total_volume, Double high_24h, Double low_24h, Double price_change_24h, Double price_change_percentage_24h, Long market_cap_change_24h, Double market_cap_change_percentage_24h, Double circulating_supply, Double total_supply, Double max_supply, Long ath, Double ath_change_percentage, String ath_date, Double atl, Double atl_change_percentage, String atl_date, String last_updated,String type) {
        this.id = id;
        this.symbol = symbol;
        this.name = name;
        this.image = image;
        this.current_price = current_price;
        this.market_cap = market_cap;
        this.market_cap_rank = market_cap_rank;
        this.fully_diluted_valuation = fully_diluted_valuation;
        this.total_volume = total_volume;
        this.high_24h = high_24h;
        this.low_24h = low_24h;
        this.price_change_24h = price_change_24h;
        this.price_change_percentage_24h = price_change_percentage_24h;
        this.market_cap_change_24h = market_cap_change_24h;
        this.market_cap_change_percentage_24h = market_cap_change_percentage_24h;
        this.circulating_supply = circulating_supply;
        this.total_supply = total_supply;
        this.max_supply = max_supply;
        this.ath = ath;
        this.ath_change_percentage = ath_change_percentage;
        this.ath_date = ath_date;
        this.atl = atl;
        this.atl_change_percentage = atl_change_percentage;
        this.atl_date = atl_date;
        this.last_updated = last_updated;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Double getCurrent_price() {
        return current_price;
    }

    public void setCurrent_price(Double current_price) {
        this.current_price = current_price;
    }

    public Long getMarket_cap() {
        return market_cap;
    }

    public void setMarket_cap(Long market_cap) {
        this.market_cap = market_cap;
    }

    public String getMarket_cap_rank() {
        return market_cap_rank;
    }

    public void setMarket_cap_rank(String market_cap_rank) {
        this.market_cap_rank = market_cap_rank;
    }

    public Long getFully_diluted_valuation() {
        return fully_diluted_valuation;
    }

    public void setFully_diluted_valuation(Long fully_diluted_valuation) {
        this.fully_diluted_valuation = fully_diluted_valuation;
    }

    public Long getTotal_volume() {
        return total_volume;
    }

    public void setTotal_volume(Long total_volume) {
        this.total_volume = total_volume;
    }

    public Double getHigh_24h() {
        return high_24h;
    }

    public void setHigh_24h(Double high_24h) {
        this.high_24h = high_24h;
    }

    public Double getLow_24h() {
        return low_24h;
    }

    public void setLow_24h(Double low_24h) {
        this.low_24h = low_24h;
    }

    public Double getPrice_change_24h() {
        return price_change_24h;
    }

    public void setPrice_change_24h(Double price_change_24h) {
        this.price_change_24h = price_change_24h;
    }

    public Double getPrice_change_percentage_24h() {
        return price_change_percentage_24h;
    }

    public void setPrice_change_percentage_24h(Double price_change_percentage_24h) {
        this.price_change_percentage_24h = price_change_percentage_24h;
    }

    public Long getMarket_cap_change_24h() {
        return market_cap_change_24h;
    }

    public void setMarket_cap_change_24h(Long market_cap_change_24h) {
        this.market_cap_change_24h = market_cap_change_24h;
    }

    public Double getMarket_cap_change_percentage_24h() {
        return market_cap_change_percentage_24h;
    }

    public void setMarket_cap_change_percentage_24h(Double market_cap_change_percentage_24h) {
        this.market_cap_change_percentage_24h = market_cap_change_percentage_24h;
    }

    public Double getCirculating_supply() {
        return circulating_supply;
    }

    public void setCirculating_supply(Double circulating_supply) {
        this.circulating_supply = circulating_supply;
    }

    public Double getTotal_supply() {
        return total_supply;
    }

    public void setTotal_supply(Double total_supply) {
        this.total_supply = total_supply;
    }

    public Double getMax_supply() {
        return max_supply;
    }

    public void setMax_supply(Double max_supply) {
        this.max_supply = max_supply;
    }

    public Long getAth() {
        return ath;
    }

    public void setAth(Long ath) {
        this.ath = ath;
    }

    public Double getAth_change_percentage() {
        return ath_change_percentage;
    }

    public void setAth_change_percentage(Double ath_change_percentage) {
        this.ath_change_percentage = ath_change_percentage;
    }

    public String getAth_date() {
        return ath_date;
    }

    public void setAth_date(String ath_date) {
        this.ath_date = ath_date;
    }

    public Double getAtl() {
        return atl;
    }

    public void setAtl(Double atl) {
        this.atl = atl;
    }

    public Double getAtl_change_percentage() {
        return atl_change_percentage;
    }

    public void setAtl_change_percentage(Double atl_change_percentage) {
        this.atl_change_percentage = atl_change_percentage;
    }

    public String getAtl_date() {
        return atl_date;
    }

    public void setAtl_date(String atl_date) {
        this.atl_date = atl_date;
    }

    public String getLast_updated() {
        return last_updated;
    }

    public void setLast_updated(String last_updated) {
        this.last_updated = last_updated;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
