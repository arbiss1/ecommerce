package ecommerce.web.app.controller.enums;

public enum Currency{
    ALL,
    EUR,
    USD,
    GBP;

    public ecommerce.web.app.enums.Currency mapToStatus(){
        if(this == Currency.ALL){
            return ecommerce.web.app.enums.Currency.ALL;
        } else if(this == Currency.EUR){
            return ecommerce.web.app.enums.Currency.EUR;
        } else if(this == Currency.USD){
            return ecommerce.web.app.enums.Currency.USD;
        }else {
            return ecommerce.web.app.enums.Currency.GBP;
        }
    }
}