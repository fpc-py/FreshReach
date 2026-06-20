package com.takeout.xianda.controller.admin;


import com.takeout.xianda.dto.EmployeePageQueryDTO;
import com.takeout.xianda.entity.User;
import com.takeout.xianda.result.PageResult;
import com.takeout.xianda.result.Result;
import com.takeout.xianda.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@Tag(name = "员工管理",description = "员工管理相关接口")
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

//    @GetMapping("/list")
//    public Result<List<User>> list(){
//        List<User> list = employeeService.list();
//        return Result.success(list);
//    }

    @Operation(summary = "分页查询",description = "分页")
    @GetMapping("/page")
    public Result<PageResult> list(EmployeePageQueryDTO employeePageQueryDTO){
      PageResult pageResult = employeeService.pageQuery(employeePageQueryDTO);
      return Result.success(pageResult);
    }


}
