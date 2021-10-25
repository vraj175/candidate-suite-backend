package com.aspire.kgp.service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.aspire.kgp.dto.ContactDTO;
import com.aspire.kgp.dto.ContactReferencesDTO;
import com.aspire.kgp.dto.DocumentDTO;
import com.aspire.kgp.dto.SearchDTO;

public interface ContactService {
  public ContactDTO getContactDetails(String contactId);

  public byte[] getContactImage(String contactId);

  public String updateContactDetails(String contactId, String contactData)
      throws UnsupportedEncodingException;

  public String updateContactReference(String referenceId, String referenceData)
      throws UnsupportedEncodingException;

  String addContactReference(String contactId, String referenceData);

  String uploadCandidateResume(MultipartFile multipartFile, String contactId, String type);

  String uploadContactImage(MultipartFile multipartFile, String contactId);

  DocumentDTO getContactResumes(String contactId);

  void downloadDocument(String documentName, String attachmentId, HttpServletResponse response);

  List<ContactReferencesDTO> getListOfReferences(String contactId);

  List<SearchDTO> getListOfContactSearches(String contactId);

  List<ContactDTO> getListOfContactByName(String contactName);

  String addNewContact(String contactData);

  public DocumentDTO getContactOfferLetter(String contactId);
}
