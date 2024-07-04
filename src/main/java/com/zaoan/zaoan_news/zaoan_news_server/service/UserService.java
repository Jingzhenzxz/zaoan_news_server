package com.zaoan.zaoan_news.zaoan_news_server.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zaoan.zaoan_news.zaoan_news_server.model.dto.user.UserQueryRequest;
import com.zaoan.zaoan_news.zaoan_news_server.model.entity.User;
import com.zaoan.zaoan_news.zaoan_news_server.model.vo.LoginUserVO;
import com.zaoan.zaoan_news.zaoan_news_server.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/07/30/ 16:37
 * @description
 */
public interface UserService extends IService<User> {
    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 是否为管理员
     *
     * @param user
     * @return
     */
    boolean isAdmin(User user);

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    boolean isAdmin(HttpServletRequest request);

    /**
     * @author Jingzhen
     * @date 2024/6/22 14:03
     * @description 注册
     * @Param username:
     * @Param email:
     * @Param password:
     * @Param confirmPassword:
     * @return: long
     */
    long userRegister(String username, String email, String password, String confirmPassword);

    /**
     * @author Jingzhen
     * @date 2024/6/22 14:03
     * @description 登录
     * @Param email:
     * @Param password:
     * @Param request:
     * @return: com.zaoan.zaoan_news.zaoan_news_server.model.vo.LoginUserVO
     */
    LoginUserVO userLogin(String email, String password, HttpServletRequest request);

    LoginUserVO getLoginUserVO(User user);

    boolean userLogout(HttpServletRequest request);

    /**
     * 获取脱敏的用户信息
     *
     * @param user
     * @return
     */
    UserVO getUserVO(User user);

    List<UserVO> getUserVO(List<User> userList);

    Wrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);
}
