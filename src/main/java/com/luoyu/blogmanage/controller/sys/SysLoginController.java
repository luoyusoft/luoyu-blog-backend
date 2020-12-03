package com.luoyu.blogmanage.controller.sys;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IOUtils;
import com.luoyu.blogmanage.common.exception.MyException;
import com.luoyu.blogmanage.entity.base.Response;
import com.luoyu.blogmanage.entity.base.AbstractController;
import com.luoyu.blogmanage.entity.sys.SysUser;
import com.luoyu.blogmanage.entity.sys.vo.SysLoginVO;
import com.luoyu.blogmanage.common.enums.ResponseEnums;
import com.luoyu.blogmanage.mapper.sys.SysUserMapper;
import com.luoyu.blogmanage.service.sys.SysCaptchaService;
import com.luoyu.blogmanage.service.sys.SysUserTokenService;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * SysLoginController
 *
 * @author luoyu
 * @date 2018/10/19 18:47
 * @description
 */
@RestController
public class SysLoginController extends AbstractController {

    @Autowired
    private SysCaptchaService sysCaptchaService;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysUserTokenService sysUserTokenService;

    /**
     * 获取图片验证码
     */
    @GetMapping("captcha.jpg")
    public void captcha(HttpServletResponse response, String uuid) throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");

        //获取图片验证码
        BufferedImage image = sysCaptchaService.getCaptcha(uuid);

        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
        IOUtils.closeQuietly(out);
    }

    /**
     * 登录
     */
    @PostMapping("/admin/sys/login")
    public Response login(@RequestBody SysLoginVO form) {
        boolean captcha = sysCaptchaService.validate(form.getUuid(),form.getCaptcha());
        if(!captcha){
            // 验证码不正确
            throw new MyException(ResponseEnums.CAPTCHA_WRONG);
        }

        // 用户信息
        SysUser user = sysUserMapper.selectOne(new QueryWrapper<SysUser>()
                .lambda()
                .eq(SysUser:: getUsername,form.getUsername()));
        if(user == null || !user.getPassword().equals(new Sha256Hash(form.getPassword(),user.getSalt()).toHex())){
            // 用户名或密码错误
            throw new MyException(ResponseEnums.USERNAME_OR_PASSWORD_WRONG);
        }
        if(user.getStatus() == 0){
            throw new MyException(ResponseEnums.ACCOUNT_LOCK);
        }

        //生成token，并保存到redis
        return Response.success(sysUserTokenService.createToken(user.getUserId()));
    }

    /**
     * 退出
     */
    @PostMapping("/sys/logout")
    public Response logout() {
        sysUserTokenService.logout(getUserId());
        return Response.success();
    }

}
