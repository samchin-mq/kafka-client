package org.example.kafka.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class Order implements Serializable {
    private String id;
    private String lottery;
    private String userid;
    private String username;
    private String game;
    private String gameText;
    private String gkey;
    private String gname;
    private String range;
    private Integer userType;
    private Integer betType;
    private String drawNumber;
    private String text;
    private String oddsText;
    private String oddsDetail;
    private String contents;
    private Double amount;
    private Integer multiple;
    private String state;
    private Date created;
    private Date saveTime;
    private Integer result;
    private Double odds;
    private Double dividend;
    private Date settleTime;
    private String channel;
    private String ip;
    private String remark;
    private Double cm;
    private Double cma;
    private OrderShares[] shares;
    private String title;
    private String titleV2;
    private double freezeAmount;
    private String orderId;
    private String backId;
    private double autoBackAmount;
    private LocalDateTime oddsTime;
    private Integer accountType;
    private Date drawDate;
    private String userKey;
    private String ck;
    private String parentid;
    private Integer periodSeq;


}
