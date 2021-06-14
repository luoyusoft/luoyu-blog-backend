package com.jinhx.blog.controller.sys;

import com.jinhx.blog.common.exception.MyException;
import com.jinhx.blog.common.util.SysAdminUtils;
import com.jinhx.blog.entity.base.Response;
import com.jinhx.blog.entity.sys.vo.SysLoginVO;
import com.jinhx.blog.common.enums.ResponseEnums;
import com.jinhx.blog.entity.sys.dto.SysUserDTO;
import com.jinhx.blog.service.sys.SysCaptchaService;
import com.jinhx.blog.service.sys.SysUserService;
import com.jinhx.blog.service.sys.SysUserTokenService;
import org.apache.commons.io.IOUtils;
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
 * @author jinhx
 * @date 2018/10/19 18:47
 * @description
 */
@RestController
public class SysLoginController {

    @Autowired
    private SysCaptchaService sysCaptchaService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysUserTokenService sysUserTokenService;

    /**
     * 获取验证码
     * @param uuid uuid
     * @return 验证码
     */
    @GetMapping("captcha.jpg")
    public void captcha(HttpServletResponse response, String uuid) throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");

        // 获取图片验证码
        BufferedImage image = sysCaptchaService.getCaptcha(uuid);

        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
        IOUtils.closeQuietly(out);
    }

    /**
     * 登录
     * @param sysLoginVO sysLoginVO
     * @return token
     */
    @PostMapping("/manage/sys/login")
    public Response login(@RequestBody SysLoginVO sysLoginVO) {
        boolean captcha = sysCaptchaService.validate(sysLoginVO.getUuid(),sysLoginVO.getCaptcha());
        if(!captcha){
            // 验证码不正确
            throw new MyException(ResponseEnums.CAPTCHA_WRONG);
        }

        // 用户信息
        SysUserDTO sysUserDTO = sysUserService.getSysUserDTOByUsername(sysLoginVO.getUsername());
        if(sysUserDTO == null || !sysUserDTO.getPassword().equals(new Sha256Hash(sysLoginVO.getPassword(),sysUserDTO.getSalt()).toHex())){
            // 用户名或密码错误
            throw new MyException(ResponseEnums.USERNAME_OR_PASSWORD_WRONG);
        }
        if(sysUserDTO.getStatus() == 0){
            throw new MyException(ResponseEnums.ACCOUNT_LOCK);
        }

        //生成token，并保存到redis
        return Response.success(sysUserTokenService.createToken(sysUserDTO.getId()));
    }

    /**
     * 退出
     */
    @PostMapping("/manage/sys/logout")
    public Response logout() {
        sysUserTokenService.logout(SysAdminUtils.getUserId());
        return Response.success();
    }

}
