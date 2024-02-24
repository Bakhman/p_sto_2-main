package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.pagination.PaginationDtoAble;
import com.javamentor.qa.platform.exception.PageException;
import com.javamentor.qa.platform.service.abstracts.dto.PaginationServiceDto;
import com.javamentor.qa.platform.models.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

//Сервис для конструирования страницы PageDTO<T>
@Service
public class PaginationServiceDtoImpl<T> implements PaginationServiceDto<T> {

    private Map<String, PaginationDtoAble<?>> map;

    @Autowired
    public void setMap(Map<String, PaginationDtoAble<?>> map) {
        this.map = map;
    }

    @Override
    @Transactional
    public PageDto<T> getPageDto(int currentPageNumber, int itemsOnPage, Map<String, Object> param) {
        //Здесь мы достаем нужную пагинацию, которая была помещена в контроллере по ключу class, и собираем PageDto
        if (currentPageNumber < 0 || itemsOnPage < 0) {
            throw new PageException("currentPageNumber or itemsOnPage less than 0");
        }
        if (!map.containsKey(param.get("class"))) {
            throw new PageException(param.get("class") + " is not a PaginationDto");
        }
        if (!param.containsKey("currentPageNumber") || !param.containsKey("itemsOnPage")) {
            throw new PageException("PaginationMap is not contain key currentPageNumber or itemsOnPage");
        }
        PaginationDtoAble<T> dtoAble = (PaginationDtoAble<T>) map.get(param.get("class"));
        int totalResultCount = dtoAble.getTotalResultCount(param);
        int totalPageCount = totalResultCount%itemsOnPage == 0 ? totalResultCount/itemsOnPage : totalResultCount/itemsOnPage + 1;
        return new PageDto<> (currentPageNumber, totalPageCount, totalResultCount, dtoAble.getItems(param), itemsOnPage);
    }


}