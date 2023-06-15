package groupquattro.demo.services;

import groupquattro.demo.dto.CKExpenceSummaryDto;
import groupquattro.demo.dto.GroupFormDto;
import groupquattro.demo.dto.GroupInfoDto;
import groupquattro.demo.dto.GroupPageDto;
import groupquattro.demo.exceptions.DuplicateResourceException;
import groupquattro.demo.exceptions.ResourceNotFoundException;
import groupquattro.demo.exceptions.UserAlreadyAMemberException;

import java.util.List;

public interface GroupService {

    public List<GroupInfoDto> getAllGroups() throws ResourceNotFoundException;

    public GroupPageDto findGroupById(String idGroup) throws ResourceNotFoundException;

    public GroupPageDto findGroupByGroupName(String groupName) throws ResourceNotFoundException;

    public GroupPageDto createGroup(GroupFormDto groupFormDto) throws DuplicateResourceException;

    public GroupPageDto updateGroup(GroupPageDto groupPageDto) throws ResourceNotFoundException;

    void updateGroupExpences(CKExpenceSummaryDto ckExpenceSummaryDto, String groupName) throws ResourceNotFoundException;

    GroupPageDto addUser(GroupPageDto groupPageDto, String username, String groupId) throws ResourceNotFoundException, UserAlreadyAMemberException;

//    List<CKExpenceSummaryDto> getExpenceWhoseDescriptioIs(String description);
}
