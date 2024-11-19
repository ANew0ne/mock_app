package com.example.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RequestDTO {
    private String rqUID;
    private String clientID;
    private String account;
    private String openDate;
    private String closeDate;
}
