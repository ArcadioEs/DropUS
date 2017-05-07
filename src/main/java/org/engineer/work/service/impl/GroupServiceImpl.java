package org.engineer.work.service.impl;

import org.engineer.work.dto.GroupDTO;
import org.engineer.work.exception.group.GroupExistsException;
import org.engineer.work.exception.group.GroupNotFoundException;
import org.engineer.work.exception.user.UserNotFoundException;
import org.engineer.work.model.GroupEntity;
import org.engineer.work.model.UserEntity;
import org.engineer.work.repository.GroupRepository;
import org.engineer.work.service.GroupService;
import org.engineer.work.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger LOG = LoggerFactory.getLogger(GroupServiceImpl.class);

	@Autowired
	private GroupRepository groupRepository;

	@Autowired
	private UserService userService;

	@Override
	public GroupEntity getGroupByName(final String name) {
		return groupRepository.findOne(name);
	}

	@Override
	@Transactional
	public void createGroup(final GroupDTO groupDTO) throws GroupExistsException {
		if (!groupRepository.exists(groupDTO.getName())) {
			groupRepository.save(new GroupEntity(groupDTO));
		} else {
			throw new GroupExistsException(groupDTO.getName());
		}
	}

	@Override
	public List<GroupEntity> getAllGroups() {
		List<GroupEntity> list = new ArrayList<>();
		groupRepository.findAll().iterator().forEachRemaining(list::add);

		return list;
	}

	@Override
	public void deleteGroup(final String name) throws GroupNotFoundException {
		if (groupRepository.exists(name)) {
			for (final UserEntity userEntity : this.getGroupByName(name).getUsers()) {
				userEntity.getGroups().removeIf(group -> group.getName().equals(name));
				try {
					userService.updateUser(userEntity);
				} catch (UserNotFoundException e) {
					LOG.warn("User could not be found", e);
				}
			}
			groupRepository.delete(name);
		} else {
			throw new GroupNotFoundException(name);
		}
	}
}
