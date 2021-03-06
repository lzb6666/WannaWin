package citi.vo;

import citi.funcModule.mscard.CardDescriptionBean;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import java.util.HashMap;
import java.util.Map;

public class Merchant {
    @Expose
    private String merchantID;
    @Expose
    private String name;
    @Expose(serialize = false)
    private String password;
    @Expose
    private String description;
    @Expose
    private String cardDescription;
    @Expose
    private String address;
    @Expose
    private String merchantLogoURL;
    @Expose
    private String cardLogoURL;
    @Expose
    private Double proportion;
    @Expose
    private String cardType;
    @Expose
    private String businessType;

    public enum CardType {
        cardNum, phoneNum, cardNumWithPassword, phoneNumWithPassword;

        static Map<String, CardType> enumMap1 = new HashMap<>();
        static Map<CardType, String> enumMap2 = new HashMap<>();

        static {
            enumMap1.put("cardNum", cardNum);
            enumMap1.put("phoneNum", phoneNum);
            enumMap1.put("cardNumWithPassword", cardNumWithPassword);
            enumMap1.put("phoneNumWithPassword", phoneNumWithPassword);

            enumMap2.put(cardNum, "cardNum");
            enumMap2.put(phoneNum, "phoneNum");
            enumMap2.put(cardNumWithPassword, "cardNumWithPassword");
            enumMap2.put(phoneNumWithPassword, "phoneNumWithPassword");
        }

        public static CardType getCardType(String cardType) {
            return enumMap1.get(cardType);
        }

        public static String getCardTypeString(CardType cardType) {
            return enumMap2.get(cardType);
        }

    }

    public enum BusinessType {
        normal, catering, exercise, bank, costume, education, communication,
        operator, aviation, hotel, supermarket, movie;

        static Map<String, BusinessType> enumMap1 = new HashMap<>();
        static Map<BusinessType, String> enumMap2 = new HashMap<>();

        static {
            enumMap1.put("normal", normal);
            enumMap1.put("catering", catering);
            enumMap1.put("exercise", exercise);
            enumMap1.put("bank", bank);
            enumMap1.put("costume", costume);
            enumMap1.put("education", education);
            enumMap1.put("communication", communication);
            enumMap1.put("operator", operator);
            enumMap1.put("aviation", aviation);
            enumMap1.put("hotel", hotel);
            enumMap1.put("supermarket", supermarket);
            enumMap1.put("movie", movie);


            enumMap2.put(normal, "normal");
            enumMap2.put(catering, "catering");
            enumMap2.put(exercise, "exercise");
            enumMap2.put(bank, "bank");
            enumMap2.put(costume, "costume");
            enumMap2.put(education, "education");
            enumMap2.put(communication, "communication");
            enumMap2.put(operator, "operator");
            enumMap2.put(aviation, "aviation");
            enumMap2.put(hotel, "hotel");
            enumMap2.put(supermarket, "supermarket");
            enumMap2.put(movie, "movie");
        }

        public static BusinessType getBusinessType(String businessType) {
            return enumMap1.get(businessType);
        }

        public static String getBusinessTypeString(BusinessType businessType) {
            return enumMap2.get(businessType);
        }

    }


    //for DB
    public Merchant(String merchantID, String name, String password, String description, String cardDescription, String address, String merchantLogoURL, String cardLogoURL, Double proportion, String cardType, String businessType) {
        this.merchantID = merchantID;
        this.name = name;
        this.password = password;
        this.description = description;
        this.cardDescription = cardDescription;
        this.address = address;
        this.merchantLogoURL = merchantLogoURL;
        this.cardLogoURL = cardLogoURL;
        this.proportion = proportion;
        this.cardType = cardType;
        this.businessType = businessType;
    }


    //for programmer
    public Merchant(String merchantID, String name, String password, String description, String cardDescription, String address, String merchantLogoURL, String cardLogoURL, Double proportion, CardType cardType, BusinessType businessType) {
        this.merchantID = merchantID;
        this.name = name;
        this.password = password;
        this.description = description;
        this.cardDescription = cardDescription;
        this.address = address;
        this.merchantLogoURL = merchantLogoURL;
        this.cardLogoURL = cardLogoURL;
        this.proportion = proportion;
        this.cardType = CardType.getCardTypeString(cardType);
        this.businessType = BusinessType.getBusinessTypeString(businessType);
    }


    public Merchant() {
    }

    public String getCardDescription() {
        Gson gson = new Gson();
        CardDescriptionBean jsonObject = gson.fromJson(cardDescription, CardDescriptionBean.class);//把JSON字符串转为对象
        String description = jsonObject.getCardDescription();
        return description;
    }

    public int getStyle(){
        Gson gson = new Gson();
        CardDescriptionBean jsonObject = gson.fromJson(cardDescription, CardDescriptionBean.class);//把JSON字符串转为对象
        String style = jsonObject.getCardStyle();
        return Integer.valueOf(style);
    }

    public void setCardDescription(String cardDescription) {
        this.cardDescription = cardDescription;
    }

    public String getCardLogoURL() {
        return cardLogoURL;
    }

    public void setCardLogoURL(String cardLogoURL) {
        this.cardLogoURL = cardLogoURL;
    }

    public Double getProportion() {
        return proportion;
    }

    public void setProportion(Double proportion) {
        this.proportion = proportion;
    }

    public String getMerchantID() {
        return merchantID;
    }

    public void setMerchantID(String merchantID) {
        this.merchantID = merchantID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMerchantLogoURL() {
        return merchantLogoURL;
    }

    public void setMerchantLogoURL(String logoURL) {
        this.merchantLogoURL = logoURL;
    }

    public BusinessType getBusinessType() {
        return BusinessType.getBusinessType(businessType);
    }

    public void setBusinessType(BusinessType businessType) {
        this.businessType = BusinessType.getBusinessTypeString(businessType);
    }

    public CardType getCardType() {
        return CardType.getCardType(cardType);
    }

    public void setCardType(CardType cardType) {
        this.cardType = CardType.getCardTypeString(cardType);
    }

}