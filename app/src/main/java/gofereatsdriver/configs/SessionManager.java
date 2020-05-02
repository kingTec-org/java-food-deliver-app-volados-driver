package gofereatsdriver.configs;
/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage configs
 * @category SessionManager
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.content.SharedPreferences;

import javax.inject.Inject;

/*****************************************************************
 Session manager to set and get glopal values
 ***************************************************************/
public class SessionManager {
    public @Inject
    SharedPreferences sharedPreferences;

    public SessionManager() {
        AppController.getAppComponent().inject(this);
    }


    public void clearToken() {
        sharedPreferences.edit().putString("token", "").apply();
    }

    public void clearAll() {
        sharedPreferences.edit().clear().apply();
        setType("2");
    }

    public String getToken() {
        return sharedPreferences.getString("token", "");
    }

    public void setToken(String token) {
        sharedPreferences.edit().putString("token", token).apply();
    }

    public String getType() {
        return sharedPreferences.getString("type", "");
    }

    public void setType(String type) {
        sharedPreferences.edit().putString("type", type).apply();
    }

    public String getGender() {
        return sharedPreferences.getString("gender", "");
    }

    public void setGender(String gender) {
        sharedPreferences.edit().putString("gender", gender).apply();
    }

    public String getCurrency() {
        return sharedPreferences.getString("currency", "");
    }

    public void setCurrency(String currency) {
        sharedPreferences.edit().putString("currency", currency).apply();
    }

    public String getCountryCurrencyType() {
        return sharedPreferences.getString("setCountryCurrencyType", "");
    }

    public void setCountryCurrencyType(String setCountryCurrencyType) {
        sharedPreferences.edit().putString("setCountryCurrencyType", setCountryCurrencyType).apply();
    }

    public String getCountry() {
        return sharedPreferences.getString("country", "");
    }

    public void setCountry(String country) {
        sharedPreferences.edit().putString("country", country).apply();
    }

    public String getFirstName() {
        return sharedPreferences.getString("firstname", "");
    }

    public void setFirstName(String firstName) {
        sharedPreferences.edit().putString("firstname", firstName).apply();
    }

    public String getLastName() {
        return sharedPreferences.getString("lastname", "");
    }

    public void setLastName(String lastName) {
        sharedPreferences.edit().putString("lastname", lastName).apply();
    }

    public void setEmail(String email) {
        sharedPreferences.edit().putString("email", email).apply();
    }

    public String getemail() {
        return sharedPreferences.getString("email", "");
    }

    public String getPassword() {
        return sharedPreferences.getString("Password", "");
    }

    public void setPassword(String password) {
        sharedPreferences.edit().putString("password", password).apply();
    }

    public String getCity() {
        return sharedPreferences.getString("city", "");
    }

    public void setCity(String city) {
        sharedPreferences.edit().putString("city", city).apply();
    }

    public String getPhoneNumber() {
        return sharedPreferences.getString("phoneNumber", "");
    }

    public void setPhoneNumber(String phoneNumber) {
        sharedPreferences.edit().putString("phoneNumber", phoneNumber).apply();
    }

    public String getCountryCode() {
        return sharedPreferences.getString("countryCode", "");
    }

    public void setCountryCode(String countryCode) {
        sharedPreferences.edit().putString("countryCode", countryCode).apply();
    }

    public String getDeviceId() {
        return sharedPreferences.getString("deviceId", "");
    }

    public void setDeviceId(String deviceId) {
        sharedPreferences.edit().putString("deviceId", deviceId).apply();
    }

    public String getCurrentLat() {
        return sharedPreferences.getString("currentLat", "");
    }

    public void setCurrentLat(String currentLat) {
        sharedPreferences.edit().putString("currentLat", currentLat).apply();
    }

    public String getPushNotification() {
        return sharedPreferences.getString("pushNotification", "");
    }

    public void setPushNotification(String pushNotification) {
        sharedPreferences.edit().putString("pushNotification", pushNotification).apply();
    }

    public String getCurrentLng() {
        return sharedPreferences.getString("currentLng", "");
    }

    public void setCurrentLng(String currentLng) {
        sharedPreferences.edit().putString("currentLng", currentLng).apply();
    }

    public Integer getTripId() {
        return sharedPreferences.getInt("tripId", 0);
    }

    public void setTripId(Integer tripId) {
        sharedPreferences.edit().putInt("tripId", tripId).apply();
    }

