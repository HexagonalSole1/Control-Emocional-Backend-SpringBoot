package com.example.APISkeleton.web.dtos.emotions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmotionDTO {
    private Long id;
    private String name;
    private String description;
    private String color;
    private String icon;
}