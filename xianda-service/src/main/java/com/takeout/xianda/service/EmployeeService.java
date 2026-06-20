package com.takeout.xianda.service;

import com.takeout.xianda.dto.EmployeeDTO;
import com.takeout.xianda.dto.EmployeePageQueryDTO;
import com.takeout.xianda.entity.User;
import com.takeout.xianda.result.PageResult;

import java.util.List;

public interface EmployeeService {
    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    User getById(Long id);


    void save(EmployeeDTO employeeDTO);

//    List<User> list();
}
