package com.example.xiergc.controller;

import com.example.xiergc.entity.Article;
import com.example.xiergc.entity.Result;
import com.example.xiergc.entity.User;
import com.example.xiergc.service.UserService;
import com.example.xiergc.utils.JwtUtil;
import com.example.xiergc.utils.MD5Utils;
import com.example.xiergc.utils.ThreadLocalUtil;
import com.mysql.cj.util.StringUtils;
import jakarta.validation.Valid;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //注册用户
    @PostMapping("/register")
    public Result register(@Valid @RequestBody User user) {
        //查询用户
        User u=userService.findByUserName(user.getUsername());
        if(u==null){
            //没有占用
            //注册
            userService.register(user);
            return Result.success();
        }else{
            //占用
            return Result.error("账号已被占用");
        }
    }

    //登录用户
    @PostMapping("/login")
    public Result<String> login(@Valid @RequestBody User user) {
        //根据账号查询用户
        User loginUser = userService.findByUserName(user.getUsername());
        //判断该用户是否存在
        if(loginUser==null){
            return Result.error("账号错误");
        }
        //判断密码是否正确
        if(MD5Utils.passwordIsTrue(user.getPassword(),loginUser.getPassword())){
            //登录成功
            Map<String,Object> claims=new HashMap<>();
            claims.put("id",loginUser.getId());
            claims.put("username",loginUser.getUsername());
            String token = JwtUtil.genToken(claims);
            //把token存储到redis中
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            operations.set(token,token,1, TimeUnit.HOURS);
            userService.updatelogintime(user.getUsername());
            return Result.success(token);
        }

        return Result.error("密码错误");
    }

    //查询用户详细信息
    @GetMapping("/profile")
    public Result<User> profile() {
        Map<String,Object> map= ThreadLocalUtil.get();
        Long id=((Number) map.get("id")).longValue();
        User user = userService.findById(id);
        return Result.success(user);
    }

    //修改昵称
    @PutMapping("/name")
    public Result updatename(@RequestBody Map<String, String> request) {
        String name = request.get("name");
        userService.updatename(name);
        return Result.success();
    }

    //修改头像
    @PatchMapping("/avatar")
    public Result updateAvatar(@RequestBody Map<String, String> request) {
        String avatarUrl = request.get("avatarUrl");
        userService.updateAvatar(avatarUrl);
        return Result.success();
    }

    //更改密码
    @PatchMapping("/password")
    public Result updatePassword(@RequestBody Map<String,String> params,@RequestHeader("Authorization") String token) {
        //1.校验参数
        String oldPwd = params.get("oldPassword");
        String newPwd = params.get("newPassword");
        String rePwd = params.get("rePassword");

        if(StringUtils.isEmptyOrWhitespaceOnly(oldPwd)||
                StringUtils.isEmptyOrWhitespaceOnly(newPwd)||
                StringUtils.isEmptyOrWhitespaceOnly(rePwd)){
            return Result.error("缺少必要的参数");
        }

        if(oldPwd.equals(newPwd)){
            return Result.error("修改密码与原密码不能一致");
        }
        //原密码是否正确
        Map<String,Object> map = ThreadLocalUtil.get();
        String username=(String) map.get("username");
        User loginUser = userService.findByUserName(username);
        if(!MD5Utils.passwordIsTrue(oldPwd,loginUser.getPassword())){
            return Result.error("原密码填写不正确");
        }

        //两次密码是否一致
        if(!rePwd.equals(newPwd)){
            return Result.error("两次填写的密码不一致");
        }
        //2.完成密码更新
        userService.updatePwd(newPwd);
        //删除redis中对应token
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.getOperations().delete(token);

        return Result.success();
    }

    //查询用户创建的文章
    @GetMapping("/created")
    public Result<List<Article>> created() {
        Map<String,Object> map= ThreadLocalUtil.get();
        Long id =((Number) map.get("id")).longValue();
        List<Article> articles = userService.GetCreated(id);
        return Result.success(articles);
    }

    //查询用户点赞的文章
    @GetMapping("/liked")
    public Result<List<Article>> liked() {
        Map<String,Object> map= ThreadLocalUtil.get();
        Long id =((Number) map.get("id")).longValue();
        List<Article> articles = userService.Getliked(id);
        return Result.success(articles);
    }

    //查询用户收藏的文章
    @GetMapping("/collected")
    public Result<List<Article>> collected() {
        Map<String,Object> map= ThreadLocalUtil.get();
        Long id =((Number) map.get("id")).longValue();
        List<Article> articles = userService.GetCollected(id);
        return Result.success(articles);
    }
}
