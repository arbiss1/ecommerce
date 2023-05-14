package ecommerce.web.app.controller.enums;


public enum AdvertIndex{
    FREE,
    LOW_PLAN,
    HIGH_PLAN;

    public ecommerce.web.app.enums.AdvertIndex mapToStatus(){
        if(this == AdvertIndex.FREE){
            return ecommerce.web.app.enums.AdvertIndex.FREE;
        } else if(this == AdvertIndex.HIGH_PLAN){
            return ecommerce.web.app.enums.AdvertIndex.HIGH_PLAN;
        } else {
            return ecommerce.web.app.enums.AdvertIndex.LOW_PLAN;
        }
    }
}