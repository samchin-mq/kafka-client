package org.example.kafka.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderShares implements Serializable {
    private String bid;
    private String userid;
    private String parentid;
    private String childid;
    private Double share;
    private Double amount;
    private Double shareAmount;
    private Double cm;
    private Double scm;
    
}
