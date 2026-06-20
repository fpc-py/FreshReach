package com.takeout.xianda.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.takeout.xianda.dto.EmployeeDTO;
import com.takeout.xianda.dto.EmployeePageQueryDTO;

import com.takeout.xianda.result.PageResult;
import com.takeout.xianda.entity.User;
import com.takeout.xianda.mapper.EmployeeMapper;
import com.takeout.xianda.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;
    @Override
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {

        Page<User> page = new Page<>(employeePageQueryDTO.getPageNum(),employeePageQueryDTO.getPageSize());
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getStatus,1);
        if (employeePageQueryDTO.getUserName() != null && employeePageQueryDTO.getUserName()!= ""){
            wrapper.like(User::getUsername,employeePageQueryDTO.getUserName());
        }
        employeeMapper.selectPage(page, wrapper);
        return new PageResult(page.getTotal(), page.getRecords());

    }

    @Override
    public User getById(Long id) {
        User user = employeeMapper.selectById(id);

        return user;


    }

    @Override
    public void save(EmployeeDTO employeeDTO) {
        User user = new User();
        BeanUtils.copyProperties(employeeDTO,user);
        if (employeeDTO.getPassword() != null && employeeDTO.getPassword()!=""){
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encode = passwordEncoder.encode(employeeDTO.getPassword());
            user.setPassword(encode);
        }

        user.setStatus(1);
        employeeMapper.insert(user);

    }

    @Override
    public void deletedById(Long id) {
        employeeMapper.deleteById(id);
    }

    @Override
    public void update(EmployeeDTO employeeDTO) {
        User user = new User();
        BeanUtils.copyProperties(employeeDTO,user);

        user.setPassword("123456");
        user.setStatus(1);
        employeeMapper.updateById(user);
    }

    @Override
    public void startOrStop(Integer status, Long id) {
        User user = User.builder()
                .status(status)
                .id(id)
                .build();
        employeeMapper.updateById(user);

    }

//    @Override
//    public List<User> list() {
//        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
//        //只查询正常启用的员工
//        wrapper.eq(User::getStatus,1);
//        return employeeMapper.selectList(wrapper);
//    }
}
