package com.citi.dde.common.util;







public enum CountryCode
{
  AU,  SG,  IN,  HK,  CH,  TH,  ID,  VN,  MY,  NOCOUNTRY;
  
  public static CountryCode getCountry(String countryCode) {
    if (countryCode != null) {
      try {
        return valueOf(countryCode.toUpperCase());
      } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException("Country is incorrect", e);
      }
    }
    throw new IllegalArgumentException("Country is not provided");
  }
}
