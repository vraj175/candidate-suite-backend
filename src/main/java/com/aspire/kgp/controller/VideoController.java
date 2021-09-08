package com.aspire.kgp.controller;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.dto.DocumentDTO;
import com.aspire.kgp.exception.APIException;
import com.aspire.kgp.model.UserVideo;
import com.aspire.kgp.service.UserVideoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1.0")
@Tag(name = "Video", description = "REST API for User Videos")
public class VideoController {

  @Autowired
  UserVideoService service;

  @Operation(summary = "Add Video for user")
  @PostMapping(value = "/video/add")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK",
      content = @Content(mediaType = "application/json", schema = @Schema(type = "Json",
          example = "{  \"timestamp\": \"2021-09-06T08:53:39.690+00:00\", \"status\": \"OK\", \"message\": \"Video add successfully\" }")))})
  public ResponseEntity<Object> addVideo(@RequestParam String candidateId,
      @RequestParam String fileToken) {

    UserVideo userVideo = service.addCandidateVideo(candidateId, fileToken);

    if (userVideo != null) {
      Map<String, Object> body = new LinkedHashMap<>();
      body.put(Constant.TIMESTAMP, new Date());
      body.put(Constant.STATUS, HttpStatus.OK);
      body.put(Constant.MESSAGE, "Video add successfully");
      return new ResponseEntity<>(body, HttpStatus.OK);
    }
    throw new APIException("Error in add video");
  }
  
  @Operation(summary = "Get Video for Candidate")
  @GetMapping(value = {"/video/{candidateId}"})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK",
      content = @Content(mediaType = "application/json", schema = @Schema(type = "Video",
          example = "{ \"id\": \"string\",\"fileName\": \"string\",\"createdAt\": \"string\" }")))})
  public DocumentDTO getCandidateVideo(@PathVariable("candidateId") String candidateId) {
    List<UserVideo> userVideos = service.findByCandidateId(candidateId);
    if(userVideos.isEmpty())
      return null;
    
    UserVideo userVideo = userVideos.get(0);
    
    DocumentDTO documentDTO = new DocumentDTO();
    documentDTO.setId(String.valueOf(userVideo.getId()));
    documentDTO.setFileName(userVideo.getFileToken());
    documentDTO.setCreatedAt(String.valueOf(userVideo.getCreatedDate()));
    
    return documentDTO;
  }

}
