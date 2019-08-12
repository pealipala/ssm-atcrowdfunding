package com.pealipala.utils;

import lombok.Data;

@Data
public class AjaxResult {
    private boolean success;
    private String message;
    private Page page;
    private Object data ;
}
