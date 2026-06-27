package com.takeout.xianda.controller.admin;


import com.takeout.xianda.dto.EmployeeDTO;
import com.takeout.xianda.dto.EmployeePageQueryDTO;
import com.takeout.xianda.entity.User;
import com.takeout.xianda.result.PageResult;
import com.takeout.xianda.result.Result;
import com.takeout.xianda.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public Result<PageResult> list(@RequestParam("userName")String userName,
                                   @RequestParam("page")Integer page,
                                   @RequestParam("pageSize")Integer pageSize){
      PageResult pageResult = employeeService.pageQuery(page,pageSize,userName);
      return Result.success(pageResult);
    }

    @Operation(summary = "根据id查询员工信息")
    @GetMapping("/{id}")
    public Result<User> getEmployeeById(@PathVariable Long id){
        User user = employeeService.getById(id);
        return Result.success(user);
    }

    @Operation(summary = "新增员工")
    @PostMapping
    public Result addEmployee(@RequestBody EmployeeDTO employeeDTO){
        employeeService.save(employeeDTO);
        return Result.success();
    }


    @Operation(summary = "删除员工")
    @DeleteMapping("/{id}")
    public Result deleteEmployeeById(@PathVariable Long id){
        employeeService.deletedById(id);
        return Result.success();
    }


    @Operation(summary = "更新员工信息")
    @PutMapping
    public Result updateEmployee(@RequestBody EmployeeDTO employeeDTO){
        employeeService.update(employeeDTO);
        return Result.success();
    }

    @Operation(summary = "启用禁用员工")
    @PostMapping("/status/{status}")
    public Result updateEmployeeStatus(Long id,@PathVariable Integer status){
        employeeService.startOrStop(status,id);
        return Result.success();

    }

}
