package com.MMRSheikh2001.workbridgeandroid.enums;

public enum GigOrderStatus {

    ORDER_PLACED,//Buyer place order and chat opens
    QUOTED,  //Seller sent a quote
    QUOTE_ACCEPTED,  //buyer accepts the quote and payment locked about
    QUOTE_REJECTED, //buyer rejects the quote and chat closes

    DELIVERED,//Seller uploads the work and the job is finished
    BUYER_ACCEPTED,//buyer accepts and seller receives money
    BUYER_REJECTED,//buyer rejects and seller has 7 days to raise dispute
    BUYER_CANCELLED,//buyer cancelled the order before delivery and seller has 7 days to dispute,then payment refunded
    SELLER_CANCELLED,//Seller cancels order due to sickness or things ,buyer refuned money
    SELLER_DISPUTED,//seller opens dispute, waiting for admin
    PAYMENT_RELEASED,//seller wins dispute and gets money
    REFUNDED//Buyer wins dispute and gets money returned


}

