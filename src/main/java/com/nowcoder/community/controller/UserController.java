package com.nowcoder.community.controller;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author mjz
 * @create 2023-03-01-21:32
 * @description
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    // 域名
    @Value("${community.path.domain}")
    private String domain;

    @Value("${community.path.upload}")
    private String uploadPath;

    // 项目名
    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @LoginRequired
    @RequestMapping(path = "/setting", method = RequestMethod.GET)
    public String getSettingPage() {
        return "/site/setting";
    }

    /**
     * 更改头像的图片
     *
     * @param headerImage
     * @param model
     * @return
     */
    @LoginRequired
    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    public String uploadHeader(MultipartFile headerImage, Model model) {
        // 判断上传的图片是否为空
        if (headerImage == null) {
            model.addAttribute("error", "您还没有选择图片!");
            return "/site/setting";
        }

        // 判断文件格式是否正确，并给上传的图片更名，以防重复
        String fileName = headerImage.getOriginalFilename(); // 取图片名
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1); // 取图片后缀
        if (StringUtils.isBlank(suffix)) {
            model.addAttribute("error", "文件的格式不正确!");
            return "/site/setting";
        }

        // 生成随机文件名
        fileName = CommunityUtil.generateUUID() + "." + suffix;
        // 确定文件存放的路径
        File dest = new File(uploadPath + "/" + fileName);
        try {
            // 存储文件
            headerImage.transferTo(dest);
        } catch (IOException e) {
            logger.error("上传文件失败：" + e.getMessage());
            throw new RuntimeException("上传文件失败，服务器发送异常!", e);
        }

        // 更新当前用户的头像的路径(Web访问路径)
        // http://localhost:8080/community/user/header/xxx.png 自定义路径，配合请求使用
        User user = hostHolder.getUser();
        String headerUrl = domain + contextPath + "/user/header/" + fileName;
        userService.updateHeader(user.getId(), headerUrl);

        return "redirect:/index";
    }

    @RequestMapping(path = "/header/{fileName}", method = RequestMethod.GET)
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        // 获取服务器存放路径
        fileName = uploadPath + "/" + fileName;
        // 文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        // 响应图片
        response.setContentType("image/" + suffix); // 设置响应格式
        try (
                OutputStream os = response.getOutputStream(); // 获取输出流(write)
                FileInputStream fis = new FileInputStream(fileName); // 获取文件的输入流(read)
        ) {
            byte[] buffer = new byte[1024]; // 建立缓冲区，一批一批地输出
            int b = 0; // 建立游标
            while ((b = fis.read(buffer)) != -1) {
                os.write(buffer, 0, b);
            }
        } catch (IOException e) {
            logger.error("读取头像失败了：" + e.getMessage());
        }
    }

    @RequestMapping(path = "/updatePassword", method = RequestMethod.POST)
    public String updatePassword(String oldPassword, String newPassword, String confirmPassword, Model model) {
        // 空值处理
        if (StringUtils.isBlank(oldPassword) || StringUtils.isBlank(newPassword) || StringUtils.isBlank(confirmPassword)) {
            model.addAttribute("passwordMsg", "密码不能为空!");
            return "/site/setting";
        }
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("passwordMsg", "密码不一致!");
            return "/site/setting";
        }

        // 比较原始密码
        User user = hostHolder.getUser(); // 获取当前用户
        oldPassword = CommunityUtil.md5(oldPassword + user.getSalt()); // 验证密码 - 先加密，再与数据库中的密码比较
        if (!user.getPassword().equals(oldPassword)) {
            model.addAttribute("passwordMsg", "原始密码不正确!");
            return "/site/setting";
        }

        // 新密码先加密，再更改
        newPassword = CommunityUtil.md5(newPassword + user.getSalt());
        userService.updatePassword(user.getId(), newPassword);

        return "redirect:/index";

    }
}
