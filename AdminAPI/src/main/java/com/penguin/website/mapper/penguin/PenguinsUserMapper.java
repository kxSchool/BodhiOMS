package com.penguin.website.mapper.penguin;

import com.penguin.website.model.domain.PenguinsUser;
import java.util.List;
import com.penguin.website.bean.req.ReqUserPage;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PenguinsUserMapper {
    int deleteByPrimaryKey(String userId);

    int insert(PenguinsUser record);

    int insertSelective(PenguinsUser record);

    PenguinsUser selectByPrimaryKey(String userId);

    int updateByPrimaryKeySelective(PenguinsUser record);

    int updateByPrimaryKey(PenguinsUser record);

    List<PenguinsUser> selectUserListByModel(ReqUserPage record);

    PenguinsUser selectByAccessToken(String accessToken);

    int selectUserCount(ReqUserPage record);

    int delUserToken(String userId);
}