package com.penguin.website.mapper.master.generator;

import com.penguin.website.model.domain.User;
import com.penguin.website.model.domain.UserExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    long countByExample(UserExample example);

    int deleteByExample(UserExample example);

    int deleteByPrimaryKey(Integer userId);

    int insert(User record);

    int insertSelective(User record);

    List<User> selectByExample(UserExample example);

    User selectByPrimaryKey(Integer userId);

    int updateByExampleSelective(@Param("record") User record, @Param("example") UserExample example);

    int updateByExample(@Param("record") User record, @Param("example") UserExample example);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);



    /**
     * 根据账号查询
     * @param userName
     * @return
     */
    User findUserByName(@Param("userName") String userName);

    /**
     * 统计账号数量
     * @param userName
     * @return
     */
    Long countUserByName(@Param("userName") String userName);

    /**
     * 根据id修改不未空的数据
     * @param user
     * @return
     */
    int updateById(User user);

    /**
     * 查询所有用户
     * @return
     */
    List<User> findAll();
}