    public Integer getDriverStatus() {
        return sharedPreferences.getInt("driverStatus", -1);
    }

    public void setDriverStatus(Integer driverStatus) {
        sharedPreferences.edit().putInt("driverStatus", driverStatus).apply();
    }

    public Integer getTripStatus() {
        return sharedPreferences.getInt("tripStatus", -1);
    }

    public void setTripStatus(Integer tripStatus) {
        sharedPreferences.edit().putInt("tripStatus", tripStatus).apply();
    }

    public Integer getVehicleId() {
        return sharedPreferences.getInt("vehicleId", 0);
    }

    public void setVehicleId(Integer vehicleId) {
        sharedPreferences.edit().putInt("vehicleId", vehicleId).apply();
    }


    public String getDoc1() {
        return sharedPreferences.getString("doc1", "");
    }

    public void setDoc1(String doc1) {
        sharedPreferences.edit().putString("doc1", doc1).apply();
    }

    public String getDoc2() {
        return sharedPreferences.getString("doc2", "");
    }

    public void setDoc2(String doc2) {
        sharedPreferences.edit().putString("doc2", doc2).apply();
    }

    public String getDoc3() {
        return sharedPreferences.getString("doc3", "");
    }

    public void setDoc3(String doc3) {
        sharedPreferences.edit().putString("doc3", doc3).apply();
    }

    public String getDoc4() {
        return sharedPreferences.getString("doc4", "");
    }

    public void setDoc4(String doc4) {
        sharedPreferences.edit().putString("doc4", doc4).apply();
    }

    public String getDoc5() {
        return sharedPreferences.getString("doc5", "");
    }

    public void setDoc5(String doc5) {
        sharedPreferences.edit().putString("doc5", doc5).apply();
    }

    public String getCurrencyCode() {
        return sharedPreferences.getString("currencyCode", "");
    }

    public void setCurrencyCode(String currencyCode) {
        sharedPreferences.edit().putString("currencyCode", currencyCode).apply();
    }

    public String getCurrencySymbol() {
        return sharedPreferences.getString("currencysymbol", "");
    }

    public void setCurrencySymbol(String currencySymbol) {
        sharedPreferences.edit().putString("currencysymbol", currencySymbol).apply();
    }

    // 0 for empty 1 for stripe  2 for others
    public Integer getPaymentMethod() {
        return sharedPreferences.getInt("paymentMethod", 0);
    }

    public void setPaymentMethod(Integer paymentMethod) {
        sharedPreferences.edit().putInt("paymentMethod", paymentMethod).apply();
    }

    public String getCardValue() {
        return sharedPreferences.getString("cardValue", "");
    }

    public void setCardValue(String cardValue) {
        sharedPreferences.edit().putString("cardValue", cardValue).apply();
    }

    public String getCardBrand() {
        return sharedPreferences.getString("cardBrand", "");
    }

    public void setCardBrand(String cardBrand) {
        sharedPreferences.edit().putString("cardBrand", cardBrand).apply();
    }

    public String getOweAmount() {
        return sharedPreferences.getString("oweAmount", "");
    }

    public void setOweAmount(String oweAmount) {
        sharedPreferences.edit().putString("oweAmount", oweAmount).apply();
    }

    public Boolean getIsStripe() {
        return sharedPreferences.getBoolean("isStripe", false);
    }

    public void setIsStripe(boolean isStripe) {
        sharedPreferences.edit().putBoolean("isStripe", isStripe).apply();
    }

    // Money Add to wallet by Card
    public Integer getWalletCard() {
        return sharedPreferences.getInt("walletCard", 0);
    }

    public void setWalletCard(Integer walletCard) {
        sharedPreferences.edit().putInt("walletCard", walletCard).apply();
    }
    public String getLanguage() {
        return sharedPreferences.getString("language", "English");
    }

    public void setLanguage(String language) {
        sharedPreferences.edit().putString("language", language).apply();
    }

    public String getLanguageCode() {
        return sharedPreferences.getString("languagecode", "en");
    }

    public void setLanguageCode(String languagecode) {
        sharedPreferences.edit().putString("languagecode", languagecode).apply();
    }
    public String getAccessToken() {
        return sharedPreferences.getString("access_token", "");
    }

    public void setAcesssToken(String access_token) {
        sharedPreferences.edit().putString("access_token", access_token).apply();
    }
}
