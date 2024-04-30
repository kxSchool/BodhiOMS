package com.penguin.website.service.impl;

import java.util.List;

import com.penguin.website.model.dto.AttachmentResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.penguin.website.mapper.master.generator.AttachmentMapper;
import com.penguin.website.model.domain.Attachment;
import com.penguin.website.model.domain.AttachmentExample;
import com.penguin.website.service.AttachmentService;


@Service
@Transactional(rollbackFor = RuntimeException.class)
public class AttachmentServiceImpl implements AttachmentService {

	private static final String ATTACHMENTS_CACHE_NAME = "attachments";

	@Autowired
	private AttachmentMapper attachmentMapper;

	@Override
	@CacheEvict(value = ATTACHMENTS_CACHE_NAME, allEntries = true, beforeInvocation = true)
	public void save(Attachment attachment) throws Exception {
		attachmentMapper.insert(attachment);
	}

	@Override
	public PageInfo<Attachment> getAttachment(int page, int limit) {
		PageHelper.startPage(page, limit);
		AttachmentExample example = new AttachmentExample();
		example.setOrderByClause("id desc");
		List<Attachment> lists = attachmentMapper.selectByExample(example);
		return new PageInfo<>(lists);
	}

	@Override
	@Cacheable(value = ATTACHMENTS_CACHE_NAME, key = "'findById'+#id")
	public Attachment findById(int id) {
		return attachmentMapper.selectByPrimaryKey(id);
	}

	@Override
//	@Cacheable(value = ATTACHMENTS_CACHE_NAME, key = "'findRespById'+#id")
	public AttachmentResp findRespById(int id) {
		return attachmentMapper.selectRespByPrimaryKey(id);
	}

	@Override
	@CacheEvict(value = ATTACHMENTS_CACHE_NAME, allEntries = true, beforeInvocation = true)
	public void deleteAttachment(int id) throws Exception {
		attachmentMapper.deleteByPrimaryKey(id);
	}

	@Override
	@Cacheable(value = ATTACHMENTS_CACHE_NAME, key = "'countAttachment'")
	public List<Attachment> countAttachment() {
		return attachmentMapper.selectByExample(null);
	}

}
