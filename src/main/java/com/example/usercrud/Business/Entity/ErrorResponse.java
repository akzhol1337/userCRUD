package com.example.usercrud.Business.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private List<String> errorDescriptions;
    private Date date;

    public ErrorResponse(Date date) {
        this.errorDescriptions = new ArrayList<>();
        this.date = date;
    }
}
