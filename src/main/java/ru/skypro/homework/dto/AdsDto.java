package ru.skypro.homework.dto;

import lombok.Data;

/**
 * Класс DTO для передачи информации об объявлении
 */
@Data
public class AdsDto {
    //id объявления
    private int pk;
    //id автора объявления
    private int author;
    //ссылка на картинку объявления
    private String image;
    //цена объявления
    private int price;
    //заголовок объявления
    private String title;
}
