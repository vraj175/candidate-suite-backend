package com.aspire.kgp.service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.aspire.kgp.dto.ContactDTO;
import com.aspire.kgp.dto.DocumentDTO;
import com.aspire.kgp.dto.SearchDTO;
import com.aspire.kgp.model.Contact;
import com.aspire.kgp.model.Reference;

public interface ContactService {
  public ContactDTO getContactDetails(String contactId);

  public byte[] getContactImage(String contactId);

  public String updateContactDetails(String contactId, String contactData)
      throws UnsupportedEncodingException;

  public Reference saveAndUpdateContactReference(String referenceId, String referenceData, String contactId)
      throws UnsupportedEncodingException;

  String addContactReference(String contactId, String referenceData) throws UnsupportedEncodingException;

  String uploadCandidateResume(MultipartFile multipartFile, String contactId, String type,
      String candidateId, HttpServletRequest request);

  String uploadContactImage(MultipartFile multipartFile, String contactId);

  DocumentDTO getContactResumes(String contactId);

  void downloadDocument(String documentName, String attachmentId, HttpServletResponse response);

  List<Reference> getListOfReferences(String contactId);

  List<SearchDTO> getListOfContactSearches(String contactId);

  List<ContactDTO> getListOfContactByName(String contactName);

  String addNewContact(String contactData);

  public DocumentDTO getContactOfferLetter(String contactId);

  public Contact findByGalaxyId(String galaxyId);

  public Contact saveOrUpdateContact(ContactDTO contactDTO);

  public String updateContactEducationDetails(String contactId, String contactData);

  public String deleteJobHistoryById(String id);

  public String deleteBoardHistoryById(String id);
}
