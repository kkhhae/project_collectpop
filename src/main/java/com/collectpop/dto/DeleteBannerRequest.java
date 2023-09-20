package com.collectpop.dto;

import lombok.Data;

@Data
public class DeleteBannerRequest {
    private Long bnId;
    private String file_name;
    private String file_path;
}
