package groupquattro.demo.services;

import groupquattro.demo.dto.CKExpenceSummaryDto;
import groupquattro.demo.dto.GroupFormDto;
import groupquattro.demo.dto.GroupInfoDto;
import groupquattro.demo.dto.GroupPageDto;
import groupquattro.demo.exceptions.DuplicateResourceException;
import groupquattro.demo.exceptions.ResourceNotFoundException;
import groupquattro.demo.exceptions.UserAlreadyAMemberException;
import groupquattro.demo.mapper.GroupInfoMapper;
import groupquattro.demo.mapper.GroupPageMapper;
import groupquattro.demo.model.CKExpence;
import groupquattro.demo.model.Group;
import groupquattro.demo.repos.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GroupServiceImpl implements GroupService {


    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupPageMapper groupMapper;

    @Autowired
    private GroupInfoMapper groupInfoMapper;

    @Autowired
    private UserService userService;

    @Override
    public List<GroupInfoDto> getAllGroups() throws ResourceNotFoundException {
        List<Group> groupList = groupRepository.findAll();
        return groupInfoMapper.convertGroupListToGroupInfoList(groupList);
    }

    @Override
    public GroupPageDto findGroupById(String idGroup) throws ResourceNotFoundException {
        Optional<Group> group = groupRepository.findById(idGroup);
        if(!group.isPresent()){
            throw new ResourceNotFoundException("group" + idGroup + "not found");
        }
        else{
            return groupMapper.toDto(group.get());
        }
    }

    @Override
    public GroupPageDto findGroupByGroupName(String groupName) throws ResourceNotFoundException {
        Optional<Group> group = groupRepository.findGroupByGroupName(groupName);
        if(!group.isPresent()){
            throw new ResourceNotFoundException("group " + groupName + " not found");
        }
        else{
            return groupMapper.toDto(group.get());
        }
    }

    @Override
    public GroupPageDto createGroup(GroupFormDto groupFormDto) throws DuplicateResourceException {
       Group group = Group.builder().groupOwner(groupFormDto.getGroupOwner()).groupName(groupFormDto.getGroupName()).members(new ArrayList<String>()).build();
       group.getMembers().add(group.getGroupOwner());
       group.setExpences(new ArrayList<CKExpence>());
       Group savedGroup = null;
       GroupPageDto savedGroupDto = null;

       if(!groupRepository.findGroupByGroupName(groupFormDto.getGroupName()).isPresent()){
           try{
               savedGroup = groupRepository.save(group);
               savedGroupDto = groupMapper.toDto(savedGroup);
               userService.addGroup(groupMapper.toDto(savedGroup), groupFormDto.getGroupOwner());//update user groupList
               return savedGroupDto;
           }catch (ResourceNotFoundException e){
               System.err.println(e.getMessage());
               return null;
           }
       }
       else{
           throw new DuplicateResourceException("groupName " + groupFormDto.getGroupName() + " is already in use");
       }
    }

    @Override
    public GroupPageDto updateGroup(GroupPageDto groupPageDto) throws ResourceNotFoundException {
        Optional<Group> optionalGroup = groupRepository.findGroupByIdGroup(groupPageDto.getIdGroup());
        if(!optionalGroup.isPresent()){
            throw new ResourceNotFoundException("group "+ groupPageDto.getIdGroup()+ " not found");
        }
        else{
            Group updatedGroup = groupMapper.updateModel(groupPageDto, optionalGroup.get());
            groupRepository.save(updatedGroup);
            return groupMapper.toDto(updatedGroup);
        }
    }

    @Override
    public void updateGroupExpences(CKExpenceSummaryDto ckExpenceSummaryDto, String groupName) throws ResourceNotFoundException {
        Optional<Group> group = groupRepository.findGroupByGroupName(groupName);
        if(group.isPresent()){
            GroupPageDto groupPageDto = groupMapper.toDto(group.get());
            groupPageDto.getExpences().add(ckExpenceSummaryDto);
            Group updatedGroup = groupMapper.toModel(groupPageDto);
            groupRepository.save(updatedGroup);
        }else{
            throw new ResourceNotFoundException("group "+ groupName + "not found");
        }
    }

    /**
     *
     * @param username
     * @param groupPageDto
     * @param groupId act as a key that users can use to access this group
     * @return
     */
    @Override
    public GroupPageDto addUser(GroupPageDto groupPageDto, String username, String groupId) throws ResourceNotFoundException, UserAlreadyAMemberException {
        Optional<Group> optionalGroup = groupRepository.findGroupByIdGroup(groupId);
        if(!optionalGroup.isPresent()){
            throw new ResourceNotFoundException("group "+ groupPageDto.getGroupName() + " not found");
        }
        else{
            Group updatedGroup = null;
            Group group = optionalGroup.get();
            try{
                if(group.getMembers().add(username)==false){
                    throw new UserAlreadyAMemberException();
                }
                updatedGroup = groupRepository.save(group);
                GroupPageDto updatedPage = groupMapper.toDto(group);
                userService.addGroup(updatedPage, username);
                return updatedPage;
            }catch(UserAlreadyAMemberException e){
                throw e;
            }catch (ResourceNotFoundException resourceNotFoundException){
                try{
                    groupRepository.delete(updatedGroup);
                }catch (Exception e){
                    throw e;
                }
                return null;
            }

        }
    }

}
