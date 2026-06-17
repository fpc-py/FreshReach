package com.takeout.xianda.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.takeout.xianda.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
