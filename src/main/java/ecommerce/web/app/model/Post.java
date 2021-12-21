package ecommerce.web.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "post")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long postId;
    @NotEmpty(message = "post_title empty")
    public String postTitle;
    public String postDescription;
    @NotEmpty(message = "post_price empty")
    public String postPrice;
    public String postColor;
    public String postQuantity;
    public String postCode;

//    {
//        "postTitle": "Makine opel corsa 1.3",
//            "postDescription" : "Gjendja spektakel",
//            "postPrice" : "2.5 euro",
//            "postColor": "Blu",
//            "postQuantity" : "1"
//    }

    private String address;
    private String number;
    private String firstName;
    private String lastName;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private User user;
}
