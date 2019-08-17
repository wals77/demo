package com.springboot.demo.controller;


import com.springboot.demo.model.RequestDataDO;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class ActionCountController {


    @RequestMapping(value = "count", method = RequestMethod.POST)
    public void count(@RequestBody @Valid RequestDataDO requestDataDO, BindingResult bindingResult){

        List<ObjectError> allErrors = bindingResult.getAllErrors();
        for (ObjectError objectError : allErrors){
            System.out.println(objectError.getDefaultMessage());
        }

        System.out.println(requestDataDO);

    }

}
