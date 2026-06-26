package com.takeout.xianda.controller.user;

import com.takeout.xianda.result.Result;
import com.takeout.xianda.service.UserHomeService;
import com.takeout.xianda.vo.HomeVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "首页数据")
@RestController
@RequestMapping("api/home")
public class UserHomeController {


    @Autowired
    private UserHomeService userHomeService;

    @Operation(summary = "获取首页所有数据")
    @GetMapping
    public Result<HomeVO> getHomeData(){
        HomeVO vo = userHomeService.getHomeAllData();
        return Result.success(vo);
    }
}
