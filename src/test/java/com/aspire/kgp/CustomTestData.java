package com.aspire.kgp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.dto.BoardDetailsDTO;
import com.aspire.kgp.dto.CandidateDTO;
import com.aspire.kgp.dto.CompanyDTO;
import com.aspire.kgp.dto.ContactDTO;
import com.aspire.kgp.dto.EducationDTO;
import com.aspire.kgp.dto.InviteDTO;
import com.aspire.kgp.dto.JobHistoryDTO;
import com.aspire.kgp.dto.PickListDTO;
import com.aspire.kgp.dto.ResetPasswordDTO;
import com.aspire.kgp.dto.SearchDTO;
import com.aspire.kgp.dto.UserDTO;
import com.aspire.kgp.model.Language;
import com.aspire.kgp.model.Role;
import com.aspire.kgp.model.User;
import com.aspire.kgp.model.UserSearch;
import com.aspire.kgp.model.UserVideo;
import com.aspire.kgp.security.UserEntity;
import com.aspire.kgp.util.CommonUtil;

public class CustomTestData {
  
  public static MockHttpServletRequest getRequest() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    return request;
  }
  
  public static Language getLanguage() {
    Timestamp t1 = new Timestamp(System.currentTimeMillis());

    Language language = new Language();
    language.setId(Long.MIN_VALUE);
    language.setName(Constant.ENGLISH_CODE);
    language.setCreatedDate(t1);
    language.setModifyDate(t1);

    return language;
  }

  public static List<Language> getLanguages() {
    List<Language> languages = new ArrayList<>();
    languages.add(getLanguage());
    return languages;
  }
  
  public static Role getRole() {
    Timestamp t1 = new Timestamp(System.currentTimeMillis());

    Role role = new Role();
    role.setId(Long.MIN_VALUE);
    role.setName(Constant.TEST);
    role.setCreatedDate(t1);
    role.setModifyDate(t1);

    return role;
  }

  public static List<Role> getRoles() {
    List<Role> roles = new ArrayList<>();
    roles.add(getRole());
    return roles;
  }
  
  public static User getUser() {
    Timestamp t1 = new Timestamp(System.currentTimeMillis());

    User user = new User();
    user.setId(Long.MIN_VALUE);
    user.setCreatedDate(t1);
    user.setModifyDate(t1);
    user.setGalaxyId(Constant.TEST);
    user.setDeleted(Boolean.FALSE);
    user.setEmail(Constant.TEST);
    user.setLastLogin(t1);
    user.setPassword(CommonUtil.hash(Constant.TEST));
    user.setPasswordReset(Boolean.FALSE);
    user.setLanguage(getLanguage());
    user.setRole(getRole());

    return user;
  }
  
  public static UserDTO getUserDTO() {
    UserDTO user = new UserDTO();
    user.setId(Constant.TEST);
    user.setName(Constant.TEST);
    user.setFirstName(Constant.TEST);
    user.setLastName(Constant.TEST);
    user.setWorkEmail(Constant.TEST);
    user.setEmail(Constant.TEST);
    user.setRole(Constant.TEST);
    user.setTitle(Constant.TEST);
    user.setToken(Constant.TEST);
    user.setCountry(Constant.TEST);
    user.setLinkedinUrl(Constant.TEST);
    user.setBio(Constant.TEST);
    user.setMobilePhone(Constant.TEST);
    user.setWorkPhone(Constant.TEST);
    user.setPasswordReset(Boolean.FALSE);

    return user;
  }
  
  public static InviteDTO getInviteDTO() {
    InviteDTO invite = new InviteDTO();
    invite.setCandidateId(Constant.TEST);
    invite.setEmail(Constant.TEST);
    invite.setLanguage(Constant.TEST);
    invite.setPartnerId(Constant.TEST);
    invite.setRemoveDuplicate(Boolean.FALSE);
    invite.setBcc(new String[] {});
    return invite;
  }

  public static List<User> getUsers() {
    List<User> users = new ArrayList<>();
    users.add(getUser());
    return users;
  }
  
  public static UserSearch getUserSearch() {
    Timestamp t1 = new Timestamp(System.currentTimeMillis());

    UserSearch userSearch = new UserSearch();
    userSearch.setId(Long.MIN_VALUE);
    userSearch.setCreatedDate(t1);
    userSearch.setModifyDate(t1);
    userSearch.setDeleted(Boolean.FALSE);
    userSearch.setCandidateId(Constant.TEST);
    userSearch.setInvitedBy(getUser());
    userSearch.setSearchId(Constant.TEST);
    userSearch.setCompanyId(Constant.TEST);
    
    return userSearch;
  }
  
  public static UserSearch getUserSearch2() {
    Timestamp t1 = new Timestamp(System.currentTimeMillis());

    UserSearch userSearch = new UserSearch();
    userSearch.setId(Long.MAX_VALUE);
    userSearch.setCreatedDate(t1);
    userSearch.setModifyDate(t1);
    userSearch.setDeleted(Boolean.FALSE);
    userSearch.setCandidateId(Constant.TEST);
    userSearch.setInvitedBy(getUser());
    userSearch.setSearchId(Constant.TEST);
    userSearch.setCompanyId(Constant.TEST);
    
    return userSearch;
  }
  
  public static List<UserSearch> getUserSearches() {
    List<UserSearch> userSearches = new ArrayList<>();
    userSearches.add(getUserSearch());
    return userSearches;
  }
  
  public static ResetPasswordDTO getResetPasswordDTO() {
    ResetPasswordDTO resetPasswordDTO = new ResetPasswordDTO();
    resetPasswordDTO.setEmail(Constant.TEST);
    resetPasswordDTO.setOldPassword(Constant.TEST);
    resetPasswordDTO.setNewPassword(Constant.TEST);
    return resetPasswordDTO;
  }
  
  public static UserEntity getUserEntity() {
    UserEntity user = new UserEntity();
    user.setUsername(Constant.TEST);
    user.setPassword(Constant.TEST);
    return user;
  }
  
  public static UserVideo getUserVideo() {
    UserVideo userVideo = new UserVideo();
    userVideo.setFileToken(Constant.TEST);
    userVideo.setDeleted(Boolean.FALSE);
    //userVideo.setUserSearch(getUserSearch());
    return userVideo;
  }
  
  public static List<UserVideo> getUserVideos() {
    List<UserVideo> userVideos = new ArrayList<>();
    userVideos.add(getUserVideo());
    return userVideos;
  }
  
  public static List<String> getStrings() {
    List<String> strings = new ArrayList<>();
    strings.add(Constant.TEST);
    return strings;
  }
  
  public static SearchDTO getSearchDTO() {
    SearchDTO searchDTO = new SearchDTO();
    searchDTO.setId(Constant.TEST);
    searchDTO.setStage(Constant.TEST);
    searchDTO.setCompany(getCompanyDTO());
    return searchDTO;
  }
  
  public static List<SearchDTO> getSearchDTOs() {
    List<SearchDTO> searchDTOs = new ArrayList<>();
    searchDTOs.add(getSearchDTO());
    return searchDTOs;
  }
  
  public static CandidateDTO getCandidateDTO() {
    CandidateDTO candidateDTO = new CandidateDTO();
    candidateDTO.setId(Constant.TEST);
    candidateDTO.setContact(getContactDTO());
    candidateDTO.setSearch(getSearchDTO());
    return candidateDTO;
  }
  
  public static List<CandidateDTO> getCandidateDTOs() {
    List<CandidateDTO> candidateDTOs = new ArrayList<>();
    candidateDTOs.add(getCandidateDTO());
    return candidateDTOs;
  }
  
  public static PickListDTO getPickListDTO() {
    PickListDTO pickListDTO = new PickListDTO();
    pickListDTO.setName(Constant.TEST);
    
    return pickListDTO;
  }
  
  public static List<PickListDTO> getPickListDTOs() {
    List<PickListDTO> pickListDTOs = new ArrayList<>();
    pickListDTOs.add(getPickListDTO());
    return pickListDTOs;
  }
  
  public static CompanyDTO getCompanyDTO() {
    CompanyDTO companyDTO = new CompanyDTO();
    companyDTO.setId(Constant.TEST);
    companyDTO.setName(Constant.TEST);
    companyDTO.setDescription(Constant.TEST);
    companyDTO.setWebsite(Constant.TEST);
    return companyDTO;
  }
  
  public static BoardDetailsDTO getBoardDetailsDTO() {
    BoardDetailsDTO boardDetailsDTO = new BoardDetailsDTO();
    boardDetailsDTO.setCommittee(Constant.TEST);
    boardDetailsDTO.setEndYear(Constant.TEST);
    boardDetailsDTO.setId(Constant.TEST);
    boardDetailsDTO.setPosition(Constant.TEST);
    boardDetailsDTO.setStartYear(Constant.TEST);
    boardDetailsDTO.setTitle(Constant.TEST);
    boardDetailsDTO.setCompany(getCompanyDTO());
    return boardDetailsDTO;
  }
  
  public static List<BoardDetailsDTO> getBoardDetailsDTOs() {
    List<BoardDetailsDTO> boardDetailsDTOs = new ArrayList<>();
    boardDetailsDTOs.add(getBoardDetailsDTO());
    return boardDetailsDTOs;
  }
  
  public static EducationDTO getEducationDTO() {
    EducationDTO educationDTO = new EducationDTO();
    educationDTO.setId(Constant.TEST);
    educationDTO.setDegreeName(Constant.TEST);
    educationDTO.setDegreeYear(Constant.TEST);
    educationDTO.setMajor(Constant.TEST);
    educationDTO.setPosition(Constant.TEST);
    educationDTO.setSchoolName(Constant.TEST);
    return educationDTO;
  }
  
  public static List<EducationDTO> getEducationDTOs() {
    List<EducationDTO> educationDTOs = new ArrayList<>();
    educationDTOs.add(getEducationDTO());
    return educationDTOs;
  }
  
  public static JobHistoryDTO getJobHistoryDTO() {
    JobHistoryDTO jobHistoryDTO = new JobHistoryDTO();
    jobHistoryDTO.setId(Constant.TEST);
    jobHistoryDTO.setCompany(getCompanyDTO());
    jobHistoryDTO.setEndYear(Constant.TEST);
    jobHistoryDTO.setPosition(Constant.TEST);
    jobHistoryDTO.setStartYear(Constant.TEST);
    jobHistoryDTO.setTitle(Constant.TEST);
    
    return jobHistoryDTO;
  }
  
  public static List<JobHistoryDTO> getJobHistoryDTOs() {
    List<JobHistoryDTO> jobHistoryDTOs = new ArrayList<>();
    jobHistoryDTOs.add(getJobHistoryDTO());
    return jobHistoryDTOs;
  }
  
  public static ContactDTO getContactDTO() {
    ContactDTO contactDTO = new ContactDTO();
    contactDTO.setBaseSalary(Constant.TEST);
    contactDTO.setBio(Constant.TEST);
    contactDTO.setWorkPhone(Constant.TEST);
    contactDTO.setCurrentJobTitle(Constant.TEST);
    contactDTO.setCompensationExpectation(Constant.TEST);
    contactDTO.setCompensationNotes(Constant.TEST);
    contactDTO.setCountry(Constant.TEST);
    contactDTO.setEmail(Constant.TEST);
    contactDTO.setEquity(Constant.TEST);
    contactDTO.setFirstName(Constant.TEST);
    contactDTO.setLastName(Constant.TEST);
    contactDTO.setHomePhone(Constant.TEST);
    contactDTO.setId(Constant.TEST);
    contactDTO.setLinkedinUrl(Constant.TEST);
    contactDTO.setBoardDetails(getBoardDetailsDTOs());
    contactDTO.setCompany(getCompanyDTO());
    contactDTO.setMobilePhone(Constant.TEST);
    contactDTO.setPasswordReset(Boolean.FALSE);
    contactDTO.setWorkPhone(Constant.TEST);
    contactDTO.setWorkEmail(Constant.TEST);
    contactDTO.setToken(Constant.TEST);
    contactDTO.setTitle(Constant.TEST);
    contactDTO.setTargetBonusValue(Constant.TEST);
    contactDTO.setRole(Constant.TEST);
    contactDTO.setPublishedBio(Constant.TEST);
    contactDTO.setName(Constant.TEST);
    contactDTO.setEducationDetails(getEducationDTOs());
    contactDTO.setJobHistory(getJobHistoryDTOs());
    
    return contactDTO;
  }
}
