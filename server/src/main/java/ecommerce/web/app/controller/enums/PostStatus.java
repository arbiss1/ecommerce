package ecommerce.web.app.controller.enums;

public enum PostStatus {
    PENDING,
    ACTIVE;

    public ecommerce.web.app.enums.PostStatus mapToStatus(){
        if(this == PostStatus.PENDING){
            return ecommerce.web.app.enums.PostStatus.PENDING;
        } else {
            return ecommerce.web.app.enums.PostStatus.ACTIVE;
        }
    }
}
