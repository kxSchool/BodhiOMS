package com.penguin.website.mapper.penguin;

import com.penguin.website.model.domain.PenguinsOrder;
import java.util.List;
import com.penguin.website.bean.rsp.RspTransReport;
import org.apache.ibatis.annotations.Param;
import java.util.Date;
import com.penguin.website.bean.req.REQPenguinsOrder;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PenguinsOrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PenguinsOrder record);

    int insertSelective(PenguinsOrder record);

    PenguinsOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PenguinsOrder record);

    int updateByPrimaryKey(PenguinsOrder record);

    Integer getOrderListCount(REQPenguinsOrder record);

    List<PenguinsOrder> getOrderList(REQPenguinsOrder record);

    RspTransReport getTransReport(@Param("orderType") Integer orderType, @Param("orderStatus") Integer orderStatus, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    PenguinsOrder selectByUserId(String userId);
}