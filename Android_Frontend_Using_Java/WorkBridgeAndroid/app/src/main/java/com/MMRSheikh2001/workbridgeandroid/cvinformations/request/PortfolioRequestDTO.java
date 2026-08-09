package com.MMRSheikh2001.workbridgeandroid.cvinformations.request;

import lombok.Data;

@Data
public class PortfolioRequestDTO {

    private String title;
    private String description;

    private String projectUrl;

    private String technologies;

    private Long userProfileId;


}