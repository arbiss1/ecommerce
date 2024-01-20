package ecommerce.web.app.controller.enums;


public enum AdvertIndex {
    FREE,
    MEDIUM,
    HIGH;

    public ecommerce.web.app.enums.AdvertIndex mapToStatus(){
        if(this == AdvertIndex.FREE){
            return ecommerce.web.app.enums.AdvertIndex.FREE;
        } else if(this == AdvertIndex.HIGH){
            return ecommerce.web.app.enums.AdvertIndex.HIGH;
        } else {
            return ecommerce.web.app.enums.AdvertIndex.MEDIUM;
        }
    }
}