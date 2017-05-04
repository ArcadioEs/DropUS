package org.engineer.work.service.impl;

import org.engineer.work.dto.GroupDTO;
import org.engineer.work.model.GroupEntity;
import org.engineer.work.repository.GroupRepository;
import org.engineer.work.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/**
 * Default implementation of GroupService.
 */
@Service
public class GroupServiceImpl implements GroupService {

	@Autowired
	private GroupRepository groupRepository;

	@Override
	public GroupEntity getGroupByName(final String name) {
		return groupRepository.findOne(name);
	}

	@Override
	@Transactional
	public boolean createGroup(final GroupDTO groupDTO) {
		boolean result = false;

		if (!groupRepository.exists(groupDTO.getName()) && groupDTO.getGroupOwner() != null) {
			groupRepository.save(new GroupEntity(groupDTO));
			result = true;
		}

		return result;
	}

	@Override
	public List<GroupEntity> getAllGroups() {
		List<GroupEntity> list = new ArrayList<>();
		groupRepository.findAll().iterator().forEachRemaining(list::add);

		return list;
	}
}
